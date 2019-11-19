package com.DicesGame.game;

import com.DicesGame.game.config.JdbcDataSource;
import com.DicesGame.game.controller.VirtualPlayersController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.logging.Level;


@SpringBootApplication
public class GameApplication
{

	private static final Logger log = LoggerFactory.getLogger(GameApplication.class);

	public static void main(String[] args)
    {

    	SpringApplication.run(GameApplication.class, args);
		/* Create the tables */
		try
		{
			JdbcTemplate jdbcTemplate;
			jdbcTemplate = JdbcDataSource.getTemplate();
			log.info("Creating tables");
			/*jdbcTemplate.execute("DROP TABLE dices IF EXISTS");
			jdbcTemplate.execute("DROP TABLE players IF EXISTS");
			jdbcTemplate.execute("DROP TABLE rolls IF EXISTS");*/

			try {
				jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS dices(" +
						"id int(11) NOT NULL auto_increment, " +
						"rollsid int(11) NOT NULL, " +
						"dicenumber int(11) NOT NULL, " +
						"roll int(11) NOT NULL " +
						")");
			} catch (DataAccessException e) {
				System.out.println(e.getMessage());
			}

			try {
				jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS players(" +
						"playerId varchar(45) NOT NULL, " +
						"name varchar(45) NOT NULL, " +
						"date date NOT NULL " +
						")");
			} catch (DataAccessException e) {
				e.printStackTrace();
			}

			try {
				jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS rolls(" +
						"id int(11) NOT NULL AUTO_INCREMENT, " +
						"playersid varchar(45) NOT NULL, " +
						"result varchar(45) NOT NULL " +
						")");
			} catch (DataAccessException e) {
				e.printStackTrace();
			}

		}
		catch ( Exception e)
		{
			log.info("Error in DB connection", e.getCause());
		}

		VirtualPlayersController.cratePlayers( 10 );
	}


}
