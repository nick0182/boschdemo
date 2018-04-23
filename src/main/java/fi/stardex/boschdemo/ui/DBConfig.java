package fi.stardex.boschdemo.ui;

import liquibase.integration.spring.SpringLiquibase;
import org.h2.jdbcx.JdbcConnectionPool;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:properties/app.properties")
public class DBConfig {

    @Value("${data.source.url}")
    private String dataSourceUrl;

    @Bean
    public DataSource dataSource() {
        return JdbcConnectionPool.create(dataSourceUrl, "stardex", "sE2rtPiUKtT");
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean emFactory = new LocalContainerEntityManagerFactoryBean();
        emFactory.setDataSource(dataSource());
        emFactory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        emFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        jpaProperties.setProperty("hibernate.format_sql", "true");
        jpaProperties.setProperty("hibernate.use_sql_comments", "true");
        emFactory.setJpaProperties(jpaProperties);
        emFactory.setPackagesToScan("fi.stardex.boschdemo.persistance");
        return emFactory;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
        return transactionManager;
    }

    @Bean
    public FactoryBean<SessionFactory> sessionFactory() {
        HibernateJpaSessionFactoryBean factoryBean = new HibernateJpaSessionFactoryBean();
        factoryBean.setEntityManagerFactory(entityManagerFactoryBean().getObject());
        return factoryBean;
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setDataSource(dataSource());
        springLiquibase.setChangeLog("classpath:/db/db-changelog.xml");
        springLiquibase.setDefaultSchema("PUBLIC");

        return springLiquibase;
    }
}
