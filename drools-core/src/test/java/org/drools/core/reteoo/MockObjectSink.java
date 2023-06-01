/*
 * Copyright 2005 Red Hat, Inc. and/or its affiliates.
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

package org.drools.core.reteoo;

import java.util.ArrayList;
import java.util.List;

import org.drools.core.common.InternalFactHandle;
import org.drools.core.common.NetworkNode;
import org.drools.core.common.PropagationContext;
import org.drools.core.common.ReteEvaluator;
import org.drools.core.common.RuleBasePartitionId;
import org.kie.api.definition.rule.Rule;

public class MockObjectSink
    implements
    ObjectSinkNode,
    RightTupleSink {
    private final List     asserted  = new ArrayList();
    private final List     retracted = new ArrayList();
    private final List     updated   = new ArrayList();

    private ObjectSinkNode previousObjectSinkNode;
    private ObjectSinkNode nextObjectSinkNode;

    public void assertObject(final InternalFactHandle factHandle,
                             final PropagationContext context,
                             final ReteEvaluator reteEvaluator) {
        new RightTupleImpl( factHandle, this );
        this.asserted.add( new Object[]{factHandle, context, reteEvaluator} );
    }

    public void retractRightTuple(final RightTuple rightTuple,
                              final PropagationContext context,
                              final ReteEvaluator reteEvaluator) {
        this.retracted.add( new Object[]{rightTuple.getFactHandle(), context, reteEvaluator} );
    }

    public List getAsserted() {
        return this.asserted;
    }

    public List getRetracted() {
        return this.retracted;
    }
    
    public List getUpdated() {
        return this.updated;
    }

    /**
     * Returns the next node
     * @return
     *      The next ObjectSinkNode
     */
    public ObjectSinkNode getNextObjectSinkNode() {
        return this.nextObjectSinkNode;
    }

    /**
     * Sets the next node 
     * @param next
     *      The next ObjectSinkNode
     */
    public void setNextObjectSinkNode(final ObjectSinkNode next) {
        this.nextObjectSinkNode = next;
    }

    /**
     * Returns the previous node
     * @return
     *      The previous ObjectSinkNode
     */
    public ObjectSinkNode getPreviousObjectSinkNode() {
        return this.previousObjectSinkNode;
    }

    /**
     * Sets the previous node 
     * @param previous
     *      The previous ObjectSinkNode
     */
    public void setPreviousObjectSinkNode(final ObjectSinkNode previous) {
        this.previousObjectSinkNode = previous;
    }

    public boolean isObjectMemoryEnabled() {
        return false;
    }

    public void setObjectMemoryEnabled(boolean objectMemoryOn) {
    }

    public int getId() {
        return 0;
    }

    public RuleBasePartitionId getPartitionId() {
        return null;
    }

    public short getType() {
        return NodeTypeEnums.JoinNode;
    }

    public void modifyObject(InternalFactHandle factHandle,
                             ModifyPreviousTuples modifyPreviousTuples,
                             PropagationContext context,
                             ReteEvaluator reteEvaluator) {
        RightTuple rightTuple = modifyPreviousTuples.peekRightTuple(RuleBasePartitionId.MAIN_PARTITION);
        while ( rightTuple != null ) {
            modifyPreviousTuples.removeRightTuple(RuleBasePartitionId.MAIN_PARTITION);
            rightTuple = modifyPreviousTuples.peekRightTuple(RuleBasePartitionId.MAIN_PARTITION);
        }
        this.updated.add( new Object[]{factHandle, context, reteEvaluator} );
        
    }

    public void modifyRightTuple(RightTuple rightTuple,
                                 PropagationContext context,
                                 ReteEvaluator reteEvaluator) {
        this.updated.add( new Object[]{rightTuple, context, reteEvaluator} );
        
    }

    public void byPassModifyToBetaNode(InternalFactHandle factHandle,
                                       ModifyPreviousTuples modifyPreviousTuples,
                                       PropagationContext context,
                                       ReteEvaluator reteEvaluator) {
    }

    public boolean isAssociatedWith( Rule rule ) {
        return false;
    }

    @Override public Rule[] getAssociatedRules() {
        return new Rule[0];
    }

    @Override
    public void addAssociatedTerminal(BaseTerminalNode terminalNode) {
    }

    @Override
    public void removeAssociatedTerminal(BaseTerminalNode terminalNode) {
    }

    @Override
    public int getAssociatedTerminalsSize() {
        return 0;
    }

    @Override
    public boolean hasAssociatedTerminal(BaseTerminalNode terminalNode) {
        return false;
    }

    public ObjectTypeNode.Id getRightInputOtnId() {
        return null;
    }

    public boolean thisNodeEquals(final Object object) {
        return false;
    }

    public int nodeHashCode() {return this.hashCode();}

    public void setPartitionIdWithSinks( RuleBasePartitionId partitionId ) { }

    @Override
    public NetworkNode[] getSinks() {
        return new NetworkNode[0];
    }
}
