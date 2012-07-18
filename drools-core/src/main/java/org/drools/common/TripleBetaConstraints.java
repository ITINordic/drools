/*
 * Copyright 2005 JBoss Inc
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

package org.drools.common;

import org.drools.RuleBaseConfiguration;
import org.drools.reteoo.LeftTuple;
import org.drools.rule.ContextEntry;
import org.drools.rule.constraint.MvelConstraint;
import org.drools.spi.BetaNodeFieldConstraint;

import java.util.List;

public class TripleBetaConstraints extends MultipleBetaConstraint {

    private static final long             serialVersionUID = 510l;

    public TripleBetaConstraints() { }

    public TripleBetaConstraints(final BetaNodeFieldConstraint[] constraints,
                                 final RuleBaseConfiguration conf) {
        this(constraints,
                conf,
                false);
    }

    public TripleBetaConstraints(final BetaNodeFieldConstraint[] constraints,
                                 final RuleBaseConfiguration conf,
                                 final boolean disableIndexing) {
        super(constraints, conf, disableIndexing);
    }

    /* (non-Javadoc)
     * @see org.drools.common.BetaNodeConstraints#updateFromTuple(org.drools.reteoo.ReteTuple)
     */
    public void updateFromTuple(final ContextEntry[] context,
                                final InternalWorkingMemory workingMemory,
                                final LeftTuple tuple) {
        context[0].updateFromTuple(workingMemory,
                tuple);
        context[1].updateFromTuple(workingMemory,
                tuple);
        context[2].updateFromTuple(workingMemory,
                tuple);
    }

    /* (non-Javadoc)
     * @see org.drools.common.BetaNodeConstraints#updateFromFactHandle(org.drools.common.InternalFactHandle)
     */
    public void updateFromFactHandle(final ContextEntry[] context,
                                     final InternalWorkingMemory workingMemory,
                                     final InternalFactHandle handle) {
        context[0].updateFromFactHandle(workingMemory,
                handle);
        context[1].updateFromFactHandle(workingMemory,
                handle);
        context[2].updateFromFactHandle(workingMemory,
                handle);
    }

    public void resetTuple(final ContextEntry[] context) {
        context[0].resetTuple();
        context[1].resetTuple();
        context[2].resetTuple();
    }

    public void resetFactHandle(final ContextEntry[] context) {
        context[0].resetFactHandle();
        context[1].resetFactHandle();
        context[2].resetFactHandle();
    }

    /* (non-Javadoc)
     * @see org.drools.common.BetaNodeConstraints#isAllowedCachedLeft(java.lang.Object)
     */
    public boolean isAllowedCachedLeft(final ContextEntry[] context,
                                       final InternalFactHandle handle) {
        return (indexed[0] || constraints[0].isAllowedCachedLeft(context[0], handle)) &&
               (indexed[1] || constraints[1].isAllowedCachedLeft(context[1], handle)) &&
               (indexed[2] || constraints[2].isAllowedCachedLeft( context[2], handle ));
    }

    /* (non-Javadoc)
     * @see org.drools.common.BetaNodeConstraints#isAllowedCachedRight(org.drools.reteoo.ReteTuple)
     */
    public boolean isAllowedCachedRight(final ContextEntry[] context,
                                        final LeftTuple tuple) {
        return constraints[0].isAllowedCachedRight( tuple, context[0] ) &&
               constraints[1].isAllowedCachedRight( tuple, context[1] ) &&
               constraints[2].isAllowedCachedRight( tuple, context[2] );
    }

    public int hashCode() {
        return constraints[0].hashCode() ^ constraints[1].hashCode() ^ constraints[2].hashCode();
    }

    /**
     * Determine if another object is equal to this.
     *
     * @param object
     *            The object to test.
     *
     * @return <code>true</code> if <code>object</code> is equal to this,
     *         otherwise <code>false</code>.
     */
    public boolean equals(final Object object) {
        if ( this == object ) {
            return true;
        }

        if ( object == null || getClass() != object.getClass() ) {
            return false;
        }

        final TripleBetaConstraints other = (TripleBetaConstraints) object;

        if ( constraints[0] != other.constraints[0] && !constraints[0].equals(other.constraints[0]) ) {
            return false;
        }

        if ( constraints[1] != other.constraints[1] && !constraints[1].equals(other.constraints[1]) ) {
            return false;
        }

        if ( constraints[2] != other.constraints[2] && !constraints[2].equals(other.constraints[2]) ) {
            return false;
        }

        return true;
    }

    public BetaConstraints getOriginalConstraint() {
        throw new UnsupportedOperationException();
    }

    public long getListenedPropertyMask(List<String> settableProperties) {
        if (constraints[0] instanceof MvelConstraint && constraints[1] instanceof MvelConstraint && constraints[2] instanceof MvelConstraint) {
            return ((MvelConstraint)constraints[0]).getListenedPropertyMask(settableProperties) |
                    ((MvelConstraint)constraints[1]).getListenedPropertyMask(settableProperties) |
                    ((MvelConstraint)constraints[2]).getListenedPropertyMask(settableProperties);
        }
        return Long.MAX_VALUE;
    }
}
