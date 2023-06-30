# Project-5-CS180

## Instructions to Compile and Run

1. Use IntelliJ to run the following
2. First, run the Server class located at `/src/Server/Server.java`.
4. Next, run the Client class located at `/src/Client.java`. The client is configured to use `localhost` to connect to the server. This will open the GUI.
5. To test concurrency, you may optionally run the Client2 class located at `/src/Client2.java` while the original Client is running.

## Submissions

1. Repository (Vocareum workspace) - Submitted by Grant Rivera
2. Report - Submitted by Gabriel Iskandar
3. Presentation (video) - Submitted by Kyle Harvey

## Class Descriptions

### Clients (src/)
- Client : Launches the GUI and creates an instance of PacketHandler to establish its own threaded connection with the server
- Client2 : An optional Client duplicate to test concurrency

### Client (src/Client/) - different from above
- PacketHandler : Establishes a connection with a Socket on the server end, and contains all the methods necessary for the client-side GUI to request information from the server. It takes arguments of type String or ArrayList<String>, and returns type String, ArrayList<String>, or ArrayList<ArrayList<String>> depending on the action being performed. It also handles all communication with Strings. It will return either the information requested or an error message for the GUI class to interpret.
  
### Course (src/Course/)
- Course : Basic class to hold information about a course, such as name and quizzes. Gives information to AllCourseViewer and CourseQuizViewer. Load method is called by Client and constructor automatically creates files and folders. Write method runs at the end of the program. 
- AllCourseViewer : In charge of terminal interaction for accessing a course. Prompts user to edit or open a course. Prints a list of courses to choose from. Retrieves data from Course. Calls CourseQuizViewer after opening a course
- CourseQuizViewer : In charge of terminal interaction for accessing the quizzes in a course. Prompts user to edit or open a course. Prints a list of quizzes to choose from. Retrieves data from Course and Quiz. Calls QuizEditor or QuizTaker after opening a quiz
- CourseNotFoundException : Exception thrown if a requested course cannot be found
- DuplicateCourseException : Thrown if created course already exists
- InvalidCourseException : Thrown if course name is invalid
- Submission : Basic class to hold information about a submission, such as student, answers, and time. Gives information to SubmissionViewer. Load and write method is called by client.
- SubmissionViewer : In charge of terminal interaction for viewing submissions. Prompts user to view or grade a submission. Prints submissions to grade and individual answers. Retrieves data from Submission. Called by QuizEditor and AllCourseViewer.
  
### Exceptions (src/Exceptions/)
- CourseNotFoundException : An exception thrown and handled on the backend/server when a course is not found after being searched for
- DuplicateCourseException : An exception thrown and handled on the backend/server when an attempt is made to create a course with the same name as an existing course
- InvalidCourseException : An exception thrown and handled on the backend/server when there is an issue with a course directory
- InvalidFileImportException : An exception thrown and handled on the backend/server when a file to import as a quiz is not formatted properly

### GUI (src/GUI/)
- GuiWindow: A runnable gui class which displays all GUI components. The run method initializes the GUI window. Many functions to add buttons, dropdowns and other GUI components easily. Methods to change the layout of the window with different panels. Methods are called often from the GUIPages class.
- GuiPages: A class to display different pages in on the GUI Window. Each function is used to display a new page in the program flow. Calls methods from GuiWindow to create gui elements for each page. Page functions call each other through buttons for GUI navigation. Pages include accounts, courses, quizzes, questions and grading.

### Profile (src/Profile/)
- Account : Basic class to hold information about an account, such as username, password and teacher/student role. Gives information to AccountCreator and saves data with AccountDatabase
- AccountCreator : In charge of terminal interaction with user for creating an account. Prompts for sign-in or log-in and asks for credentials. Retrieves data from Account and AccountDatabase. Calls AllCourseViewer after account credentials.
- AccountDatabase : Stores all accounts and in charge of reading and writing account data to files. Load method is called by Client and it automatically saves data to files when an Account is modified.

### Quiz (src/Quiz/)
- Quiz : Basic class to hold information about a quiz, such as questions, course and name. Gives information to CourseQuizViewer, QuizEditor, and QuizTaker. Load and write method is called by Client.
- Question : Basic class to hold information about a question such as question header and answers. Stored in Quiz and accessed by QuizEditor and QuizTaker. 
- QuizFileImporter : Allows file imports from a path into a Quiz or Question. Accessed from QuizEditor and QuizTaker.
- InvalidFileImportException : Thrown if the path leads to an invalid file
- QuizDisplay : Framework for printing the quiz to terminal. Takes input from a Quiz and Question.
- QuizTaker : In charge of terminal interaction for taking quizzes. Prints each Question in a Quiz and prompts for an answer. Sends information to Submission
- QuizEditor : In charge of terminal interaction for editing quizzes. Prompts user to add, edit, or remove questions. Displays available questions to edit and delete. Prompts for a question and answers. Sends information to Quiz.

### Server (src/Server/)
- HandleConnection : This class handles all incoming requests from a client via an instance of the PacketHandler. It reads info sent in the form of a String, and parses that String for a request. It will then perform the request and parse other necessary information from the String, such as an Account username. The class is made to support multithreading and concurrency. It also interacts with the other classes related to Quizzes, Accounts, Courses, Submissions, etc. as it is mainly where requests are processed from the backend. Once done, it builds a String to send back to PacketHandler with the request status or requested information.
- Server : Creates a Socket with port 2424 and waits for connections to be established from the client side. Once a connection is received, it will create an instance of HandleConnection and use the HandleConnection.run() method to start a thread. Then it will continue waiting for more connections.
- ServerLog : A utility class to assist with logging server output to a file for debug purposes
  
### UI (src/UI/)
- TerminalInteraction : Old class from project 4, not used
