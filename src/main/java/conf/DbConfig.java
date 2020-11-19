
package conf;

import global.Global;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"dao"})
@PropertySource("classpath:/application.properties")
public class DbConfig {

   @Bean
   public DataSource dataSource() {
      return getDataSource();
   }

   public static DataSource getDataSource() {
      Properties properties = Global.getProperties();
      String dbToUse = properties.getProperty("dbToUse");
      if (dbToUse.equals("hsql")){
         DriverManagerDataSource ds = new DriverManagerDataSource();
         ds.setDriverClassName("org.hsqldb.jdbcDriver");
         ds.setUrl(properties.getProperty("hsql.url"));
         return ds;
      }
      throw new RuntimeException("Specify Db to use in properties!");
   }

   @Bean("dialect")
   public String dialect() {
      Properties properties = Global.getProperties();
      return properties.getProperty("dialect");
   }

   @Bean
   public EntityManagerFactory entityManagerFactory(
           DataSource dataSource,
           @Qualifier("dialect") String dialect) {

      var populator = new ResourceDatabasePopulator(
              new ClassPathResource("schema.sql"),
              new ClassPathResource("data.sql"));
      DatabasePopulatorUtils.execute(populator, dataSource);


      LocalContainerEntityManagerFactoryBean factory =
              new LocalContainerEntityManagerFactoryBean();
      factory.setPersistenceProviderClass(
              HibernatePersistenceProvider.class);
      factory.setPackagesToScan("model");
      factory.setDataSource(dataSource);
      factory.setJpaProperties(additionalProperties(dialect));
      factory.afterPropertiesSet();

      return factory.getObject();
   }

   @Bean
   public PlatformTransactionManager transactionManager(
           EntityManagerFactory entityManagerFactory) {

      return new JpaTransactionManager(entityManagerFactory);
   }

   private Properties additionalProperties(String dialect) {
      Properties properties = new Properties();
      properties.setProperty("hibernate.hbm2ddl.auto", "validate");
      properties.setProperty("hibernate.dialect", dialect);
      properties.setProperty("hibernate.show_sql", "false");
      properties.setProperty("hibernate.format_sql", "true");

      return properties;
   }

}

