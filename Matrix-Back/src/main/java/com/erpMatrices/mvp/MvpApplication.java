package com.erpMatrices.mvp;

import com.erpMatrices.mvp.config.CorsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Main class.
 */
@SpringBootApplication
@Import(CorsConfig.class)
public class MvpApplication {

    /**
     * Run spring boot application.
     *
     * @param args application arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(MvpApplication.class, args);
    }
}
