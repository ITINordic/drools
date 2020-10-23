/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.ancompiler;

import java.util.Collection;
import java.util.Map;

import org.drools.core.base.ClassFieldReader;
import org.drools.core.reteoo.AlphaNode;
import org.drools.core.reteoo.BetaNode;
import org.drools.core.reteoo.CompositeObjectSinkAdapter;
import org.drools.core.reteoo.CompositePartitionAwareObjectSinkAdapter;
import org.drools.core.reteoo.LeftInputAdapterNode;
import org.drools.core.reteoo.NodeTypeEnums;
import org.drools.core.reteoo.ObjectSink;
import org.drools.core.reteoo.ObjectSinkNode;
import org.drools.core.reteoo.ObjectSinkNodeList;
import org.drools.core.reteoo.ObjectSinkPropagator;
import org.drools.core.reteoo.ObjectTypeNode;
import org.drools.core.reteoo.SingleObjectSinkAdapter;
import org.drools.core.reteoo.WindowNode;
import org.drools.core.rule.IndexableConstraint;
import org.drools.core.spi.AlphaNodeFieldConstraint;
import org.drools.core.util.Iterator;
import org.drools.core.util.ObjectHashMap;
import org.drools.core.util.index.AlphaRangeIndex;

/**
 * This class is used for reading an {@link ObjectTypeNode} using callbacks.
 * <p/>
 * The user defines a number of callback methods in a {@link NetworkHandler} that will be called when events occur
 * during parsing. The events include :
 * <li>ObjectTypeNode</li>
 * <li>Non-hashed and hashed AlphaNodes</li>
 * <li>BetaNodes</li>
 * <li>LeftInputAdapterNodes</li>
 * <p/>
 * Events are fired when each of these network features are encountered, and again when the end of them is encountered.
 * OTN parsing is unidirectional; previously parsed data cannot be re-read without starting the parsing operation again.
 */
public class ObjectTypeNodeParser {

    /**
     * OTN we are parsing/traversing
     */
    private final ObjectTypeNode objectTypeNode;
    private IndexableConstraint indexableConstraint;

    /**
     * Creates a new parser for the specified ObjectTypeNode
     *
     * @param objectTypeNode otn to parse
     */
    public ObjectTypeNodeParser(ObjectTypeNode objectTypeNode) {
        this.objectTypeNode = objectTypeNode;
    }

    /**
     * Parse the {@link #objectTypeNode}.
     * <p/>
     * <p>The application can use this method to instruct the OTN parser to begin parsing an {@link ObjectTypeNode}.</p>
     * <p/>
     * Once a parse is complete, an application may reuse the same Parser object, possibly with a different
     * {@link NetworkHandler}.</p>
     *
     * @param handler handler that will receieve the events generated by this parser
     * @see NetworkHandler
     */
    public void accept(NetworkHandler handler) {
        ObjectSinkPropagator propagator = objectTypeNode.getObjectSinkPropagator();

        handler.startObjectTypeNode(objectTypeNode);
        indexableConstraint = traversePropagator(propagator, handler);
        handler.endObjectTypeNode(objectTypeNode);
    }

    private IndexableConstraint traversePropagator(ObjectSinkPropagator propagator, NetworkHandler handler) {
        IndexableConstraint foundIndexableConstraint = null;
        if (propagator instanceof SingleObjectSinkAdapter) {
            // we know there is only a single child sink for this propagator
            ObjectSink sink = propagator.getSinks()[0];

            traverseSink(sink, handler);
        } else if (propagator instanceof CompositeObjectSinkAdapter) {
            CompositeObjectSinkAdapter composite = (CompositeObjectSinkAdapter) propagator;

            traverseSinkLisk(composite.getRangeIndexableSinks(), handler);
            traverseSinkLisk(composite.getHashableSinks(), handler);
            traverseSinkLisk(composite.getOthers(), handler);
            traverseRangeIndexedAlphaNodes(composite.getRangeIndexMap(), handler);
            foundIndexableConstraint = traverseHashedAlphaNodes(composite.getHashedSinkMap(), handler);
        } else if (propagator instanceof CompositePartitionAwareObjectSinkAdapter) {
            CompositePartitionAwareObjectSinkAdapter composite = (CompositePartitionAwareObjectSinkAdapter) propagator;
            traverseSinkLisk(composite.getSinks(), handler);
        }
        return foundIndexableConstraint;
    }
    private void traverseSinkLisk(ObjectSinkNodeList sinks, NetworkHandler handler) {
        if (sinks != null) {
            for (ObjectSinkNode sink = sinks.getFirst(); sink != null; sink = sink.getNextObjectSinkNode()) {
                traverseSink(sink, handler);
            }
        }
    }

    private void traverseSinkLisk(ObjectSink[] sinks, NetworkHandler handler) {
        if (sinks != null) {
            for (ObjectSink sink : sinks) {
                traverseSink(sink, handler);
            }
        }
    }

    private IndexableConstraint traverseHashedAlphaNodes(ObjectHashMap hashedAlphaNodes, NetworkHandler handler) {
        IndexableConstraint hashedFieldReader = null;
        if (hashedAlphaNodes != null && hashedAlphaNodes.size() > 0) {
            AlphaNode firstAlpha = getFirstAlphaNode(hashedAlphaNodes);
            hashedFieldReader = getClassFieldReaderForHashedAlpha(firstAlpha);

            // start the hashed alphas
            handler.startHashedAlphaNodes(hashedFieldReader);

            Iterator<ObjectHashMap.ObjectEntry> iter = hashedAlphaNodes.iterator();
            AlphaNode optionalNullAlphaNodeCase = null;
            for (ObjectHashMap.ObjectEntry entry = iter.next(); entry != null; entry = iter.next()) {
                CompositeObjectSinkAdapter.HashKey hashKey = (CompositeObjectSinkAdapter.HashKey) entry.getKey();
                AlphaNode alphaNode = (AlphaNode) entry.getValue();

                final Object objectValue = hashKey.getObjectValue();
                if (objectValue != null) {
                    handler.startHashedAlphaNode(alphaNode, objectValue);
                    // traverse the propagator for each alpha
                    traversePropagator(alphaNode.getObjectSinkPropagator(), handler);
                    handler.endHashedAlphaNode(alphaNode, hashKey.getObjectValue());
                } else {
                    optionalNullAlphaNodeCase = alphaNode;
                }
            }

            // end of the hashed alphas
            handler.endHashedAlphaNodes(hashedFieldReader);

            if (optionalNullAlphaNodeCase != null) {
                handler.nullCaseAlphaNodeStart(optionalNullAlphaNodeCase);
                traversePropagator(optionalNullAlphaNodeCase.getObjectSinkPropagator(), handler);
                handler.nullCaseAlphaNodeEnd(optionalNullAlphaNodeCase);
            }
        }
        return hashedFieldReader;
    }

    private void traverseRangeIndexedAlphaNodes(Map<CompositeObjectSinkAdapter.FieldIndex, AlphaRangeIndex> rangeIndexMap, NetworkHandler handler) {
        if (rangeIndexMap == null) {
            return;
        }
        Collection<AlphaRangeIndex> rangeIndexes = rangeIndexMap.values();
        for (AlphaRangeIndex alphaRangeIndex : rangeIndexes) {
            Collection<AlphaNode> alphaNodes = alphaRangeIndex.getAllValues();
            for (AlphaNode alphaNode : alphaNodes) {
                traverseSink(alphaNode, handler);
            }
        }
    }

    private void traverseSink(ObjectSink sink, NetworkHandler handler) {
        if (sink.getType() == NodeTypeEnums.AlphaNode) {
            AlphaNode alphaNode = (AlphaNode) sink;

            handler.startNonHashedAlphaNode(alphaNode);

            traversePropagator( alphaNode.getObjectSinkPropagator(), handler );

            handler.endNonHashedAlphaNode(alphaNode);
        } else if (NodeTypeEnums.isBetaNode( sink ) ) {
            BetaNode betaNode = (BetaNode) sink;

            handler.startBetaNode(betaNode);
            handler.endBetaNode(betaNode);
        } else if (sink.getType() == NodeTypeEnums.LeftInputAdapterNode) {
            LeftInputAdapterNode leftInputAdapterNode = (LeftInputAdapterNode) sink;

            handler.startLeftInputAdapterNode(leftInputAdapterNode);
            handler.endWindowNode(leftInputAdapterNode);
        } else if (sink.getType() == NodeTypeEnums.WindowNode) {
            WindowNode windowNode = (WindowNode) sink;

            handler.startWindowNode(windowNode);
            handler.endWindowNode(windowNode);
        }
    }

    /**
     * Returns the first {@link AlphaNode} from the specified {@link ObjectHashMap}.
     *
     * @param hashedAlphaNodes map of hashed AlphaNodes
     * @return first alpha from the specified map
     * @throws IllegalArgumentException thrown if the map doesn't contain any alpha nodes
     */
    private AlphaNode getFirstAlphaNode(final ObjectHashMap hashedAlphaNodes) throws IllegalArgumentException {
        AlphaNode firstAlphaNode;

        final Iterator iter = hashedAlphaNodes.iterator();
        final ObjectHashMap.ObjectEntry entry = (ObjectHashMap.ObjectEntry) iter.next();

        if (entry != null) {
            firstAlphaNode = (AlphaNode) entry.getValue();
        } else {
            throw new IllegalArgumentException("ObjectHashMap does not contain any hashed AlphaNodes!");
        }

        return firstAlphaNode;
    }

    /**
     * Returns the {@link ClassFieldReader} for the hashed AlphaNode. The AlphaNode's constraint has to be a
     * MvelConstraint. This is the only type of hashed alpha currently supported.
     *
     * @param alphaNode hashed alpha to get reader for
     * @return ClassFieldReader
     */
    private IndexableConstraint getClassFieldReaderForHashedAlpha(final AlphaNode alphaNode) {
        final AlphaNodeFieldConstraint fieldConstraint = alphaNode.getConstraint();

        if (!(fieldConstraint instanceof IndexableConstraint)) {
            throw new IllegalArgumentException("Only support IndexableConstraint hashed AlphaNodes, not " + fieldConstraint.getClass());
        }
        // we need to get the first alpha in the map to get the attribute name that be use for the prefix of the
        // generated variable name
        return (IndexableConstraint) fieldConstraint;
    }

    public IndexableConstraint getIndexableConstraint() {
        return indexableConstraint;
    }
}
