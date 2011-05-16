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

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.id.uuid.clock.Clock;
import org.apache.commons.id.uuid.state.Node;
import org.apache.commons.id.uuid.state.State;
import org.apache.commons.id.uuid.state.StateHelper;

/**
 * <p>Class is responsible for managing the <code>Node</code>s identified for
 * this system (JVM instance), as well as the state management for the
 * <code>Node</code>s.</p>
 *
 * @author Commons-Id team
 * @version $Revision: 480488 $ $Date: 2006-11-29 00:57:26 -0800 (Wed, 29 Nov 2006) $
 *
 */
public final class NodeManagerImpl implements NodeManager {
    /** Reference to the State implementation to use for loading and storing */
    private State nodeState;
    /** The current array index for the Node in use. */
    private int currentNodeIndex = 0;
    /** Flag indicating the node state has been initialized. */
    private boolean isInit = false;
    /** Set that references all instances. */
    private Set nodesSet;
    /** Array of the Nodes */
    private Node[] allNodes;
    /** UUID timestamp of last call to State.store */
    private long lastUUIDTimeStored = 0;
    /** Single instance of this class */
    //private NodeManagerImpl instance;

    /** Constructor for class. */
    public NodeManagerImpl() {
        super();
    }

    /** Initialization */
    public void init() {
        nodeState = StateHelper.getStateImpl();
        try {
            nodeState.load();
        } catch (Exception ex) {
          throw new RuntimeException(ex);
        }
        nodesSet = nodeState.getNodes();
        Iterator it = nodesSet.iterator();
        allNodes = new Node[nodesSet.size()];
        int i = 0;
        while (it.hasNext()) {
            allNodes[i++] = (Node) it.next();
        }
        isInit = true;
    }
    /*
     * <p>Returns the singleton instance of this class.</p>
     *
     * @return the singleton instance of this class.

    public NodeManager getInstance() {
        if (instance == null) {
            instance = new NodeManagerImpl();
        }
        return instance;
    }
    */
    /**
     * <p>Returns the current node in use for uuid generation. Calls to this
     * method also signal the NodeManagerImpl when to store uuid state information.
     * </p>
     *
     * @return the current node in use for uuid generation.
     */
    public Node currentNode() {
        if (!isInit) {
            init();
        }
        // See if we need to store state information.
        if ((lastUUIDTimeStored + nodeState.getSynchInterval()) > (findMaxTimestamp() / Clock.INTERVALS_PER_MILLI)) {
            try {
                nodeState.store(nodesSet);
            } catch (IOException ioe) {
               //@TODO add listener and send notify
            }
        }
        return allNodes[currentNodeIndex];
    }

    /**
     * <p>Returns the next available <code>Node</code> for uuid generation.</p>
     *
     * @return the next available <code>Node</code> for uuid generation.
     */
    public Node nextAvailableNode() {
        if (!isInit) {
            init();
        }
        currentNodeIndex++;
        if (currentNodeIndex >= allNodes.length) {
            currentNodeIndex = 0;
        }
        return currentNode();
    }

    /**
     * <p>Returns the maximum uuid timestamp generated from all <code>Node</code>s</p>
     *
     * @return maximum uuid timestamp generated from all <code>Node</code>s.
     */
    private long findMaxTimestamp() {
        if (!isInit) {
            init();
        }
        long max = 0;
        for (int i = 0; i < allNodes.length; i++) {
            if (allNodes[i] != null && allNodes[i].getLastTimestamp() > max) {
                max = allNodes[i].getLastTimestamp();
            }
        }
        return max;
    }

    /**
     * <p>Locks a node for use by a generator.</p>
     *
     * @param node the Node to lock.
     */
    public void lockNode(Node node) {
        //Not implemented in this version.
    }

    /**
     * <p>Releases a node locked by a generator.</p>
     *
     * @param node the Node to release.
     */
    public void releaseNode(Node node) {
        //Not implemented in this version.
    }

    /**
     * <p>Asks the State implementation to store.</p>
     *
     * @see Object#finalize
     */
    protected void finalize() throws Throwable {
        nodeState.store(nodesSet);
        super.finalize();
    }
}
