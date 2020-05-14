package com.ml;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author maling
 */
@ConfigurationProperties(prefix = "sftp")
@Data
public class SftpProperties {

    private String home = "/home";

    private Integer port = 22;

    private String hostKey = "classpath:host.ser";

    private Authorization authorization;

    @Data
    public static class Authorization {
        private SecurityAuthorizeMode mode = SecurityAuthorizeMode.STATIC;

        private List<User> users = new ArrayList<>(0);
    }

    @Data
    public static class User {

        private String username;

        private String password;
    }

    public enum SecurityAuthorizeMode {

        /**
         * 固定用户
         */
        STATIC,

        /**
         * 动态认证
         */
        DYNAMIC
    }
}
