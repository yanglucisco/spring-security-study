package org.yanglu.spring.security.study.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class ServletSpringBootHelloApp
{
    public static void main( String[] args )
    {
        System.out.println( "Hello ServletSpringBootHelloApp!" );
        SpringApplication.run(ServletSpringBootHelloApp.class, args);
    }
}
