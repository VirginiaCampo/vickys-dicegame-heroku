

function addEventsUser()
{
     var registerLINK = document.getElementById('manage-player--menu__newPlayer');
     var exitLINK = document.getElementById('manage-player--menu__deletePlayer');
     registerLINK.addEventListener( 'click' , showHideForm );
     exitLINK.addEventListener( 'click', deleteUser );

}

function createUser()
{
     userName = document.getElementById('playerName').value;
     if ( userName != "")
     {
          var localURL = window.location.href;
          var method = "POST";
          var url = "/players";
          var body = JSON.stringify({"name": userName});
          //remove eventListener
          var createPlayerBTN = document.getElementById( 'createPlayer' );
          createPlayerBTN.removeEventListener( 'click', createUser );;

          createRequestObj();
          configureRequesObj( method , url , body , "createUserReturn ( jsonObj )");
     }
     else
     {
          document.getElementById('userErrorMessage').textContent = "Name is missing.";
          document.getElementById('userErrorMessage').classList.add ("show-error");
     }


}
function createUserReturn ( JsonReturn )
{

     var type = JsonReturn["Message"]["Type"];
     var message = JsonReturn["Message"]["Message"];
     userId = JsonReturn["playerId"];
     if ( type == "ERROR")
     {
          document.getElementById('userErrorMessage').textContent = message;
          document.getElementById('userErrorMessage').classList.remove ("show-success");
          document.getElementById('userErrorMessage').classList.add ("show-error");

          var createPlayerBTN = document.getElementById( 'createPlayer' );
          createPlayerBTN.addEventListener( 'click', createUser );
     }
     else if ( type == "SUCCESS")
     {
          document.getElementById('userErrorMessage').textContent = "Welcome " + userName;
          document.getElementById('userErrorMessage').classList.add ("show-success");
          document.getElementById('userErrorMessage').classList.remove ("show-error");

          sessionStorage.setItem("userName", userName);
          sessionStorage.setItem("userId", userId);



          var delayInMilliseconds = 500;
          setTimeout(function()
          {
               showHideForm();
               doYouWantToRolldice();
          }, delayInMilliseconds);


     }


}

function modifyUser()
{
     userName = document.getElementById('playerName').value;
     if ( userName != "")
     {
          var localURL = window.location.href;
          var method = "PUT";
          var url = "/players";
          var body = JSON.stringify({"name": userName, "playerId": userId});

          createRequestObj();
          configureRequesObj( method , url , body , "modifyUserReturn ( jsonObj )");
     }
     else
     {
          document.getElementById('userErrorMessage').textContent = "Name is missing.";

          document.getElementById('userErrorMessage').classList.add ("show-error");
     }
}
function modifyUserReturn( JsonReturn )
{
     var type = JsonReturn["Message"]["Type"];
     var message = JsonReturn["Message"]["Message"];


     if ( type == "ERROR")
     {
          document.getElementById('userErrorMessage').textContent = "New name already in use.";
          document.getElementById('userErrorMessage').classList.remove ("show-success");
          document.getElementById('userErrorMessage').classList.add ("show-error");
     }
     else if ( type == "SUCCESS")
     {
          document.getElementById('userErrorMessage').textContent = "Name is changed";
          document.getElementById('userErrorMessage').classList.add ("show-success");
          document.getElementById('userErrorMessage').classList.remove ("show-error");
          sessionStorage.setItem("userName", userName);
          var delayInMilliseconds = 500;
          setTimeout(function()
          {
               showHideForm();
          }, delayInMilliseconds);
     }
}
function deleteUser()
{
     var localURL = window.location.href;
     var method = "DELETE";
     var url = "/players/" + userId;
     var body = "";

     createRequestObj();
     configureRequesObj( method , url , body , "deleteUserReturn ( jsonObj )");

}
function deleteUserReturn( JsonReturn )
{
     var type = JsonReturn["Message"]["Type"];
     var message = JsonReturn["Message"]["Message"];
     if ( type == "ERROR")
     {
          console.log ("File: User.js");
          console.log ("Error 139 - " + message );
          alert (message);
     }
     else if ( type == "SUCCESS")
     {
          sessionStorage.clear();
          userId = "";
          userName = "";
          swapRegisterItem();
          stopRolling ();
          removeGamesFromTable();
     }
}
function showHideButons()
{
     var modifyPlayerBTN = document.getElementById( 'modifyPlayer' );
     var createPlayerBTN = document.getElementById( 'createPlayer' );
     /* Show hide buttons */
     if (userId == "")
     {
          modifyPlayerBTN.style.display = "none";
          createPlayerBTN.style.display = "inline-block";
          createPlayerBTN.addEventListener( 'click', createUser );
     }
     else
     {
          modifyPlayerBTN.style.display = "inline-block";
          createPlayerBTN.style.display = "none";
          modifyPlayerBTN.addEventListener( 'click', modifyUser );
     }
};
function showHideForm()
{
     showHideButons();
     swapRegisterItem();



     var formContainer = document.getElementById('manage-player__form--container');
     /* Show / hide form */
     if (formContainer.classList.contains( "show" ))
     {
          formContainer.classList.remove ("show");
          formContainer.classList.add ("hide");
     }
     else if (formContainer.classList.contains( "hide" ))
     {
          formContainer.classList.remove ("hide");
          formContainer.classList.add ("show");
          document.getElementById('playerName').value = userName;
     }
     document.getElementById('userErrorMessage').textContent = " "

};
function swapRegisterItem()
{
     if ((userId != "")&&(userId !== undefined))
     {
          document.getElementById('manage-player--menu__newPlayer').textContent = "Modify Player";
          document.getElementById("manage-player--menu__deletePlayer").style.display="inline-block";
     }
     else if ((userId == "")||(userId == undefined))
     {
          document.getElementById('manage-player--menu__newPlayer').textContent = "Register";
          document.getElementById("manage-player--menu__deletePlayer").style.display="none";
     }
}
