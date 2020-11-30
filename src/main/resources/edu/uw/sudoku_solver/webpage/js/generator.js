function generate(){
    fetch("api/generate/", {method: 'POST', body:JSON.stringify({difficulty:8,seed:1234})}).then(res => res.json())
    .then(json => {
    	if(json.err) {
    		throw json.err;
    	}
    
    	let solved = json.board;
    	for (var i=0; i<9; i++){
	        for (var j=0; j<9; j++){
                var cursquare = inputboard.rows[i].cells[j];
                cursquare.childNodes[0].value = (solved[i][j] == 0 ? "" : solved[i][j]);
	        }
        }
    }).catch(e => console.log(e));
}