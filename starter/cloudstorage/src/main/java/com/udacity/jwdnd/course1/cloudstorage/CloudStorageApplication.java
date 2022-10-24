package com.udacity.jwdnd.course1.cloudstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CloudStorageApplication extends SpringBootServletInitializer  {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CloudStorageApplication.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(CloudStorageApplication.class, args);
	}

}
