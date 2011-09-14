/*
 * Copyright 2010 JBoss Inc
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

package org.drools.marshalling.impl;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import org.drools.common.BaseNode;
import org.drools.common.InternalRuleBase;
import org.drools.common.InternalWorkingMemory;
import org.drools.marshalling.MarshallerFactory;
import org.drools.marshalling.ObjectMarshallingStrategy;
import org.drools.reteoo.LeftTuple;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;

import org.drools.common.Scheduler.ActivationTimerJobContext;
import org.drools.common.Scheduler.ActivationTimerOutputMarshaller;
import org.drools.rule.SlidingTimeWindow;
import org.drools.rule.SlidingTimeWindow.BehaviorJobContextTimerOutputMarshaller;
import org.drools.time.JobContext;

public class MarshallerWriteContext extends ObjectOutputStream {
    public final MarshallerWriteContext              stream;
    public final InternalRuleBase                    ruleBase;
    public final InternalWorkingMemory               wm;
    public final Map<Integer, BaseNode>              sinks;
    
    public long clockTime;

    public final Map<Short, TimersOutputMarshaller>   writersByInt;
    public final Map<Class, TimersOutputMarshaller>   writersByClass;

    public final PrintStream                         out = System.out;

    public final ObjectMarshallingStrategyStore      objectMarshallingStrategyStore;

    public final Map<LeftTuple, Integer>             terminalTupleMap;

    public final boolean                             marshalProcessInstances;
    public final boolean                             marshalWorkItems;
    public final Environment                         env;

    public MarshallerWriteContext(OutputStream stream,
                                  InternalRuleBase ruleBase,
                                  InternalWorkingMemory wm,
                                  Map<Integer, BaseNode> sinks,
                                  ObjectMarshallingStrategyStore resolverStrategyFactory,
                                  Environment env) throws IOException {
        this( stream,
              ruleBase,
              wm,
              sinks,
              resolverStrategyFactory,
              true,
              true,
              env );
    }

    public MarshallerWriteContext(OutputStream stream,
                                  InternalRuleBase ruleBase,
                                  InternalWorkingMemory wm,
                                  Map<Integer, BaseNode> sinks,
                                  ObjectMarshallingStrategyStore resolverStrategyFactory,
                                  boolean marshalProcessInstances,
                                  boolean marshalWorkItems,
                                  Environment env) throws IOException {
        super( stream );
        this.stream = this;
        this.ruleBase = ruleBase;
        this.wm = wm;
        this.sinks = sinks;
        this.writersByInt = new HashMap<Short, TimersOutputMarshaller>();
        this.writersByClass = new HashMap<Class, TimersOutputMarshaller>();
        
        this.writersByClass.put( SlidingTimeWindow.BehaviorJobContext.class, new BehaviorJobContextTimerOutputMarshaller() );
        this.writersByInt.put( PersisterEnums.BEHAVIOR_TIMER, this.writersByClass.get( SlidingTimeWindow.BehaviorJobContext.class ) );
        
        this.writersByClass.put( ActivationTimerJobContext.class, new ActivationTimerOutputMarshaller() );
        this.writersByInt.put( PersisterEnums.ACTIVATION_TIMER,  this.writersByClass.get(ActivationTimerJobContext.class) );

        if ( resolverStrategyFactory == null ) {
            ObjectMarshallingStrategy[] strats = (ObjectMarshallingStrategy[]) env.get( EnvironmentName.OBJECT_MARSHALLING_STRATEGIES );
            if ( strats == null ) {
                strats = new ObjectMarshallingStrategy[]{MarshallerFactory.newSerializeMarshallingStrategy()};
            }
            this.objectMarshallingStrategyStore = new ObjectMarshallingStrategyStore( strats );
        }
        else {
            this.objectMarshallingStrategyStore = resolverStrategyFactory;
        }

        this.terminalTupleMap = new IdentityHashMap<LeftTuple, Integer>();

        this.marshalProcessInstances = marshalProcessInstances;
        this.marshalWorkItems = marshalWorkItems;
        this.env = env;
    }

}
