package com.original.officeweb.decrypt;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DecryptFactory {

    private final Map<DecryptModel, DecryptService> decryptServiceCache;

    public DecryptFactory(Set<DecryptService> decryptServices) {
        this.decryptServiceCache = new HashMap<>();
        for (DecryptService decryptService : decryptServices) {
            decryptServiceCache.put(decryptService.decryptModel(), decryptService);
        }
    }

    public DecryptService getDecryptService(DecryptModel decryptModel) {
        DecryptService decryptService = decryptServiceCache.get(decryptModel);
        if (decryptService == null) {
            return decryptServiceCache.get(DecryptModel.DES);
        }
        return decryptService;
    }
}
