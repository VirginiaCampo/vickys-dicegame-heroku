package com.DicesGame.game.model;

public class Roll
{
    private int id;
    private String playerId;
    private String result;

    public Roll( int id, String playerId, String result )
    {
        setId( id );
        setPlayerId( playerId );
        setResult( result );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + this.id + "\"" +
                ", \"playerId\":\"" + this.playerId + "\"" +
                ", \"result\":\"" + this.result + "\"}";
    }
}
