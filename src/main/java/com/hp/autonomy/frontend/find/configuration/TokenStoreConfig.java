/*
 * Copyright 2015 Hewlett-Packard Development Company, L.P.
 * Licensed under the MIT License (the "License"); you may not use this file except in compliance with the License.
 */

package com.hp.autonomy.frontend.find.configuration;

/**
 * Where to store the Haven OnDemand session token for logged in users
 */
public enum TokenStoreConfig {
    /** Store the token in memory - only good for a single Find node */
    INMEMORY,

    /** Store the token in Redis - use this for clustered Find */
    REDIS
}
