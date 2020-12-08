function solve(){
    var board = [];
    //read inputboard
    for (var i=0; i<inputboard.rows.length; i++){
        board[i] = [];
	     for (var j=0; j<inputboard.rows[0].cells.length; j++){
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
    		errorP.innerHTML = json.err;
    		throw json.err;
    	}
    	errorP.innerHTML = "";
    
    	let solved = json.board;
    	for (var i=0; i<inputboard.rows.length; i++){
	        for (var j=0; j<inputboard.rows[0].cells.length; j++){
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