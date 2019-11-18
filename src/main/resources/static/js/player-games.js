function getPlayerRolls()
{
     if (( userId != "" ) && ( userId != null))
     {
          //we check if we need to get the rolls
          //"/players/{playerId}/games
          var localURL = window.location.href;
          var method = "GET";
          var url = "/players/" + userId + "/games";
          var body = "";

          createRequestObj();
          configureRequesObj( method , url , body , "getPlayerRolls_return ( jsonObj )");
     }

}
function getPlayerRolls_return ( JsonReturn )
{
     var Dices = new Array;
     var Rolls;
     if (typeof JsonReturn["Message"] !== 'undefined')
     {
          var type = JsonReturn["Message"]["Type"];
          var message = JsonReturn["Message"]["Message"];

          if ( type == "ERROR")
          {
               console.log ("File: player-games.js");
               console.log ("Error 31 - " + message);
               alert (message);
          }
          else if ( type == "SUCCESS")
          {

          }
     }
     if (typeof JsonReturn["Rolls"] !== 'undefined')
     {
          var Rolls = JsonReturn["Rolls"];
     }
     if (typeof JsonReturn["Dices"] !== 'undefined')
     {
          var DicesJSON = JsonReturn["Dices"];
          for ( var i in DicesJSON)
          {
               rollid = DicesJSON[i]["rollid"];
               diceNumber =  DicesJSON[i]["dicenumber"];
               roll =  DicesJSON[i]["roll"];
               if (typeof Dices === 'undefined')
               {
                    Dices = new Array;
               }
               if (typeof Dices[rollid] === 'undefined')
               {
                    Dices[rollid] = new Array;
               }
               Dices[rollid][diceNumber] = roll;
          }

     }
     if ((typeof Rolls !== 'undefined')&&(typeof Dices !== 'undefined'))
     {
          addToGameTable ( Rolls , Dices );
     }
}
function addToGameTable ( Rolls , Dices )
{
     if ((typeof Rolls !== 'undefined') && (typeof Dices !== 'undefined'))
     {
          // Find a <table> element with id="myTable":
          var table = document.getElementById("playerGameTable");
          var cell = new Array;


          for ( var i in Rolls )
          {
               rollid = Rolls[i]["id"];
               tableLength = table.rows.length;
               row = table.insertRow(tableLength);
               j = 0;
               cell[j] = row.insertCell(j);
               cell[j].innerHTML = tableLength;
               j++;
               if (typeof Dices[rollid] !== 'undefined')
               {
                    for ( var k in Dices[rollid] )
                    {

                         cell[j] = row.insertCell(j);
                         cell[j].innerHTML = Dices[rollid][k];
                         j++;
                    }
               }
               cell[j] = row.insertCell(j);
               cell[j].innerHTML = Rolls[i]["result"];
               j++;


          }

     }


}
     function removeGamesFromTable()
{
     var table = document.getElementById("playerGameTable");
     while (table.rows.length > 1)
     {
          table.deleteRow(1);
     }

}
function refreshPlayerGames ()
{
     removeGamesFromTable();
     getPlayerRolls();
}
function deletePlayerGames()
{
     var localURL = window.location.href;
     var method = "DELETE";
     var url = "/players/" + userId + "/games";
     var body = "";

     createRequestObj();
     configureRequesObj( method , url , body , "deletePlayerGamesReturn ( jsonObj )");

}
function deletePlayerGamesReturn( JsonReturn )
{


     if ( typeof JsonReturn["Message"] !== undefined)
     {
          if ( JsonReturn["Message"]["Type"] == "ERROR")
          {
               console.log ("File: player-games.js");
               console.log ("Error 143 - " + JsonReturn["Message"]);
               alert ( JsonReturn["Message"] );
          }
          else if ( JsonReturn["Message"]["Type"] == "SUCCESS")
          {
               removeGamesFromTable();
               getPlayerRolls();
          }
     }

}
