package org.github.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:/application.properties")
public class DatabaseConfig {
    @Value("${db.driver}")
    private String databaseDriver;

    @Value("${db.url}")
    private String databaseUrl;

    @Value("${db.username}")
    private String databaseUsername;

    @Value("${db.password}")
    private String databasePassword;

    @Value("${db.connection-pool.initial-size}")
    private int databaseConnectionPoolInitialSize;

    @Value("${db.connection-pool.min-idle}")
    private int databaseConnectionPoolMinIdle;

    @Value("${db.connection-pool.max-idle}")
    private int databaseConnectionPoolMaxIdle;

    @Value("${db.connection-pool.max-total}")
    private int databaseConnectionPoolMaxTotal;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.show_sql}")
    private String hibernateShowSql;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hibernateHbm2DdlAuto;

    @Bean // бин для установки соединения с бд
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(databaseDriver);
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseUsername);
        dataSource.setPassword(databasePassword);

        dataSource.setInitialSize(databaseConnectionPoolInitialSize); // мин кол соед при старте
        dataSource.setMinIdle(databaseConnectionPoolMinIdle); // мин кол соед при простое
        dataSource.setMaxIdle(databaseConnectionPoolMaxIdle); // мах кол соед при умер нагрузки
        dataSource.setMaxTotal(databaseConnectionPoolMaxTotal); // мах кол соед при полной нагрузки

        return dataSource;
    }

    // с помощью этого бина можно будет создавать entityManager обьект, в которых уже реализованно CRUD методы
    // все обьекты которые будут создание, будут знать как работать
    @Bean // логика создания обьектов в бд
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("org.github.entity"); // где находятся наши entity
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter()); // конкретная реализаяция JPA конфигурации
        entityManagerFactory.setJpaProperties(getHibernateProperties());
        return entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(entityManagerFactory);
        return manager;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", hibernateDialect); // даем понять hibernate какую бд мы будем использовать и как генерировать запросы sql
        properties.setProperty("hibernate.show_sql", hibernateShowSql); // вывод всех sql запросов, для обучения
        properties.setProperty("hibernate.hbm2ddl.auto", hibernateHbm2DdlAuto); // как вести себя при случае разьездов между таблицей в бд и entity классом
        return properties;
    }

}
