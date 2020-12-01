function generate(){
    let seedInput = document.getElementById("seed");
    let difficultyInput = document.getElementById("difficulty");


    let request = {difficulty:difficultyInput.value != "" ? parseInt(difficultyInput.value):8};
    if(seedInput.value != "") {
        request.seed = parseInt(seedInput.value);
    }

    fetch("api/generate/", {method: 'POST', body:JSON.stringify(request)}).then(res => res.json())
    .then(json => {
    	if(json.err) {
    		throw json.err;
    	}
    
    	let solved = json.board;
    	for (var i=0; i<9; i++){
	        for (var j=0; j<9; j++){
                var cursquare = inputboard.rows[i].cells[j];
                cursquare.childNodes[0].value = (solved[i][j] == 0 ? "" : solved[i][j]);
                if(solved[i][j] != 0) {
                    cursquare.style.backgroundColor = "gray";
                    cursquare.childNodes[0].readOnly = true;
                }
	        }
        }
    }).catch(e => console.log(e));
}