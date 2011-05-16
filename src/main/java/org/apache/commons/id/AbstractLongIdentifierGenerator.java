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
package org.apache.commons.id;

/**
 * Abstract superclass for LongIdentifierGenerator implementations.
 *
 * @author Commons-Id team
 * @version $Id: AbstractLongIdentifierGenerator.java 480488 2006-11-29 08:57:26Z bayard $
 */
public abstract class AbstractLongIdentifierGenerator implements LongIdentifierGenerator {

    /**
     * Constructor.
     */
    protected AbstractLongIdentifierGenerator() {
        super();
    }

    /**
     * Returns the maximum value of an identifier from this generator.
     *
     * <p>The default implementation returns Long.MAX_VALUE. Implementations
     * whose identifiers are bounded below Long.MAX_VALUE should override this method to
     * return the maximum value of a generated identifier.</p>
     *
     * @return {@inheritDoc}
     */
    public long maxValue() {
        return Long.MAX_VALUE;
    }

    /**
     * Returns the minimum value of an identifier from this generator.
     *
     * <p>The default implementation returns Long.MIN_VALUE. Implementations
     * whose identifiers are bounded above Long.MIN_VALUE should override this method to
     * return the minimum value of a generated identifier.</p>
     *
     * @return {@inheritDoc}
     */
    public long minValue() {
        return Long.MIN_VALUE;
    }

    public Object nextIdentifier() {
        return this.nextLongIdentifier();
    }

    public abstract Long nextLongIdentifier();
}
