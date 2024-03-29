/*
 * Copyright 2009-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License i distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.paoding.rose.jade.context.spring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import net.paoding.rose.jade.statement.DefaultInterpreterFactory;
import net.paoding.rose.jade.statement.Interpreter;
import net.paoding.rose.jade.statement.InterpreterComparator;
import net.paoding.rose.jade.statement.InterpreterFactory;
import net.paoding.rose.jade.statement.StatementMetaData;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @author qieqie
 * 
 */
public class SpringInterpreterFactory implements InterpreterFactory, ApplicationContextAware {

    private DefaultInterpreterFactory interpreterFactory;

    private ApplicationContext applicationContext;

    public SpringInterpreterFactory() {
    }

    public SpringInterpreterFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Interpreter[] getInterpreters(StatementMetaData metaData) {
        if (interpreterFactory == null) {
            init();
        }
        return interpreterFactory.getInterpreters(metaData);
    }

    private void init() {
        synchronized (this) {
            if (interpreterFactory == null) {
                @SuppressWarnings("unchecked")
                Map<String, Interpreter> map = applicationContext.getBeansOfType(Interpreter.class);
                ArrayList<Interpreter> interpreters = new ArrayList<Interpreter>(map.values());
                Collections.sort(interpreters, new InterpreterComparator());
                interpreterFactory = new DefaultInterpreterFactory(interpreters);
            }
        }
    }

}
