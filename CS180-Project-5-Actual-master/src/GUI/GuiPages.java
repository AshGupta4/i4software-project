package GUI;

import Client.PacketHandler;
import Course.Submission;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * GuiPages - Displays all pages in the program flow
 * Each show function shows a different page in the flow
 * Sends information to the 
 *
 * @author Gabriel Iskandar, Benjamin Winther
 * @version 12/1/21
 */
public class GuiPages {
    GuiWindow guiWindow;
    PacketHandler packetHandler;
    JButton quitButton = new JButton("Quit");
    private String accountUsername;
    private String accountPassword;
    private boolean accountRole;
    private ArrayList<String> courses;
    private String curCourse;
    private ArrayList<String> quizzes;
    private String curQuiz;
    private ArrayList<ArrayList<String>> questions;
    private String curQuestion;
    private ArrayList<String> curSubmissions;
    private String curSubmission;

    public GuiPages(GuiWindow guiWindow) {
        this.guiWindow = guiWindow;
        this.packetHandler = guiWindow.packetHandler;
        quitButton.addActionListener(e -> showExitPage());
    }

    /***********
     * WELCOME *
     ***********/

    public void showWelcomePage() {
        guiWindow.title.setText("Welcome to Better Than Brightspace!");
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JLabel prompt = new JLabel("Select whether you would like to log in or sign up:", JLabel.CENTER);
        panel.add(prompt, BorderLayout.NORTH);
        guiWindow.addButton(centerPanel, "Sign Up").addActionListener(e -> showSignUpPage());
        guiWindow.addButton(centerPanel, "Log In").addActionListener(e -> showLogInPage());
        bottomPanel.add(quitButton);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(150);
        guiWindow.changePanel(panel);
    }

    public void showExitPage() {
        guiWindow.title.setText("Thanks for using Better Than Brightspace!");
        JPanel panel = new JPanel();
        guiWindow.addButton(panel, "Quit").addActionListener(e -> guiWindow.frame.dispose());
        guiWindow.changePanel(panel);
    }

    public void showSignUpPage() {
        guiWindow.title.setText("Sign Up");
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JPanel centerPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel botCenterPanel = new JPanel();
        JPanel botBottomPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JLabel prompt = new JLabel("Enter a Username and Password:", JLabel.CENTER);
        JTextField usernameTextField = guiWindow.addTextField(centerPanel, "Username:");
        JTextField passwordTextField = guiWindow.addTextField(centerPanel, "Password:");
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton teacherButton = guiWindow.addRadioButton(centerPanel, buttonGroup, "Teacher", JRadioButton.TRAILING);
        JRadioButton studentButton = guiWindow.addRadioButton(centerPanel, buttonGroup, "Student", JRadioButton.TRAILING);
        studentButton.setSelected(true);
        guiWindow.addButton(botCenterPanel, "Sign Up").addActionListener(e -> {
            accountUsername = usernameTextField.getText();
            accountPassword = passwordTextField.getText();
            accountRole = teacherButton.isSelected();

            String result;
            if (accountUsername.contains(" ")) {
                result = "Username cannot contain spaces";

            } else if (accountUsername.isEmpty()) {
                result = "Username cannot be empty";
            } else {
                result = packetHandler.createAccount(accountUsername, accountPassword, accountRole);
            }

            if (result.equals("success")) {
                if (accountRole) {
                    showCourseAccountTeacherPage();
                } else {
                    showCourseAccountStudentPage();
                }
            } else {
                guiWindow.addErrorMessage(result);
            }
        });
        botBottomPanel.add(quitButton);
        bottomPanel.add(botCenterPanel, BorderLayout.CENTER);
        bottomPanel.add(botBottomPanel, BorderLayout.SOUTH);
        panel.add(prompt, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.addEmptySpace(panel, BorderLayout.EAST, 95, panel.getHeight());
        guiWindow.addEmptySpace(panel, BorderLayout.WEST, 0, panel.getHeight());
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(35);
        guiWindow.changePanel(panel);
    }

    public void showLogInPage() {
        guiWindow.title.setText("Log In");
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JPanel centerPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel botCenterPanel = new JPanel();
        JPanel botBottomPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JLabel prompt = new JLabel("Enter a Username and Password:", JLabel.CENTER);
        JTextField usernameTextField = guiWindow.addTextField(centerPanel, "Username:");
        JTextField passwordTextField = guiWindow.addTextField(centerPanel, "Password:");
        guiWindow.addButton(botCenterPanel, "Log In", BorderLayout.SOUTH).addActionListener(e -> {
            accountUsername = usernameTextField.getText();
            accountPassword = passwordTextField.getText();
            String result = packetHandler.findAccount(accountUsername, accountPassword);
            if (result.equals("successTeacher")) {
                accountRole = true;
                showCourseAccountTeacherPage();
            } else if (result.equals("successStudent")) {
                accountRole = false;
                showCourseAccountStudentPage();
            } else {
                guiWindow.addErrorMessage(result);
            }
        });
        botBottomPanel.add(quitButton);
        bottomPanel.add(botCenterPanel, BorderLayout.CENTER);
        bottomPanel.add(botBottomPanel, BorderLayout.SOUTH);
        panel.add(prompt, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.addEmptySpace(panel, BorderLayout.EAST, 95, panel.getHeight());
        guiWindow.addEmptySpace(panel, BorderLayout.WEST, 0, panel.getHeight());
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(75);
        guiWindow.changePanel(panel);
    }

    /************************
     * COURSES AND ACCOUNTS *
     ************************/

    public void showCourseAccountTeacherPage() {
        guiWindow.title.setText("Courses and Account");
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel botRightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel botLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel prompt = new JLabel("Select whether you would like to edit courses or edit accounts:",
                JLabel.CENTER);
        panel.add(prompt, BorderLayout.NORTH);
        guiWindow.addButton(centerPanel, "Edit courses").addActionListener(e -> showCoursesTeacherPage());
        guiWindow.addButton(centerPanel, "Edit account").addActionListener(e -> showAccountPage());
        guiWindow.addButton(botLeftPanel, "Log out").addActionListener(e -> showWelcomePage());
        botRightPanel.add(quitButton);
        bottomPanel.add(botRightPanel, BorderLayout.EAST);
        bottomPanel.add(botLeftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(150);
        guiWindow.changePanel(panel);
    }

    public void showCourseAccountStudentPage() {
        guiWindow.title.setText("Courses and Account");
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel botRightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel botLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel prompt = new JLabel("<html>Select whether you would like to view your courses or grades<br>or edit your account:<html>",
                JLabel.CENTER);
        panel.add(prompt, BorderLayout.NORTH);
        guiWindow.addButton(centerPanel, "Open courses").addActionListener(e -> showCoursesStudentPage());
        guiWindow.addButton(centerPanel, "Edit account").addActionListener(e -> showAccountPage());
        guiWindow.addButton(centerPanel, "View grades").addActionListener(e -> showGradesPage());
        guiWindow.addButton(botLeftPanel, "Log out").addActionListener(e -> showWelcomePage());
        botRightPanel.add(quitButton);
        bottomPanel.add(botRightPanel, BorderLayout.EAST);
        bottomPanel.add(botLeftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(120);
        guiWindow.changePanel(panel);
    }

    public void showCourseCreatePage() {
        guiWindow.title.setText("Create a course");
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel centerCenterPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JPanel centerBottomPanel = new JPanel();
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel botRightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel botLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel prompt = new JLabel("Enter the name of the Course you wish to create:",
                JLabel.CENTER);
        panel.add(prompt, BorderLayout.NORTH);
        JTextField courseNameTextField = guiWindow.addTextField(centerCenterPanel, "Course Name:");
        guiWindow.addButton(centerBottomPanel, "Create Course", BorderLayout.SOUTH).addActionListener(e -> {
            curCourse = courseNameTextField.getText();
            String result = packetHandler.createCourse(curCourse);
            if (result.equals("success")) {
                showQuizzesTeacherPage();
            } else {
                guiWindow.addErrorMessage(result);
            }
        });
        guiWindow.addButton(botLeftPanel, "Go Back").addActionListener(e -> showCoursesTeacherPage());
        botRightPanel.add(quitButton);
        centerPanel.add(centerCenterPanel, BorderLayout.CENTER);
        centerPanel.add(centerBottomPanel, BorderLayout.SOUTH);
        guiWindow.addEmptySpace(centerPanel, BorderLayout.EAST, 80, panel.getHeight());
        guiWindow.addEmptySpace(centerPanel, BorderLayout.WEST, 0, panel.getHeight());
        bottomPanel.add(botRightPanel, BorderLayout.EAST);
        bottomPanel.add(botLeftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(120);
        guiWindow.changePanel(panel);
    }

    public void showCoursesTeacherPage() {
        courses = packetHandler.listCourses();
        guiWindow.title.setText("Edit Courses: ");
        JPanel panel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JLabel prompt = new JLabel("Select a course to open or delete:", JLabel.CENTER);
        panel.add(prompt, BorderLayout.NORTH);

        // TOP of the Center Area
        JPanel centerTopPanel = new JPanel(new BorderLayout());

        if (!courses.get(0).equals("No courses")) {
            JPanel centerTopCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
            JPanel centerTopBottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
            JComboBox comboBox = guiWindow.addComboBox(centerTopCenterPanel, courses);
            guiWindow.addButton(centerTopBottomPanel, "Open course").addActionListener(e -> {
                curCourse = comboBox.getSelectedItem().toString();
                showQuizzesTeacherPage();
            });
            guiWindow.addButton(centerTopBottomPanel, "Delete course").addActionListener(e -> {
                String result = packetHandler.deleteCourse(comboBox.getSelectedItem().toString());
                if (result.equals("success")) {
                    showCourseAccountTeacherPage();
                } else {
                    System.out.println(result);
                }
            });

            centerTopPanel.add(centerTopCenterPanel, BorderLayout.CENTER);
            centerTopPanel.add(centerTopBottomPanel, BorderLayout.SOUTH);
        } else {
            JPanel centerTopCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 70, 10));
            JComboBox comboBox = guiWindow.addComboBox(centerTopCenterPanel, courses);
            centerTopPanel.add(centerTopCenterPanel, BorderLayout.CENTER);
        }

        // BOTTOM of the center Area
        JPanel centerBotPanel = new JPanel(new BorderLayout());
        JPanel centerBotCenterPanel = new JPanel();
        JLabel centerBotPrompt = new JLabel("Or create a new course:", JLabel.CENTER);
        centerBotPanel.add(centerBotPrompt, BorderLayout.NORTH);
        guiWindow.addButton(centerBotCenterPanel, "Create new course").addActionListener(e -> showCourseCreatePage());
        centerBotPanel.add(centerBotCenterPanel, BorderLayout.CENTER);

        // Quit and go back buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        guiWindow.addButton(bottomLeftPanel, "Go Back").addActionListener(e -> showCourseAccountTeacherPage());
        bottomRightPanel.add(quitButton);
        bottomPanel.add(bottomRightPanel, BorderLayout.EAST);
        bottomPanel.add(bottomLeftPanel, BorderLayout.WEST);

        centerPanel.add(centerTopPanel);
        centerPanel.add(centerBotPanel);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(75);
        guiWindow.changePanel(panel);
    }

    public void showCoursesStudentPage() {
        courses = packetHandler.listCourses();
        guiWindow.title.setText("Open Course");
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JPanel centerPanel = new JPanel(new BorderLayout(10, 5));
        JPanel centerCenterPanel = new JPanel();
        JPanel centerBottomPanel = new JPanel();
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel botRightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel botLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

        JLabel prompt = new JLabel("Which course would you like to access:", JLabel.CENTER);
        panel.add(prompt, BorderLayout.NORTH);
        JComboBox comboBox = guiWindow.addComboBox(centerCenterPanel, courses);
        if (!courses.get(0).equals("No courses")) {
            guiWindow.addButton(centerBottomPanel, "Open course").addActionListener(e -> {
                curCourse = comboBox.getSelectedItem().toString();
                showQuizzesStudentPage();
            });
        }
        guiWindow.addButton(botLeftPanel, "Go Back").addActionListener(e -> showCourseAccountStudentPage());
        botRightPanel.add(quitButton);
        centerPanel.add(centerCenterPanel, BorderLayout.CENTER);
        centerPanel.add(centerBottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(botRightPanel, BorderLayout.EAST);
        bottomPanel.add(botLeftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.addEmptySpace(panel, BorderLayout.EAST, 5, 0);
        guiWindow.addEmptySpace(panel, BorderLayout.WEST, 5, 0);
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(120);
        guiWindow.changePanel(panel);
    }

    public void showAccountPage() {
        guiWindow.title.setText("Account");
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel botRightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel botLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel prompt = new JLabel("Would you like to delete your account?", JLabel.CENTER);
        panel.add(prompt, BorderLayout.NORTH);
        guiWindow.addButton(centerPanel, "Delete Account").addActionListener(e -> {
            String result = packetHandler.deleteAccount(accountUsername, accountPassword, accountRole);
            if (result.equals("success")) {
                showWelcomePage();
            } else {
                guiWindow.addErrorMessage(result);
            }
        });
        guiWindow.addButton(botLeftPanel, "Go Back").addActionListener(e -> {
            if (accountRole) {
                showCourseAccountTeacherPage();
            } else {
                showCourseAccountStudentPage();
            }
        });
        botRightPanel.add(quitButton);
        bottomPanel.add(botRightPanel, BorderLayout.EAST);
        bottomPanel.add(botLeftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(160);
        guiWindow.changePanel(panel);
    }

    /***********
     * QUIZZES *
     ***********/

    public void showQuizzesTeacherPage() {
        quizzes = packetHandler.listQuizzes(curCourse);
        guiWindow.title.setText("Edit Quizzes");
        JPanel panel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JLabel prompt = new JLabel("Select a quiz to open or delete:", JLabel.CENTER);
        panel.add(prompt, BorderLayout.NORTH);

        // TOP of the Center Area
        JPanel centerTopPanel = new JPanel(new BorderLayout());

        if (!quizzes.get(0).equals("No quizzes")) {
            JPanel centerTopCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
            JPanel centerTopBottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
            JComboBox comboBox = guiWindow.addComboBox(centerTopCenterPanel, quizzes);
            guiWindow.addButton(centerTopBottomPanel, "Open quiz").addActionListener(e -> {
                curQuiz = comboBox.getSelectedItem().toString();
                questions = packetHandler.listQuestions(curCourse, curQuiz);
                showQuestionsTeacherPage();
            });
            guiWindow.addButton(centerTopBottomPanel, "Delete quiz").addActionListener(e -> {
                curQuiz = comboBox.getSelectedItem().toString();
                String result = packetHandler.deleteQuiz(curCourse, curQuiz);
                if (result.equals("success")) {
                    showQuizzesTeacherPage();
                } else {
                    System.out.println(result);
                }
            });

            centerTopPanel.add(centerTopCenterPanel, BorderLayout.CENTER);
            centerTopPanel.add(centerTopBottomPanel, BorderLayout.SOUTH);
        } else {
            JPanel centerTopCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 70, 10));
            JComboBox comboBox = guiWindow.addComboBox(centerTopCenterPanel, quizzes);
            centerTopPanel.add(centerTopCenterPanel, BorderLayout.CENTER);
        }

        // BOTTOM of the center Area
        JPanel centerBotPanel = new JPanel(new BorderLayout());
        JPanel centerBotCenterPanel = new JPanel();
        JLabel centerBotPrompt = new JLabel("Or create a new quiz:", JLabel.CENTER);
        centerBotPanel.add(centerBotPrompt, BorderLayout.NORTH);
        guiWindow.addButton(centerBotCenterPanel, "Create new quiz").addActionListener(e -> showQuizCreatePage());
        centerBotPanel.add(centerBotCenterPanel, BorderLayout.CENTER);

        // Quit and go back buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        guiWindow.addButton(bottomLeftPanel, "Go back").addActionListener(e -> showCoursesTeacherPage());
        bottomRightPanel.add(quitButton);
        bottomPanel.add(bottomRightPanel, BorderLayout.EAST);
        bottomPanel.add(bottomLeftPanel, BorderLayout.WEST);

        centerPanel.add(centerTopPanel);
        centerPanel.add(centerBotPanel);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(75);
        guiWindow.changePanel(panel);
    }

    public void showQuizzesStudentPage() {
        quizzes = packetHandler.listQuizzes(curCourse);
        guiWindow.title.setText("Quizzes");
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JPanel centerPanel = new JPanel(new BorderLayout(10, 5));
        JPanel centerCenterPanel = new JPanel();
        JPanel centerBottomPanel = new JPanel();
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel botRightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel botLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel prompt = new JLabel("Which quiz would you like to access:", JLabel.CENTER);
        panel.add(prompt, BorderLayout.NORTH);

        JComboBox comboBox = guiWindow.addComboBox(centerCenterPanel, quizzes);
        if (!quizzes.get(0).equals("No quizzes")) {
            guiWindow.addButton(centerBottomPanel, "Take quiz").addActionListener(e -> {
                curQuiz = comboBox.getSelectedItem().toString();
                questions = packetHandler.listQuestions(curCourse, curQuiz);
                showQuestionsStudentPage();
            });
        }
        guiWindow.addButton(botLeftPanel, "Go Back").addActionListener(e -> showCoursesStudentPage());
        botRightPanel.add(quitButton);
        centerPanel.add(centerCenterPanel, BorderLayout.CENTER);
        centerPanel.add(centerBottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(botRightPanel, BorderLayout.EAST);
        bottomPanel.add(botLeftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.addEmptySpace(panel, BorderLayout.EAST, 5, 0);
        guiWindow.addEmptySpace(panel, BorderLayout.WEST, 5, 0);
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(120);
        guiWindow.changePanel(panel);
    }

    public void showQuizCreatePage() {
        guiWindow.title.setText("Create a quiz");
        JPanel panel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JLabel prompt = new JLabel("Enter the name of the quiz you wish to create:", JLabel.CENTER);
        JLabel centerBotPrompt = new JLabel("Or enter the filepath of a quiz you wish to import:", JLabel.CENTER);
        panel.add(prompt, BorderLayout.NORTH);

        // Top
        JPanel centerTopPanel = new JPanel(new BorderLayout(10, 10));
        JPanel centerTopCenterPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JPanel centerTopBottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JTextField quizNameTextField = guiWindow.addTextField(centerTopCenterPanel, "Quiz Name:");
        guiWindow.addButton(centerTopBottomPanel, "Create quiz").addActionListener(e -> {
            curQuiz = quizNameTextField.getText();
            String result = packetHandler.createQuiz(curCourse, curQuiz);
            if (result.equals("success")) {
                curQuestion = "";
                showQuestionCreatePage(1);
            } else {
                guiWindow.addErrorMessage(result);
            }
        });
        centerTopPanel.add(Box.createRigidArea(centerBotPrompt.getMaximumSize()), BorderLayout.NORTH);
        centerTopPanel.add(centerTopCenterPanel, BorderLayout.CENTER);
        centerTopPanel.add(centerTopBottomPanel, BorderLayout.SOUTH);
        guiWindow.addEmptySpace(centerTopPanel, BorderLayout.EAST, 75, centerTopPanel.getHeight());
        guiWindow.addEmptySpace(centerTopPanel, BorderLayout.WEST, 0, centerTopPanel.getHeight());

        // Bottom
        JPanel centerBotPanel = new JPanel(new BorderLayout(10, 10));
        JPanel centerBotCenterPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JPanel centerBotBottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JTextField quizFileTextField = guiWindow.addTextField(centerBotCenterPanel, "Quiz filepath:");
        guiWindow.addButton(centerBotBottomPanel, "Import quiz").addActionListener(e -> {
            String result = packetHandler.importQuiz(curCourse, quizFileTextField.getText());
            if (result.equals("success")) {
                showQuizzesTeacherPage();
            } else {
                guiWindow.addErrorMessage(result);
            }
        });
        centerBotPanel.add(centerBotPrompt, BorderLayout.NORTH);
        centerBotPanel.add(centerBotCenterPanel, BorderLayout.CENTER);
        centerBotPanel.add(centerBotBottomPanel, BorderLayout.SOUTH);
        guiWindow.addEmptySpace(centerBotPanel, BorderLayout.EAST, 75, centerBotPanel.getHeight());
        guiWindow.addEmptySpace(centerBotPanel, BorderLayout.WEST, 0, centerBotPanel.getHeight());

        // Quit and go back buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        guiWindow.addButton(bottomLeftPanel, "Go Back").addActionListener(e -> showCourseAccountTeacherPage());
        bottomRightPanel.add(quitButton);
        bottomPanel.add(bottomRightPanel, BorderLayout.EAST);
        bottomPanel.add(bottomLeftPanel, BorderLayout.WEST);

        centerPanel.add(centerTopPanel);
        centerPanel.add(centerBotPanel);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        //guiWindow.addEmptySpace(panel, BorderLayout.EAST, 100, 10);
        //guiWindow.addEmptySpace(panel, BorderLayout.WEST, 0, 10);
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(0);
        guiWindow.changePanel(panel);
    }

    /*************
     * QUESTIONS *
     *************/

    public void showQuestionsTeacherPage() {
        guiWindow.title.setText("Edit Questions");
        JPanel panel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JLabel prompt = new JLabel("Select a question to edit or remove from this quiz:", JLabel.CENTER);
        panel.add(prompt, BorderLayout.NORTH);

        // These are the current Questions
        questions = packetHandler.listQuestions(curCourse, curQuiz);
        ArrayList<String> questionHeads = new ArrayList<>();
        if (questions != null) {
            for (ArrayList<String> question : questions) {
                questionHeads.add(question.get(0));
            }
        }
        // TOP of the Center Area
        JPanel centerTopPanel = new JPanel(new BorderLayout());

        if (!(questions == null)) {
            JPanel centerTopCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
            JPanel centerTopBottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            JComboBox comboBox = guiWindow.addComboBox(centerTopCenterPanel, questionHeads);
            guiWindow.addButton(centerTopBottomPanel, "Edit question").addActionListener(e -> {
                curQuestion = comboBox.getSelectedItem().toString();
                showQuestionCreatePage(1);
            });
            guiWindow.addButton(centerTopBottomPanel, "Remove question").addActionListener(e -> {
                String result = packetHandler.deleteQuestion(curCourse, curQuiz, comboBox.getSelectedItem().toString());
                if (result.equals("success")) {
                    showQuestionsTeacherPage();
                } else {
                    System.out.println(result);
                }
            });

            centerTopPanel.add(centerTopCenterPanel, BorderLayout.CENTER);
            centerTopPanel.add(centerTopBottomPanel, BorderLayout.SOUTH);
        } else {
            JPanel centerTopCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 70, 10));
            questionHeads = new ArrayList<>();
            questionHeads.add("No questions");
            JComboBox comboBox = guiWindow.addComboBox(centerTopCenterPanel, questionHeads);
            centerTopPanel.add(centerTopCenterPanel, BorderLayout.CENTER);
        }

        // BOTTOM of the center Area
        JPanel centerBotPanel = new JPanel(new BorderLayout());
        JPanel centerBotCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JPanel centerBotBottomPanel = new JPanel();
        JLabel centerBotPrompt = new JLabel("<html>Or add a new question, randomize the current questions, "
                + "or grade<br>student submissions of this quiz<html>", JLabel.CENTER);
        centerBotPanel.add(centerBotPrompt, BorderLayout.NORTH);
        guiWindow.addButton(centerBotCenterPanel, "Add new question").addActionListener(e -> {
            curQuestion = "";
            showQuestionCreatePage(1);
        });
        guiWindow.addButton(centerBotCenterPanel, "Randomize questions").addActionListener(e -> showQuestionRandomizePage());
        guiWindow.addButton(centerBotBottomPanel, "Grade submissions").addActionListener(e -> showSubmissionsStudentPage());
        centerBotPanel.add(centerBotCenterPanel, BorderLayout.CENTER);
        centerBotPanel.add(centerBotBottomPanel, BorderLayout.SOUTH);

        // Quit and go back buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        guiWindow.addButton(bottomLeftPanel, "Go Back").addActionListener(e -> showQuizzesTeacherPage());
        bottomRightPanel.add(quitButton);
        bottomPanel.add(bottomRightPanel, BorderLayout.EAST);
        bottomPanel.add(bottomLeftPanel, BorderLayout.WEST);

        centerPanel.add(centerTopPanel);
        centerPanel.add(centerBotPanel);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(20);
        guiWindow.changePanel(panel);
    }

    public void showQuestionsStudentPage() {
        guiWindow.title.setText("Quiz");
        JPanel panel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel centerTopPanel = new JPanel(new GridLayout(0, 3));
        JPanel centerBotPanel = new JPanel();
        JLabel prompt = new JLabel("Select the correct response:");

        int len = questions.size();
        ArrayList<ArrayList<JRadioButton>> answerGroups = new ArrayList<ArrayList<JRadioButton>>(len);
        for (int i = 0; i < len; i++) {
            answerGroups.add(new ArrayList<JRadioButton>());
        }
        if (packetHandler.getQuizRandomize(curCourse, curQuiz).equals("random")) {

            // generate new order
            ArrayList<Integer> questionOrder = new ArrayList<>();
            ArrayList<Integer> questionShuffle = new ArrayList<>();

            for (int i = 0; i < len; i++) {
                questionOrder.add(i);
            }

            for (int i = len - 1; i >= 0; i--) {
                int idx = (int) (Math.random() * (i + 1));
                questionShuffle.add(questionOrder.get(idx));
                questionOrder.remove(idx);
            }

            for (int i : questionShuffle) {
                guiWindow.showMCQuestion(centerTopPanel, i, questions, answerGroups);
            }
        } else {
            for (int i = 0; i < questions.size(); i++) {
                guiWindow.showMCQuestion(centerTopPanel, i, questions, answerGroups);
            }
        }
        guiWindow.addButton(centerBotPanel, "Submit Quiz").addActionListener(e -> {
            ArrayList<String> answers = new ArrayList<>(answerGroups.size());
            // System.out.println(answerGroups.size());
            for (int i = 0; i < answerGroups.size(); i++) {
                answers.add(" ");
            }
            for (int i = 0; i < answerGroups.size(); i++) {
                ArrayList<JRadioButton> buttons = answerGroups.get(i);
                for (int j = 0; j < buttons.size(); j++) {
                    if (buttons.get(j).isSelected()) {
                        answers.set(i, String.valueOf((char) (j + 97)));
                    }
                }
            }
            // System.out.println(answers.toString());

            String result = packetHandler.submitQuiz(curCourse, curQuiz, answers, accountUsername);
            if (result.equals("success")) {
                showQuizzesStudentPage();
            } else {
                guiWindow.addErrorMessage(result);
            }
        });
        centerPanel.add(prompt, BorderLayout.NORTH);
        centerPanel.add(centerTopPanel, BorderLayout.CENTER);
        centerPanel.add(centerBotPanel, BorderLayout.SOUTH);

        // Quit and go back buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        guiWindow.addButton(bottomLeftPanel, "Go Back").addActionListener(e -> showQuizzesStudentPage());
        bottomRightPanel.add(quitButton);
        bottomPanel.add(bottomRightPanel, BorderLayout.EAST);
        bottomPanel.add(bottomLeftPanel, BorderLayout.WEST);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(0);
        guiWindow.changePanel(panel);
    }

    public void showQuestionCreatePage(int numAnswers) {
        guiWindow.title.setText("Create a question");
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel centerTopPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JPanel centerBotPanel = new JPanel();
        JLabel prompt = new JLabel("Enter a Question Header and its Answers:", JLabel.CENTER);
        JTextField questionHeadTextField = guiWindow.addTextField(centerTopPanel, "Question:");
        int bottomSpace = 110;
        ArrayList<JTextField> answerTextFields = new ArrayList<>();
        // Adding num questions depending on how many wanted
        answerTextFields.add(guiWindow.addTextField(centerTopPanel, "Answer:"));
        for (int i = 1; i < numAnswers; i++) {
            answerTextFields.add(guiWindow.addTextField(centerTopPanel, "Answer:"));
            bottomSpace -= (110 / 3);
        }
        if (numAnswers < 4) {
            guiWindow.addButton(centerBotPanel, "Add answer").addActionListener(e -> showQuestionCreatePage(numAnswers + 1));
        }
        if (numAnswers > 1) {
            guiWindow.addButton(centerBotPanel, "Remove answer").addActionListener(e -> showQuestionCreatePage(numAnswers - 1));
        }
        guiWindow.addButton(centerBotPanel, "Create question").addActionListener(e -> {
            ArrayList<String> answers = new ArrayList<>();
            for (JTextField answer : answerTextFields) {
                answers.add(answer.getText());
            }
            String result;
            if (curQuestion.equals("")) {
                result = packetHandler.createQuestion(curCourse, curQuiz, questionHeadTextField.getText(), answers);
            } else {
                result = packetHandler.modifyQuestion(curCourse, curQuiz, curQuestion, questionHeadTextField.getText(), answers);
            }

            if (result.equals("success")) {
                showQuestionsTeacherPage();
            } else {
                guiWindow.addErrorMessage(result);
            }

        });
        centerPanel.add(prompt, BorderLayout.NORTH);
        centerPanel.add(centerTopPanel, BorderLayout.CENTER);
        centerPanel.add(centerBotPanel, BorderLayout.SOUTH);
        guiWindow.addEmptySpace(centerPanel, BorderLayout.EAST, 90, centerPanel.getHeight());

        // Quit and go back buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        guiWindow.addButton(bottomLeftPanel, "Go Back").addActionListener(e -> showQuestionsTeacherPage());
        bottomRightPanel.add(quitButton);
        bottomPanel.add(bottomRightPanel, BorderLayout.EAST);
        bottomPanel.add(bottomLeftPanel, BorderLayout.WEST);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(bottomSpace);
        guiWindow.changePanel(panel);
    }

    public void showQuestionRandomizePage() {
        guiWindow.title.setText("Toggle randomization");
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel centerTopPanel = new JPanel();
        JPanel centerBotPanel = new JPanel();
        JLabel prompt = new JLabel("Enter a Question Header and its Answers:", JLabel.CENTER);
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton randomizedButton = guiWindow.addRadioButton(centerTopPanel, buttonGroup, "Randomized", JRadioButton.CENTER);
        JRadioButton inOrderButton = guiWindow.addRadioButton(centerTopPanel, buttonGroup, "In Order", JRadioButton.CENTER);
        String randomizeString = packetHandler.getQuizRandomize(curCourse, curQuiz);
        if (randomizeString.equals("random")) {
            randomizedButton.setSelected(true);
        } else if (randomizeString.equals("in order")) {
            inOrderButton.setSelected(true);
        }
        guiWindow.addButton(centerBotPanel, "Confirm").addActionListener(e -> {
            boolean isRandom = randomizedButton.isSelected();
            String result = packetHandler.setQuizRandomize(curCourse, curQuiz, isRandom);
            if (result.equals("success")) {
                showQuestionsTeacherPage();
            } else {
                guiWindow.addErrorMessage(result);
            }
        });
        centerPanel.add(centerTopPanel, BorderLayout.CENTER);
        centerPanel.add(centerBotPanel, BorderLayout.SOUTH);

        // Quit and go back buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        guiWindow.addButton(bottomLeftPanel, "Go Back").addActionListener(e -> showQuestionsTeacherPage());
        bottomRightPanel.add(quitButton);
        bottomPanel.add(bottomRightPanel, BorderLayout.EAST);
        bottomPanel.add(bottomLeftPanel, BorderLayout.WEST);

        panel.add(prompt, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(150);
        guiWindow.changePanel(panel);
    }

    /***************
     * SUBMISSIONS *
     ***************/

    public void showSubmissionsStudentPage() {
        ArrayList<ArrayList<String>> submissions = packetHandler.getSubmissions(curCourse, curQuiz);
        ArrayList<String> students = new ArrayList();
        if (submissions != null) {
            for (int i = 0; i < submissions.size(); i++) {
                students.add(submissions.get(i).get(0));
            }
        }

        guiWindow.title.setText("Student Submissions");
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JPanel centerPanel = new JPanel(new BorderLayout(10, 5));
        JPanel centerCenterPanel = new JPanel();
        JPanel centerBottomPanel = new JPanel();
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel botRightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel botLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel prompt = new JLabel("Select a student to grade:", JLabel.CENTER);
        panel.add(prompt, BorderLayout.NORTH);

        JComboBox comboBox = guiWindow.addComboBox(centerCenterPanel, students);
        if (submissions != null) {
            guiWindow.addButton(centerBottomPanel, "Grade Student").addActionListener(e -> {
                int selected = comboBox.getSelectedIndex();
                curSubmissions = submissions.get(selected);
                //showGraderPage(students.get(selected));
                showGraderPage(students.get(selected));
            });
        }
        guiWindow.addButton(botLeftPanel, "Go Back").addActionListener(e -> showQuestionsTeacherPage());
        botRightPanel.add(quitButton);
        centerPanel.add(centerCenterPanel, BorderLayout.CENTER);
        centerPanel.add(centerBottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(botRightPanel, BorderLayout.EAST);
        bottomPanel.add(botLeftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.addEmptySpace(panel, BorderLayout.EAST, 5, 0);
        guiWindow.addEmptySpace(panel, BorderLayout.WEST, 5, 0);
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(120);
        guiWindow.changePanel(panel);
    }

    public void showSubmissionsOfStudentPage(String student) {
        guiWindow.title.setText(student + "'s Submissions");
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JPanel centerPanel = new JPanel(new BorderLayout(10, 5));
        JPanel centerCenterPanel = new JPanel();
        JPanel centerBottomPanel = new JPanel();
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel botRightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel botLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel prompt = new JLabel("Which submission would you like to access:", JLabel.CENTER);
        panel.add(prompt, BorderLayout.NORTH);

        JComboBox comboBox = guiWindow.addComboBox(centerCenterPanel, quizzes);
        if (!quizzes.get(0).equals("No Submissions")) {
            guiWindow.addButton(centerBottomPanel, "Grade Submission").addActionListener(e -> {
                curSubmission = comboBox.getSelectedItem().toString();
                questions = packetHandler.listQuestions(curCourse, curQuiz);
                showGraderPage(student);
            });
        }
        guiWindow.addButton(botLeftPanel, "Go Back").addActionListener(e -> showSubmissionsStudentPage());
        botRightPanel.add(quitButton);
        centerPanel.add(centerCenterPanel, BorderLayout.CENTER);
        centerPanel.add(centerBottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(botRightPanel, BorderLayout.EAST);
        bottomPanel.add(botLeftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.addEmptySpace(panel, BorderLayout.EAST, 5, 0);
        guiWindow.addEmptySpace(panel, BorderLayout.WEST, 5, 0);
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(120);
        guiWindow.changePanel(panel);
    }

    public void showGraderPage(String student) {
        guiWindow.title.setText("Submission Grader");
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JPanel centerPanel = new JPanel(new BorderLayout(10, 5));
        JPanel centerCenterPanel = new JPanel();
        JPanel centerBottomPanel = new JPanel();
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel botRightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel botLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel prompt = new JLabel("Grade this Submission: ", JLabel.CENTER);
        panel.add(prompt, BorderLayout.NORTH);

        //Display all questions with a text input box for a grade.
        questions = packetHandler.listQuestions(curCourse, curQuiz);
        System.out.println(questions.toString());
        ArrayList<JTextField> gradeFields = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            guiWindow.showMCQuestionTeacher(centerCenterPanel, i, questions, curSubmissions, gradeFields);
        }
        guiWindow.addButton(centerBottomPanel, "Submit Grade").addActionListener(e -> {
            ArrayList<String> grades = new ArrayList<>();
            for (int i = 0; i < gradeFields.size(); i++) {
                grades.add("0");
            }
            for (int i = 0; i < gradeFields.size(); i++) {
                grades.set(i, gradeFields.get(i).getText());
            }

            String result = packetHandler.submitSubmission(curCourse, curQuiz, grades, student);
            if (result.equals("success")) {
                showQuestionsTeacherPage();
            } else {
                guiWindow.addErrorMessage(result);
            }
        });

        guiWindow.addButton(botLeftPanel, "Go Back").addActionListener(e -> showSubmissionsOfStudentPage(student));
        botRightPanel.add(quitButton);
        centerPanel.add(centerCenterPanel, BorderLayout.CENTER);
        centerPanel.add(centerBottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(botRightPanel, BorderLayout.EAST);
        bottomPanel.add(botLeftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.addEmptySpace(panel, BorderLayout.EAST, 5, 0);
        guiWindow.addEmptySpace(panel, BorderLayout.WEST, 5, 0);
        guiWindow.changeSideSpace(50);
        guiWindow.changeBottomSpace(0);
        guiWindow.changePanel(panel);
    }

    public void showGradesPage() {
        guiWindow.title.setText("Grades");
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createBevelBorder(1));
        JPanel centerPanel = new JPanel(new GridLayout(0, 4, 5, 5));
        JLabel prompt = new JLabel("These are your grades: ", JLabel.CENTER);
        panel.add(prompt, BorderLayout.NORTH);
        ArrayList<ArrayList<String>> grades = packetHandler.getGrades(accountUsername);
        if (grades != null) {
            for (int i = 0; i < grades.size(); i++) {
                ArrayList<String> grade = grades.get(i);
                JPanel curGrade = new JPanel(new BorderLayout());
                curGrade.add(new JLabel(grade.get(0), JLabel.CENTER), BorderLayout.NORTH);
                JPanel curQuestions = new JPanel(new GridLayout(0, 1));
                for (int j = 1; j < grade.size(); j++) {
                    JLabel curQuestion = new JLabel("Question " + j + ": " + grade.get(j), JLabel.CENTER);
                    curQuestions.add(curQuestion);
                }
                curGrade.add(curQuestions, BorderLayout.CENTER);
                centerPanel.add(curGrade);
            }
            panel.add(centerPanel, BorderLayout.CENTER);
        } else {
            JLabel noGrades = new JLabel("You have no grades", JLabel.CENTER);
            panel.add(noGrades, BorderLayout.CENTER);
        }

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel bottomRightPanel = new JPanel();
        JPanel bottomLeftPanel = new JPanel();
        bottomRightPanel.add(quitButton);
        guiWindow.addButton(bottomLeftPanel, "Go Back").addActionListener(e -> showCourseAccountStudentPage());
        bottomPanel.add(bottomRightPanel, BorderLayout.EAST);
        bottomPanel.add(bottomLeftPanel, BorderLayout.WEST);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        guiWindow.changeSideSpace(50);
        if (grades == null) {
            guiWindow.changeBottomSpace(120);
        } else if (grades.size() <= 4) {
            guiWindow.changeBottomSpace(120);
        } else if (grades.size() <= 8) {
            guiWindow.changeBottomSpace(60);
        } else {
            guiWindow.changeBottomSpace(0);
        }
        guiWindow.changePanel(panel);
    }
}

