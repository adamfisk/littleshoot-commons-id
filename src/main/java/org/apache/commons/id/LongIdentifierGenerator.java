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
 * <code>LongIdentifier</code> defines a simple interface for
 * Long based identifier generation.
 *
 * @author Commons-Id team
 * @version $Id: LongIdentifierGenerator.java 480488 2006-11-29 08:57:26Z bayard $
 */
public interface LongIdentifierGenerator extends IdentifierGenerator {

    /**
     * Gets the next identifier in the sequence.
     *
     * @return the next Long identifier in sequence
     */
    Long nextLongIdentifier();

    /**
     * Returns the maximum value of an identifier from this generator.
     *
     * @return the maximum identifier value
     */
    long maxValue();

    /**
     * Returns the minimum value of an identifier from this generator.
     *
     * @return the minimum identifier value
     */
    long minValue();

}
