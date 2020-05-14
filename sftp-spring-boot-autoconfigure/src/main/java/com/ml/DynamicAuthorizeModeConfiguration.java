package com.ml;

import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.RejectAllPasswordAuthenticator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author maling
 */

@ConditionalOnProperty(value = "sftp.authorization.mode", havingValue = "dynamic")
class DynamicAuthorizeModeConfiguration {

    @Bean
    @ConditionalOnMissingBean(PasswordAuthenticator.class)
    PasswordAuthenticator passwordAuthenticator() {
        return RejectAllPasswordAuthenticator.INSTANCE;
    }

}
