
function biggestWinnerRefresh ()
{
     if ( userId != "" )
     {
          var localURL = window.location.href;
          var method = "GET";
          var url = "/players/ranking/winner";
          var body = "{nothing}";

          createRequestObj();
          configureRequesObj( method , url , body , "biggestWinnerRefreshReturn ( jsonObj )");
     }
}
function biggestWinnerRefreshReturn ( JsonReturn )
{
     var type = JsonReturn["Message"]["Type"];
     var message = JsonReturn["Message"]["Message"];
     if ( type == "ERROR")
     {
          console.log ("File: biggest.js");
          console.log ("Error 25 - " + message);
          alert (message);
     }
     else if ( type == "SUCCESS")
     {
          playerName = JsonReturn["Biggest"]["Name"];
          document.getElementById('biggestWinnerName').textContent = playerName;
     }
}


function biggestLoserRefresh ()
{
     if ( userId != "" )
     {
          var localURL = window.location.href;
          var method = "GET";
          var url = "/players/ranking/loser";
          var body = "";

          createRequestObj();
          configureRequesObj( method , url , body , "biggestLoserRefreshReturn ( jsonObj )");
     }
}
function biggestLoserRefreshReturn ( JsonReturn )
{
     var type = JsonReturn["Message"]["Type"];
     var message = JsonReturn["Message"]["Message"];
     if ( type == "ERROR")
     {
          console.log ("File: biggest.js");
          console.log ("Error 25 - " + message);
          alert (message);
     }
     else if ( type == "SUCCESS")
     {
          playerName = JsonReturn["Biggest"]["Name"];
          document.getElementById('biggestLoserName').textContent = playerName;
     }
}
