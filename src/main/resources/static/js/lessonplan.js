'use strict';

console.log("external js loaded");

// window.addEventListener("load", function(event){
//     console.log("body onload event seems to be working");
// });

//window.addEventListener("load", addChecked);

// document.getElementById("showSearch").addEventListener('click', function(event){
// console.log("debugging event " + event);
// 		event.preventDefault();
//         console.log("showSearch click working");
//     });

document.getElementById('showSearch').addEventListener('click', (e) => showMenu(e));
document.getElementById('hideSearch').addEventListener('click', function(event){
        hideMenu(event);
});


document.getElementById('showAllGrammar').addEventListener('click', (e) => showAllGrammar(e));
document.getElementById('hideAllGrammar').addEventListener('click', (e) => hideAllGrammar(e));

function showAllGrammar(e){
    e.preventDefault();
    //document.getElementById('grammarSnapshot').classList.add("hide");
    document.getElementById('allGrammar').classList.remove('hide');
    document.getElementById('showAllGrammar').classList.add('hide');
    document.getElementById('hideAllGrammar').classList.remove('hide');
}

function hideAllGrammar(e){
    e.preventDefault();
    document.getElementById('allGrammar').classList.add("hide");
   // document.getElementById('grammarSnapshot').classList.remove('hide');
    document.getElementById('hideAllGrammar').classList.add('hide');
    document.getElementById('showAllGrammar').classList.remove('hide');
}



function showMenu(e) {
    //addChecked();
    e.preventDefault();
    document.getElementById('mega-menu').style.display = 'block';
    document.getElementById('showSearch').classList.add('hide');
    document.getElementById('hideSearch').classList.remove('hide');
    
}

function hideMenu(e) {
	e.preventDefault();
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