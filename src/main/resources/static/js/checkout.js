'use strict';

//stripe-button-el

document.getElementById("customSubmit").addEventListener('click', function(e){
    e.preventDefault();
    document.querySelector(".stripe-button-el").click();

});