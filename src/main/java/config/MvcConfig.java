package config;

import global.Global;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.util.Properties;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "app", "jdbc" })
@PropertySource("classpath:/application.properties")
public class MvcConfig {
   @Bean
   public JdbcTemplate getTemplate(DataSource ds) {
      return new JdbcTemplate(ds);
   }

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

         var populator = new ResourceDatabasePopulator(
                 new ClassPathResource("schema.sql"),
                 new ClassPathResource("data.sql"));
         DatabasePopulatorUtils.execute(populator, ds);

         return ds;
      }
      throw new RuntimeException("Specify Db to use in properties!");
   }
}