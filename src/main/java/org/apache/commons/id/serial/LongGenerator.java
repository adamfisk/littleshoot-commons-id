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

package org.apache.commons.id.serial;

import org.apache.commons.id.AbstractLongIdentifierGenerator;

import java.io.Serializable;

/**
 * <code>LongGenerator</code> is an Identifier Generator
 * that generates an incrementing number as a Long object.
 *
 * <p>If the <code>wrap</code> argument passed to the constructor is set to
 * <code>true</code>, the sequence will wrap, returning negative values when
 * {@link Long#MAX_VALUE} reached; otherwise an {@link IllegalStateException}
 * will be thrown.</p>
 *
 * @author Commons-Id team
 * @version $Id: LongGenerator.java 480488 2006-11-29 08:57:26Z bayard $
 */
public class LongGenerator extends AbstractLongIdentifierGenerator implements Serializable {

    /**
     * <code>serialVersionUID</code> is the serializable UID for the binary version of the class.
     */
    private static final long serialVersionUID = 20060122L;

    /** Should the counter wrap. */
    private boolean wrapping;
    /** The counter. */
    private long count = 0;
    
    /**
     * Constructor.
     *
     * @param wrap should the factory wrap when it reaches the maximum
     *  long value (or throw an exception)
     * @param initialValue  the initial long value to start at
     */
    public LongGenerator(boolean wrap, long initialValue) {
        super();
        this.wrapping = wrap;
        this.count = initialValue;
    }

    /**
     * Getter for property wrap.
     *
     * @return <code>true</code> if this generator is set up to wrap.
     *
     */
    public boolean isWrap() {
        return wrapping;
    }

    /**
     * Sets the wrap property.
     *
     * @param wrap value for the wrap property
     *
     */
    public void setWrap(boolean wrap) {
        this.wrapping = wrap;
    }
    
    public Long nextLongIdentifier() {
        long value = 0;
        if (wrapping) {
            synchronized (this) {
                value = count++;
            }
        } else {
            synchronized (this) {
                if (count == Long.MAX_VALUE) {
                    throw new IllegalStateException
                    ("The maximum number of identifiers has been reached");
                }
                value = count++;
            }
        }
        return new Long(value);
    }
}
