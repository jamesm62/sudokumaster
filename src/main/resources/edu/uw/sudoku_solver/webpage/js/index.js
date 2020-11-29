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
    fetch("api/solve/", {method: 'POST', body:JSON.stringify({"board":board})}).then(res => res.json())
    .then(json => {
    	if(json.err) {
            alert(json.err);
    		throw json.err;
    	}
    
    	let solved = json.board;
    	console.log(json);
    	for (var i=0; i<9; i++){
	        for (var j=0; j<9; j++){
                var cursquare = inputboard.rows[i].cells[j];
                if (cursquare.childNodes[0].value != 0){
                    cursquare.style.backgroundColor = "gray";
                } else {
                    cursquare.childNodes[0].value = solved[i][j];
                }
                cursquare.childNodes[0].readOnly = true;
	        }
        }
    }).catch(e => console.log(e));
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
