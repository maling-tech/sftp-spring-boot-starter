package com.ml;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.SshServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author maling
 */
public class SftpServerMain {

    @Slf4j
    @Component
    static class ServerStart implements ApplicationListener<ContextRefreshedEvent> {

        private final SshServer sshServer;

        ServerStart(SshServer sshServer) {
            this.sshServer = sshServer;
        }

        @Override
        @SneakyThrows
        public void onApplicationEvent(ContextRefreshedEvent event) {
            sshServer.start();
            log.info("sftp server start success");
        }
    }

    @Component
    @Slf4j
    static class ServerStop implements ApplicationListener<ContextClosedEvent> {

        private final SshServer sshServer;

        ServerStop(SshServer sshServer) {
            this.sshServer = sshServer;
        }

        @Override
        @SneakyThrows
        public void onApplicationEvent(ContextClosedEvent event) {
            if (sshServer.isStarted()) {
                sshServer.stop();
                log.info("sftp server stop");
            }
        }
    }

}
