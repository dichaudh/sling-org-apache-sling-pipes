/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.pipes;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.event.jobs.Job;
import org.osgi.annotation.versioning.ProviderType;

import java.util.Map;

/**
 * Builder and Runner of a pipe, based on a fluent API, for script and java usage.
 */
@ProviderType
public interface PipeBuilder {
    /**
     * attach a new pipe to the current context
     * @param type resource type (should be registered by the plumber)
     * @return updated instance of PipeBuilder
     */
    PipeBuilder pipe(String type);

    /**
     * attach a move pipe to the current context
     * @param expr target of the resource to move
     * @return updated instance of PipeBuilder
     */
    PipeBuilder mv(String expr);

    /**
     * attach a write pipe to the current context
     * @param conf configuration parameters
     * @return updated instance of PipeBuilder
     * @throws IllegalAccessException in case it's called with bad configuration
     */
    PipeBuilder write(Object... conf) throws IllegalAccessException;

    /**
     * attach a filter pipe to the current context
     * @param conf configuration parameters
     * @return updated instance of PipeBuilder
     * @throws IllegalAccessException in case it's called with bad configuration
     */
    PipeBuilder grep(Object... conf) throws IllegalAccessException;

    /**
     * attach an authorizable pipe to the current context
     * @param conf configuration key value pairs for authorizable (see pipe's doc)
     * @return updated instance of PipeBuilder
     * @throws IllegalAccessException in case it's called with bad configuration
     */
    PipeBuilder auth(Object... conf) throws IllegalAccessException;

    /**
     * attach a xpath pipe to the current context
     * @param expr xpath expression
     * @return updated instance of PipeBuilder
     */
    PipeBuilder xpath(String expr);

    /**
     * attach a sling query children pipe to the current context
     * @param expr sling query expression
     * @return updated instance of PipeBuilder
     */
    PipeBuilder children(String expr);

    /**
     * attach a sling query siblings pipe to the current context
     * @param expr sling query expression
     * @return updated instance of PipeBuilder
     */
    PipeBuilder siblings(String expr);

    /**
     * attach a rm pipe to the current context
     * @return updated instance of PipeBuilder
     */
    PipeBuilder rm();

    /**
     * attach a csv pipe to the current context
     * @param expr csv expr or URL or path in the resource tree
     * @return updated instance of PipeBuilder
     */
    PipeBuilder csv(String expr);

    /**
     * attach a json pipe to the current context
     * @param expr json expr or URL or path in the resource tree
     * @return updated instance of PipeBuilder
     */
    PipeBuilder json(String expr);

    /**
     * attach a Regexp pipe to the current context
     * @param expr text expr or URL or path in the resource tree
     * @return updated instance of PipeBuilder
     */
    PipeBuilder egrep(String expr);

    /**
     * Attach a path pipe to the current context
     * @param expr path to create
     * @return updated instance of PipeBuilder
     */
    PipeBuilder mkdir(String expr);

    /**
     * attach a base pipe to the current context
     * @param path pipe path
     * @return updated instance of PipeBuilder
     */
    PipeBuilder echo(String path);

    /**
     * attach a traverse pipe to the current context
     * @return updated instance of PipeBuilder
     */
    PipeBuilder traverse();

    /**
     * attach a sling query parent pipe to the current context
     * @return updated instance of PipeBuilder
     */
    PipeBuilder parent();

    /**
     * attach a sling query parents pipe to the current context
     * @param expr expression
     * @return updated instance of PipeBuilder
     */
    PipeBuilder parents(String expr);

    /**
     * attach a sling query closest pipe to the current context
     * @param expr expression
     * @return updated instance of PipeBuilder
     */
    PipeBuilder closest(String expr);

    /**
     * attach a sling query find pipe to the current context
     * @param expr expression
     * @return updated instance of PipeBuilder
     */
    PipeBuilder $(String expr);

    /**
     * attach a reference pipe to the current context
     * @param expr reference
     * @return updated instance of PipeBuilder
     */
    PipeBuilder ref(String expr);

    /**
     * attach a package pipe, in filter collection mode as default
     * @param expr path of the pipe
     * @return updated instance of PipeBuilder
     */
    PipeBuilder pkg(String expr);

    /**
     * attach a not pipe to the current context
     * @param expr reference
     * @return updated instance of PipeBuilder
     */
    PipeBuilder not(String expr);

    /**
     * parameterized current pipe in the context
     * @param params key value pair of parameters
     * @return updated instance of PipeBuilder
     * @throws IllegalAccessException in case it's called with wrong # of arguments
     */
    PipeBuilder with(Object... params) throws IllegalAccessException;

    /**
     * set an expr configuration to the current pipe in the context
     * @param value expression value
     * @return updated instance of PipeBuilder
     * @throws IllegalAccessException in case it's called in a bad time
     */
    PipeBuilder expr(String value) throws IllegalAccessException;

    /**
     * sets a pipe name, important in case you want to reuse it in another expression
     * @param name to overwrite default binding name (otherwise it will be "one", "two", ...)
     * @return updated instance of PipeBuilder
     * @throws IllegalAccessException in case it's called before a pipe is configured
     */
    PipeBuilder name(String name) throws IllegalAccessException;

    /**
     * set a path configuration to the current pipe in the context
     * @param value path value
     * @return updated instance of PipeBuilder
     * @throws IllegalAccessException in case it's called before a pipe is configured
     */
    PipeBuilder path(String value) throws IllegalAccessException;

    /**
     * Building up a set of configurations for the current pipe
     * @param properties configuration key value pairs (must be an even number of arguments)
     * @return updated instance of PipeBuilder
     * @throws IllegalAccessException in case it's called in a bad time
     */
    PipeBuilder conf(Object... properties) throws IllegalAccessException;


    /**
     * add outputs passed key
     * @param keys properties of the outputs resource to output
     * @return current instance of PipeBuilder
     */
    PipeBuilder outputs(String ... keys);

    /**
     * builds a configured pipe. The configuration will be placed in a balanced tree under <code>/var/pipes</code>
     * @return Created (not executed) Pipe instance.
     * @throws PersistenceException error occuring when saving the pipe configuration
     */
    Pipe build() throws PersistenceException;

    /**
     * builds a configured pipe. The configuration will be placed under <code>path</code>
     * @param path path under which the generated configuration should be stored
     * @return Created (not executed) Pipe instance
     * @throws PersistenceException error occuring when saving the pipe configuration
     */
    Pipe build(String path) throws PersistenceException;

    /**
     * builds and run configured pipe
     * @return set of resource path, output of the pipe execution
     * @throws Exception exceptions thrown by the build or the pipe execution itself
     */
    ExecutionResult run() throws Exception;

    /**
     * allow execution of a pipe, with more parameter
     * @param bindings additional bindings
     * @return set of resource path, output of the pipe execution
     * @throws Exception in case something goes wrong with pipe execution
     */
    ExecutionResult run(Map bindings) throws Exception;

    /**
     * allow execution of a pipe, with more parameter
     * @param bindings additional bindings, should be key/value format
     * @return set of resource path, output of the pipe execution
     * @throws Exception in case something goes wrong with pipe execution
     */
    ExecutionResult runWith(Object... bindings) throws Exception;

    /**
     * run a pipe asynchronously
     * @param bindings additional bindings for the execution (can be null)
     * @return registered job for the pipe execution
     * @throws PersistenceException in case something goes wrong in the job creation
     */
    Job runAsync(Map bindings) throws PersistenceException;
}
