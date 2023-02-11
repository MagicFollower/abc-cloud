package com.abc.business.images;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

/**
 * @author trivis
 */
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        System.out.println(MediaTypeFactory.getMediaType("aaa.tzt").orElse(MediaType.ALL));
        System.out.println(MediaTypeFactory.getMediaType("aaa.txt").orElse(MediaType.ALL));
        System.out.println(MediaTypeFactory.getMediaType("aaa.png").orElse(MediaType.ALL));
        System.out.println(MediaTypeFactory.getMediaType("aaa.gif").orElse(MediaType.ALL));
        System.out.println(MediaTypeFactory.getMediaType("aaa.xlsx").orElse(MediaType.ALL));
        System.out.println(MediaTypeFactory.getMediaType("aaa.pdf").orElse(MediaType.ALL));
        SpringApplication.run(App.class, args);
    }
}
