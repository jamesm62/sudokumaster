function generate(){
    let seedInput = document.getElementById("seed");
    let difficultyInput = document.getElementById("difficulty");


    let request = {difficulty:difficultyInput.value != "" ? parseInt(difficultyInput.value):8, size:inputboard.rows.length};
    if(seedInput.value != "") {
        request.seed = parseInt(seedInput.value);
    }

    fetch("api/generate/", {method: 'POST', body:JSON.stringify(request)}).then(res => res.json())
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
                cursquare.childNodes[0].value = (solved[i][j] == 0 ? "" : solved[i][j]);
                cursquare.style.backgroundColor = BACKGROUND_COLOR;
                cursquare.childNodes[0].readOnly = false;
                if(solved[i][j] != 0) {
                    cursquare.style.backgroundColor = "gray";
                    cursquare.childNodes[0].readOnly = true;
                }
	        }
        }
    }).catch(e => console.log(e));
}