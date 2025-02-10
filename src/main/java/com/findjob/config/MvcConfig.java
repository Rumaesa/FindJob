package com.findjob.config;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
//	This configuration class will map requests for /photos to serve files from a directory on our file System.
	
	private static final String UPLOAD_DIR = "photos";
	
//	Override the default implementation to setup a custom resource handler.
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		exposeDirectory(UPLOAD_DIR, registry);
	}

//	Converts the uploadDir string to a path
//	Maps requests starting with "/photos/**" to a file system location file:<absolute path to photos directory>
//	the ** will match on all sub-directories 
	private void exposeDirectory(String uploadDir, ResourceHandlerRegistry registry) {
		Path path = Paths.get(uploadDir);
		registry.addResourceHandler("/"+uploadDir+"/**").addResourceLocations("file:"+path.toAbsolutePath() + "/");	
		
	}

}
