/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.drlonyaml.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class StringThen extends AbstractThen {
    @JsonValue
    private String then;
    
    @JsonCreator
    public StringThen(final String then) {
        this.then = then;
    }
    
    private StringThen() {
        // no-arg.
    }

    public static StringThen from(String then) {
        StringThen result = new StringThen();
        result.then = then;
        return result;
    }

    public String getThen() {
        return then;
    }
}
