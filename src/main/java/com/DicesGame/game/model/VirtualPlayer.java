package com.DicesGame.game.model;
import com.DicesGame.game.controller.PlayerController;
import com.DicesGame.game.controller.VirtualPlayersController;
import com.DicesGame.game.model.Player;

import java.util.concurrent.atomic.AtomicBoolean;

public class VirtualPlayer implements Runnable
{
    private Thread t;
    private Player player;
    private int key;
    private AtomicBoolean running = new AtomicBoolean(false);
    private PlayerController playerController = new PlayerController();
    public VirtualPlayer ( Player player )
    {
        System.out.println("******************************************************");
        System.out.println( player.getName() + " has joined the game ");
        System.out.println("******************************************************");
        setPlayer( player );
        running.set( true );
        start ();
    }

    /**
     * Getter and Setters
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Action specific of the game
     * Rolling / Sleeping / Deleting
     * @return
     */
    public void rollDices ()
    {

        playerController.rollDices( player.getPlayerId() );
        System.out.println(player.getName() + " just rolled the dices.");
    }
    public void sleeping ()
    {

        try {
            long start = System.currentTimeMillis();
            Thread.sleep(2000);
            System.out.println(player.getName() + " Sleep time in ms = "+(System.currentTimeMillis()-start));
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    public void delete ()
    {

        running.set( false );
        String playerId = player.getPlayerId();
        playerController.deletePlayer( playerId );
        System.out.println("******************************************************");
        System.out.println("Player " + player.getName() + " has left the building.");
        System.out.println("******************************************************");
        VirtualPlayersController.PlayerLeftTheBuilding ( player );
    }


    /**
     * Start & Run methods
     */
    public void start ()
    {
        /* Generates all the things we need to run the thread */
        if (t == null) {
            t = new Thread( this );
            t.start ();
        }
    }
    public void run()
    {
        String[] states = {"Rolling", "Rolling", "Rolling", "Rolling", "Sleeping", "Sleeping", "Sleeping",  "Deleting"};
        double min = Math.ceil(1);
        double max = Math.floor(states.length);
        int stateId;

        while ( running.get() )
        {
            stateId = (int)(Math.floor(Math.random() * (max - min + 1)) + min);
            /* Rolling / Sleeping / Deleting */

            switch(states[stateId - 1]) {
                case "Rolling":
                    rollDices ();
                    break;
                case "Sleeping":
                    sleeping ();
                    break;
                case "Deleting":
                    delete ();
                    break;
            }
            if ( states[stateId - 1] != "Deleting" ) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println( "Breaking Thread" );
                break;
            }
        }


    }
}
