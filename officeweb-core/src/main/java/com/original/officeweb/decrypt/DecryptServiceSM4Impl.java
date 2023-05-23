package com.original.officeweb.decrypt;

import com.epoint.boot.core.utils.security.crypto.sm.sm4.SM4Utils;
import com.original.officeweb.config.OfficeWebProperties;
import org.springframework.stereotype.Component;

@Component
public class DecryptServiceSM4Impl implements DecryptService {

    private final OfficeWebProperties officeWebProperties;

    public DecryptServiceSM4Impl(OfficeWebProperties officeWebProperties) {
        this.officeWebProperties = officeWebProperties;
    }

    @Override
    public String decrypt(String data) {
        return SM4Utils.decrypt(data, officeWebProperties.getEncrypt().getKey(),
                officeWebProperties.getEncrypt().getIv());
    }

    @Override
    public DecryptModel decryptModel() {
        return DecryptModel.SM4;
    }

}
