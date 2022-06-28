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
package org.kie.efesto.compilationmanager.core.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.kie.efesto.common.api.io.IndexFile;
import org.kie.efesto.compilationmanager.api.model.EfestoResource;
import org.kie.efesto.compilationmanager.api.service.CompilationManager;
import org.kie.memorycompiler.KieMemoryCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.kie.efesto.compilationmanager.core.utils.CompilationManagerUtils.populateIndexFilesWithProcessedResource;

public class CompilationManagerImpl implements CompilationManager {

    private static final Logger logger = LoggerFactory.getLogger(CompilationManagerImpl.class.getName());

    @Override
    public Collection<IndexFile> processResource(KieMemoryCompiler.MemoryCompilerClassLoader memoryCompilerClassLoader,
                                                 EfestoResource... toProcess) {
        final Collection<IndexFile> toReturn = new LinkedHashSet<>();
        Arrays.stream(toProcess).sequential().forEach(resource -> populateIndexFilesWithProcessedResource(toReturn,
                                                                                                          resource,
                                                                                                          memoryCompilerClassLoader));
        return toReturn;
    }

}
