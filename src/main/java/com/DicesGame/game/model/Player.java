package com.DicesGame.game.model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Player
{
    private String playerId;
    private String name;
    private String date;
    public Player( String playerId, String name, String date )
    {
        try
        {
            setPlayerId( playerId );
            setName( name );
            setDate( date );
        }
        catch (Exception e)
        {
            e.getMessage();
        }

    }

    public String getPlayerId()
    {
        return playerId;
    }

    public void setPlayerId(String playerId)
    {

        this.playerId = playerId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name) throws Exception
    {
        if ((name != "") && (name != null))
            this.name = name;
        else
            throw (new Exception ("Error 36 - Invalid Player Name"));
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date) throws Exception
    {
        String pattern  = "\\d{4}-\\d{2}-\\d{2}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(date);

        if (m.matches( ))
        {
            this.date = date;
        }
        else
        {
            throw (new Exception ("Error 58 - Invalid date format"));
        }

    }

    @Override
    public String toString() {
        return "{" +
                "\"playerId\":\"" + playerId + "\"" +
                ", \"name\":\"" + name + "\"" +
                ", \"date\":\"" + date + "\"" +
                "}";
    }
}
