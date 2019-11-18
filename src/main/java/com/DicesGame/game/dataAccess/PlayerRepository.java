package com.DicesGame.game.dataAccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import com.DicesGame.game.model.Player;
import com.DicesGame.game.config.JdbcDataSource;

import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PlayerRepository
{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = Logger.getLogger( PlayerRepository.class.getName() );

    public PlayerRepository()
    {
        try
        {
            jdbcTemplate = JdbcDataSource.getTemplate();
        }
        catch ( Exception e)
        {
            LOGGER.log(Level.FINE, "Error in DB connection", e.getCause());
        }

    }
    public static String generateRandomId ()
    {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    public String save ( String name ) throws Exception
    {
        String playerid = generateRandomId();
        LocalDate date = LocalDate.now();
        String insertSql =  "INSERT INTO players ( playerid,  name, date ) VALUES ( ? , ? , ? )";
        Object[] params = new Object[] { playerid, name, date.toString() };
        int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
        try
        {
            int row = jdbcTemplate.update(insertSql, params, types);

            return playerid;
        }
        catch (DataAccessException e)
        {
            throw ( new Exception ("Player - "  + e.getMessage()));
        }

    }
    public String delete ( String playerId) throws Exception
    {
        try
        {
            String deleteSql = "DELETE FROM players WHERE playerid = ?";
            int row = jdbcTemplate.update( deleteSql , playerId );
            if ( row == 0)
            {
                throw ( new Exception("Error 68 - No record was deleted"));
            }
            return playerId;
        }
        catch (DataAccessException e)
        {
            throw ( new Exception ("Player - " + e.getMessage()));
        }
    }
    public String update ( String playerId , String name ) throws Exception
    {
        String updateSql = "UPDATE players SET name = ? WHERE playerid = ?";
        Object[] params = new Object[] { name , playerId };
        int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
        try
        {
            int row = jdbcTemplate.update(updateSql, params, types);
            return playerId;
        }
        catch (DataAccessException e)
        {
            throw ( new Exception ("Player - " + e.getMessage()));
        }
    }
    public List<Player> findAll () throws Exception
    {
        try
        {
            List<Player> players = new ArrayList<>();
            jdbcTemplate.query(
                    "SELECT * FROM players Order by name asc",
                    (resultSet, rowNum) -> new Player(resultSet.getString("playerId"), resultSet.getString("name"), resultSet.getString("date"))
            ).forEach(player -> players.add(player));
            LOGGER.log(Level.FINE, "getAllPlayers()", players);
            return (players);
        } catch (DataAccessException e)
        {
            throw (new Exception( e.getMessage() ));
        }
    }

    public List<Player> findByName ( String playerName )
    {
        List<Player> players = new ArrayList<>();
        jdbcTemplate.query(
                "SELECT * FROM players WHERE name = ?", new Object[] { playerName },
                (resultSet, rowNum) -> new Player(resultSet.getString("playerId"), resultSet.getString("name"), resultSet.getString("date"))
        ).forEach(player -> players.add(player));
        return (players);
    }
    public Player findByPlayerid ( String playerId )
    {

        List<Player> players = new ArrayList<>();
        jdbcTemplate.query(
                "SELECT * FROM players WHERE playerid = ?", new Object[] { playerId },
                (resultSet, rowNum) -> new Player(resultSet.getString("playerId"), resultSet.getString("name"), resultSet.getString("date"))
        ).forEach(player -> players.add(player));

        if ( players.size() > 0)
        {
            Iterator iter = players.iterator();
            Player singlePlayer = (Player) iter.next();
            return singlePlayer;
        }
        else
        {
            return null;
        }



    }
    public List<String> findAllIds()
    {
        List<String> playersIds = new ArrayList<>();
        jdbcTemplate.query(
                "SELECT playerid FROM players",
                (resultSet, rowNum) -> playersIds.add(resultSet.getString("playerid")));
        return (playersIds);
    }


}
