/*
 ** Copyright (c) 2023, Oracle and/or its affiliates.
 ** Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl/
 */

package com.oracle.cloud.spring.logging;

import com.oracle.bmc.auth.BasicAuthenticationDetailsProvider;
import com.oracle.bmc.auth.RegionProvider;
import com.oracle.bmc.loggingingestion.Logging;
import com.oracle.bmc.loggingingestion.LoggingClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration for initializing the OCI LoggingSvc component.
 *  Depends on {@link com.oracle.cloud.spring.autoconfigure.core.CompartmentProviderAutoConfiguration},
 *  {@link com.oracle.cloud.spring.autoconfigure.core.CredentialsProviderAutoConfiguration} and
 *  {@link com.oracle.cloud.spring.autoconfigure.core.RegionProviderAutoConfiguration}
 *  for loading the Authentication configuration
 *
 * @see com.oracle.cloud.spring.logging.LoggingSvc
 */
@AutoConfiguration
@ConditionalOnClass({LoggingSvc.class})
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnProperty(name = "spring.cloud.oci.logging.enabled", havingValue = "true", matchIfMissing = true)
public class LoggingAutoConfiguration {

    private final LoggingProperties properties;

    public LoggingAutoConfiguration(LoggingProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(LoggingSvc.class)
    LoggingSvc getLoggingImpl(Logging logging) {
        return new LoggingImpl(logging, properties.getLogId());
    }

    @Bean
    @ConditionalOnMissingBean
    Logging loggingClient(BasicAuthenticationDetailsProvider adp,
                          RegionProvider regionProvider) {
        Logging logging = new LoggingClient(adp);
        logging.setRegion(regionProvider.getRegion());
        return logging;
    }
}
