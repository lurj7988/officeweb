package com.original.officeweb.web.security;

import com.epoint.boot.core.utils.string.StringUtils;
import com.epoint.boot.web.security.RequestParameterHandler;
import com.original.officeweb.decrypt.DecryptFactory;
import com.original.officeweb.decrypt.DecryptModel;
import com.original.officeweb.decrypt.DecryptService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OfficeWebRequestParameterHandler implements RequestParameterHandler {

    private static final Log logger = LogFactory.getLog(OfficeWebRequestParameterHandler.class);

    protected final DecryptFactory decryptFactory;

    public OfficeWebRequestParameterHandler(DecryptFactory decryptFactory) {
        this.decryptFactory = decryptFactory;
    }

    @Override
    public void handle(Map<String, String[]> parameters) {
        String furl = getParameter(parameters, "furl");
        if (StringUtils.isNotBlank(furl)) {
            try {
                DecryptService decryptService = decryptFactory.getDecryptService(DecryptModel.to(getParameter(parameters, "decryptmodel")));
                furl = decryptService.decrypt(furl);
                parameters.put("furl", new String[]{furl});
            } catch (Exception e) {
                logger.error("furl:" + furl + "解密失败", e);
            }
        }
    }

    public String getParameter(Map<String, String[]> parameters, String name) {
        String[] values = parameters.get(name);
        return values != null && values.length > 0 ? values[0] : null;
    }
}
