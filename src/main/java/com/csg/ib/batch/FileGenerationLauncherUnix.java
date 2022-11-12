package com.csg.ib.batch;

import java.util.Arrays;

import org.slf4j.Logger;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableBatchProcessing
@ImportResource({ "classpath:/jobs/fileGeneration/file-generation-ftp.jobs.xml",
		"classpath:/jobs/fileGeneration/file-generation-parsing.jobs.xml" })
@ComponentScan(basePackages = "com.csg.ib.batch")
@Profile("UNIX")
public class FileGenerationLauncherUnix {

	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FileGenerationLauncherUnix.class);

	public static void main(String[] args) {

		System.getProperties().setProperty("spring.batch.job.names", "file.generation.ftp.job");
		System.getProperties().setProperty("spring.profiles.active", "UNIX");

		LOGGER.info("Input Params : {}", Arrays.asList(args));
		System.exit(SpringApplication.exit(SpringApplication.run(FileGenerationLauncherUnix.class, args)));

	}

}
