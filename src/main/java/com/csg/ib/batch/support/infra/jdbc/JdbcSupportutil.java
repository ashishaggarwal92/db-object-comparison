package com.csg.ib.batch.support.infra.jdbc;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JdbcSupportutil {

	@Value("${spring.datasource.DB_1.url}")
	private String dbUrl1;

	@Value("${spring.datasource.DB_1.username}")
	private String userName1;

	@Value("${spring.datasource.DB_1.password}")
	private String password1;

	@Value("${spring.datasource.DB_2.url}")
	private String dbUrl2;

	@Value("${spring.datasource.DB_2.username}")
	private String userName2;

	@Value("${spring.datasource.DB_2.password}")
	private String password2;

	@Autowired
	private DataSource dataSource;

	@Bean("jpaTransactionManager")
	public PlatformTransactionManager transactionManager() {
		var jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entiryManagerFactoryBean().getObject());
		return jpaTransactionManager;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entiryManagerFactoryBean() {

		var em = new LocalContainerEntityManagerFactoryBean();
		// em.setDataSource(dataSource());
		em.setDataSource(dataSource);
		em.setPackagesToScan("com.csg.ib.batch.domain.db");

		JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(jpaVendorAdapter);
		return em;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean("getDB1Connection")
	public Connection getDB1Connection() {

		Connection connection = DbConnectionUtil.getDBConnection(dbUrl1, userName1, password1);
		return connection;
	}

	@Bean("getDB2Connection")
	public Connection getDB2Connection() {

		Connection connection = DbConnectionUtil.getDBConnection(dbUrl2, userName2, password2);
		return connection;
	}

}
