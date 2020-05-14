package com.ml;

import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author maling
 */

@ConditionalOnProperty(value = "sftp.authorization.mode", havingValue = "static", matchIfMissing = true)
class StaticAuthorizeModeConfiguration {

    private final SftpProperties sftpProperties;

    public StaticAuthorizeModeConfiguration(SftpProperties sftpProperties) {
        this.sftpProperties = sftpProperties;
    }

    @Bean
    @ConditionalOnMissingBean(PasswordAuthenticator.class)
    PasswordAuthenticator passwordAuthenticator() {
        return ((username, password, session) -> authenticate(username, password));
    }

    private boolean authenticate(String username, String password) {
        SftpProperties.Authorization authorization = sftpProperties.getAuthorization();
        List<SftpProperties.User> users = authorization.getUsers();
        if (CollectionUtils.isEmpty(users)) {
            return false;
        }
        return users.stream().anyMatch(user -> Objects.equals(username, user.getUsername()) && Objects.equals(password, user.getPassword()));
    }

}
