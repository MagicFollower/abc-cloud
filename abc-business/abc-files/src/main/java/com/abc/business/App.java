package com.abc.business;

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
        System.out.println(MediaTypeFactory.getMediaType("aaa.tzt").orElse(MediaType.TEXT_XML));  // X
        System.out.println(MediaTypeFactory.getMediaType("aaa.txt").orElse(MediaType.TEXT_XML));
        System.out.println(MediaTypeFactory.getMediaType("aaa.png").orElse(MediaType.TEXT_XML));
        System.out.println(MediaTypeFactory.getMediaType("aaa.gif").orElse(MediaType.TEXT_XML));
        System.out.println(MediaTypeFactory.getMediaType("aaa.xlsx").orElse(MediaType.TEXT_XML));
        System.out.println(MediaTypeFactory.getMediaType("aaa.pdf").orElse(MediaType.TEXT_XML));
        System.out.println(MediaTypeFactory.getMediaType("aaa.ts").orElse(MediaType.TEXT_XML));  // X
        System.out.println(MediaTypeFactory.getMediaType("aaa.flv").orElse(MediaType.TEXT_XML));
        System.out.println(MediaTypeFactory.getMediaType("aaa.mp4").orElse(MediaType.TEXT_XML));
        SpringApplication.run(App.class, args);
    }
}
