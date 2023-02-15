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

package org.kie.internal.conf;

import org.kie.api.KieBaseConfiguration;
import org.kie.api.conf.KieBaseOption;
import org.kie.api.conf.MultiValueKieBaseOption;
import org.kie.api.conf.SingleValueKieBaseOption;
import org.kie.internal.utils.ChainedProperties;

public class CompositeBaseConfiguration extends CompositeConfiguration<KieBaseOption, SingleValueKieBaseOption, MultiValueKieBaseOption> implements KieBaseConfiguration {

    public CompositeBaseConfiguration(ChainedProperties properties, ClassLoader classloader,
                                      ConfigurationFactory<KieBaseOption, SingleValueKieBaseOption, MultiValueKieBaseOption>... factories) {
        super(properties, classloader, factories);
    }

}
