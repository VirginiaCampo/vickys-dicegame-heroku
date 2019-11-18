package com.DicesGame.game.dataAccess;
import com.DicesGame.game.model.Player;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import com.DicesGame.game.model.Roll;
import com.DicesGame.game.config.JdbcDataSource;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RollRepository
{
    private JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = Logger.getLogger( PlayerRepository.class.getName() );

    public RollRepository()
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
    public int save ( String playersid , String result ) throws Exception
    {
        try
        {
            String insertSql =  "INSERT INTO rolls ( playersid, result ) VALUES ( ? , ? )";
            KeyHolder holder = new GeneratedKeyHolder();

            int row = jdbcTemplate.update(new PreparedStatementCreator()
            {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
                {
                    PreparedStatement ps =connection.prepareStatement( insertSql, Statement.RETURN_GENERATED_KEYS );
                    ps.setString(1, playersid);
                    ps.setString(2, result);
                    return ps;
                }
            }, holder );
            int newRollId = (int)holder.getKey().longValue();
            return newRollId;
        }
        catch (DataAccessException e)
        {
            throw ( new Exception ("roll - " + e.getMessage()));
        }
    }
    public Roll findByRollid ( int rollId) throws Exception
    {
        try
        {
            List<Roll> rolls = new ArrayList<>();
            jdbcTemplate.query(
                    "SELECT * FROM rolls WHERE id = ?", new Object[] { rollId },
                    (resultSet, rowNum) -> new Roll(resultSet.getInt("id"), resultSet.getString("playersid"), resultSet.getString("result"))
            ).forEach(roll -> rolls.add(roll));
            if ( rolls.size() > 0)
            {
                Iterator iter = rolls.iterator();
                Roll singleRoll = (Roll) iter.next();
                return ( singleRoll );
            }
            else
            {
                return null;
            }
        } catch (DataAccessException e)
        {
            throw (new Exception( e.getMessage() ));
        }
    }

    public List<Roll> findAllByPlayerid ( String playerId) throws Exception
    {
        try
        {
            List<Roll> rolls = new ArrayList<>();
            jdbcTemplate.query(
                    "SELECT * FROM rolls WHERE playersid = ?", new Object[] { playerId },
                    (resultSet, rowNum) -> new Roll(resultSet.getInt("id"), resultSet.getString("playersid"), resultSet.getString("result"))
            ).forEach(roll -> rolls.add(roll));
            return (rolls);
        } catch (DataAccessException e)
        {
            throw ( new Exception ("roll-" + e.getMessage()));
        }
    }
    public boolean deleteByPlayerId ( String playerId ) throws Exception
    {
        try
        {
            String deleteSql = "DELETE FROM rolls WHERE playersid = ?";
            int row = jdbcTemplate.update( deleteSql , new Object[] { playerId } );
            return true;
        } catch (Exception e)
        {
            throw ( new Exception ("roll - " + e.getMessage()));
        }
    }
    public HashMap<String, Double> findAverageWins ( String playerId )
    {
        HashMap<String, Double> results  = new HashMap<>();
        jdbcTemplate.query(
                "SELECT result, COUNT(*) plays FROM rolls WHERE playersid = ? GROUP BY result", new Object[] { playerId },
                (resultSet, rowNum) -> results.put(resultSet.getString("result") , resultSet.getDouble("plays")));
        if ( results.size() > 0 )
        {
            double wins = 0;
            double loses = 0;
            if ( results.get("WIN") != null )
                wins = results.get("WIN");
            if ( results.get("LOST") != null )
                loses = results.get("LOST");
            double total = wins + loses;

            double averageWins = (wins*100 / total);
            double averageLosts = (loses*100 / total);

            results.put("AVG_WINS" , averageWins);
            results.put("AVG_LOSTS" , averageLosts);
            return results;
        }
        else {
            return results;
        }

    }
    public String findAllAverageWins()
    {
        HashMap<String, Integer> results  = new HashMap<>();
        jdbcTemplate.query(
                "SELECT result, COUNT(*) plays FROM rolls GROUP BY result" ,
                (resultSet, rowNum) -> results.put(resultSet.getString("result") , resultSet.getInt("plays")));
        if ( results.size() > 0 )
        {

            int wins = 0;
            int loses = 0;

            if ( results.get("WIN") != null)
                wins = results.get("WIN");
            if ( results.get("LOST") != null)
                loses = results.get("LOST");

            int total = wins + loses;

            double averageWins = (wins*100 / total);
            StringBuilder jsonString = new StringBuilder();
            jsonString.append( "{");
            jsonString.append( "\"Wins\":\"" + wins + "\", ");
            jsonString.append( "\"Loses\":\"" + loses + "\", ");
            jsonString.append( "\"WinsPercentage\":\"" + averageWins + "\" ");
            jsonString.append( "}, ");
            return jsonString.toString();
        }
        else
        {
            return "{}";
        }
    }

    public String findBiggest ( List<String> playersIds, String type )
    {
        Iterator iterator = playersIds.iterator();
        HashMap<String, Double> results  = new HashMap<>();
        double oldAVG = -1;
        String biggestId = "";
        while ( iterator.hasNext() )
        {
            String playerId = iterator.next().toString();

            results = this.findAverageWins ( playerId );
            if (type =="winner")
            {
                if ( results.size() != 0 )
                {
                    //look for the lowest AVG_WINS
                    if ( ( biggestId == "" ) || ( results.get("AVG_WINS") > oldAVG) )
                    {
                        oldAVG = results.get("AVG_WINS");
                        biggestId = playerId;
                    }
                }
            }
            else if (type =="loser")
            {
                if ( results.size() != 0 )
                {
                    //look for the lowest AVG_LOSTS
                    if ( ( biggestId == "" ) || ( results.get("AVG_WINS") < oldAVG) )
                    {
                        oldAVG = results.get("AVG_WINS");
                        biggestId = playerId;
                    }
                }
            }


        }
        PlayerRepository playerRepo = new PlayerRepository();
        Player winner = playerRepo.findByPlayerid( biggestId );
        StringBuilder jsonString = new StringBuilder();
        jsonString.append( "Biggest:{");
        jsonString.append( "\"PlayerId\":\"" + biggestId + "\", ");
        jsonString.append( "\"type\":\"" + type + "\", ");
        jsonString.append( "\"Name\":\"" + winner.getName() + "\", ");
        jsonString.append( "\"AVG_WINS\":\"" + oldAVG + "\" ");
        jsonString.append( "}, ");
        return jsonString.toString();
    }
}
