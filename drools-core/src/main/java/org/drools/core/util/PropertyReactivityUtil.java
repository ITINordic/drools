/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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

package org.drools.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.kie.api.definition.type.Modifies;

import static org.drools.util.ClassUtils.getter2property;

public class PropertyReactivityUtil {

    public static List<String> getAccessibleProperties(Class<?> clazz ) {
        Set<PropertyInClass> props = new TreeSet<>();
        for (Method m : clazz.getMethods()) {
            if (m.getParameterTypes().length == 0) {
                String propName = getter2property(m.getName());
                if (propName != null && !propName.equals( "class" )) {
                    props.add( new PropertyInClass( propName, m.getDeclaringClass() ) );
                }
            }

            processModifiesAnnotation(clazz, props, m);
        }

        for (Field f : clazz.getFields()) {
            if ( Modifier.isPublic( f.getModifiers() ) && !Modifier.isStatic( f.getModifiers() ) ) {
                props.add( new PropertyInClass( f.getName(), f.getDeclaringClass() ) );
            }
        }

        List<String> accessibleProperties = new ArrayList<>();
        for ( PropertyInClass setter : props ) {
            accessibleProperties.add(setter.setter);
        }
        return accessibleProperties;
    }

    private static void processModifiesAnnotation(Class<?> clazz, Set<PropertyInClass> props, Method m ) {
        Modifies modifies = m.getAnnotation( Modifies.class );
        if (modifies != null) {
            for (String prop : modifies.value()) {
                prop = prop.trim();
                try {
                    Field field = clazz.getField(prop);
                    props.add( new PropertyInClass( field.getName(), field.getDeclaringClass() ) );
                } catch (NoSuchFieldException e) {
                    String getter = "get" + prop.substring(0, 1).toUpperCase() + prop.substring(1);
                    try {
                        Method method = clazz.getMethod(getter);
                        props.add( new PropertyInClass( prop, method.getDeclaringClass() ) );
                    } catch (NoSuchMethodException e1) {
                        getter = "is" + prop.substring(0, 1).toUpperCase() + prop.substring(1);
                        try {
                            Method method = clazz.getMethod(getter);
                            props.add( new PropertyInClass( prop, method.getDeclaringClass() ) );
                        } catch (NoSuchMethodException e2) {
                            throw new RuntimeException(e2);
                        }
                    }
                }
            }
        }
    }

    private static class PropertyInClass implements Comparable {
        private final String setter;
        private final Class<?> clazz;

        private PropertyInClass( String setter, Class<?> clazz ) {
            this.setter = setter;
            this.clazz = clazz;
        }

        public int compareTo(Object o) {
            PropertyInClass other = (PropertyInClass) o;
            if (clazz == other.clazz) {
                return setter.compareTo(other.setter);
            }
            return clazz.isAssignableFrom(other.clazz) ? -1 : 1;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof PropertyInClass)) {
                return false;
            }
            PropertyInClass other = (PropertyInClass) obj;
            return clazz == other.clazz && setter.equals(other.setter);
        }

        @Override
        public int hashCode() {
            return 29 * clazz.hashCode() + 31 * setter.hashCode();
        }
    }
}
