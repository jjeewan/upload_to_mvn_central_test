/*
 ** Copyright (c) 2023, Oracle and/or its affiliates.
 ** Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl/
 */

package com.oracle.cloud.spring.core.region;

import com.oracle.bmc.Region;
import com.oracle.bmc.auth.RegionProvider;
import org.springframework.util.Assert;

/**
 * Provider to wrap Region details like name for Region configuration
 */
public class StaticRegionProvider implements RegionProvider {

    private final Region region;

    public StaticRegionProvider(String regionName) {
        Assert.notNull(regionName, "region is required");

        try {
            region = Region.valueOf(regionName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The region '" + regionName + "' is not a valid region!", e);
        }
    }

    @Override
    public Region getRegion() {
        return region;
    }

}
