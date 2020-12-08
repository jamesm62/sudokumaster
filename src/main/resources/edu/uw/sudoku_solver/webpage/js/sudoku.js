var inputboard = document.getElementById("board");
var errorP = document.getElementById("error");

const BACKGROUND_COLOR = "lightgoldenrodyellow"; // can play around with this


//initialize board
function initializeBoard(size) {
	while(inputboard.hasChildNodes()){
		inputboard.removeChild(inputboard.childNodes[0]);
	}
	
	for (var i=0; i< size * size; i++){
	    var nextRow = document.createElement("tr");
	    for (var j=0; j< size * size; j++){
	        var cursquare = document.createElement("td");
	        cursquare.style.backgroundColor = BACKGROUND_COLOR;
	        
	        if(j % size == size - 1) {
            	cursquare.className += "right"
            }

            if(i % size == size - 1) {
            	cursquare.className += " bottom"
            }
	        
	        var numinput = document.createElement("input");
	        numinput.type = "number"; 
	        numinput.className = "numinput";

	        numinput.size = "1"; // visual purposes
	        
	        numinput.min = 1;
	        numinput.max = size * size;	
	        
	        cursquare.appendChild(numinput);
	        nextRow.appendChild(cursquare);
	    }
	    inputboard.appendChild(nextRow);
	}
}

function clearBoard(){
    for (var i=0; i<inputboard.rows.length; i++){
        for (var j=0; j<inputboard.rows[i].cells.length; j++){
            inputboard.rows[i].cells[j].style.backgroundColor = BACKGROUND_COLOR;
            inputboard.rows[i].cells[j].childNodes[0].value = "";
            inputboard.rows[i].cells[j].childNodes[0].readOnly = false;
        }
    }
}

function resizeBoard(){
	initializeBoard(document.getElementById("boardsize").value);
}

initializeBoard(3);
