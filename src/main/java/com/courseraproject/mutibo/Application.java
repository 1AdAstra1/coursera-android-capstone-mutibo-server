package com.courseraproject.mutibo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.courseraproject.mutibo.model.GameRepository;
import com.courseraproject.mutibo.model.MovieRepository;
import com.courseraproject.mutibo.model.SetRepository;
import com.courseraproject.mutibo.model.UserRepository;


@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackageClasses = {SetRepository.class, GameRepository.class, UserRepository.class, MovieRepository.class})
@ComponentScan
@EnableWebMvc
@Import({RepositoryRestMvcConfiguration.class, OAuth2SecurityConfiguration.class})
public class Application extends RepositoryRestMvcConfiguration {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	

}
