function fixSizeLocation ( targetDiv )
{
     if ( targetDiv != null)
     {
          //hide all
          document.getElementsByTagName('hgroup')[0].classList.add("hide");
          document.getElementById('dice-roll').classList.add("hide");

          //show the targetDiv
          targetDiv.classList.remove("hide");
          targetDiv.classList.add("show");
     }

}
function doSessionStorage()
{
     if (( sessionStorage.getItem("userName") != null )&&( sessionStorage.getItem("userName") != "" ))
     {
          userName = sessionStorage.getItem("userName");

     }
     else
     {
          sessionStorage.setItem("userName", userName);
     }

     if (( sessionStorage.getItem("userId") != null )&&( sessionStorage.getItem("userId") != "" ))
     {
          userId = sessionStorage.getItem("userId");
     }
     else
     {
          sessionStorage.setItem("userId", userId);
     }
}
