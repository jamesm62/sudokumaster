var inputboard = document.getElementById("board");
const BACKGROUND_COLOR = "lightgoldenrodyellow"; // can play around with this
//initialize board
for (var i=0; i<9; i++){
    var nextRow = document.createElement("tr");
    for (var j=0; j<9; j++){
        var cursquare = document.createElement("td");
        cursquare.style.backgroundColor = BACKGROUND_COLOR;
        var numinput = document.createElement("input");
        numinput.type = "text"; 
        numinput.className = "numinput";
        numinput.size = "1"; // visual purposes
        numinput.maxLength = "1"; // makes it so user can only type 1 character (1 digit number)
        numinput.onkeypress = function(event){
            let code = event.key - '0';
            return code >= 1 && code <= 9;
        } // makes it so only numbers between 1-9 are allowed
        cursquare.appendChild(numinput);
        nextRow.appendChild(cursquare);
    }
    inputboard.appendChild(nextRow);
}

function clearBoard(){
    for (var i=0; i<9; i++){
        for (var j=0; j<9; j++){
            inputboard.rows[i].cells[j].style.backgroundColor = BACKGROUND_COLOR;
            inputboard.rows[i].cells[j].childNodes[0].value = "";
            inputboard.rows[i].cells[j].childNodes[0].readOnly = false;
        }
    }
}
