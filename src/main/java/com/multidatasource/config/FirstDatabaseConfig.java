package com.multidatasource.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.multidatasource.literals.ApplicationLiterals;

@Configuration
@EnableTransactionManagement

@EnableJpaRepositories(entityManagerFactoryRef = "firstEntityManagerFactory", transactionManagerRef ="db2TransactionManager",basePackages = {
		"com.multidatasource.repository" })
public class FirstDatabaseConfig {

	@Autowired
	private Environment env;
	
	
	/**
	 * Get Transaction Manager
	 */
	@Bean(name = "db2TransactionManager")
	public JpaTransactionManager db2TransactionManager() {

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(firstEntityManagerFactory().getObject());
		return transactionManager;
	}

	/**
	 * JPA vendor adapter as per database. This is for DB2
	 */
	@Bean
	public HibernateJpaVendorAdapter hibernateJpaVendorDB2Adapter() {

		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setDatabasePlatform(env.getProperty(ApplicationLiterals.FIRST_DIALECT));
		hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
		hibernateJpaVendorAdapter.setShowSql(true);
		hibernateJpaVendorAdapter.setGenerateDdl(false);

		return hibernateJpaVendorAdapter;
	}
	
	/**
	 * Get Entity Manager Factory
	 */

	@Bean(name="firstEntityManagerFactory")
	LocalContainerEntityManagerFactoryBean firstEntityManagerFactory() {

		Properties jpaProperties = new Properties();
		jpaProperties.put(org.hibernate.cfg.Environment.DEFAULT_SCHEMA, ApplicationLiterals.FIRST_SCHEMA);

		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setDataSource(db2DataSource());
		factoryBean.setJpaVendorAdapter(hibernateJpaVendorDB2Adapter());
		factoryBean.setPersistenceUnitName(ApplicationLiterals.FIRST_PERSISTENCE_UNIT_NAME);
		factoryBean.setJpaProperties(jpaProperties);
		factoryBean.setPackagesToScan("com.multidatasource.domain");
		factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);

		return factoryBean;
	}
	
	/**
	 * Get DB2 DataSource
	 */

	@Bean
	public DataSource db2DataSource() {

		return DataSourceBuilder.create().url(env.getProperty(ApplicationLiterals.FISRT_URL))
				.username(env.getProperty(ApplicationLiterals.USERNAME))
				.password(env.getProperty(ApplicationLiterals.PASSWORD))
				.driverClassName(env.getProperty(ApplicationLiterals.DRIVER)).build();
	}
}
