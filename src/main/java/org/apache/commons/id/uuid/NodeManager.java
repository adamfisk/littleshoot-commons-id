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
package org.apache.commons.id.uuid;

import org.apache.commons.id.uuid.state.Node;

/**
 * <p>Class is responsible for managing the <code>Node</code>s identified for
 * this system (JVM instance), as well as the state management for the
 * <code>Node</code>s.</p>
 *
 * @author Commons-Id team
 * @version $Revision: 480488 $ $Date: 2006-11-29 00:57:26 -0800 (Wed, 29 Nov 2006) $
 *
 */
public interface NodeManager {

    /**
     * <p>Returns the current node in use for uuid generation. Calls to this
     * method also signal the NodeManagerImpl when to store uuid state information.
     * </p>
     *
     * @return the current node in use for uuid generation.
     */
    Node currentNode();

    /**
     * <p>Returns the next available <code>Node</code> for uuid generation.</p>
     *
     * @return the next available <code>Node</code> for uuid generation.
     */
    Node nextAvailableNode();

    /**
     * <p>Locks a node for use by a generator.</p>
     *
     * @param node the Node to lock.
     */
    void lockNode(Node node);

    /**
     * <p>Releases a node locked by a generator.</p>
     *
     * @param node the Node to release.
     */
    void releaseNode(Node node);
}
