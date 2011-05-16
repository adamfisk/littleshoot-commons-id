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

import org.apache.commons.discovery.tools.DiscoverSingleton;
import org.apache.commons.id.IdentifierGenerator;
import org.apache.commons.id.uuid.clock.OverClockedException;
import org.apache.commons.id.uuid.state.Node;

/**
 * Class is responsible for generating version 1 UUID's per RFC 4122.
 * This class attempts to locate the machine's node identifier
 * first by attempting to loading the properties file UUID.properties
 * from the system classpath. If the UUID.properties file does not exist
 * then the generator will create a node identifier from random information as
 * defined in the
 * <a href="ftp://ftp.rfc-editor.org/in-notes/rfc4122.txt">RFC 4122:
 * A Universally Unique IDentifier (UUID) URN Namespace</a>.
 *
 * @author Commons-Id team
 * @version $Revision: 480488 $ $Date: 2006-11-29 00:57:26 -0800 (Wed, 29 Nov 2006) $
 *
 */

public final class VersionOneGenerator implements IdentifierGenerator, Constants {

    /** Positions 10-16: Length of node bytes */
    private static final int NODE_ID_BYTE_LENGTH = 6;

    /** Position 8: CLOCK_SEQ_LOW byte */
    private static final int CLOCK_HI_VARIANT_BYTE8 = 8;
    /** Position 9: CLOCK_SEQ_HIGH and VARIANT byte */
    private static final int CLOCK_LOW_BYTE9 = 9;
    /** Positions 10-16: Start of node bytes */
    private static final int NODE_ID_BYTE10 = 10;

    /** The default NodeManager implementation. */
    private static final String DEFAULT_NODEMANAGER_IMPL = NodeManagerImpl.class.getName();

    /** The NodeManager implementation */
    private NodeManager manager;

    /** Singleton instance such that only one instance is accessing the static
     * fields at any time.
     */
    private static VersionOneGenerator generator;

    /**
     *  <p>Private singleton constructor.</p>
     */
    private VersionOneGenerator() {
        super();
        manager = (NodeManager) DiscoverSingleton.find( NodeManager.class, DEFAULT_NODEMANAGER_IMPL);
    }

    /**
     * <p>Returns the singleton instance of the version one UUID generator.</p>
     *
     * @return the singleton instance of the version one UUID generator.
     */
    public static VersionOneGenerator getInstance()  {
            if (generator == null) {
                generator = new VersionOneGenerator();
            }
            return generator;
    }
    
    /**
     * @see org.apache.commons.id.IdentifierGenerator#nextIdentifier()
     */
    public Object nextIdentifier() {
        return nextUUID();
    }

    /**
     * <p>Returns a new version 1 UUID. The method acts upons static variables
     * and so should be sychronized.</p>
     *
     * @return Returns a new version 1 UUID.
     */
    public synchronized UUID nextUUID() {
        byte[] rawUUID = new byte[UUID_BYTE_LENGTH];
        long time = 0;
        short clockSq = 0;
        Node node = manager.currentNode();
        while (time < 1) {
            try {
                manager.lockNode(node);
                time = node.getUUIDTime();
                clockSq = node.getClockSequence();
                System.arraycopy(node.getNodeIdentifier(), 0, rawUUID, NODE_ID_BYTE10, NODE_ID_BYTE_LENGTH);
                manager.releaseNode(node);
            } catch (OverClockedException e) {
                node = manager.nextAvailableNode();
            } finally {
                manager.releaseNode(node);
            }
        }
        byte[] timeBytes = Bytes.toBytes(time);
        //Copy time low
        System.arraycopy(timeBytes, TIME_LOW_TS_POS, rawUUID, TIME_LOW_START_POS, TIME_LOW_BYTE_LEN);
        //Copy time mid
        System.arraycopy(timeBytes, TIME_MID_TS_POS, rawUUID, TIME_MID_START_POS, TIME_MID_BYTE_LEN);
        //Copy time hi
        System.arraycopy(timeBytes, TIME_HI_TS_POS, rawUUID, TIME_HI_START_POS, TIME_HI_BYTE_LEN);
        //Set version
        rawUUID[6] |= 0x10; // 0001 0000
        //Set clock sequence
        rawUUID[CLOCK_HI_VARIANT_BYTE8] = (byte) ((clockSq & 0x3F00) >>> 8);
        rawUUID[CLOCK_HI_VARIANT_BYTE8] |= 0x80;
        rawUUID[CLOCK_LOW_BYTE9] = (byte) (clockSq & 0xFF);

        return new UUID(rawUUID);
    }
}
