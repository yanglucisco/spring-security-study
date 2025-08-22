package org.yanglu.spring.security.study.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class OAuthResourceServerApp
{
    public static void main( String[] args )
    {
        System.out.println( "Hello OAuthResourceServerApp!" );
        SpringApplication.run(OAuthResourceServerApp.class, args);
    }
}
