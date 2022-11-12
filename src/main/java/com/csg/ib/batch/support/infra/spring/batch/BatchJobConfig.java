package com.csg.ib.batch.support.infra.spring.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@PropertySource(value = { "classpath:application-page-size.yaml",
		"${spring.dbconfig.location}" }, factory = YamlPropertySourceFactory.class)
public class BatchJobConfig implements BatchConfigurer {
	
	@Autowired
	private DataSource dataSource;
	
	
	@Value("${spring.batch.table.prefix}")
	private String batchTablePrefix;
	
	@Value("${task.executor.core.pool.size}")
	private int corePoolSize;
	
	@Value("${task.executor.max.pool.size}")
	private int maxPoolSize;
	
	@Value("${task.executor.queue.capacity}")
	private int queueCapacity;
	
	@Value("${task.executor.concurrency.limit}")
	private int concurrencyLimit;
	

	@Override
	public JobRepository getJobRepository() throws Exception {

		var factory = new JobRepositoryFactoryBean();
		factory.setDataSource(dataSource);
		factory.setTransactionManager(getTransactionManager());
		factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
		
		//Uncomment below property if you want to create batch meta data table with different prefix name
		// We need to create batch table first in DB if we want to create with other name
		
		//factory.setTablePrefix(batchTablePrefix);
		
		return factory.getObject();
	}

	@Override
	public PlatformTransactionManager getTransactionManager() throws Exception {
		return new DataSourceTransactionManager(dataSource);
	}

	@Override
	public JobLauncher getJobLauncher() throws Exception {
		var jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(getJobRepository());
		jobLauncher.afterPropertiesSet();		
		return jobLauncher;
	}

	@Override
	public JobExplorer getJobExplorer() throws Exception {
		
		var jobExplorerFactoryBean = new JobExplorerFactoryBean();
		jobExplorerFactoryBean.setDataSource(dataSource);
		jobExplorerFactoryBean.afterPropertiesSet();
		
		
		return jobExplorerFactoryBean.getObject();
	}
	
	
	@Bean("simpleAsyncTaskExecutor")
	public SimpleAsyncTaskExecutor simpleAsyncTaskExecutor() {
		var te = new SimpleAsyncTaskExecutor();
		te.setConcurrencyLimit(concurrencyLimit);
		return te;
	}
	
	@Bean("taskExecutorThreadPool")
	public ThreadPoolTaskExecutor taskExecutorThreadPooled() {
		var tetp = new ThreadPoolTaskExecutor();
		tetp.setCorePoolSize(corePoolSize);
		tetp.setMaxPoolSize(maxPoolSize);
		tetp.setQueueCapacity(queueCapacity);
		
		return tetp;
	}
	
}
