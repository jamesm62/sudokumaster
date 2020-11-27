var inputboard = document.getElementById("board");
//initialize board
for (var i=0; i<9; i++){
    var nextRow = document.createElement("tr");
    for (var j=0; j<9; j++){
        var cursquare = document.createElement("td");
        var numinput = document.createElement("input");
        numinput.type = "text"; 
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


function solve(){
    var board = [];
    //read inputboard
    for (var i=0; i<9; i++){
        board[i] = [];
        for (var j=0; j<9; j++){
            let inputvalue = parseInt(inputboard.rows[i].cells[j].childNodes[0].value);
            if (isNaN(inputvalue)){
                board[i][j] = 0;
            } else {
                board[i][j] = inputvalue;
            }
        }
    }
    console.log(board);
}
