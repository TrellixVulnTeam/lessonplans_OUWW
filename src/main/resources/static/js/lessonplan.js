'use strict';

console.log("external js loaded");

// window.addEventListener("load", function(event){
//     console.log("body onload event seems to be working");
// });

window.addEventListener("load", addChecked);



function showMenu() {
    //addChecked();
    document.getElementById('mega-menu').style.display = 'block';
    document.getElementById('showSearch').classList.add('hide');
    document.getElementById('hideSearch').classList.remove('hide');
    
}

function hideMenu() {
    document.getElementById('mega-menu').style.display = 'none';
    document.getElementById('hideSearch').classList.add('hide');
    document.getElementById('showSearch').classList.remove('hide');
    
}

function addChecked() {     	
    let checkboxesToCheck = "[[${checkboxesToCheck}]]";
    
    if (checkboxesToCheck){
        checkboxesToCheck = checkboxesToCheck.slice(1, checkboxesToCheck.length -1 );
        checkboxesToCheck = checkboxesToCheck.split(',');	
        checkboxesToCheck.forEach(element => console.log(element));
        
        //loop over and add checkbox          	
            console.log(checkboxesToCheck.length)
            console.log("hello");
            checkboxesToCheck.forEach(element => {	
                element = element.trim();
                if (document.getElementById(element)){	
                    document.getElementById(element).checked = true
                }
            
            });
    }
}