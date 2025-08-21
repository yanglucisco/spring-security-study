package org.yanglu.spring.security.study.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yanglu.spring.security.study.example.testbpp.Bean4BBP;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class HelloSecurityExplicitApplication
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(HelloSecurityExplicitApplication.class, args);
    }
}
