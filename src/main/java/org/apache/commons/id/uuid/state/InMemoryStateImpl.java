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

package org.apache.commons.id.uuid.state;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>The <code>InMemoryStateImpl</code> is an implementation of the
 * <code>State</code> interface.</p> 
 * <p>This implementation is the <b>worst-case</b> scenario and provides
 * the least guarantee of no UUID duplication. This implementation is provided for
 * systems that truly have no other means of writing to or reading from stable
 * storage.</p>
 *
 * @author Commons-Id team
 * @version $Id: InMemoryStateImpl.java 480488 2006-11-29 08:57:26Z bayard $
 */
public class InMemoryStateImpl implements State {

    /** The nodes to return from the <code>State</code> implementation */
    private  static HashSet nodes = new HashSet(1);

    /**
     * @see org.apache.commons.id.uuid.state.State#load()
     */
    public void load() {
       Node one = new Node(StateHelper.randomNodeIdentifier());
       nodes.add(one);
    }

    /**
     * @see org.apache.commons.id.uuid.state.State#getNodes()
     */
    public Set getNodes() {
       return nodes;
    }

    /**
     * @see org.apache.commons.id.uuid.state.State#store(Set)
     */
    public void store(Set nodesCollection) {
        // Nothing to do - this is the worse case senerio since with no
        // peristent storage.
        return;
    }

    /**
     * @see org.apache.commons.id.uuid.state.State#store(Set, long)
     */
    public void store(Set nodesCollection, long timestamp) {
        // Nothing to do - this is the worse case senerio since with no
        // peristent storage.
        return;
    }

    /**
     * @see State#getSynchInterval
     */
    public long getSynchInterval() {
        return Long.MAX_VALUE;
    }
}
