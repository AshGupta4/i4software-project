# Testing
All test cases are run with the `Server/Server.java` file running.

## Test 1: Teacher Sign Up

Steps:
1. Launch application
2. Select `Sign Up` button
3. Enter username via keyboard into `Username: ` text input
4. Enter password via keyboard into `Password: ` text input
5. Select `Teacher` button
6. Select `Sign Up` button

Expected Result: Account created on server and teacher course page loads

Result Status: Passed

## Test 2: Student Sign Up

Steps:
1. Launch application
2. Select `Sign Up` button
3. Enter username via keyboard into `Username: ` text input
4. Enter password via keyboard into `Password: ` text input
5. Select `Student` button
6. Select `Sign Up` button

Expected Result: Account created on server and student course page loads

Result Status: Passed

## Test 3: User Log In

Steps:
1. Use Test 1 or 2 to create an account
2. Launch application
3. Select `Log In` button
4. Enter username via keyboard into `Username: ` text input
5. Enter password via keyboard into `Password: ` text input
6. Select `Log In` button

Expected Result: If the password is valid, verify credentials and course page loads.
Otherwise display invalid password message.

Result Status: Passed

## Test 4: Create a course

Steps:
1. Use Test 3 to log into a teacher account
2. Select `Edit courses` button
3. Select `Create new course` button
4. Enter a course name via keyboard into `Course Name: ` text input
5. Select `Create course` button

Expected Result: Create a new course and quiz page loads

Result Status: Passed

## Test 5: Open a course as a teacher

Steps:
1. Use Test 4 to create a course
2. Use Test 3 to log into a teacher account
3. Select `Edit courses` button
4. Select a course from the `Select a course to open or delete` dropdown
5. Select `Open Course` button

Expected Result: Open course and quiz page loads

Result Status: Passed

## Test 6: Delete a course

Steps:
1. Use Test 4 to create a course
2. Use Test 3 to log into a teacher account
3. Select `Edit courses` button
4. Select a course from the `Select a course to open or delete` dropdown
5. Select `Delete Course` button

Expected Result: Delete course and course page loads

Result Status: Passed

## Test 7: Create a quiz with 3 questions

Steps:
1. Use Test 5 to open a course
2. Select `Create new quiz` button
3. Enter a quiz name via keyboard into `Quiz Name: ` text input
4. Select `Create quiz` button
5. Select `Add answer` button 3 times
6. Enter a question via keyboard into `Question: ` text input
7. Enter answers via keyboard into all `Answer: ` text inputs
8. Select `Create question` button
9. Select `Add new question` button
10. Repeat steps 5 - 8, 2 more times

Expected Result: Create quiz with questions and question page loads with created questions

Result Status: Passed

## Test 8: Open a quiz and edit a question

Steps:
1. Use Test 7 to create a quiz
2. Use Test 5 to open a course
3. Select a quiz from the `Select a quiz to open or delete:` dropdown
4. Select `Open quiz` button
5. Select a quiz from the `Select a question to edit or remove from this quiz:` dropdown
6. Select `Edit question` button
7. Select `Add answer` button up to 3 times
8. Enter a question via keyboard into `Question: ` text input
9. Enter answers via keyboard into all `Answer: ` text inputs
10. Select `Create question` button

Expected Result: Change the selected question and question page loads with questions

Result Status: Passed

## Test 9: Open a quiz and delete a question

Steps:
1. Use Test 7 to create a quiz
2. Use Test 5 to open a course
3. Select a quiz from the `Select a quiz to open or delete:` dropdown
4. Select `Open quiz` button
5. Select a quiz from the `Select a question to edit or remove from this quiz:` dropdown
6. Select `Remove question` button

Expected Result: Remove the selected question and question page loads with questions

Result Status: Passed

## Test 10: Open a quiz and randomize questions

Steps:
1. Use Test 7 to create a quiz
2. Use Test 5 to open a course
3. Select a quiz from the `Select a quiz to open or delete:` dropdown
4. Select `Open quiz` button
5. Select `Randomize questions` button
6. Select `Randomized` button
7. Select `Confirm` button

Expected Result: Toggle random questions and question page loads

Result Status: Passed

## Test 11: Delete a quiz

Steps:
1. Use Test 7 to create a quiz
2. Use Test 5 to open a course
3. Select a quiz from the `Select a quiz to open or delete:` dropdown
4. Select `Delete quiz` button

Expected Result: Delete quiz and quiz page loads

Result Status: Passed

## Test 12: Import a quiz

Steps:
1. Use Test 5 to open a course
2. Select `Create new quiz` button
3. Enter a quiz filepath via keyboard into `Quiz filepath: ` text input
4. Select `Import quiz` button

Expected Result: Import quiz and course page loads

Result Status: Passed

## Test 13: View grades before taking a quiz

Steps:
1. Use Test 3 to log into a student account
2. Select `View Grades` button

Expected Result: Grade page loads with message `You have no grades`

Result Status: Passed

## Test 14: Take a quiz

Steps:
1. Use Test 7 to create a quiz
2. Use Test 3 to log into a student account
3. Select `Open Courses` button
4. Select a course from the `Which course would you like to access:` dropdown
5. Select `Open Course` button
6. Select a quiz from the `Which quiz would you like to access:` dropdown
7. Select `Take quiz` button
8. Select an answer button for each question
9. Select `Submit quiz` button

Expected Result: Quiz submits and quiz page loads

Result Status: Passed

## Test 15: Grade submission

Steps:
1. Use Test 14 to submit a quiz
2. Use Test 5 to open a course
3. Select a quiz from the `Select a quiz to open or delete:` dropdown which a student has taken
4. Select `Open quiz` button
5. Select `Grade submissions` button
6. Select a student from the `Select a student to grade:` dropdown
7. Select `Grade student` button
8. Enter grades via keyboard into all `Score: ` text inputs (a non-numerical value will result in a grade of 0 for that value)
9. Select `Submit grade` button

Expected Result: Grade submits and quiz page loads

Result Status: Passed

## Test 16: View grades

Steps:
1. Use Test 15 to grade a student submission
2. Use Test 3 to log into the same student's account
3. Select `View Grades` button (ungraded questions will show as 0)

Expected Result: Grade submits and quiz page loads

Result Status: Passed

## Test 17: Log out

Steps:
1. Use Test 3 to log into an account
2. Select `Log out` button

Expected Result: Log out of account and welcome page loads

Result Status: Passed

## Test 18: Delete teacher account

Steps:
1. Use Test 3 to log into a teacher account
2. Select `Edit account` button
3. Select `Delete account` button

Expected Result: Delete account and welcome page loads

Result Status: Failed

Related Bugs: Account isn't deleted on server

## Test 19: Delete student account

Steps:
1. Use Test 3 to log into a student account
2. Select `Edit account` button
3. Select `Delete account` button

Expected Result: Delete account and welcome page loads

Result Status: Failed

Related Bugs: Account isn't deleted on server

## Test 20: Course concurrency

Steps:
1. Use Test 3 to log into a teacher account
2. Use Test 3 to log into a student account on a separate client (`Client2.java`)
3. Perform the following steps on the teacher account
4. Use Test 4 to create a course
5. Perform the following steps on the student account
6. Select `Open courses` button
7. View `Which course would you like to access:` dropdown

Expected Result: New course loads on student course page

Result Status: Passed

## Test 21: Quiz concurrency

Steps:
1. Use Test 20 to open 2 accounts and create a course
2. Perform the following steps on the teacher account
3. Use Test 7 to create a quiz
4. Perform the following steps on the student account
5. Select the new course from the `Which course would you like to access:` dropdown
6. Select `Open course` button
7. Select the new quiz from the `Which quiz would you like to access:` dropdown
8. Select `Take quiz` button

Expected Result: New quiz loads on student quiz page

Result Status: Passed

## Test 22: Quit the program

Steps:
1. Select `Quit` button
2. Select `Quit` button

Expected Result: Load quit page then end program

Result Status: Passed
