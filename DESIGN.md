# Design
## Solving and Generating Algorithm (Java)
Both the solving and generating algorithms use a class called SudokuBoard to represent sudoku puzzles. The solve() method takes in a SudokuBoard as a parameter and passes it to a private recursive helper method that returns a solution. The recursive helper method keeps track of row and column indexes as it traverses the board data and inputs possible values into empty spaces (trying values starting from 1 and ending at the side length of the board). Every inputted number is checked against a set of other existing numbers in the same row, column, and square to ensure that the solution follows the rules of Sudoku. When it finds the solution that fills out every empty space in the sudoku board, it returns that solution. The generating algorithm starts with an empty SudokuBoard and initially uses a modified version of the solving algorithm to generate a randomly-solved sudoku board. The modified version inputs randomly-chosen possible numbers from the parameter numGenerator (of type Random) into spaces instead of following numerical order. The next step of the generating algorithm removes numbers from the randomly-solved sudoku board based on whether each difficultyAssessor.nextDouble() value (from the other Random parameter) is less than the normalized difficulty parameter provided in the method call. Finally, it returns the partially-solved SudokuBoard, posing a challenge to the user to fill out the unfilled boxes.

## Server and Api Structure
### `Main`
- Entry point for the program.
- No command line arguments.
- Creates a new `Server`, starts it, waits for input on `System.in`, then closes the server and terminates the program.
- Has several `System.out.println` calls to let the user know the status of the server.

### `Server`
- Logical abstraction of the server to leave `Main.java` clean.
- Has one static field, `PORT`, which is the port the server runs on. This can be set from environment variables for custom setups and server deployment.
- Constructor does nothing.
- One private instance field, `HttpServer`, which is the actual server that is run.
- Method `start`:
  - Checks if `server` is null, and if not throws an error on starting the server twice.
  - Initializes `server` from `HttpServer.create`
  - Adds 3 contexts: `/`, `/solve`, and `/generate` as the three available pages to navigate to
  - Adds 2 static file paths: `/js/` and `/css/` for the static file delivery
  - Adds 2 api calls: `/api/solve/` and `/api/generate` using a `ServerApiHandler` (Custom class)
    - `/api/solve` first checks if there is a board in the request, and if not, throws `No board in request`, which is sent as an error. It then creates a new board, and then uses `SudokuSolver.solve` to solve the board. If the returned board is null, the method throws `Unable to solve board`, which is sent as an error. It then converts the solved board into a Json string, and returns a Json string of the board to the converted board.
    - `/api/generate` first determines if there is a seed in the request, using it or picking a random number as the seed. It then confirms that the request has both a `difficulty` and a `size`, throwing errors if those Json values are not present. It then recives a generated board from `SudokuGenerator.getRandomPuzzle`, passing in the values from the request. If the generated board is null, an error is thrown. Finally, it converts the generated board to Json and returns a string of the board to the converted board.
  - It then starts the `server`
  - If there are any initialization errors, not errors in the `HttpContext`s the program exits and prints the stack trace.
- Method `end`:
  - Allows the server to finish processing requests for one second, then stops it.
- Static method `sendWebpageFile`:
  - Param `httpExchange`: the `HttpExchange` between the server and the client
  - Param `fileName`: the file name for the html file from the path `/edu/uw/sudoku_solver/webpage/`
  - Param `contentType`: The type of content being sent, ie html, css, js
  - Sets the content type for the response header
  - Creates an `IndexStream` to the html file, which allows the resources to be build into the jar for easy relocation and running
  - Reads the file, sends a 200 status code, and writes the file to the response body
  - Closes the `httpExchange`
  
### `ServerApiHandler`
  - Implements `HttpHandler` for easy inlining to `HttpServer#createContext`
  - Abstract class
  - Abstracts out some of the code used in both api handlers
  - Method `handle`:
    - Param `httpExchange`: the `HttpExchange` between the server and the client
    - Parses the body of the request into Json
    - Gets the return value from `getResponse`
    - Responds to the client with the response data
    - If any errors are thrown, they are caught by a catch block, and are sent to the client incase of client error to let them know of the issue
    - Closes the `httpExchange`
  - Method `getResponse`:
    - Param `httpExchange`: the `HttpExchange` between the server and the client
    - Param `response`: the body for the response to the client
    - Sets the response headers and body.
    - Was abstracted to remove nested `try/catch` and redundancies
  - Abstract Method `getResponse`:
    - Param `request`: The Json representation for the request body
    - Throws exception, allowing for any errors to easily be send to the client
    - Should be used to process the data from the request and respond to the client if needed.

## Website design
Written with html, css, and javascript. Nothing too technical about the implementation. Most of the design decisions were based on improving the ease of use and clean asthetic of the website, such as bordering 3x3 squares with thicker lines and placing the board and buttons in intuitive locations. 
