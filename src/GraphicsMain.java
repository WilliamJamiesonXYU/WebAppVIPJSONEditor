import net.miginfocom.swing.MigLayout;
import org.json.simple.*;
import org.json.simple.parser.*;


import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Stack;

public class GraphicsMain {

    Stack<String> undoStack = new Stack<>();
    JSONObject alertDict;
    JSONObject currAlert;
    JFrame window;
    JPanel panel;
    JPanel textPanel;
    JButton confirmButton = new JButton("Undo");
    JButton nextButton = new JButton("Next Section");
    JButton saveButton = new JButton("Save JSON");
    JTextArea alertText = new JTextArea(25, 125);
    ButtonListener btnListener = new ButtonListener();
    InputListener inputListener = new InputListener();
    String insertString;
    static final String ENDING_TAG = "</a>";
    int caretStartPos;
    int caretEndPos;

    boolean fixOrSum = true;

    int jsonIndex;


    public static void main(String[] args) {
        new GraphicsMain();

    }

    GraphicsMain() {
        initJSON();
        initializeWindow();
    }

    private void initJSON() {
        try {
            Object obj = new JSONParser().parse(new FileReader("C:\\Users\\jamie\\IdeaProjects\\WebAppVIPJSONEditor/src/webScraperResults.json"));
            alertDict = (JSONObject) obj;
            currAlert = (JSONObject) alertDict.get("" + jsonIndex);
            System.out.println(currAlert);
        }
        catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }

    }
    private void initializeWindow() {
        window = new JFrame("Data Entry Helper");
        window.setTitle(currAlert.get("alertName") + " " + jsonIndex);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setResizable(true);

        initPanel();

        window.add(panel);
        window.addKeyListener(inputListener);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private void initPanel() {
        panel = new JPanel();
        panel.setLayout(new MigLayout("wrap 3"));
        textPanel = new JPanel();
        alertText.setLineWrap(true);
        alertText.setWrapStyleWord(true);
        alertText.append((String) currAlert.get("alertSolution"));
        alertText.addKeyListener(inputListener);
        textPanel.add(alertText);
        panel.add(textPanel, "span");
        confirmButton.addActionListener(btnListener);
        nextButton.addActionListener(btnListener);
        saveButton.addActionListener(btnListener);
        panel.add(confirmButton);
        panel.add(nextButton);
        panel.add(saveButton);


    }

    private void saveToJson() {
        try {
            FileWriter fileWriter = new FileWriter("C:\\Users\\jamie\\IdeaProjects\\WebAppVIPJSONEditor/src/webScraperResults.json", false);
            fileWriter.write(alertDict.toJSONString());
            fileWriter.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(window, "Couldn't save to file!");
            System.out.println(ex);
        }
    }

    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            //player selected new game
            if (e.getActionCommand().equals("Undo")) {
                try {
                    alertText.setText(undoStack.pop());
                } catch(EmptyStackException ex) {
                    JOptionPane.showMessageDialog(window, "No older version to go to!");
                }
            }

            //opens the level builder
            if (e.getActionCommand().equals("Next Section")) {
                String tempString = alertText.getText();
                if (fixOrSum) {
                    currAlert.replace("alertSolution", tempString);
                    alertText.setText((String) currAlert.get("alertSummary"));

                } else {
                    currAlert.replace("alertSummary", tempString);
                    alertDict.replace("" + jsonIndex, currAlert);
                    System.out.println(alertDict.get("" + jsonIndex));
                    jsonIndex++;
                    currAlert = (JSONObject) alertDict.get("" + jsonIndex);
                    alertText.setText((String) currAlert.get("alertSolution"));
                    window.setTitle(currAlert.get("alertName") + " " + jsonIndex);
                    if (jsonIndex % 10 == 0) {
                        saveToJson();
                    }
                }
                fixOrSum = !fixOrSum;
            }
            if (e.getActionCommand().equals("Save JSON")) {
                saveToJson();

            }

        }

    }

    class InputListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                caretStartPos = alertText.getCaretPosition();
            }
            else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                caretEndPos = alertText.getCaretPosition();
                String tempString = null;
                try {
                    tempString = alertText.getText(caretStartPos, caretEndPos - caretStartPos);
                } catch (BadLocationException ex) {
                    JOptionPane.showMessageDialog(window, "Invalid start and end!");
                }
                insertString = "<a href=\"https://www.google.com/search?q=" + tempString + "\" target=\"blank\">";
                undoStack.add(alertText.getText());
                alertText.insert(ENDING_TAG, caretEndPos);
                alertText.insert(insertString, caretStartPos);
            }
        }
    }
}
