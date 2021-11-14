/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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

package org.drools.xml.support.converters;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.drools.compiler.kproject.models.WorkItemHandlerModelImpl;

public class WorkItemHandelerConverter extends AbstractXStreamConverter {

    public WorkItemHandelerConverter() {
        super(WorkItemHandlerModelImpl.class);
    }

    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        WorkItemHandlerModelImpl wih = (WorkItemHandlerModelImpl) value;
        writer.addAttribute("name", wih.getName());
        writer.addAttribute("type", wih.getType());
    }

    public Object unmarshal(HierarchicalStreamReader reader, final UnmarshallingContext context) {
        final WorkItemHandlerModelImpl wih = new WorkItemHandlerModelImpl();
        wih.setName(reader.getAttribute("name"));
        wih.setType(reader.getAttribute("type"));
        return wih;
    }
}