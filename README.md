# Sudoku Master

## Setup
1) Download files. You can make a folder and clone the project or download and extract the zip. 
2) In cmd, navigate to the folder with the files.
3) Type `gradlew run` for Windows or `./gradlew run` for Unix Systems to start the web server.
4) Visit `localhost:8000` on your web browser. If you have set the `PORT` environment variable, visit `localhost:PORT`. Press `enter` or `ctrl+c` to terminate the server.

## Webpage
- The Master page has links to the Solver and to the Generator
- Each page has a link back to the main page

### Solver
- To use the solver, simply input the puzzle you want to solve and it will show a solution!
- Click the `Clear Board` button to reset the board.
- Your inputted values will be made gray to show you what has been solved.

### Generator
- To use the generator, click generate to see a randomly generated puzzle! 
- You can change the difficulty on a scale from 1 - 10, with 10 being the most difficult. 
- Setting a seed will generate a puzzle based on that seed, letting your friend to try and solve the same puzzle.

## API
The webpage exposes an api that can be used in other http request projects, not nessicarily needing a webpage. All api requests are sent to `page/api/<api_request>/`. If there is a server error, the response will be `{"err": "Error processing request"}`

### Solving API `page/api/solve/`
- A POST Request. It requires a JSON Object with a `board` corresponding to a square integer matrix. ex. `{"board": [[1,2,3,4],[0,0,0,0],[0,0,0,0],[0,0,0,0]]}`
- In the board array, all zeros correspond to an empty cell, and numbers greater than zero are already solved numbers.
- This responds with a JSON Object, once again with a board corresponding to a square integer matrix. ex. `{"board": [[1,2,3,4],[3,4,1,2],[2,1,4,3],[4,3,2,1]]}`

### Generating API `page/api/generate/`
- A POST request. This requires a JSON Object with a `difficulty` as an integer. There is an optional value in the POST request, `seed`, which is an integer for the seed of the board. ex `{"difficulty": 5 <, "seed" : 1234> }` 
- This responds with a JSON Object, with a `board` that has empty squares filled with zero, and non-empty squares are filled with numbers. ex. `{"board": [[1,2,3,4],[0,0,0,0],[0,0,0,0],[0,0,0,0]]}`

