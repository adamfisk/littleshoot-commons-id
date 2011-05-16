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

package org.apache.commons.id.uuid.task;

import org.apache.commons.id.uuid.UUID;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.EnumeratedAttribute;


/**
 * Simple Ant task to generate a UUID. See the following Ant script for usage:
 * 
 * <pre>
 *  &lt;project default=&quot;generate.uuid&quot; name=&quot;uuid&quot; basedir=&quot;.&quot;&gt;
 *  
 *    &lt;taskdef name=&quot;uuid&quot; className=&quot;org.apache.commons.id.uuid.task.UUIDTask&quot;/&gt;
 *  
 *    &lt;target name=&quot;generate.uuid&quot; description=&quot;Generates a UUID&quot;&gt;
 *      &lt;uuid version=&quot;VERSION_ONE&quot;/&gt;
 *      &lt;echo message=&quot;${uuid}&quot;/&gt; 
 *      &lt;uuid version=&quot;VERSION_THREE&quot;/&gt; 
 *      &lt;echo message=&quot;${uuid}&quot;/&gt;
 *      &lt;uuid version=&quot;VERSION_FOUR&quot;/&gt; 
 *      &lt;echo message=&quot;${uuid}&quot;/&gt;
 *      &lt;uuid version=&quot;VERSION_FIVE&quot;/&gt; 
 *      &lt;echo message=&quot;${uuid}&quot;/&gt;
 *    &lt;/target&gt;
 *  &lt;/project&gt;
 * </pre>
 * 
 * The namespace <em>urn:uuid:B4F00409-CEF8-4822-802C-DEB20704C365</em> and the name
 * <em>www.apache.org</em> is used as default to generate the UUIDs for version 3 and 5.
 * 
 * @version $Id: UUIDTask.java 480488 2006-11-29 08:57:26Z bayard $
 * @since 1.0
 */
public class UUIDTask extends Task {

    private String uuidVersion = "VERSION_FOUR";
    private String name = "www.apache.org";
    private String namespace = "urn:uuid:B4F00409-CEF8-4822-802C-DEB20704C365";

    /**
     * Setter for the name used to generate a UUID version 3 or 5.
     * 
     * @param name the name ot use
     * @since 1.0
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for the namespace used to generate a UUID version 3 or 5.
     * 
     * @param namespace the name ot use
     * @since 1.0
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * The enum for the UUID version.
     * 
     * @since 1.0
     */
    public static class UUIDVersion extends EnumeratedAttribute {
        public String[] getValues() {
            return new String[]{"VERSION_ONE", "VERSION_THREE", "VERSION_FOUR", "VERSION_FIVE"};
        }
    }

    /**
     * Set the UUID version to generate.
     * 
     * @param newVersion the UUID version
     * @since 1.0
     */
    public void setVersion(UUIDVersion newVersion) {
        uuidVersion = newVersion.getValue();
    }

    public void execute() throws BuildException {
        UUID uuid = null;
        if (uuidVersion.equals("VERSION_THREE")) {
            uuid = UUID.nameUUIDFromString(name, new UUID(namespace), UUID.MD5_ENCODING);
        } else if (uuidVersion.equals("VERSION_FIVE")) {
            uuid = UUID.nameUUIDFromString(name, new UUID(namespace), UUID.SHA1_ENCODING);
        } else if (uuidVersion.equals("VERSION_FOUR")) {
            uuid = UUID.randomUUID();
        } else if (uuidVersion.equals("VERSION_ONE")) {
            uuid = UUID.timeUUID();
        }

        setProperty("uuid", uuid.toString());
    }

    private void setProperty(String name, String value) {
        getProject().setProperty(name, value);
    }

}
