package com.homework26.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "cloud.aws")
public class AwsS3Properties {

    private S3Properties s3;

    private RegionProperties region;

    /**
     * Настройки TLS-соединения, указание параметров для keystore/truststore
     */
    private TlsProperties tls;

    private CredentialsProperties credentials;

    @Data
    public static class S3Properties {

        private String bucket;

        private String endpoint;

        private boolean disableSslValidation;
    }

    @Data
    public static class CredentialsProperties {

        private String accessKey;

        private String secretKey;
    }

    @Data
    public static class RegionProperties {

        private String staticRegion;

        public String getStatic() {
            return staticRegion;
        }

        public void setStatic(String staticRegion) {
            // Feels like validation should be done to make sure this is a valid AWS region
            // value. However current
            // configuration in ContextRegionProviderAutoConfiguration doesn't seem to check
            // property is valid before
            // creating a bean definition. Leaving for now.
            // - tgianos 11/26/2018
            this.staticRegion = staticRegion;
        }
    }

    @Data
    public static class TlsProperties {

        private Boolean enabled;

        private String keystoreType;

        private Resource keystoreLocation;

        private String keystorePassword;

        private String truststoreType;

        private Resource truststoreLocation;

        private String truststorePassword;
    }
}
