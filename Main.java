import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

class Main {

    JFrame f;
    JTabbedPane tabbedPane;
    JPanel tab1, tab2, tab3;
    Border border = new LineBorder(Color.lightGray, 1);
    Color navyBlue = Color.decode("#1A3D64");
    Color likegreen = Color.decode("#2973B2");
    Font f1 = new Font("Calibri", Font.BOLD, 18);
    Font f2 = new Font("Calibri", Font.BOLD, 40);
    Dimension buttonSize = new Dimension(120, 40);
    ArrayList<String> wordList = new ArrayList<>();
    ArrayList<String[]> wordListTab3 = new ArrayList<>();
    int currentIndex = 0;
    int fileIndex = 1;
    JLabel l4 = new JLabel();
    JLabel l5 = new JLabel();
    JComboBox<String> comboBox;
    File[] files;

    Main() {
        createFrame();
        createTabbedPane();
        f.setVisible(true);

    }

    void createFrame() {
        f = new JFrame("Flash Card Game");
        f.setBounds(600, 200, 400, 500);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void refreshSetList() {
        File folder = new File("sets");
        if (!folder.exists())
            folder.mkdirs();

        files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        ArrayList<String> setList = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (File file : files) {
                setList.add(file.getName().replace(".txt", ""));
            }
        } else {
            setList.add("No sets available");
        }

        // Update combo box model
        comboBox.setModel(new DefaultComboBoxModel<>(setList.toArray(new String[0])));
    }

    void addTab1() {
        tab1 = new JPanel();
        tab1.setLayout(new BoxLayout(tab1, BoxLayout.Y_AXIS));
        tab1.setBackground(navyBlue);

        // Create components
        JLabel l1 = new JLabel("Set Name:");
        l1.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        l1.setForeground(Color.white);
        l1.setFont(f1);

        JTextField t1 = new JTextField();
        t1.setPreferredSize(new Dimension(300, 30));
        t1.setMaximumSize(new Dimension(300, 30));
        t1.setMinimumSize(new Dimension(300, 30));
        t1.setAlignmentX(JTextField.CENTER_ALIGNMENT);
        t1.setBorder(border);

        JLabel l2 = new JLabel("Word:");
        l2.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        l2.setForeground(Color.white);
        l2.setFont(f1);

        JTextField t2 = new JTextField();
        t2.setPreferredSize(new Dimension(300, 30));
        t2.setMaximumSize(new Dimension(300, 30));
        t2.setMinimumSize(new Dimension(300, 30));
        t2.setAlignmentX(JTextField.CENTER_ALIGNMENT);
        t2.setBorder(border);

        JLabel l3 = new JLabel("Definition:");
        l3.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        l3.setForeground(Color.white);
        l3.setFont(f1);

        JTextArea t3 = new JTextArea();
        t3.setPreferredSize(new Dimension(300, 100));
        t3.setMaximumSize(new Dimension(300, 100));
        t3.setMinimumSize(new Dimension(300, 100));
        t3.setAlignmentX(JTextField.CENTER_ALIGNMENT);
        t3.setLineWrap(true);
        t3.setBorder(border);

        JButton b1 = new JButton("Add Word");
        b1.setPreferredSize(buttonSize);
        b1.setMaximumSize(buttonSize);
        b1.setMinimumSize(buttonSize);
        b1.setAlignmentX(JButton.CENTER_ALIGNMENT);
        b1.setForeground(Color.white);
        b1.setBackground(likegreen);

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String word = t2.getText().trim();
                String def = t3.getText().trim();

                if (word.isEmpty() || def.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter both Word and Definition.");
                    return;
                }

                wordList.add(word + " : " + def);

                t2.setText("");
                t3.setText("");

            }
        });

        JButton b2 = new JButton("Save Set");
        b2.setPreferredSize(buttonSize);
        b2.setMaximumSize(buttonSize);
        b2.setMinimumSize(buttonSize);
        b2.setAlignmentX(JButton.CENTER_ALIGNMENT);
        b2.setForeground(Color.white);
        b2.setBackground(likegreen);

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String setName = t1.getText().trim();
                if (setName.isEmpty()) {
                    JOptionPane.showMessageDialog(f, "Please enter Set Name.");
                    return;
                }
                if (wordList.isEmpty()) {
                    JOptionPane.showMessageDialog(f, "No words to save.");
                    return;
                }

                try (FileWriter fw = new FileWriter("sets/" + setName + ".txt")) {
                    for (String s : wordList)
                        fw.write(s + "\n");
                    JOptionPane.showMessageDialog(f, "Set saved as '" + setName + ".txt'");
                    t1.setText("");
                    wordList.clear();

                    // REFRESH combo box in Tab 2
                    refreshSetList();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Add components with vertical spacing
        tab1.add(Box.createVerticalStrut(30)); // top space
        tab1.add(l1);
        tab1.add(Box.createVerticalStrut(3));
        tab1.add(t1);
        tab1.add(Box.createVerticalStrut(15));

        tab1.add(l2);
        tab1.add(Box.createVerticalStrut(3));
        tab1.add(t2);
        tab1.add(Box.createVerticalStrut(15));

        tab1.add(l3);
        tab1.add(Box.createVerticalStrut(3));
        tab1.add(t3);
        tab1.add(Box.createVerticalStrut(15));

        tab1.add(b1);
        tab1.add(Box.createVerticalStrut(5));
        tab1.add(b2);
        tab1.add(Box.createVerticalGlue()); // pushes components to center

        // Add tab to tabbed pane
        tabbedPane.addTab("Create Set", tab1);
    }

    void addTab2() {
        tab2 = new JPanel();
        tab2.setLayout(new BoxLayout(tab2, BoxLayout.Y_AXIS));
        tab2.setBackground(navyBlue);

        File folder = new File("sets");
        if (!folder.exists())
            folder.mkdirs(); // create if not exists

        files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });

        String[] setNames;
        if (files != null && files.length > 0) {
            setNames = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                setNames[i] = files[i].getName().replace(".txt", "");
            }
        } else {
            setNames = new String[] { "No sets available" };
        }

        comboBox = new JComboBox<>(setNames);
        comboBox.setPreferredSize(new Dimension(300, 30));
        comboBox.setMaximumSize(new Dimension(300, 30));
        comboBox.setMinimumSize(new Dimension(300, 30));
        comboBox.setAlignmentX(JTextField.CENTER_ALIGNMENT);
        comboBox.setBorder(border);

        JButton b3 = new JButton("Select Set");
        b3.setPreferredSize(buttonSize);
        b3.setMaximumSize(buttonSize);
        b3.setMinimumSize(buttonSize);
        b3.setAlignmentX(JButton.CENTER_ALIGNMENT);
        b3.setForeground(Color.white);
        b3.setBackground(likegreen);
        b3.addActionListener(e -> {
            // Get selected index
            int selectedIndex = comboBox.getSelectedIndex();
            if (selectedIndex < 0 || files == null || files.length == 0)
                return;

            fileIndex = selectedIndex; // set fileIndex to selected set
            currentIndex = 0; // reset currentIndex
            wordListTab3.clear(); // clear previous words

            // Load words from selected file
            try (BufferedReader br = new BufferedReader(new FileReader(files[fileIndex]))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(":", 2);
                    if (parts.length == 2) {
                        wordListTab3.add(new String[] { parts[0].trim(), parts[1].trim() });
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(f, "Error loading set file!");
                return;
            }

            // Show first word in tab 3
            if (!wordListTab3.isEmpty()) {
                l4.setText(wordListTab3.get(currentIndex)[0]); // Word
                l5.setText(""); // Definition
            }

            // Switch to Tab 3
            tabbedPane.setSelectedIndex(0);
        });

        JButton b4 = new JButton("Delete Set");
        b4.setPreferredSize(buttonSize);
        b4.setMaximumSize(buttonSize);
        b4.setMinimumSize(buttonSize);
        b4.setAlignmentX(JButton.CENTER_ALIGNMENT);
        b4.setForeground(Color.white);
        b4.setBackground(likegreen);
        b4.addActionListener(e -> {
            int selectedIndex = comboBox.getSelectedIndex();
            if (selectedIndex < 0 || files == null || files.length == 0)
                return;

            files[selectedIndex].delete(); // Delete file
            refreshSetList(); // Refresh combo box
        });

        tab2.add(Box.createVerticalStrut(40));
        tab2.add(comboBox);
        tab2.add(Box.createVerticalStrut(30));
        tab2.add(b3);
        tab2.add(Box.createVerticalStrut(5));
        tab2.add(b4);

        tabbedPane.addTab("Select Set", tab2);
    }

    void addTab3() {

        tab3 = new JPanel();
        tab3.setLayout(new BoxLayout(tab3, BoxLayout.Y_AXIS));
        tab3.setBackground(navyBlue);

        File folder = new File("sets");
        if (folder.exists()) {

            File[] files = folder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".txt");
                }
            });

            if (files != null && files.length > 0) {

                try {
                    File firstFile = files[fileIndex]; // Select first file
                    BufferedReader br = new BufferedReader(new FileReader(firstFile));
                    String line;

                    while ((line = br.readLine()) != null) {
                        // Format: word : definition
                        String[] parts = line.split(":", 2);
                        if (parts.length == 2) {
                            wordListTab3.add(new String[] { parts[0].trim(), parts[1].trim() });
                        }
                    }
                    br.close();

                    if (!wordListTab3.isEmpty()) {
                        l4.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                        l4.setForeground(Color.white);
                        l4.setFont(f2);
                        l4.setText(wordListTab3.get(currentIndex)[0]); // First word

                        l5.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                        l5.setForeground(Color.white);
                        l5.setFont(f1);
                        l5.setText("");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error loading set file!");
                }

            }
        }

        JButton b5 = new JButton("See");
        b5.setPreferredSize(buttonSize);
        b5.setMaximumSize(buttonSize);
        b5.setMinimumSize(buttonSize);
        b5.setAlignmentX(JButton.CENTER_ALIGNMENT);
        b5.setForeground(Color.white);
        b5.setFont(new Font("Cambria", Font.BOLD, 20));
        b5.setBackground(likegreen);
        b5.addActionListener(e -> {
            if (!wordListTab3.isEmpty())
                l5.setText("<html><center><div style='width:290px; text-align:center;'>"
                        + wordListTab3.get(currentIndex)[1]
                        + "</div></center></html>");
        });

        JPanel smallPanel = new JPanel();
        smallPanel.setBackground(navyBlue);
        smallPanel.setLayout(new BoxLayout(smallPanel, BoxLayout.X_AXIS));

        ImageIcon nextIcon = new ImageIcon("arrow-next.png");
        ImageIcon prevIcon = new ImageIcon("arrow-previous.png");

        Image scaledImg;

        scaledImg = nextIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon nextIcon24 = new ImageIcon(scaledImg);

        scaledImg = prevIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon prevIcon24 = new ImageIcon(scaledImg);

        JButton b6 = new JButton("Previous");
        b6.setBackground(likegreen);
        b6.setAlignmentY(Component.CENTER_ALIGNMENT);
        b6.setForeground(Color.white);
        b6.setHorizontalTextPosition(SwingConstants.RIGHT); // text to the right of icon
        b6.setIcon(prevIcon24); // Previous button

        JButton b7 = new JButton("Next");
        b7.setBackground(likegreen);
        b7.setAlignmentY(Component.CENTER_ALIGNMENT);
        b7.setForeground(Color.white);
        b7.setHorizontalTextPosition(SwingConstants.LEFT); // text to the left of icon
        b7.setIcon(nextIcon24);

        Dimension navButtonSize = new Dimension(130, 30);
        b6.setPreferredSize(navButtonSize);
        b6.setMaximumSize(navButtonSize);
        b6.setMinimumSize(navButtonSize);
        b6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (wordListTab3.isEmpty())
                    return;

                currentIndex = (currentIndex - 1 + wordListTab3.size()) % wordListTab3.size(); // move backward
                l4.setText(wordListTab3.get(currentIndex)[0]); // update word

                l5.setText("");
            }
        });

        b7.setPreferredSize(navButtonSize);
        b7.setMaximumSize(navButtonSize);
        b7.setMinimumSize(navButtonSize);
        b7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (wordListTab3.isEmpty())
                    return;

                currentIndex = (currentIndex + 1) % wordListTab3.size(); // move forward
                l4.setText(wordListTab3.get(currentIndex)[0]); // update word

                l5.setText("");
            }
        });

        smallPanel.add(Box.createHorizontalGlue());
        smallPanel.add(b6);
        smallPanel.add(Box.createHorizontalStrut(60)); // space between buttons
        smallPanel.add(b7);
        smallPanel.add(Box.createHorizontalGlue());

        tab3.add(Box.createVerticalStrut(50));
        tab3.add(l4);
        tab3.add(Box.createVerticalStrut(20));
        tab3.add(l5);
        tab3.add(Box.createVerticalStrut(80));
        tab3.add(b5);
        tab3.add(Box.createVerticalStrut(20)); // space before panel
        tab3.add(smallPanel);

        tabbedPane.addTab("Flash Cards", tab3);
    }

    void createTabbedPane() {
        tabbedPane = new JTabbedPane();
        addTab3();
        addTab2();
        addTab1();
        f.add(tabbedPane);
    }

    public static void main(String[] args) {
        Main m = new Main();
    }
}