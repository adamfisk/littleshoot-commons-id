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
package org.apache.commons.id.uuid.clock;

/**
 * <p>This exception is raised whenever the clock generates too many timestamps
 * in a given system interval. In otherwords the exception occurs when the last
 * timestamp is the same as the current timestamp. The has overclocked.</p>
 *
 * @author Commons-Id Team
 * @version $Revision: 480488 $ $Date: 2006-11-29 00:57:26 -0800 (Wed, 29 Nov 2006) $
 */
public class OverClockedException extends Exception {
    /**
     * <p>Constructs a <code>OverClockedException</code> with no message.</p>
     */
    public OverClockedException() {
        super();
    }

    /**
     * </p>Constructs a <code>OverClockedException</code> with a message.</p>
     *
     * @param msg the String message for this exception.
     */
    public OverClockedException(String msg) {
        super(msg);
    }
}
