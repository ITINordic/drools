/*
 * Copyright 2005 JBoss Inc
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

package org.kie.dmn.core.compiler.alphanetbased;

import java.util.ArrayList;
import java.util.List;

import org.drools.compiler.builder.impl.KnowledgeBuilderImpl;
import org.drools.core.base.ClassObjectType;
import org.drools.core.common.DefaultFactHandle;
import org.drools.core.common.InternalFactHandle;
import org.drools.core.common.InternalWorkingMemory;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.drools.core.reteoo.AlphaNode;
import org.drools.core.reteoo.EntryPointNode;
import org.drools.core.reteoo.LeftInputAdapterNode;
import org.drools.core.reteoo.ModifyPreviousTuples;
import org.drools.core.reteoo.ObjectSource;
import org.drools.core.reteoo.ObjectTypeNode;
import org.drools.core.reteoo.builder.BuildContext;
import org.drools.core.reteoo.compiled.CompiledNetwork;
import org.drools.core.rule.Declaration;
import org.drools.core.rule.Pattern;
import org.drools.core.spi.PropagationContext;
import org.drools.model.SingleConstraint;
import org.drools.model.Variable;
import org.drools.model.constraints.SingleConstraint1;
import org.drools.model.functions.Predicate1;
import org.drools.modelcompiler.constraints.ConstraintEvaluator;
import org.drools.modelcompiler.constraints.LambdaConstraint;
import org.kie.dmn.feel.lang.EvaluationContext;

import static org.drools.compiler.reteoo.compiled.ObjectTypeNodeCompiler.compile;
import static org.drools.core.reteoo.builder.BuildUtils.attachNode;
import static org.drools.model.DSL.declarationOf;
import static org.kie.dmn.feel.codegen.feel11.CompiledFEELSemanticMappings.gracefulEq;
import static org.kie.dmn.feel.codegen.feel11.CompiledFEELSemanticMappings.gt;
import static org.kie.dmn.feel.codegen.feel11.CompiledFEELSemanticMappings.includes;
import static org.kie.dmn.feel.codegen.feel11.CompiledFEELSemanticMappings.lt;
import static org.kie.dmn.feel.codegen.feel11.CompiledFEELSemanticMappings.range;
public class CompiledAlphaNetwork {

    private final ResultCollector resultCollector = new ResultCollector();

    private CompiledNetwork compiledNetwork;

    public Object evaluate( EvaluationContext evalCtx ) {
        resultCollector.results.clear();
        compiledNetwork.assertObject( new DefaultFactHandle( 1L, evalCtx ), null, null );
        return applyHitPolicy( resultCollector.results );
    }

    private Object applyHitPolicy( List<Object> results ) {
        return results.get(0);
    }

    public static final org.kie.dmn.feel.runtime.UnaryTest UT1 = (feelExprCtx, left) -> gracefulEq(feelExprCtx, "false", left);

    public static final java.math.BigDecimal K_80 = new java.math.BigDecimal(80, java.math.MathContext.DECIMAL128);
    public static final java.math.BigDecimal K_90 = new java.math.BigDecimal(90, java.math.MathContext.DECIMAL128);
    public static final java.math.BigDecimal K_100 = new java.math.BigDecimal(100, java.math.MathContext.DECIMAL128);
    public static final java.math.BigDecimal K_110 = new java.math.BigDecimal(110, java.math.MathContext.DECIMAL128);
    public static final java.math.BigDecimal K_120 = new java.math.BigDecimal(120, java.math.MathContext.DECIMAL128);
    public static final java.math.BigDecimal K_130 = new java.math.BigDecimal(130, java.math.MathContext.DECIMAL128);

    public static final org.kie.dmn.feel.runtime.UnaryTest UT2 = (feelExprCtx, left) -> lt(left, K_100);


    public static final org.kie.dmn.feel.runtime.UnaryTest UT3 = (feelExprCtx, left) -> includes(feelExprCtx, range(feelExprCtx,
                                                                                                                    org.kie.dmn.feel.runtime.Range.RangeBoundary.CLOSED,
                                                                                                                    K_100, K_120,
                                                                                                                    org.kie.dmn.feel.runtime.Range.RangeBoundary.OPEN), left);


    public static final org.kie.dmn.feel.runtime.UnaryTest UT4 = (feelExprCtx, left) -> includes(feelExprCtx, range(feelExprCtx,
                                                                                                                    org.kie.dmn.feel.runtime.Range.RangeBoundary.CLOSED,
                                                                                                                    K_120, K_130,
                                                                                                                    org.kie.dmn.feel.runtime.Range.RangeBoundary.CLOSED), left);

    public static final org.kie.dmn.feel.runtime.UnaryTest UT5 = (feelExprCtx, left) -> gt(left, K_130);

    public static final org.kie.dmn.feel.runtime.UnaryTest UT6 = (feelExprCtx, left) -> gracefulEq(feelExprCtx, "true", left);

    public static final org.kie.dmn.feel.runtime.UnaryTest UT7 = (feelExprCtx, left) -> lt(left, K_80);

    public static final org.kie.dmn.feel.runtime.UnaryTest UT8 = (feelExprCtx, left) -> includes(feelExprCtx, range(feelExprCtx,
                                                                                                                    org.kie.dmn.feel.runtime.Range.RangeBoundary.CLOSED,
                                                                                                                    K_80, K_90,
                                                                                                                    org.kie.dmn.feel.runtime.Range.RangeBoundary.OPEN), left);

    public static final org.kie.dmn.feel.runtime.UnaryTest UT9 = (feelExprCtx, left) -> includes(feelExprCtx, range(feelExprCtx,
                                                                                                                    org.kie.dmn.feel.runtime.Range.RangeBoundary.CLOSED,
                                                                                                                    K_90, K_110,
                                                                                                                    org.kie.dmn.feel.runtime.Range.RangeBoundary.CLOSED), left);

    public static final org.kie.dmn.feel.runtime.UnaryTest UT10 = (feelExprCtx, left) -> gt(left, K_110);

    public static CompiledAlphaNetwork generateCompiledNetwork() {
        CompiledAlphaNetwork network = new CompiledAlphaNetwork();

        NetworkBuilderContext ctx = new NetworkBuilderContext();

        AlphaNode alphac1r1 = createAlphaNode(ctx, ctx.otn, x -> UT1.apply(x, x.getValue("Existing Customer")));

        AlphaNode alphac2r1 = createAlphaNode(ctx, alphac1r1, x -> UT2.apply(x, x.getValue("Application Risk Score")));
        addResultSink(ctx, network, alphac2r1, "HIGH");
        AlphaNode alphac2r2 = createAlphaNode(ctx, alphac1r1, x -> UT3.apply(x, x.getValue("Application Risk Score")));
        addResultSink(ctx, network, alphac2r2, "MEDIUM");
        AlphaNode alphac2r3 = createAlphaNode(ctx, alphac1r1, x -> UT4.apply(x, x.getValue("Application Risk Score")));
        addResultSink(ctx, network, alphac2r3, "LOW");
        AlphaNode alphac2r4 = createAlphaNode(ctx, alphac1r1, x -> UT5.apply(x, x.getValue("Application Risk Score")));
        addResultSink(ctx, network, alphac2r4, "VERY LOW");

        AlphaNode alphac1r5 = createAlphaNode(ctx, ctx.otn, x -> UT6.apply(x, x.getValue("Existing Customer")));

        AlphaNode alphac2r5 = createAlphaNode(ctx, alphac1r5, x -> UT7.apply(x, x.getValue("Application Risk Score")));
        addResultSink(ctx, network, alphac2r5, "DECLINE");
        AlphaNode alphac2r6 = createAlphaNode(ctx, alphac1r5, x -> UT8.apply(x, x.getValue("Application Risk Score")));
        addResultSink(ctx, network, alphac2r6, "HIGH");
        AlphaNode alphac2r7 = createAlphaNode(ctx, alphac1r5, x -> UT9.apply(x, x.getValue("Application Risk Score")));
        addResultSink(ctx, network, alphac2r7, "MEDIUM");
        AlphaNode alphac2r8 = createAlphaNode(ctx, alphac1r5, x -> UT10.apply(x, x.getValue("Application Risk Score")));
        addResultSink(ctx, network, alphac2r8, "LOW");

        network.compiledNetwork = compile(new KnowledgeBuilderImpl(ctx.kBase), ctx.otn);
        network.compiledNetwork.setObjectTypeNode(ctx.otn);
        return network;
    }

    private static void addResultSink( NetworkBuilderContext ctx, CompiledAlphaNetwork network, ObjectSource source, Object result ) {
        source.addObjectSink( new ResultCollectorAlphaSink( ctx.buildContext.getNextId(), source, ctx.buildContext, result, network.resultCollector ) );
    }

    private static AlphaNode createAlphaNode( NetworkBuilderContext ctx, ObjectSource source, Predicate1<EvaluationContext> predicate ) {
        SingleConstraint constraint = new SingleConstraint1(ctx.variable, predicate);

        LambdaConstraint c1 = new LambdaConstraint(new ConstraintEvaluator(new Declaration[] { ctx.declaration }, constraint));
        return attachNode( ctx.buildContext, new AlphaNode( ctx.buildContext.getNextId(), c1, source, ctx.buildContext ) );
    }

    static class ResultCollector {
        final List<Object> results = new ArrayList<>();
    }

    public static class ResultCollectorAlphaSink extends LeftInputAdapterNode {

        private final Object result;
        private final ResultCollector resultCollector;

        public ResultCollectorAlphaSink( int id, ObjectSource source, BuildContext context, Object result, ResultCollector resultCollector ) {
            super( id, source, context );
            this.result = result;
            this.resultCollector = resultCollector;
        }

        @Override
        public void assertObject( InternalFactHandle factHandle, PropagationContext propagationContext, InternalWorkingMemory workingMemory ) {
            resultCollector.results.add(result);
        }

        @Override
        public void modifyObject( InternalFactHandle factHandle, ModifyPreviousTuples modifyPreviousTuples, PropagationContext context, InternalWorkingMemory workingMemory ) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void byPassModifyToBetaNode( InternalFactHandle factHandle, ModifyPreviousTuples modifyPreviousTuples, PropagationContext context, InternalWorkingMemory workingMemory ) {
            throw new UnsupportedOperationException();
        }
    }

    private static class NetworkBuilderContext {
        public InternalKnowledgeBase kBase;
        public BuildContext buildContext;
        public Variable<EvaluationContext> variable;
        public Declaration declaration;
        public ObjectTypeNode otn;

        public NetworkBuilderContext() {
            kBase = ( InternalKnowledgeBase ) KnowledgeBaseFactory.newKnowledgeBase();
            buildContext = new BuildContext( kBase );
            EntryPointNode entryPoint = buildContext.getKnowledgeBase().getRete().getEntryPointNodes().values().iterator().next();
            ClassObjectType objectType = new ClassObjectType( EvaluationContext.class );
            variable = declarationOf( EvaluationContext.class, "$ctx" );

            Pattern pattern = new Pattern( 1, objectType, "$ctx" );
            declaration = pattern.getDeclaration();

            otn = new ObjectTypeNode( buildContext.getNextId(), entryPoint, objectType, buildContext );
            buildContext.setObjectSource( otn );
        }
    }
}
