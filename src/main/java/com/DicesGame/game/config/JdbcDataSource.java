package com.DicesGame.game.config;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class JdbcDataSource
{

    private static final String driverClassName = "org.h2.Driver";
    private static final String url = "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1";
    private static final String dbUsername = "sa";
    private static final String dbPassword = "sa";
    private static JdbcTemplate template;

    private DataSource dataSource;

    public static void JdbcDataConnection() throws Exception
    {
        DriverManagerDataSource dataSource = getDataSource();
        //JdbcTemplate template = new JdbcTemplate(dataSource); // constructor
        template = new JdbcTemplate();
        template.setDataSource(dataSource);
    }

    private static DriverManagerDataSource getDataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    public static JdbcTemplate getTemplate() throws Exception
    {
        if ( template == null )
        {
            JdbcDataConnection();
        }
        return template;
    }
}
