'use strict';

//stripe-button-el

document.getElementById("customSubmit").addEventListener('click', function(e){
    e.preventDefault();
    console.log("I'm here");
    document.querySelector(".stripe-button-el").click();

});