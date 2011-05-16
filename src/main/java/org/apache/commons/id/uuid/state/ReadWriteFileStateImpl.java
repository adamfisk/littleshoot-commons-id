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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>The <code>ReadWriteFileStateImpl</code> is an implementation of the
 * <code>State</code> interface. This implementation provides better guarantees
 * that no duplicate UUID's will be generated since the last time stamp, and
 * last clock sequence are stored to a persistent file.
 *
 * @author Commons-Id team
 * @version $Id: ReadWriteFileStateImpl.java 480488 2006-11-29 08:57:26Z bayard $
 */
public class ReadWriteFileStateImpl extends ReadOnlyResourceStateImpl implements State {

    /**
     * Persists the UUID generator state to file.
     *
     * @see org.apache.commons.id.uuid.state.State#store(java.util.Set)
     */
    public void store(Set nodes) throws IOException {
        writeXML(genXML(nodes));
    }

    /**
     * Returns an XML string of the node Set.
     *
     * @param nodes the Set to create xml for.
     * @return an XML string of the node Set.
     * @throws IOException an IOException.
     */
    private String genXML(Set nodes) throws IOException {
        Iterator it = nodes.iterator();
        StringBuffer buf = new StringBuffer(1024);
        buf.append(StateHelper.XML_DOC_START);
        buf.append(getSynchInterval());
        buf.append(StateHelper.XML_DOC_START_END);
        while (it.hasNext()) {
            Node n = (Node) it.next();
            buf.append(StateHelper.XML_NODE_TAG_START);
            buf.append(StateHelper.encodeMACAddress(n.getNodeIdentifier()));
            buf.append(StateHelper.XML_NODE_TAG_AFTER_ID);
            buf.append(n.getClockSequence());
            buf.append(StateHelper.XML_NODE_TAG_AFTER_CSEQ);
            buf.append(n.getLastTimestamp());
            buf.append(StateHelper.XML_NODE_TAG_END);
        }
        buf.append(StateHelper.XML_DOC_END);
        return buf.toString();
    }

    /**
     * Returns an XML string of the node Set using a predetermined last time stamp for all <code>Node</code>s.
     *
     * @param nodes the Set to create xml for.
     * @param timestamp the timestamp to write for all nodes.
     * @return an XML string of the node Set.
     * @throws IOException an IOException.
     */
    private String genXML(Set nodes, long timestamp)  throws IOException {
        Iterator it = nodes.iterator();
        StringBuffer buf = new StringBuffer(1024);
        buf.append(StateHelper.XML_DOC_START);
        buf.append(getSynchInterval());
        buf.append(StateHelper.XML_DOC_START_END);
        while (it.hasNext()) {
            Node n = (Node) it.next();
            buf.append(StateHelper.XML_NODE_TAG_START);
            buf.append(StateHelper.encodeMACAddress(n.getNodeIdentifier()));
            buf.append(StateHelper.XML_NODE_TAG_AFTER_ID);
            buf.append(n.getClockSequence());
            buf.append(StateHelper.XML_NODE_TAG_AFTER_CSEQ);
            buf.append(timestamp);
            buf.append(StateHelper.XML_NODE_TAG_END);
        }
        buf.append(StateHelper.XML_DOC_END);
        return buf.toString();
    }

    /**
     * <p>Writes the XML String to the file system.</p>
     *
     * @param xml the xml string to write.
     */
    private void writeXML(String xml) {
        String resourceName = System.getProperty(CONFIG_FILENAME_KEY);
        if (resourceName == null) {
            return;
        } else {
            URL rUrl = ClassLoader.getSystemResource(resourceName);
            if (rUrl != null) {
                File file = new File(rUrl.getFile());
                if (file != null && file.canWrite()) {
                    FileWriter fw = null;
                    try {
                        fw = new FileWriter(file);
                        fw.write(xml);
                        fw.close();
                    } catch (IOException ioe) {
                        //@TODO log it?
                    } finally {
                        try {
                            fw.close();
                        } catch (IOException ioee) {
                            ; //Nothing to do.
                        }
                        fw = null;
                        file = null;
                    }
                }
            }
        }
    }
}
