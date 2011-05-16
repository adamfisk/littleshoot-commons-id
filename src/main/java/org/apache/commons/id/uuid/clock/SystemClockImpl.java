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
 * <p><code>SystemClockImpl</code> provides a timing mechanism for returning the
 * current time in 100-nano second intervals since 00:00:00.00, 15 October 1582.
 * </p>
 *
 * @see org.apache.commons.id.uuid.clock.Clock
 * @author Commons-Id Team
 * @version $Revision: 480488 $ $Date: 2006-11-29 00:57:26 -0800 (Wed, 29 Nov 2006) $
 */

public final class SystemClockImpl implements Clock {
    /** Counter for the number of calls during the current millisecond */
    private long generatedThisMilli = 0;

    /** The current time in milliseconds held in this clock thread. */
    private long currentTimeMillis;

    /**
     * <p>Public constructor.</p>
     */
    public SystemClockImpl() {
        super();
    }

    /** @see org.apache.commons.id.uuid.clock.Clock#getUUIDTime() */
    public long getUUIDTime() throws OverClockedException {
        return getTimeSynchronized();
    }

    /**
     * <p>Object synchronized method returns the current time in 100ns
     * intervals since the Gregorian change offset.</p>
     *
     * @return  the current time in 100ns intervals since the Gregorian change
     *          offset.
     * @throws  OverClockedException an exception raised if too many timestamps
     *          have been generated for the system time interval.
     */
    private synchronized long getTimeSynchronized()
            throws OverClockedException {
        if (currentTimeMillis != System.currentTimeMillis()) {
            currentTimeMillis = System.currentTimeMillis();
            generatedThisMilli = 0;
        }
        // Set time as current time millis plus offset times 100 ns ticks
        long currentTime =
            (currentTimeMillis + GREGORIAN_CHANGE_OFFSET)
                * INTERVALS_PER_MILLI;

        // Return the same time - generator/client code must check to see if overclocked
        if (generatedThisMilli + 1 >= INTERVALS_PER_MILLI) {
            throw new OverClockedException();
        }
        // Return the uuid time plus the artifical tick incremented
        return (currentTime + generatedThisMilli++);
    }
}
