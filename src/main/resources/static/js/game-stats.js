function getPlayerStats()
{
     if (( userId != "" ) && ( userId != null))
     {
          var localURL = window.location.href;
          var method = "GET";
          var url = "/players";
          var body = "";

          createRequestObj();
          configureRequesObj( method , url , body , "getPlayerStatsReturn ( jsonObj )");
     }
}
function getPlayerStatsReturn ( JsonReturn )
{
     if ( typeof JsonReturn["Message"] !== undefined)
     {
          if ( JsonReturn["Message"]["Type"] == "ERROR")
          {
               console.log ("File: game-stats.js");
               console.log ("Error 23 - " + JsonReturn["Message"]["Message"]);
               alert ( JsonReturn["Message"]["Message"] );
          }
          else if ( JsonReturn["Message"]["Type"] == "SUCCESS")
          {
               var Players = JsonReturn["Players"];
               PlayersList = new Array;
               //Order the WinLossObject
               var wpObj = JsonReturn["WinsPercentage"];

               // First sort the array
               wpObj.sort(function(a, b){return a.WinsPercentage - b.WinsPercentage});
               // Then reverse it:
               wpObj.reverse();
               for ( var i in wpObj )
               {
                    playerId = wpObj[i]["PlayerId"];
                    LostPercentage = wpObj[i]["LostPercentage"];
                    WinsPercentage = wpObj[i]["WinsPercentage"];
                    Wins = wpObj[i]["Wins"];
                    Loses = wpObj[i]["Loses"];
                    if ( typeof PlayersList[playerId] === 'undefined' )
                    {
                         PlayersList[playerId] = new Array;
                    }
                    PlayersList[playerId]["name"] = playerName;
                    PlayersList[playerId]["Wins"] = Wins;
                    PlayersList[playerId]["Loses"] = Loses;
                    PlayersList[playerId]["WinsPercentage"] = WinsPercentage;
                    PlayersList[playerId]["LostPercentage"] = LostPercentage;

               }
               //AddPlayerDetails
               for ( var i in Players )
               {

                    playerName = Players[i]["name"];
                    playerId = Players[i]["playerId"];
                    if ( typeof PlayersList[playerId] === 'undefined' )
                    {
                         PlayersList[playerId] = new Array;
                    }
                    PlayersList[playerId]["name"] = playerName;

               }


          }
          if ((typeof PlayersList !== 'undefined'))
          {
               addToStatsTable ( PlayersList );
          }

     }
}
function addToStatsTable ( PlayersList )
{
     if ((typeof PlayersList !== 'undefined'))
     {
          // Find a <table> element with id="myTable":
          var table = document.getElementById("gameStatsTable");
          var cell = new Array;


          for ( var i in PlayersList )
          {

               console.log (i);

               tableLength = table.rows.length;
               row = table.insertRow(tableLength);
               if (i == userId)
               {
                    row.classList.add("mePlayer");
               }

               j = 0;
               cell[j] = row.insertCell(j);
               cell[j].innerHTML = tableLength;
               j++;
               for ( var k in PlayersList[i] )
               {

                    if (( k == "WinsPercentage")||( k == "LostPercentage")||( k == "Wins")||( k == "Loses"))
                    {


                         value = parseFloat( PlayersList[i][k] ).toFixed(2);
                         if (isNaN(value) )
                         {
                              value = "0.00";
                         }
                    }
                    else
                    {
                         value = PlayersList[i][k];
                    }
                    cell[j] = row.insertCell(j);
                    cell[j].innerHTML = value;
                    j++;
               }
          }
     }
}
function removeStatsFromTable()
{
     var table = document.getElementById("gameStatsTable");
     while (table.rows.length > 1)
     {
          table.deleteRow(1);
     }
}
function refreshPlayerStats()
{
     removeStatsFromTable();
     getPlayerStats();

}
