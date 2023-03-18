/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.reliability;

import org.drools.core.common.InternalWorkingMemory;
import org.drools.core.impl.RuleBase;
import org.drools.core.phreak.PropagationList;
import org.drools.kiesession.agenda.DefaultAgenda;
import org.infinispan.Cache;

public class ReliableAgenda extends DefaultAgenda {

    public ReliableAgenda() { }

    public ReliableAgenda(RuleBase kBase) {
        super( kBase );
    }

    public ReliableAgenda(RuleBase kBase, boolean initMain) {
        super( kBase, initMain );
    }

    @Override
    public void setWorkingMemory(InternalWorkingMemory workingMemory) {
        super.setWorkingMemory(workingMemory);
    }

    @Override
    protected PropagationList createPropagationList() {
        Cache<String, Object> componentsCache = CacheManager.INSTANCE.getOrCreateCacheForSession(workingMemory, "components");
        ReliablePropagationList propagationList = (ReliablePropagationList) componentsCache.get("PropagationList");
        if (propagationList == null) {
            propagationList = new ReliablePropagationList(workingMemory);
            componentsCache.put("PropagationList", propagationList);
        } else {
            propagationList = new ReliablePropagationList(workingMemory, propagationList);
        }
        return propagationList;
    }
}
