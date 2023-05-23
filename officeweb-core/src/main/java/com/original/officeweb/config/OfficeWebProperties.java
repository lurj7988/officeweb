package com.original.officeweb.config;

import com.epoint.boot.core.utils.security.crypto.sm.sm4.SM4Utils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties("office.web")
@EnableConfigurationProperties(OfficeWebProperties.class)
@Getter
@Setter
public class OfficeWebProperties {

    private Encrypt encrypt;
    private File file;

    @Setter
    @Getter
    public static class File {
        private String cache;
        private int limitsize;
    }

    public static class Encrypt {

        private String key;
        private String iv;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            try {
                this.key = SM4Utils.decrypt(key);
            } catch (Exception e) {
                this.key = key;
            }
        }

        public String getIv() {
            return iv;
        }

        public void setIv(String iv) {
            try {
                this.iv = SM4Utils.decrypt(iv);
            } catch (Exception e) {
                this.iv = iv;
            }
        }
    }
}
