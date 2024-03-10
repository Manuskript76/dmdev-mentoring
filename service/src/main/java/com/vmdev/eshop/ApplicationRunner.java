package com.vmdev.eshop;

import com.vmdev.eshop.config.ApplicationConfiguration;
import lombok.Cleanup;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationRunner {

    public static void main(String[] args) {
        @Cleanup var context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        Object productRepository = context.getBean("productRepository");
        System.out.println(productRepository);
    }
}
