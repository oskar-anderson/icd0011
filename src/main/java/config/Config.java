package config;

import jdbc.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"jdbc"})
@PropertySource("classpath:/application.properties")
public class Config  {

    @Autowired
    public Environment env;


    @Bean
    public JdbcTemplate getTemplate(DataSource ds) {
        return new JdbcTemplate(ds);
    }

    public OrderDao getOrderDao(DataSource ds)
    {
        return new OrderDao(getTemplate(ds));
    }

    @Bean
    public DataSource dataSource(Environment env) {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        // ds.setUrl("jdbc:postgresql://db.mkalmo.xyz:5432/kaande");
        // ds.setUsername("kaande");
        // ds.setPassword("fb85");
        ds.setUsername(env.getProperty("dbUser"));
        ds.setPassword(env.getProperty("dbPassword"));
        ds.setUrl(env.getProperty("dbUrl"));

        return ds;
    }

}