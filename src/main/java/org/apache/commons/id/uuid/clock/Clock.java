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
 * <p>Provides a timing mechanism for returning the current time in
 * 100-nano second intervals since 00:00:00.00, 15 October 1582.</p>
 *
 * <p>As described below this is useful for generating Version 1 UUIDs</p>
 *
 * <p>See the <a href="ftp://ftp.rfc-editor.org/in-notes/rfc4122.txt">RFC 4122:
 * A Universally Unique IDentifier (UUID) URN Namespace</a>
 * for more information.</p>
 *
 * <p>Selected quotes from IETF document pertaining to this class:</p>
 *
 * <p>====================================================================</p>
 * <dl>
 * <dt>Timestamp</dt>
 * <dd>"The timestamp is a 60 bit value. For UUID version 1, this is
 * represented by Coordinated Universal Time (UTC) as a count of 100-
 * nanosecond intervals since 00:00:00.00, 15 October 1582 (the date of
 * Gregorian reform to the Christian calendar)."</dd>
 *
 * <dt>System Clock Resolution</dt>
 * <dd>"If a system overruns the generator by requesting too many UUIDs
 * within a single system time interval, the UUID service MUST either
 * return an error, or stall the UUID generator until the system clock
 * catches up.<br>
 * <br>
 * A high resolution timestamp can be simulated by keeping a count of
 * the number of UUIDs that have been generated with the same value of
 * the system time, and using it to construct the low order bits of the
 * timestamp.  The count will range between zero and the number of
 * 100-nanosecond intervals per system time interval.<br>
 * <br>
 * Note: If the processors overrun the UUID generation frequently,
 * additional node identifiers can be allocated to the system, which
 * will permit higher speed allocation by making multiple UUIDs
 * potentially available for each time stamp value."</dd>
 *
 * <p>The above quotations are protected under the following copyright notice </p>
 * <p><code>Copyright (C) The Internet Society (2005).<br>
 * <br>
 * This document is subject to the rights, licenses and restrictions
 * contained in BCP 78, and except as set forth therein, the authors
 * retain all their rights.<br>
 * <br>
 * This document and the information contained herein are provided on an
 * "AS IS" basis and THE CONTRIBUTOR, THE ORGANIZATION HE/SHE REPRESENTS
 * OR IS SPONSORED BY (IF ANY), THE INTERNET SOCIETY AND THE INTERNET
 * ENGINEERING TASK FORCE DISCLAIM ALL WARRANTIES, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO ANY WARRANTY THAT THE USE OF THE
 * INFORMATION HEREIN WILL NOT INFRINGE ANY RIGHTS OR ANY IMPLIED
 * WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.
 * </code></p>
 *
 * ====================================================================
 *
 * @author Commons-Id Team
 * @version $Revision: 480488 $ $Date: 2006-11-29 00:57:26 -0800 (Wed, 29 Nov 2006) $
 *
 */

public interface Clock {
    /** The default Clock implementation if not configured */
    String DEFAULT_CLOCK_IMPL = "org.apache.commons.id.uuid.clock.SystemClockImpl";

    /** Offset from GregorianCalendar Change over to Jan 1 1970 00:00:00.00 */
    long GREGORIAN_CHANGE_OFFSET =  12219292800000L;

    /** Maximum ticks per millisecond interval (1 millisecond = 1 000 000 nanoseconds) / 100 */
    long INTERVALS_PER_MILLI = 10000L;

    /**
     * <p>Returns the current time.</p>
     *
     * @return the current time in 100-nano second intervals since 00:00:00.00,
     * 15 October 1582 UTC.
     *
     * @throws OverClockedException an Exception raised if the number of calls
     * in a system time interval exceeds the number of 100-nanosecond intervals
     * allowed.
     */
    long getUUIDTime() throws OverClockedException;
}
