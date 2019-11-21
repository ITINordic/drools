/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.dmn.backend.marshalling.v1_3.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kie.dmn.model.api.DMNModelInstrumentedBase;
import org.kie.dmn.model.api.DecisionRule;
import org.kie.dmn.model.api.LiteralExpression;
import org.kie.dmn.model.api.RuleAnnotation;
import org.kie.dmn.model.api.UnaryTests;
import org.kie.dmn.model.v1_3.TDecisionRule;

public class DecisionRuleConverter extends DMNElementConverter {
    public static final String OUTPUT_ENTRY = "outputEntry";
    public static final String INPUT_ENTRY = "inputEntry";
    public static final String ANNOTATION_ENTRY = "annotationEntry";

    @Override
    protected void assignChildElement(Object parent, String nodeName, Object child) {
        DecisionRule dr = (DecisionRule) parent;

        if (INPUT_ENTRY.equals(nodeName)) {
            dr.getInputEntry().add((UnaryTests) child);
        } else if (OUTPUT_ENTRY.equals(nodeName)) {
            dr.getOutputEntry().add((LiteralExpression) child);
        } else if (ANNOTATION_ENTRY.equals(nodeName)) {
            dr.getAnnotationEntry().add((RuleAnnotation) child);
        } else {
            super.assignChildElement(parent, nodeName, child);
        }
    }

    @Override
    protected void assignAttributes(HierarchicalStreamReader reader, Object parent) {
        super.assignAttributes(reader, parent);
        
        // no attributes.
    }

    @Override
    protected void writeChildren(HierarchicalStreamWriter writer, MarshallingContext context, Object parent) {
        super.writeChildren(writer, context, parent);
        DecisionRule dr = (DecisionRule) parent;
        
        for (UnaryTests ie : dr.getInputEntry()) {
            writeChildrenNode(writer, context, ie, INPUT_ENTRY);
        }
        for (LiteralExpression oe : dr.getOutputEntry()) {
            writeChildrenNode(writer, context, oe, OUTPUT_ENTRY);
        }
        for (RuleAnnotation a : dr.getAnnotationEntry()) {
            writeChildrenNode(writer, context, a, ANNOTATION_ENTRY);
        }
    }

    @Override
    protected void writeAttributes(HierarchicalStreamWriter writer, Object parent) {
        super.writeAttributes(writer, parent);
        
        // no attributes.
    }

    public DecisionRuleConverter(XStream xstream) {
        super(xstream);
    }

    @Override
    protected DMNModelInstrumentedBase createModelObject() {
        return new TDecisionRule();
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(TDecisionRule.class);
    }

}
