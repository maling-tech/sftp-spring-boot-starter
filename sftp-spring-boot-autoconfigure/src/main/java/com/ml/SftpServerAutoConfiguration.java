package com.ml;

import lombok.SneakyThrows;
import org.apache.sshd.common.file.FileSystemFactory;
import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.Collections;

/**
 * @author maling
 */
@Configuration
@EnableConfigurationProperties(SftpProperties.class)
@Import({StaticAuthorizeModeConfiguration.class, SftpServerListener.class, PersistLogSftpEventListener.class})
public class SftpServerAutoConfiguration {

    @Bean
    SshServer sftpServer(SftpProperties sftpProperties, KeyPairProvider defaultKeyPairProvider, FileSystemFactory defaultFileSystemFactory, PasswordAuthenticator passwordAuthenticator, PersistLogSftpEventListener logSftpEventListener) {
        SshServer server = SshServer.setUpDefaultServer();
        server.setPort(sftpProperties.getPort());

        server.setKeyPairProvider(defaultKeyPairProvider);

        SftpSubsystemFactory.Builder builder = new SftpSubsystemFactory.Builder();
        builder.addSftpEventListener(logSftpEventListener);

        SftpSubsystemFactory factory = builder.build();
        server.setSubsystemFactories(Collections.singletonList(factory));

        server.setFileSystemFactory(defaultFileSystemFactory);

        server.setPasswordAuthenticator(passwordAuthenticator);

        return server;
    }

    @Bean
    @ConditionalOnMissingBean(KeyPairProvider.class)
    @SneakyThrows
    KeyPairProvider defaultKeyPairProvider(SftpProperties sftpProperties) {
        File file = ResourceUtils.getFile(sftpProperties.getHostKey());
        return new SimpleGeneratorHostKeyProvider(file.toPath());
    }

    @Bean
    FileSystemFactory defaultFileSystemFactory(SftpProperties sftpProperties) {
        return new DefaultFileSystemFactory(sftpProperties.getHome());
    }

}
