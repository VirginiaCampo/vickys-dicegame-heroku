package com.DicesGame.game.controller;

import com.DicesGame.game.dataAccess.PlayerRepository;
import com.DicesGame.game.model.Player;
import com.DicesGame.game.model.VirtualPlayer;

import java.util.ArrayList;
import java.util.List;

public class VirtualPlayersController
{
    private static List<VirtualPlayer> virtualPlayerList = new ArrayList<>();
    private static PlayerRepository playerRepo= new PlayerRepository();
    private static String[] names = {
            "Jorge", "Mario","Pepe","Maria","Luisa","Alfonso","Anonymous","Andres","Vicky","Aike","Alex",
            "Alexis","Akira","Charlie","Eider","Joss","Morgan","Tyler","Robin","Zuri","Sasha" };
    public static void cratePlayers ( int numberOfVP )
    {
        //Get the number of virtual players we are going to generate.
        int randomName = 0;
        boolean nameCorrect = true;

        for (int i = 0; i < numberOfVP; i++)
        {
            double min = Math.ceil(1);
            double max = Math.floor(names.length-1);
            randomName = (int)(Math.floor(Math.random() * (max - min + 1)) + min);

            do
            {
                nameCorrect = false;
                if ( playerRepo.findByName( names[randomName] ).size() == 0 )
                {
                    nameCorrect = true;
                    try {
                        String playerId = playerRepo.save( names[randomName] );
                        Player player = playerRepo.findByPlayerid( playerId );
                        VirtualPlayer vp = new VirtualPlayer( player );
                        virtualPlayerList.add ( vp );
                    } catch (Exception e)
                    {
                        e.printStackTrace();

                    }
                }
                else
                {
                    nameCorrect = true;
                    Player player = playerRepo.findByName( names[randomName] ).get(0);
                    VirtualPlayer vp = new VirtualPlayer( player );
                    virtualPlayerList.add ( vp );
                }

            } while ( ! nameCorrect );
        }
    }
    public static void PlayerLeftTheBuilding ( Player player )
    {
        System.out.println("We have " + virtualPlayerList.size() + " Currently playing");

        getReplacementPlayer ( player );
    }
    public static void getReplacementPlayer ( Player player )
    {
        //player = null;
        double min = Math.ceil(1);
        double max = Math.floor(names.length-1);
        int randomName = (int)(Math.floor(Math.random() * (max - min + 1)) + min);

        boolean nameCorrect = false;
        if ( playerRepo.findByName( names[randomName] ).size() == 0 )
        {
            nameCorrect = true;
            try {
                String playerId = playerRepo.save( names[randomName] );
                player = playerRepo.findByPlayerid( playerId );
                VirtualPlayer vp = new VirtualPlayer( player );
            } catch (Exception e)
            {
                e.printStackTrace();

            }
        }
        else
        {
            nameCorrect = true;
            player = playerRepo.findByName( names[randomName] ).get(0);
            VirtualPlayer vp = new VirtualPlayer( player );
        }

    }

}
