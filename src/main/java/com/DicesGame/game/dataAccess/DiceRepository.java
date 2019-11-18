package com.DicesGame.game.dataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import com.DicesGame.game.model.Dice;
import com.DicesGame.game.config.JdbcDataSource;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiceRepository
{
    private JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = Logger.getLogger( PlayerRepository.class.getName() );
    private List<Dice> dices;

    public DiceRepository()
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
    public int save ( int rollsid , int dicenumber , int roll ) throws Exception
    {
        try
        {
            String insertSql =  "INSERT INTO dices ( rollsid, dicenumber, roll ) VALUES ( ? , ? , ? )";
            KeyHolder holder = new GeneratedKeyHolder();

            int row = jdbcTemplate.update(new PreparedStatementCreator()
            {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
                {
                    PreparedStatement ps =connection.prepareStatement( insertSql , Statement.RETURN_GENERATED_KEYS );
                    ps.setInt(1, rollsid);
                    ps.setInt(2, dicenumber);
                    ps.setInt(3, roll);
                    return ps;
                }
            }, holder );
            int newDiceId = (int)holder.getKey().longValue();
            return newDiceId;
        }
        catch (DataAccessException e)
        {
            throw ( new Exception ("Dice - " + e.getMessage()));
        }
    }
    public List<Dice> findByRollsId ( int rollsId ) throws Exception
    {
        try
        {
            List<Dice> dices = new ArrayList<>();
            jdbcTemplate.query(
                    "SELECT * FROM dices WHERE rollsId = ?", new Object[] { rollsId },
                    (resultSet, rowNum) -> new Dice (resultSet.getInt("id"), resultSet.getInt("rollsid"), resultSet.getInt("dicenumber"), resultSet.getInt("roll"))
            ).forEach(dice -> dices.add(dice));
            return dices;
        }
        catch (DataAccessException e)
        {
            throw (new Exception( e.getMessage() ));
        }
    }
    public List<Dice> findByRollsIds ( List<Integer> thisRollId ) throws Exception
    {
        try
        {
            Iterator iterator = thisRollId.iterator();
            List<Dice> dices = new ArrayList<>();
            while ( iterator.hasNext() )
            {

                jdbcTemplate.query(
                        "SELECT * FROM dices WHERE rollsId = ?", new Object[] { iterator.next() },
                        (resultSet, rowNum) -> new Dice (resultSet.getInt("id"), resultSet.getInt("rollsid"), resultSet.getInt("dicenumber"), resultSet.getInt("roll"))
                ).forEach(dice -> dices.add(dice));

            }
            return dices;

        }
        catch (DataAccessException e)
        {
            throw (new Exception( e.getMessage() ));
        }



    }
    public boolean deleteByRollIds ( List<Integer> rollsIds ) throws Exception
    {
        try
        {
            Iterator iterator = rollsIds.iterator();
            String deleteSql = "DELETE FROM dices WHERE rollsId = ?";
            while ( iterator.hasNext() )
            {
                int row = jdbcTemplate.update( deleteSql , iterator.next() );
            }
            return true;
        } catch (Exception e)
        {
            throw ( new Exception ("Dice - " + e.getMessage()));
        }
    }
}
