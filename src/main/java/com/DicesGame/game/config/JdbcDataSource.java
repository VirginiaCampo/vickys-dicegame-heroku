package com.DicesGame.game.config;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class JdbcDataSource
{

    private static final String driverClassName = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/db_dicegame?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String dbUsername = "root";
    private static final String dbPassword = "root";
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
