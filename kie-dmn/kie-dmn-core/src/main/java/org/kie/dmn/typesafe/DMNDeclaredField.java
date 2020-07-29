/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.dmn.typesafe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.drools.core.util.StringUtils;
import org.drools.modelcompiler.builder.generator.declaredtype.api.AnnotationDefinition;
import org.drools.modelcompiler.builder.generator.declaredtype.api.FieldDefinition;
import org.drools.modelcompiler.builder.generator.declaredtype.api.SimpleAnnotationDefinition;
import org.kie.dmn.api.core.DMNType;
import org.kie.dmn.feel.codegen.feel11.CodegenStringUtil;
import org.kie.dmn.feel.runtime.UnaryTestImpl;

import static com.github.javaparser.StaticJavaParser.parseClassOrInterfaceType;
import static org.drools.core.util.StringUtils.ucFirst;

public class DMNDeclaredField implements FieldDefinition {

    private static final String OBJECT_TYPE = "Object";

    private DMNAllTypesIndex index;
    private String fieldName;
    private String originalMapKey;
    private DMNType fieldDMNType;
    private DMNStronglyCodeGenConfig codeGenConfig;

    DMNDeclaredField(DMNAllTypesIndex index, Map.Entry<String, DMNType> dmnField, DMNStronglyCodeGenConfig codeGenConfig) {
        this.index = index;
        this.fieldName = CodegenStringUtil.escapeIdentifier(StringUtils.lcFirst(dmnField.getKey()));
        this.originalMapKey = dmnField.getKey();
        this.fieldDMNType = dmnField.getValue();
        this.codeGenConfig = codeGenConfig;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    public String getOriginalMapKey() {
        return originalMapKey;
    }

    @Override
    public String getObjectType() {
        String asJava = index.asJava(fieldDMNType);
        if (fieldDMNType.isCollection() && fieldDMNType.isComposite() && fieldDMNType.getBaseType() == null) {
            return DMNAllTypesIndex.juCollection(asJava); // Anonymous inner composite collection, need to render as a FIELD of Java type Collection<GeneratedType>
        }
        return asJava;
    }

    // This returns the generic type i.e. when Collection<String> then String
    private String fieldTypeUnwrapped() {
        if (fieldDMNType.isCollection()) {
            return index.asJava(DMNTypeUtils.genericOfCollection(fieldDMNType));
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String getInitExpr() {
        return null;
    }

    @Override
    public List<AnnotationDefinition> getterAnnotations() {
        List<AnnotationDefinition> annotations = new ArrayList<>();
        annotations.add(new SimpleAnnotationDefinition("org.kie.dmn.feel.lang.FEELProperty")
                                .addValue("value", "\"" + originalMapKey + "\""));
        if (codeGenConfig.isWithJacksonAnnotation()) {
            annotations.add(new SimpleAnnotationDefinition("com.fasterxml.jackson.annotation.JsonProperty")
                                    .addValue("value", "\"" + originalMapKey + "\""));
        }
        if (codeGenConfig.isWithMPOpenApiAnnotation()) {
            if (fieldDMNType.getAllowedValues() != null && !fieldDMNType.getAllowedValues().isEmpty() && getObjectType().equals("java.lang.String")) {
                String enumeration = fieldDMNType.getAllowedValues().stream()
                                                 .map(UnaryTestImpl.class::cast)
                                                 .map(UnaryTestImpl::toString)
                                                 .collect(Collectors.joining(","));
                annotations.add(new SimpleAnnotationDefinition("org.eclipse.microprofile.openapi.annotations.media.Schema")
                                .addValue("enumeration", "{" + enumeration + "}"));
            }
        }
        return annotations;
    }

    @Override
    public boolean isKeyField() {
        return false;
    }

    @Override
    public boolean createAccessors() {
        return true;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean isFinal() {
        return false;
    }

    public BlockStmt createFromMapEntry(BlockStmt simplePropertyBlock,
                                        BlockStmt pojoPropertyBlock,
                                        BlockStmt collectionsPropertyBlock, BlockStmt collectionsBasic) {
        if (fieldDMNType.isCollection() && fieldIsBasic()) {
            return replaceTemplate(collectionsBasic, fieldTypeUnwrapped());
        } else if (fieldDMNType.isCollection() && fieldIsDifferentThanObject()) {
            return replaceTemplate(collectionsPropertyBlock, fieldTypeUnwrapped());
        } else if (fieldDMNType.isComposite()) {
            return replaceTemplate(pojoPropertyBlock, getObjectType());
        } else if (fieldIsDifferentThanObject()) {
            return replaceTemplate(simplePropertyBlock, getObjectType());
        } else if (DMNTypeUtils.isFEELAny(fieldDMNType)) { // feel:Any
            return replaceTemplate(simplePropertyBlock, getObjectType());
        } else {
            return new BlockStmt();
        }
    }

    private boolean fieldIsBasic() {
        return fieldDMNType.getBaseType() != null && !fieldDMNType.getBaseType().isComposite();
    }

    private boolean fieldIsDifferentThanObject() {
        if (DMNTypeUtils.isFEELAny(fieldDMNType)) {
            return false;
        }
        boolean isOtherObject = fieldDMNType.isCollection() ? fieldTypeUnwrapped().equals(OBJECT_TYPE) : getObjectType().equals(OBJECT_TYPE);
        return !isOtherObject;
    }

    private BlockStmt replaceTemplate(BlockStmt pojoPropertyBlock, String objectType) {
        BlockStmt clone = pojoPropertyBlock.clone();
        clone.removeComment();

        clone.findAll(NameExpr.class, this::propertyPlaceHolder)
                .forEach(n -> n.replace(new NameExpr(fieldName)));

        clone.findAll(StringLiteralExpr.class, this::propertyPlaceHolder)
                .forEach(n -> n.replace(new StringLiteralExpr(originalMapKey)));

        clone.findAll(ClassOrInterfaceType.class, this::propertyTypePlaceHolder)
                .forEach(n -> n.replace(parseClassOrInterfaceType(objectType)));

        return clone;
    }

    private boolean propertyPlaceHolder(NameExpr n) {
        return n.toString().equals("$property$");
    }

    private boolean propertyPlaceHolder(StringLiteralExpr n) {
        return n.asString().equals("$property$");
    }

    private boolean propertyTypePlaceHolder(Object n) {
        return n.toString().equals("PropertyType");
    }

    @Override
    public Optional<String> overriddenGetterName() {
        String value = "get" + CodegenStringUtil.escapeIdentifier(ucFirst(originalMapKey));
        if (value.equals("getClass")) { // see Object#getClass() exists
            value = "get_class";
        }
        return Optional.of(value);
    }

    @Override
    public Optional<String> overriddenSetterName() {
        return Optional.of("set" + CodegenStringUtil.escapeIdentifier(ucFirst(originalMapKey)));
    }

}
