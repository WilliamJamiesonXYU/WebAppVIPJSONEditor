import net.miginfocom.swing.MigLayout;
import org.json.simple.*;
import org.json.simple.parser.*;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;

public class GraphicsMain {

    JSONObject alertDict;
    JSONObject currAlert;
    JFrame window;
    JPanel panel;
    JPanel textPanel;
    JButton confirmButton = new JButton("Confirm Switch");
    JButton nextButton = new JButton("Next Alert");
    JButton saveButton = new JButton("Save JSON");
    JTextArea alertText = new JTextArea();
    ButtonListener btnListener = new ButtonListener();

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
        }
        catch (Exception e) {
            System.out.println(System.getProperty("user.dir"));
            System.exit(0);
        }

    }
    private void initializeWindow() {
        window = new JFrame("Data Entry Helper");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setResizable(false);

        initPanel();

        window.add(panel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private void initPanel() {
        panel = new JPanel();
        panel.setLayout(new MigLayout("wrap 3"));
        textPanel = new JPanel();
        alertText.append((String) currAlert.get("alertSolution"));
        textPanel.add(alertText);
        panel.add(textPanel, "span");
        confirmButton.addActionListener(btnListener);
        nextButton.addActionListener(btnListener);
        saveButton.addActionListener(btnListener);
        panel.add(confirmButton);
        panel.add(nextButton);
        panel.add(saveButton);


    }

    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            //player selected new game
            if (e.getActionCommand().equals("Confirm Switch")) {

            }

            //opens the level builder
            if (e.getActionCommand().equals("Next Alert")) {

            }
            if (e.getActionCommand().equals("Save JSON")) {

            }

        }

    }
}
