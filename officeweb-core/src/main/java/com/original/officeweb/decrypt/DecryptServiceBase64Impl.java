package com.original.officeweb.decrypt;


import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class DecryptServiceBase64Impl implements DecryptService {
    @Override
    public String decrypt(String data) {
        return new String(Base64.decodeBase64(data), StandardCharsets.UTF_8);
    }

    @Override
    public DecryptModel decryptModel() {
        return DecryptModel.Base64;
    }
}
