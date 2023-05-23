package com.original.officeweb.decrypt;

import com.epoint.boot.core.utils.security.crypto.DESUtils;
import com.original.officeweb.config.OfficeWebProperties;
import org.springframework.stereotype.Component;

@Component
public class DecryptServiceDESImpl implements DecryptService {

    private final OfficeWebProperties officeWebProperties;

    public DecryptServiceDESImpl(OfficeWebProperties officeWebProperties) {
        this.officeWebProperties = officeWebProperties;
    }

    @Override
    public String decrypt(String data) {
        return DESUtils.decode(data, officeWebProperties.getEncrypt().getKey(),
                officeWebProperties.getEncrypt().getIv());
    }

    @Override
    public DecryptModel decryptModel() {
        return DecryptModel.DES;
    }
}