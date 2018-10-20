package com.dang.comicdownload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ComicDownloadApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComicDownloadApplication.class, args);
    }
}
