'use strict';

document.getElementById("loginRegister").addEventListener('click', (e) => 
    {
        e.preventDefault();
        document.getElementById("sendPreviousPage").submit();
    }
)
