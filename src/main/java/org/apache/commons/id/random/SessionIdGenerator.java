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

package org.apache.commons.id.random;

import org.apache.commons.id.AbstractStringIdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

/**
 * <code>SessionIdGenerator</code> is an identifier generator
 * that generates an alphanumeric 10+ character identifier.
 * 
 * <p>The exact length depends on the number of ids requested per time 
 * period. Multiple instances of the class generate still unique ids.</p>
 *
 * <p>Originally designed for JServ sessions. Uses synchronized count and
 * time to ensure uniqueness. Not guaranteed unique across JVMs, but
 * fairly safe nonetheless.</p>

 * @author Commons-Id team
 * @version $Id: SessionIdGenerator.java 480488 2006-11-29 08:57:26Z bayard $
 */
public class SessionIdGenerator extends AbstractStringIdentifierGenerator implements Serializable {

    /**
     * <code>serialVersionUID</code> is the serializable UID for the binary version of the class.
     */
    private static final long serialVersionUID = 20060118L;
    /**
     * We want to have a random string with a length of 6 characters.
     * Since we encode it base-36, we modulo the random number with
     * this value.
     */
    private static final long MAX_RANDOM_LEN = 2176782336L; // 36 ** 6
    /**
     * <p>The identifier must be unique within the typical lifespan of a
     * session; the value can roll over after that.</p>3 characters:
     * (this means a roll over after over a day, which is much larger
     * than a typical lifespan).
     */
    private static final long MAX_TIME_SECTION_LEN = 46656L; // 36 ** 3
    /**
     * Milliseconds between different tics.  The 3-character time
     * string has a new value every 2 seconds.
     */
    private static final long TIC_DIFFERENCE = 2000;
    /**
     * Length of random segment
     */
    private static final int RANDOM_LENGTH = 6;
    /**
     * Length of time segment
     */
    private static final int TIME_LENGTH = 3;

    /** The incrementing counter. */
    private int counter = 0;
    /** The last time. */
    private long lastTimeValue = 0;
    /** The randmonizer. */
    private static Random randomizer = new Random();

    /**
     * Constructor.
     */
    public SessionIdGenerator() {
        super();
    }

    public long maxLength() {
        return RANDOM_LENGTH + TIME_LENGTH
            + AbstractStringIdentifierGenerator.MAX_INT_ALPHANUMERIC_VALUE_LENGTH;
    }

    public long minLength() {
        return RANDOM_LENGTH + TIME_LENGTH + 1;
    }

    /**
     * Gets the next new identifier.
     *
     * <p>Only guaranteed unique within this JVM, but fairly safe
     * for cross JVM usage as well.</p>
     *
     * <p>Format of identifier is
     * <code>[6 chars random][3 chars time][1+ chars count]</code></p>
     *
     * @return the next 10 char String identifier
     */
    public String nextStringIdentifier() {

        // Random value
        //--------------
        long currentRandom = randomizer.nextLong();
        if (currentRandom < 0) {
            currentRandom = -currentRandom;
        }
        // force value into 6 char range, and add to pad with zeros
        // this gives a length of 7, when converted to base 36, and
        // the first character (always 1 from the add) is dropped
        currentRandom %= MAX_RANDOM_LEN;
        currentRandom += MAX_RANDOM_LEN;

        long currentTimeValue = 0;
        int currentCount = 0;

        synchronized (this) {
            // Time
            //--------------
            currentTimeValue = (System.currentTimeMillis() / TIC_DIFFERENCE);

            // force value into 3 char range, and add to pad with zeros
            // this gives a length of 4, when converted to base 36, and
            // the first character (always 1 from the add) is dropped
            currentTimeValue %= MAX_TIME_SECTION_LEN;
            currentTimeValue += MAX_TIME_SECTION_LEN;

            // Count
            //--------------
            // Make the string unique by appending the count since last
            // time flip.

            // Count sessions only within tics (so the 'real' counter
            // isn't exposed to the public).
            if (lastTimeValue != currentTimeValue) {
                lastTimeValue = currentTimeValue;
                counter = 0;
            }
            currentCount = counter++;
        }

        // build string
        //--------------
        StringBuffer id = new StringBuffer
            (AbstractStringIdentifierGenerator.DEFAULT_ALPHANUMERIC_IDENTIFIER_SIZE);
        id.append(Long.toString(currentRandom,
            AbstractStringIdentifierGenerator.ALPHA_NUMERIC_CHARSET_SIZE).substring(1));  // 6 chars
        id.append(Long.toString(currentTimeValue,
            AbstractStringIdentifierGenerator.ALPHA_NUMERIC_CHARSET_SIZE).substring(1));  // 3 chars
        id.append(Long.toString(currentCount,
            AbstractStringIdentifierGenerator.ALPHA_NUMERIC_CHARSET_SIZE));  // 1+ chars
        return id.toString();
    }
}
