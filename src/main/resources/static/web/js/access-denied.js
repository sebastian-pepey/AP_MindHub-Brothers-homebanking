const sound = document.getElementById('player');

window.onload = playSound();
 
function playSound() {
  document.getElementById("player").play();
}

function stopSound() {
    alert("PARAAAAA!!!")
    document.getElementById("player").pause();
  }