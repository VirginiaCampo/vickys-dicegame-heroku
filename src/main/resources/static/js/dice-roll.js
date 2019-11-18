var dice_roll;
var opacity;
var delayInMilliseconds = 1000;
var dicesIMG = new Array;
var loopCounter = 0;
var returnJson;
function doYouWantToRolldice()
{
     if (userId != "")
     {
          console.log ("*************************************");
          console.log ("**** - Activate rolling screen - ****");
          console.log ("*************************************");
          activeSection = document.getElementById('dice-roll');
          fixSizeLocation ( activeSection );
          document.getElementById('diceRollingBTN').addEventListener( 'click', letsRollTheDice );

          getPlayerRolls();
          refreshPlayerStats();
          biggestWinnerRefresh();
          biggestLoserRefresh();

          dicesIMG = document.getElementsByClassName('dice-roll__rollDices--dices--img');
          document.getElementById("refreshHistory").addEventListener( 'click' , refreshPlayerGames );
          document.getElementById("deleteHistory").addEventListener( 'click' , deletePlayerGames );
          document.getElementById("refreshStats").addEventListener( 'click' , refreshPlayerStats );
          document.getElementById("biggestWinnerRefreshIcon").addEventListener( 'click' , biggestWinnerRefresh );
          document.getElementById("biggestLoserRefreshIcon").addEventListener( 'click' , biggestLoserRefresh );
     }


}
function stopRolling ()
{
     activeSection = document.getElementsByTagName('hgroup')[0];
     fixSizeLocation ( activeSection );

}
function letsRollTheDice ()
{
     var localURL = window.location.href;
     var method = "POST";
     var url = "/players/" + userId + "/games";
     var body = JSON.stringify({"name": userName});

     createRequestObj();
     configureRequesObj( method , url , body , "getRollTheDice ( jsonObj )");
     document.getElementById('diceRollingBTN').textContent = "Rolling...";
     startRollingDice();
}
function getRollTheDice( jsonObj )
{
     returnJson = jsonObj;
}
function startRollingDice()
{
     if ( loopCounter < 15)
     {
          loopCounter ++;
          for ( var i in dicesIMG )
          {
               if (typeof dicesIMG[i].src !== 'undefined')
               {
                    random = Math.floor(Math.random() * 6) + 1;
                    root = dicesIMG[i].src.substring(0, dicesIMG[i].src.length-5);
                    dicesIMG[i].src = root + random + ".svg";
               }

          }
          myVar = setTimeout(startRollingDice, 150);

     }
     else
     {
               var type = returnJson["Message"]["Type"];
               var message = returnJson["Message"]["Message"];
               if ( type == "SUCCESS")
               {
                    var Roll = returnJson["Roll"];
                    // chec if is a win or loose
                    if ( Roll["result"] =="LOST" )
                    {
                         document.getElementById('diceRollingBTN').textContent = "You lost, try again!";
                    }
                    else if ( Roll["result"] =="LOST" )
                    {
                         document.getElementById('diceRollingBTN').textContent = "Yay! you win! keep it up!";
                    }
                    //place the dices
                    var DicesJson = returnJson["Dices"];

                    for ( var i in dicesIMG )
                    {
                         if (typeof dicesIMG[i].src !== 'undefined')
                         {
                              // Put the results in the dices
                              root = dicesIMG[i].src.substring(0, dicesIMG[i].src.length-5);
                              dicesIMG[i].src = root + DicesJson[i]['roll'] + ".svg";

                              rollid = DicesJson[i]["rollid"];
                              diceNumber =  DicesJson[i]["dicenumber"];
                              roll =  DicesJson[i]["roll"];
                              if (typeof Dices === 'undefined')
                              {
                                   var Dices = new Array;
                              }
                              if (typeof Dices[rollid] === 'undefined')
                              {
                                   Dices[rollid] = new Array;
                              }
                              Dices[rollid][diceNumber] = roll;
                         }
                    }
                    //add the roll to the table
                    var Rolls = new Array;
                    Rolls[0] = Roll;
                    addToGameTable ( Rolls , Dices );
               }
               else if ( type == "ERROR")
               {
                    console.log ("File: dice-rolls.js");
                    console.log ("Error 118 - " + message);
                    alert ( message );
               }
               loopCounter = 0;
     }
}
