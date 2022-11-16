/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
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

package org.drools.core.reteoo.builder;

import org.drools.core.rule.GroupElement;
import org.drools.core.rule.NamedConsequence;
import org.drools.core.rule.RuleConditionElement;
import org.drools.core.time.impl.Timer;

public class NamedConsequenceBuilder implements ReteooComponentBuilder {

    public void build(BuildContext context, BuildUtils utils, RuleConditionElement rce) {
        NamedConsequence namedConsequence = (NamedConsequence) rce;

        Timer timer = context.getRule().getTimer();

        ReteooRuleBuilder.buildTerminalNodeForConsequence(context, (GroupElement) context.peek(), context.getSubRuleIndex(),
                                                          namedConsequence, timer, utils);
        context.setTerminated(true); // assumes named consequences, not in a conditional branch are always terminal.
    }

    public boolean requiresLeftActivation(BuildUtils utils, RuleConditionElement rce) {
        return false;
    }
}
