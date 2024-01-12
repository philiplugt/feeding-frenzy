/**
 * View.java
 * 
 * @author pxv8780
 * @date   2023/03/28
 */

import java.awt.Color;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


// View handles all the visual aspects.
public class View extends JFrame implements ViewObserver {

    int size;

    // Info panel
    JLabel countLabel = new JLabel("0");
    JLabel gameState = new JLabel("Game Running!");
    
    // Events panel
    JTextArea textBox = new JTextArea();

    // Menu panel
    JCheckBox pauseCheckBox = new JCheckBox("Pause");
    JButton increaseButton = new JButton("Increase");
    JButton decreaseButton = new JButton("Decrease");
    JButton restartButton = new JButton("Restart");
    JButton quitButton = new JButton("Quit");
    
    // Board grid buttons
    JPanel grid;
    JButton[][] gridButtons;

    public View(int size) {

        this.size = size;
        gridButtons = new JButton[size][size];

        // Create all necessary panels
        JPanel info = createGUIInfoPanel();
        JPanel menu = createGUIMenuPanel();
        grid = createGUIGrid();
        JScrollPane scrollpane = createGUIEventPanel();
        
        // Put all the panels together into (this) JFrame
        JPanel top = new JPanel(new BorderLayout());
        top.add(info, BorderLayout.WEST);
        top.add(scrollpane, BorderLayout.CENTER);
        top.add(menu, BorderLayout.EAST);
        JPanel main = new JPanel(new BorderLayout());
        main.add(top, BorderLayout.NORTH);
        main.add(grid, BorderLayout.CENTER);
        this.add(main);
    }

    public void init() {
        setGUIStyle(this);
        setTitle(Constants.TITLE);
        setSize(Constants.SCREEN.width, Constants.SCREEN.height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // The info panel shows details on the amount of eaten enemies and if the game is running
    private JPanel createGUIInfoPanel() {
        JPanel info = new JPanel(new GridLayout(2, 2));
        countLabel.setBorder(new TitledBorder(""));
        gameState.setBorder(new TitledBorder(""));
        info.add(new JLabel("Consumption"));
        info.add(countLabel);
        info.add(new JLabel("Player is P"));
        info.add(gameState);
        return info;
    }

    // The event panel is a scroll pane which shows who has eaten who, and the game status
    private JScrollPane createGUIEventPanel() {
        textBox.setEditable(false);
        textBox.append("Nothing eaten yet.\n");
        JScrollPane scrollpane = new JScrollPane(textBox, 
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        scrollpane.setPreferredSize(new Dimension( 100, 70));
        return scrollpane;
    }

    // The menu panel shows controls for pausing, game speed, restarting and quitting the game
    private JPanel createGUIMenuPanel() {
        JPanel menu = new JPanel(new GridLayout(3, 2));
        menu.add(pauseCheckBox);
        menu.add(new JLabel(""));
        menu.add(increaseButton);
        menu.add(decreaseButton);
        menu.add(restartButton);
        menu.add(quitButton);
        return menu;
    }

    // Creates the grid on which the player and enemies move and eat each other
    private JPanel createGUIGrid() {
        JPanel grid = new JPanel(new GridLayout(size, size));
        grid.setBorder(new TitledBorder("Game Board"));
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                gridButtons[y][x] = new JButton();
                gridButtons[y][x].setMargin(new Insets(0, 0, 0, 0));
                grid.add(gridButtons[y][x]);
            }
        }
        return grid;
    }

    public void onMessageNotification(String event, String message) {
        SwingUtilities.invokeLater(() -> {
            switch (event) {
                case Constants.STATE_UPDATE -> gameState.setText(message);
                case Constants.COUNT_UPDATE -> countLabel.setText(message);
                case Constants.TEXT_UPDATE -> {
                    textBox.append(message + "\n");
                    textBox.setCaretPosition(textBox.getDocument().getLength());
                }
                case Constants.DISABLE_UPDATE -> {
                    pauseCheckBox.setEnabled(false);
                    grid.setVisible(false);
                    for (int x = 0; x < size; x++) {
                        for (int y = 0; y < size; y++) {
                            gridButtons[y][x].setEnabled(false);
                        }
                    }
                    grid.setVisible(true);
                }
                case Constants.RESET_UPDATE -> {
                    pauseCheckBox.setEnabled(true);
                    grid.setVisible(false);
                    for (int x = 0; x < size; x++) {
                        for (int y = 0; y < size; y++) {
                            gridButtons[y][x].setBackground(null);
                            gridButtons[y][x].setText("");
                            gridButtons[y][x].setEnabled(true);
                        }
                    }
                    grid.setVisible(true);
                }
                case Constants.QUIT_UPDATE -> this.dispose();
            }
        });
    }

    public void onActionNotification(String event, int x, int y, int x0, int y0, char name, Color color) {
        SwingUtilities.invokeLater(() -> {
            switch (event) {
                case Constants.CREATE_UPDATE -> {
                    gridButtons[x][y].setBackground(color);
                    gridButtons[x][y].setText("" + name);
                }
                case Constants.MOVE_UPDATE -> {
                    gridButtons[x0][y0].setBackground(null);
                    gridButtons[x0][y0].setText("");
                    gridButtons[x][y].setBackground(color);
                    gridButtons[x][y].setText("" + name);
                }
            }
        });
    }

    // Set the UI style the Java's default style for all OS's
    private static void setGUIStyle(JFrame frame) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());  
            SwingUtilities.updateComponentTreeUI(frame);    
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            System.err.println("Setting of look and feel failed.");
            e.printStackTrace();
        }
    }
}