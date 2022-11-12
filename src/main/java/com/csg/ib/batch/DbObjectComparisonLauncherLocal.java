package com.csg.ib.batch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableBatchProcessing
@ImportResource({ "classpath:/jobs/fileGeneration/db-object-comparison.jobs.xml" })
@ComponentScan(basePackages = "com.csg.ib.batch")
@Profile("LOCAL")
public class DbObjectComparisonLauncherLocal {

	private static final Logger LOGGER = LoggerFactory.getLogger(DbObjectComparisonLauncherLocal.class);

	public static void main(String[] args) {

		var calendar = Calendar.getInstance();
		var timeMilli2 = calendar.getTimeInMillis();

		List<String> jobParameter = new ArrayList<>(Arrays.asList(args));
		// order date in yyMmdd format
		var orderDate = "orderDate=221105";
		jobParameter.add(orderDate);

		// Random Runcount for every instance of job
		var runCount = "rc=" + timeMilli2;
		jobParameter.add(runCount);

		System.getProperties().setProperty("spring.batch.job.names", "test.job1.db.object.comparison");
		//System.getProperties().setProperty("spring.batch.job.names", "test.job2.parse.files");
		System.getProperties().setProperty("spring.profiles.active", "LOCAL");

		args = jobParameter.toArray(args);

		LOGGER.info("Input Params : {}", Arrays.asList(args));
		System.exit(SpringApplication.exit(SpringApplication.run(DbObjectComparisonLauncherLocal.class, args)));

	}

}
