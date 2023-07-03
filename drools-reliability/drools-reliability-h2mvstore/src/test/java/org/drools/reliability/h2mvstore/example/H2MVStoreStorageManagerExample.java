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

package org.drools.reliability.h2mvstore.example;

import java.util.ArrayList;
import java.util.List;

import org.drools.model.codegen.ExecutableModelProject;
import org.drools.reliability.h2mvstore.H2MVStoreStorageManager;
import org.drools.reliability.h2mvstore.Person;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.PersistedSessionOption;
import org.kie.internal.utils.KieHelper;



public class H2MVStoreStorageManagerExample {


    public static final String BASIC_RULE =
            "import " + Person.class.getCanonicalName() + ";" +
                    "global java.util.List results;" +
                    "rule X when\n" +
                    "  $s: String()\n" +
                    "  $p: Person( getName().startsWith($s) )\n" +
                    "then\n" +
                    "  results.add( $p.getName() );\n" +
                    "end";

    public static void main(String[] args) {
        H2MVStoreStorageManager.cleanUpDatabase();
        System.out.println("### Deleted database file : " + H2MVStoreStorageManager.STORE_FILE_NAME);

        KieSession session = getKieSession(PersistedSessionOption.newSession().withPersistenceStrategy(PersistedSessionOption.PersistenceStrategy.STORES_ONLY));

        long savedSessionId = session.getIdentifier();
        System.out.println("savedSessionId = " + savedSessionId);

        session.insert("M");
        session.insert(new Person("Mario", 40));

        //--- Simulate a crash
        System.out.println("Simulating a crash");
    }

    public static KieSession getKieSession(PersistedSessionOption option) {
        KieBase kbase = new KieHelper().addContent(BASIC_RULE, ResourceType.DRL).build(ExecutableModelProject.class);
        KieSessionConfiguration conf = KieServices.get().newKieSessionConfiguration();
        conf.setOption(option);
        KieSession session = kbase.newKieSession(conf, null);
        List<Object> results = new ArrayList<>();
        session.setGlobal("results", results);
        return session;
    }
}
