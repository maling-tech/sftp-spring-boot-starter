package com.ml;

import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.SshServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;

/**
 * @author maling
 */
@Configuration
@Slf4j
public class SftpServerListener {

    private final SshServer sshServer;

    public SftpServerListener(SshServer sshServer) {
        this.sshServer = sshServer;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (sshServer.isStarted()) {
            return;
        }
        try {
            sshServer.start();
            log.info("Ftp Server start");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Ftp start fail, Event: " + event.getSource());
        }
    }

    @EventListener
    public void onApplicationEvent(ContextStoppedEvent event) {
        if (sshServer.isClosed()) {
            return;
        }
        try {
            sshServer.stop();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Ftp stop fail, Event: " + event.getSource());
        }
    }

}
