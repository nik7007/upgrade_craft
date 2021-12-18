package com.nik7.upgradecraft.utils;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public final class LazyOptionalHelper {
    private LazyOptionalHelper() {
    }

    @Nullable
    public static <T> T getHandler(ICapabilityProvider provider, Capability<T> capability) {
        return getHandler(provider.getCapability(capability));
    }

    @Nullable
    public static <T> T getHandler(LazyOptional<T> lazyOptional) {
        if (lazyOptional.isPresent()) {
            return lazyOptional.orElseThrow(() -> new RuntimeException("Failed to retrieve handler when it should be present inside lazy optional!"));
        }
        return null;
    }
}
