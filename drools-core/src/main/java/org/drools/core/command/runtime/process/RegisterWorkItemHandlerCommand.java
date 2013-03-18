/*
 * Copyright 2010 JBoss Inc
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

package org.drools.core.command.runtime.process;


import org.drools.core.command.impl.GenericCommand;
import org.drools.core.command.impl.KnowledgeCommandContext;
import org.kie.internal.command.Context;
import org.kie.runtime.KieSession;
import org.kie.runtime.process.WorkItemHandler;

public class RegisterWorkItemHandlerCommand implements GenericCommand<Object> {

    private WorkItemHandler handler;
    private String workItemName;

        public RegisterWorkItemHandlerCommand() {
        }

        public RegisterWorkItemHandlerCommand(String workItemName, WorkItemHandler handler) {
            this.handler = handler;
            this.workItemName = workItemName;
        }
        
    public WorkItemHandler getHandler() {
        return handler;
    }

    public void setHandler(WorkItemHandler handler) {
        this.handler = handler;
    }

    public String getWorkItemName() {
        return workItemName;
    }

    public void setWorkItemName(String workItemName) {
        this.workItemName = workItemName;
    }

    public Object execute(Context context) {
        KieSession ksession = ((KnowledgeCommandContext) context).getKieSession();
        ksession.getWorkItemManager().registerWorkItemHandler(workItemName, handler);
        return null;
    }

    public String toString() {
        return "session.getWorkItemManager().registerWorkItemHandler("
            + workItemName + ", " + handler +  ");";
    }

}
