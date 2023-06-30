package GUI;

import Client.PacketHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * GuiWindow - Runs on the EDT and initializes with content BorderLayout
 * Changes panel in CENTER when showPage is called
 * Methods to add different JFrame components
 *
 * @author Gabriel Iskandar, Benjamin Winther
 * @version 12/1/2021
 */
public class GuiWindow extends JComponent implements Runnable {
    JFrame frame;
    Container content;
    Container bottomSpace;
    JLabel title;
    JLabel errorMessage;
    JPanel curPanel;
    Component curEmptyBottomSpace;
    Component curSideSpaceR;
    Component curSideSpaceL;
    PacketHandler packetHandler;

    public GuiWindow(PacketHandler packetHandler) {
        this.packetHandler = packetHandler;
    }

    /**
     * Initializes frame with content in BorderLayout
     * Calls showPage(0) to load first page
     */
    @Override
    public void run() {
        frame = new JFrame("Better Than Brightspace");

        content = frame.getContentPane();
        content.setLayout(new BorderLayout(50, 30));

        frame.setSize(600, 400);
        frame.setResizable(false); // Window size cannot be altered
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        title = new JLabel();
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font(Font.SERIF, Font.BOLD, 25));
        content.add(title, BorderLayout.NORTH);

        bottomSpace = new JPanel(new BorderLayout());

        errorMessage = new JLabel("");
        errorMessage.setHorizontalAlignment(JLabel.CENTER);
        errorMessage.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        errorMessage.setForeground(Color.RED);
        bottomSpace.add(errorMessage, BorderLayout.SOUTH);
        content.add(bottomSpace, BorderLayout.SOUTH);

        GuiPages guiPages = new GuiPages(this);
        guiPages.showWelcomePage();
    }

    /**
     * Creates a button and adds it to a panel
     * Returns button
     *
     * @param panel
     * @param title
     * @return
     */
    public JButton addButton(JPanel panel, String title) {
        JButton button = new JButton(title);
        panel.add(button);
        return button;
    }

    /**
     * Creates a button and adds it to a panel
     * Returns button
     *
     * @param panel
     * @param title
     * @return
     */
    public JButton addButton(JPanel panel, String title, String location) {
        JButton button = new JButton(title);
        panel.add(button, location);
        return button;
    }

    /**
     * Creates a radio button and adds it to a panel and button group
     * Returns button
     *
     * @param panel
     * @param buttonGroup
     * @param title
     * @return
     */
    public JRadioButton addRadioButton(JPanel panel, ButtonGroup buttonGroup, String title, int alignment) {
        JRadioButton radioButton = new JRadioButton(title);
        radioButton.setHorizontalAlignment(alignment);
        panel.add(radioButton);
        buttonGroup.add(radioButton);
        return radioButton;
    }

    /**
     * Creates a text field and adds it to a panel
     * Returns text field
     *
     * @param panel
     * @param labelText
     * @return
     */
    public JTextField addTextField(JPanel panel, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(JLabel.TRAILING);
        JTextField textField = new JTextField(10);
        panel.add(label);
        panel.add(textField);
        return textField;
    }

    public JComboBox addComboBox(JPanel panel, ArrayList<String> options) {
        JComboBox comboBox = new JComboBox();
        for (String option: options) {
            comboBox.addItem(option);
        }
        comboBox.setMaximumRowCount(5);
        panel.add(comboBox);
        return comboBox;
    }

    public void showMCQuestion(JPanel panel, int index, ArrayList<ArrayList<String>> questions,
                               ArrayList<ArrayList<JRadioButton>> answerGroups) {
        JPanel curQuestionPanel = new JPanel(new BorderLayout());
        JPanel curAnswers = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        ButtonGroup buttonGroup = new ButtonGroup();
        ArrayList<String> question = questions.get(index);
        ArrayList<JRadioButton> answerGroup = new ArrayList<JRadioButton>(question.size() - 1);
        for (int i = 0; i < question.size() - 1; i++) {
            answerGroup.add(null);
        }
        for (int j = 1; j < question.size(); j++) {
            JRadioButton radioButton = addRadioButton(curAnswers, buttonGroup, question.get(j), JRadioButton.CENTER);
            answerGroup.set(j - 1, radioButton);
        }
        answerGroups.set(index, answerGroup);
        curQuestionPanel.add(new JLabel(question.get(0)), BorderLayout.NORTH);
        curQuestionPanel.add(curAnswers, BorderLayout.CENTER);
        panel.add(curQuestionPanel);
    }

    public void showMCQuestionTeacher(JPanel panel, int index, ArrayList<ArrayList<String>> questions,
                                      ArrayList<String> submission, ArrayList<JTextField> gradeFields) {
        JPanel curQuestionPanel = new JPanel(new BorderLayout(5,5));
        JPanel curAnswers = new JPanel(new GridLayout(0, 1));
        ArrayList<String> question = questions.get(index);
        for (int j = 1; j < question.size(); j++) {
            JLabel label = new JLabel(question.get(j), JLabel.CENTER);
            curAnswers.add(label, BorderLayout.NORTH);
        }
        curQuestionPanel.add(new JLabel(question.get(0)), BorderLayout.NORTH);
        curQuestionPanel.add(curAnswers, BorderLayout.CENTER);
        panel.add(curQuestionPanel);

        JPanel botPanel = new JPanel(new BorderLayout());
        JLabel subLabel = new JLabel("Student Answer: " + submission.get(index + 1), JLabel.CENTER);
        botPanel.add(subLabel, BorderLayout.CENTER);

        curQuestionPanel.add(botPanel);
        JTextField gradeTextField = addTextField(panel, "Grade: ");
        botPanel.add(gradeTextField, BorderLayout.SOUTH);
        gradeFields.add(gradeTextField);
        panel.add(curQuestionPanel);
    }

    public void addErrorMessage(String message) {
        errorMessage.setText(message);
        changeBottomSpace(curEmptyBottomSpace.getHeight() - 27);
    }

    public void addEmptySpace(Container space, String location,int width,int height) {
        Component emptySpace = Box.createRigidArea(new Dimension(width, height));
        space.add(emptySpace, location);
    }

    /**
     * Updates the CENTER content with a new panel
     *
     * @param panel
     */
    public void changePanel(JPanel panel) {
        if (curPanel != null) {
            content.remove(curPanel);
        }
        content.add(panel, BorderLayout.CENTER);
        errorMessage.setText("");
        content.revalidate();
        content.repaint();
        curPanel = panel;
    }

    /**
     * Updates the SOUTH panel with an empty panel of height "height"
     *
     * @param height
     */
    public void changeBottomSpace(int height) {
        if (curEmptyBottomSpace != null) {
            content.remove(curEmptyBottomSpace);
        }
        Component emptyBottomSpace = Box.createRigidArea(new Dimension(frame.getWidth(), height));
        bottomSpace.add(emptyBottomSpace, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
        curEmptyBottomSpace = emptyBottomSpace;
    }

    /**
     * Updates the EAST and WEST panels with empty panels of width "width"
     *
     * @param width
     */
    public void changeSideSpace(int width) {
        if (curSideSpaceR != null) {
            content.remove(curSideSpaceR);
        }
        if (curSideSpaceL != null) {
            content.remove(curSideSpaceL);
        }
        Component sideSpaceE = Box.createRigidArea(new Dimension(width / 2, frame.getHeight()));
        Component sideSpaceW = Box.createRigidArea(new Dimension(width / 2, frame.getHeight()));
        content.add(sideSpaceE, BorderLayout.EAST);
        content.add(sideSpaceW, BorderLayout.WEST);
        content.revalidate();
        curSideSpaceR = sideSpaceE;
        curSideSpaceL = sideSpaceW;
    }
}
