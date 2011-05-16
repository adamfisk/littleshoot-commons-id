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
 * <p>{@link Clock} provides a timing mechanism for returning the current time in
 * 100-nano second intervals since 00:00:00.00, 15 October 1582.</p>
 *
 * <p>This Class consumes a single thread which will die off if not utilized before
 * the expiration. Subsequent calls after a thread expires start a new thread.
 * Compensates for jvm time resolution issues. This clock should be used in
 * instances where the system resolution does not perform adequately - the
 * clocking resolution on some windows virtual machines can range from 10 to 50
 * milliseconds before the System.currentTimeMillis changes. In instances where
 * 10,000 or more uuid's may be generated in a millisecond this Clock
 * implementation may be required.</p>
 *
 * @author Commons-Id Team
 * @version $Revision: 480488 $ $Date: 2006-11-29 00:57:26 -0800 (Wed, 29 Nov 2006) $
 */

public final class ThreadClockImpl extends Thread implements Clock {

    /** Default time to live of the Clock thread in milliseconds */
    public static final long DEFAULT_THREAD_LIFE = 200;

    /** Life time of the clock thread in milliseconds */
    private static long threadLife = DEFAULT_THREAD_LIFE;

    /** Singleton instance of the Clock thread*/
    private static ThreadClockImpl worker = null;

    /** The current time in milliseconds held in this clock thread. */
    private static long currentTimeMillis;

    /** Time when the clock thread should die */
    private static long expires = threadLife;

    //---------------- Instance members ---------------------
    /** The time in time milliseconds used in this instance */
    private long lastTimeMs = 0;

    /** The counter for nanoseconds generated during this system interval(ms) */
    private int generatedThisInterval;

    /** The system time interval to increment the clock */
    private static short sysInterval = 1;
    // See bug parade 4814012, 4500388
    static {
        if (System.getProperty("os.name").indexOf("Windows") != -1) {
            sysInterval = 10;
        }
    }

    /**
     * <p>Public constructor to instantiate a Clock instance.</p>
     */
    public ThreadClockImpl() {
        if (worker == null) {
            synchronized (ThreadClockImpl.class) {
                worker = new ThreadClockImpl(true);
            }
        }
    }

    /**
     * Private constructor for clock implementation. Utilizes a single thread to
     * increment the clock every milli seconds this should be more
     * accurate than System.currentTimeMillis() as described in
     * the javaworld article:
     * http://www.javaworld.com/javaworld/javaqa/2003-01/01-qa-0110-timing.html
     *
     * @param isWorkerThread boolean indicating this is the worker thread.
     */
    private ThreadClockImpl(boolean isWorkerThread) {
        setDaemon(true);
        setPriority(Thread.MAX_PRIORITY);
        currentTimeMillis = System.currentTimeMillis();
        generatedThisInterval = 0;
        start();
    }

    /**
     * Returns the thread life in milliseconds. If the clock thread is not
     * accessed within this time span the thread will die off.
     *
     * @return thread life time span in milliseconds
     */
    public static long getThreadLife() {
        return ThreadClockImpl.threadLife;
    }

    /**
     * @param threadLifeLen milliseconds this thread should live for. Each
     * call to getCurrentTime resets the expiration time value.
     */
    public static void setThreadLife(long threadLifeLen) {
        ThreadClockImpl.threadLife = threadLifeLen;
    }

    /**
     * Threads run method that increments the clock and resets the generated
     * nano seconds counter.
     */
    public void run() {
        try {
            while (--expires >= 0) {
                sleep(sysInterval);
                synchronized (ThreadClockImpl.class) {
                    currentTimeMillis += sysInterval;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Clock thread interrupted");
        }
    }

    /**
     * Returns the current time as described in the clock resolution and
     * timestamp sections of the uuid specification.
     *
     * @return the current time in 100-nano second intervals (simulated)
     * @throws  OverClockedException an exception when the number of timestamps
     *          generated exceeds the allowable timestamps for the system time
     *          interval.
     */
    private synchronized long getTimeSynchronized()
            throws OverClockedException {
        long current = 0;
        synchronized (ThreadClockImpl.class) {
            current = currentTimeMillis;
        }
        if (current != lastTimeMs) {
            generatedThisInterval = 0;
            lastTimeMs = current;
        }
        if (generatedThisInterval + 1 >= (INTERVALS_PER_MILLI * sysInterval)) {
            throw new OverClockedException();
        }
        return ((current + GREGORIAN_CHANGE_OFFSET) * INTERVALS_PER_MILLI)
                + generatedThisInterval++;
      }

    /**
     * Method returns the clocks current time in 100-nanosecond intervals
     * since the Gregorian calander change. Calendar.GREGORIAN_OFFSET
     *
     * @return  Coordinated Universal Time (UTC) as a count of 100- nanosecond
     *          intervals since 00:00:00.00, 15 October 1582.
     * @throws  OverClockedException an exception when the number of timestamps
     *          generated exceeds the allowable timestamps for the system time
     *          interval.
     */
    public long getUUIDTime() throws OverClockedException {
            if (!worker.isAlive()) {
                synchronized (SystemClockImpl.class) {
                    currentTimeMillis = System.currentTimeMillis();
                    worker.start();
                }
                generatedThisInterval = 0;
            }
        return getTimeSynchronized();
    }
}
