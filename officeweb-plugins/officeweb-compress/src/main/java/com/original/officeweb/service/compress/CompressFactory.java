package com.original.officeweb.service.compress;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Component
public class CompressFactory {

    private final Map<String, CompressService> compressServiceCache;

    public CompressFactory(Set<CompressService> compressServices) {
        this.compressServiceCache = new HashMap<>();
        for (CompressService compressService : compressServices) {
            List<String> supports = compressService.supports();
            if (!CollectionUtils.isEmpty(supports)) {
                supports.forEach(p -> compressServiceCache.put(p, compressService));
            }
        }
    }

    public Collection<String> getSupports() {
        return compressServiceCache.keySet();
    }

    public CompressService getCompressService(String suffix) {
        return compressServiceCache.get(suffix);
    }
}
