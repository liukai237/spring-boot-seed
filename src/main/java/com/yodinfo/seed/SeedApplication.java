package com.yodinfo.seed;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
@SpringBootApplication
public class SeedApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SeedApplication.class, args);
    }
}
