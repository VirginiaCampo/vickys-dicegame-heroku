package com.DicesGame.game;

import com.DicesGame.game.controller.VirtualPlayersController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class GameApplication
{
	public static void main(String[] args)
    {

    	SpringApplication.run(GameApplication.class, args);
		VirtualPlayersController.cratePlayers( 10 );
	}


}
