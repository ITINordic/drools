/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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
package org.kie.efesto.compilationmanager.core.mocks;

import java.util.Collections;
import java.util.List;

import org.kie.efesto.common.api.identifiers.LocalUri;
import org.kie.efesto.common.api.identifiers.ModelLocalUriId;
import org.kie.efesto.compilationmanager.api.model.EfestoCallableOutput;

public class MockEfestoCallableOutputE implements EfestoCallableOutput {

    private ModelLocalUriId modelLocalUriId = new ModelLocalUriId(LocalUri.parse("/mock/efesto/output/moduleE"));
    @Override
    public ModelLocalUriId getModelLocalUriId() {
        return modelLocalUriId;
    }

    @Override
    public List<String> getFullClassNames() {
        return Collections.singletonList("mock.efesto.output.ModuleE");
    }
}
