package com.ml;

import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author maling
 */
@SpringBootApplication
public class SftpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SftpApplication.class, args);
    }

//    @Bean
//    PasswordAuthenticator passwordAuthenticator() {
//        return ((username, password, session) -> true);
//    }
}
