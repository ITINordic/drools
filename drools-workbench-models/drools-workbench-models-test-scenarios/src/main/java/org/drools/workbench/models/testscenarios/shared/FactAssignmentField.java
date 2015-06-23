/*
 * Copyright 2015 JBoss Inc
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

package org.drools.workbench.models.testscenarios.shared;

public class FactAssignmentField
        implements Field {

    private String fieldName;

    private Fact fact;

    public FactAssignmentField() {
    }

    public FactAssignmentField( final String fieldName,
                                final String factType ) {
        this.fieldName = fieldName;
        this.fact = new Fact( factType );
    }

    public void setName( final String fieldName ) {
        this.fieldName = fieldName;
    }

    public void setFact( final Fact fact ) {
        this.fact = fact;
    }

    @Override
    public String getName() {
        return fieldName;
    }

    public Fact getFact() {
        return fact;
    }
}
