package org.xiaowu.behappy.screw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableAsync
@SpringBootApplication(scanBasePackages = "org.xiaowu.behappy.screw")
public class ScrewApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScrewApplication.class, args);
    }
}
