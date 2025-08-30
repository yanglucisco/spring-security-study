package org.yanglu.spring.security.study.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class GatewayApp
{
    public static void main( String[] args )
    {
        System.out.println( "Hello GatewayApp!" );
        SpringApplication.run(GatewayApp.class,args);
    }
}
