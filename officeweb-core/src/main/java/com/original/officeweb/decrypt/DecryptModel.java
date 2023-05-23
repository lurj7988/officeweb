package com.original.officeweb.decrypt;


import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum DecryptModel {

    DES("0"),
    Base64("1"),
    SM4("2");

    private final String model;

    DecryptModel(String model) {
        this.model = model;
    }

    private static final Map<String, DecryptModel> DECRYPT_MODEL_MAPPER = new HashMap<>();

    static {
        DECRYPT_MODEL_MAPPER.put(DecryptModel.DES.getModel(), DecryptModel.DES);
        DECRYPT_MODEL_MAPPER.put(DecryptModel.Base64.getModel(), DecryptModel.Base64);
        DECRYPT_MODEL_MAPPER.put(DecryptModel.SM4.getModel(), DecryptModel.SM4);
    }

    public static DecryptModel to(String model) {
        if (!StringUtils.hasText(model)) {
            return DecryptModel.DES;
        }
        return DECRYPT_MODEL_MAPPER.get(model);
    }

    public String getModel() {
        return model;
    }
}
