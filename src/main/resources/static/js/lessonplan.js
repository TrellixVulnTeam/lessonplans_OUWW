'use strict';

console.log("external js loaded");

// window.addEventListener("load", function(event){
//     console.log("body onload event seems to be working");
// });

window.addEventListener("load", showMenuIfAllGrammarCheckboxChecked);

// document.getElementById("showSearch").addEventListener('click', function(event){
// console.log("debugging event " + event);
// 		event.preventDefault();
//         console.log("showSearch click working");
//     });

let allGrammar= document.getElementById("allGrammar");
console.log("allGrammar" + allGrammar);

let allGrammarChildren = allGrammar.children;

// for (const ch of allGrammarChildren){
//     let grammarCheckbox = ch.getElementsByTagName('input')[0];
//    if (grammarCheckbox instanceof HTMLInputElement && grammarCheckbox === 'checkbox'){
//         console.log("in in in for");

//         if (grammarCheckbox.checked){

//             console.log("about to call showAllGrammar");
//             showAllGrammar();
//         }
//         console.log("Checked? " + ch.getElementsByTagName('input')[0].checked);
//     } else {
//         console.log("else entered");
//     }
// }

// for (const ch of allGrammarChildren){
//     let grammarCheckbox = ch.getElementsByTagName('input')[0];
//    if (ch.getElementsByTagName('input')[0] instanceof HTMLInputElement && ch.getElementsByTagName('input')[0].type === 'checkbox'){
//         console.log("in in in for");

//         if (ch.getElementsByTagName('input')[0].checked){

//             console.log("about to call showAllGrammar");
//             showAllGrammar();
//         }
//         console.log("Checked? " + ch.getElementsByTagName('input')[0].checked);
//     } else {
//         console.log("else entered");
//     }
// }

function showMenuIfAllGrammarCheckboxChecked(){
    console.log("debugging:  showMenuIfAllGrammarCheckboxChecked called");
    for (const ch of allGrammarChildren){
        let grammarCheckbox = ch.getElementsByTagName('input')[0];
       if (grammarCheckbox instanceof HTMLInputElement && grammarCheckbox.type === 'checkbox'){
            console.log("in in in for");

            if (grammarCheckbox.checked){

                console.log("about to call showAllGrammar");
                showAllGrammar();
            }
            console.log("Checked? " + ch.getElementsByTagName('input')[0].checked);
        }
    }
}







document.getElementById('showSearch').addEventListener('click', (e) => showMenu(e));
document.getElementById('hideSearch').addEventListener('click', function(event){
        hideMenu(event);
});


document.getElementById('showAllGrammar').addEventListener('click', (e) => showAllGrammar(e));
document.getElementById('hideAllGrammar').addEventListener('click', (e) => hideAllGrammar(e));

function showAllGrammar(e){
    console.log("debugging: howAllGrammar called");
    e?.preventDefault();
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