package com.DicesGame.game.controller;


import com.DicesGame.game.dataAccess.*;
import com.DicesGame.game.model.*;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/")
public class PlayerController
{
    private List<Player> players;
    private Player singlePlayers;
    private List<Roll> rolls;
    private Roll singleRoll;
    private List<Dice> dices;
    private Dice singleDice;

    private PlayerRepository playerRepo  = new PlayerRepository();
    private RollRepository rollRepo  = new RollRepository();
    private DiceRepository diceRepo  = new DiceRepository();


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping ( value = "/players/home" )
    public String homePage (  )
    {
        try {
            this.clearData ();
            players = playerRepo.findAll();
            String jsonString = "{" + this.toString() + "}";
            JSONObject sendData = new JSONObject( jsonString );
            return sendData.toString();
        } catch (Exception e)
        {
            String jsonString = generateMessageJson ( "ERROR", "Error 39 - " + e.getMessage() );
            JSONObject sendData = new JSONObject( "{" + jsonString + "}" );
            return sendData.toString();
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping (value ="/autogenerate")
    public String autogenerate ()
    {

        try
        {
            this.clearData ();
            List<Player> localPlayers;
            localPlayers = playerRepo.findAll();
            Iterator iterator = localPlayers.iterator();
            String playerId = "";
            int randomPlays = (int)(1 + (Math.random() * (10 - 1)));
            while ( iterator.hasNext() )
            {
                Player player = (Player) iterator.next();
                playerId = player.getPlayerId();

                System.out.println("player id - " + playerId + " - randomPlays - " + randomPlays);
                for (int i = 0; i < randomPlays ; i++)
                {
                    System.out.println( this.rollDices ( playerId ) );

                }

            }
            return ("ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ("error");

    }

    //POST: /players: crea un jugador
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping ( value = "/players" )
    public String createPlayer ( @RequestBody String dataString )
    {
        this.clearData ();
        //check if name available
        JSONObject getData = new JSONObject( dataString );
        String name = getData.getString("name");

        List<Player> players = null;
        if ( name.equals("") )
        {
            name = "Anonymous";
        }
        else
        {
            players = playerRepo.findByName( name );
        }
        if (( players != null )&&( players.size() > 0 ))
        {
            //players exists and cannot be added again
            String jsonString = generateMessageJson ( "ERROR", "That name already exists in the db." );
            JSONObject sendData = new JSONObject( "{" + jsonString + "}" );
            return sendData.toString();
        }
        else
        {

            try
            {
                String playerId = playerRepo.save( name );
                String jsonString = generateMessageJson ( "SUCCESS", "Player added to the db." );
                JSONObject sendData = new JSONObject( "{" + jsonString + "}" );
                sendData.put("playerId", playerId);
                return sendData.toString();
            }
            catch (Exception e)
            {
                String jsonString = generateMessageJson ( "ERROR 83 - ", e.getMessage() );
                JSONObject sendData = new JSONObject( "{" + jsonString + "}" );
                return sendData.toString();
            }


        }
    }

    //PUT / players: modifica el nombre del jugador
    //@RequestMapping( value = "/employees", produces = "application/json", method = {RequestMethod.PUT})
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping  ( value = "/players" )
    public String modifyPlayer ( @RequestBody String dataString )
    {
        this.clearData ();
        //check if name available
        JSONObject getData = new JSONObject( dataString );
        String playerId = getData.getString("playerId");
        String name = getData.getString("name");
        List<Player> players = null;
        if ( name.equals("") )
        {
            name = "Anonymous";
        }
        else
        {
            players = playerRepo.findByName( name );
        }

        if (( players != null )&&( players.size() > 0 ))
        {
            //players exists and cannot be added again
            String jsonString = generateMessageJson ( "ERROR", "That new name already exists in the db." );

            JSONObject sendData = new JSONObject( "{" + jsonString + "}" );
            return sendData.toString();
        }
        else
        {
            try
            {
                playerId = playerRepo.update(playerId ,  name );
                String jsonString = generateMessageJson ( "SUCCESS", "Player modified in the db." );
                JSONObject sendData = new JSONObject( "{" + jsonString + "}" );
                sendData.put("playerId", playerId);
                return sendData.toString();
            }
            catch (Exception e)
            {
                String jsonString = generateMessageJson ( "ERROR 132 - ", e.getMessage() );
                JSONObject sendData = new JSONObject( "{" + jsonString + "}" );
                return sendData.toString();
            }
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping (value = "/players/{playerId}")
    public String deletePlayer ( @PathVariable String playerId )
    {
        this.clearData ();
        try
        {
            //check if we have this player in the db
            Player players = playerRepo.findByPlayerid( playerId );
            if ( players != null )
            {
                playerId = playerRepo.delete(playerId);
                deletePlayerRolls ( playerId );
                String jsonString = generateMessageJson ( "SUCCESS", "Player was removed from the db." );
                JSONObject sendData = new JSONObject( "{" + jsonString + "}" );
                sendData.put("playerId", playerId);
                return sendData.toString();
            }
            else
            {
                String jsonString = generateMessageJson ( "ERROR", "No player was found with this playerId" );
                JSONObject sendData = new JSONObject( "{" + jsonString + "}" );
                return sendData.toString();
            }

        }
        catch (Exception e)
        {
            String jsonString = generateMessageJson ( "ERROR 165 - ", e.getMessage() );
            JSONObject sendData = new JSONObject( "{" + jsonString + "}" );
            return sendData.toString();
        }

    }

    //POST / players / {id} / games /: un jugador específico realiza una tirada de los dados.
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping (value = "/players/{playerId}/games")
    public String rollDices ( @PathVariable String playerId )
    {
        this.clearData ();
        //set parameters for this game
        int numberOfDices = 6;
        int min = 1;
        int max = 6;
        //throw the dices
        int[] results = new int[numberOfDices];
        int range = (max - min) + 1;
        String result = "";
        boolean wonGame = true;
        for (int i = 0; i < numberOfDices; i++)
        {
            do
            {
                results[i] = (int) (Math.random() * range) + min;

            } while ( (results[i] < min ) || (results[i] > max ) );

        }
        result = this.testResults ( results );
        try
        {
            //save in the rolls table (using the player id / get the roll id for dices)
            int rollId = rollRepo.save( playerId , result );
            //save in the dices table (using the rolls Id)
            for (int i = 0; i < numberOfDices; i++)
            {
                int diceId = diceRepo.save( rollId , i , results[i] );
            }
            //add success message
            StringBuilder jsonString = new StringBuilder( generateMessageJson ( "SUCCESS", "Successful roll ." ) );

            //get player, rolls and dices info
            //this.singlePlayers = playerRepo.findByPlayerid( playerId );
            this.singleRoll = rollRepo.findByRollid ( rollId );
            this.dices = diceRepo.findByRollsId( rollId );

            jsonString.append(", ");
            jsonString.append(this.toString());

            JSONObject sendData = new JSONObject( "{" + jsonString.toString() + "}" );
            return sendData.toString();
        }
        catch ( Exception e )
        {
            String jsonString = generateMessageJson ( "ERROR 231 - ", e.getMessage() );
            JSONObject sendData = new JSONObject( "{" + jsonString + "}" );
            return sendData.toString();
        }

    }

    /**
     * Test the result of the dices for a win or lost
     * @param resultsArray
     * @return
     */
    private String testResults (int [] resultsArray )
    {
        int[] count = { 0, 0, 0, 0, 0,0 };
        //check the result - win - the sum is 7 / lose anything else.
        for (int i = 0; i < resultsArray.length ; i++)
        {
            int index = resultsArray[i]-1;
            count[ index ] ++;
            if ( count [ index ]>2)
                return "WIN";
        }
        return "LOST";
    }


    //DELETE / players / {id} / games: elimina las tiradas del jugador.
    @CrossOrigin(origins = "*", allowedHeaders = "*")
        @DeleteMapping (value = "/players/{playerId}/games")
    public String deletePlayerRolls ( @PathVariable String playerId )
    {
        try
        {
            this.clearData ();
            //get the rolls id's
            this.rolls = rollRepo.findAllByPlayerid( playerId );
            Iterator iterator = rolls.iterator();
            List<Integer> rollsIds = new ArrayList<>();
            while ( iterator.hasNext() )
            {
                rollsIds.add( ((Roll)iterator.next()).getId() );
            }
            //erase the dices
            if ( diceRepo.deleteByRollIds ( rollsIds ) )
            {
                //erase the rolls
                if ( ! rollRepo.deleteByPlayerId ( playerId ) )
                {
                    String jsonString = generateMessageJson ( "ERROR", "Error 259 - The rolls were not deleted." );
                    JSONObject sendData = new JSONObject( "{" + jsonString + "}" );
                    return sendData.toString();
                }
            }
            else
            {
                String jsonString = generateMessageJson ( "ERROR", "Error 266 - The dices were not deleted." );
                JSONObject sendData = new JSONObject( "{" + jsonString + "}" );
                return sendData.toString();
            }
            String jsonString = generateMessageJson ( "SUCCESS", "Rolls were erased successfully " );
            JSONObject sendData = new JSONObject( "{" + jsonString + "}" );
            return sendData.toString();
        } catch (Exception e)
        {
            String jsonString = generateMessageJson ( "ERROR", "Error 275 - " + e.getMessage() );
            JSONObject sendData = new JSONObject( "{" + jsonString + "}" );
            return sendData.toString();
        }
    }

    //GET / players / {id} / games: devuelve el listado de jugadas por un jugador.
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping (value = "/players/{playerId}/games")
    public String getPlayerRolls (@PathVariable String playerId )
    {
        this.clearData ();
        try
        {
            //get player, rolls and dices info
            this.singlePlayers = playerRepo.findByPlayerid( playerId );
            this.rolls = rollRepo.findAllByPlayerid ( playerId );
            Iterator iterator = this.rolls.iterator();
            List<Integer> RollsIds = new ArrayList<>();
            while ( iterator.hasNext() )
            {
                RollsIds.add( ( (Roll)iterator.next() ).getId() );
            }
            this.dices = diceRepo.findByRollsIds( RollsIds );
        }
        catch (NullPointerException e)
        {

            String jsonString = generateMessageJson ( "ERROR", "Error 302 - " + e.getMessage() );
            JSONObject sendData = new JSONObject( "{" + jsonString + "}" );
            return sendData.toString();
        }
        catch ( Exception e )
        {
            String jsonString = generateMessageJson ( "ERROR", "Error 308 - " + e.getMessage() );
            JSONObject sendData = new JSONObject( "{" + jsonString + "}" );
            return sendData.toString();
        }

        StringBuilder jsonString = new StringBuilder();
        jsonString.append(this.toString());
        JSONObject sendData = new JSONObject( "{" + jsonString.toString() + "}" );
        return sendData.toString();
    }

    @Override
    public String toString ()
    {
        StringBuilder jsonString = new StringBuilder ("");
        if ( ( players != null ) && ( !players.isEmpty() ) )
        {
            jsonString.append("\"Player\": [");
            players.forEach( player -> jsonString.append( player.toString() + ", " ) );
            jsonString.delete(jsonString.length()-2 , jsonString.length());
            jsonString.append("]");

        }
        if ( ( rolls != null ) && ( !rolls.isEmpty() ) )
        {
            if ( ! jsonString.toString().equals("") )
            {
                jsonString.append(", ");
            }
            jsonString.append("\"Rolls\": [");
            rolls.forEach( roll -> jsonString.append( roll.toString() + ", " ) );
            jsonString.delete(jsonString.length()-2 , jsonString.length());
            jsonString.append("]");

        }
        if ( (dices != null) && ( !dices.isEmpty() ) )
        {
            if ( ! jsonString.toString().equals("") )
            {
                jsonString.append(", ");
            }
            jsonString.append("\"Dices\": [");
            dices.forEach( dice -> jsonString.append( dice.toString() + ", " ) );
            jsonString.delete(jsonString.length()-2 , jsonString.length());
            jsonString.append("]");

        }
        if ( singlePlayers != null )
        {
            if ( ! jsonString.toString().equals("") )
            {
                jsonString.append(", ");
            }
            jsonString.append("\"Player\":");
            jsonString.append( singlePlayers.toString() );

        }
        if ( singleRoll != null )
        {
            if ( ! jsonString.toString().equals("") )
            {
                jsonString.append(", ");
            }
            jsonString.append("\"Roll\":");
            jsonString.append( singleRoll.toString() );

        }
        if ( singleDice != null )
        {
            if ( ! jsonString.toString().equals("") )
            {
                jsonString.append(", ");
            }
            jsonString.append("\"Dice\":");
            jsonString.append( singleDice.toString() );

        }
        return jsonString.toString();
    }

    public String generateMessageJson ( String type, String message )
    {
        StringBuilder jsonString = new StringBuilder("Message:{");
        jsonString.append("\"Type\":\"" + type + "\", ");
        jsonString.append("\"Message\":\"" + message + "\" ");
        jsonString.append("}");
        return jsonString.toString();
    }

    public void clearData ()
    {
        if ( players != null)
        {
            players.clear();
        }
        if ( rolls != null)
        {
            rolls.clear();
        }
        if ( dices != null) {
            dices.clear();
        }

        singlePlayers = null;
        singleRoll = null;
        singleDice = null;
    }







}
