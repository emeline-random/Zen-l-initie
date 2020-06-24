package game.view;


import game.controller.MenuBarListeners;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Allows to create a popup frame that shows some buttons that all have the same size and that depends on a frame.
 */
public class PopupFrame extends JDialog {

    /**
     * The height of all buttons in PopupFrames
     */
    private final static int COMPONENTS_HEIGHT = 70;
    /**
     * The buttons that are in the main page of the popup
     */
    private final List<JButton> components;

    /**
     * Constructor of the class that allow to define the main frame and that allows to
     * initialise th components list.
     * @param mainFrame the frame on which depends the popup
     * @param buttons the buttons contained in the popup
     */
    public PopupFrame(JFrame mainFrame, JButton ... buttons){
        super(mainFrame, true);
        this.components = Arrays.asList(buttons);
        this.setLayout(new GridLayout(1, this.components.size()));
        for (JComponent component : this.components) {
            this.add(component);
        }
        this.pack();
    }

    /**
     * Allows to show the frame at the center of its master frame.
     */
    public void showFrame() {
        this.setLocationRelativeTo(this.getRootPane());
        this.setVisible(true);
    }

    /**
     * Allows to reset the content of the popup to its initial components list
     */
    private void homeScreen() {
        this.getContentPane().removeAll();
        for (JComponent component : this.components) {
            this.add(component);
        }
        this.pack();
    }

    /**
     * Allows to get a button that will show the initial content of the popup
     * @return the home button of the popup
     */
    public JButton getHomeButton() {
        JButton button = new JButton();
        try {
            Image image = ImageIO.read(GraphicalInterface.class.getResourceAsStream("/res/pictures/icons/return.png"));
            int width = PopupFrame.COMPONENTS_HEIGHT * image.getWidth(null) / image.getHeight(null);
            button.setIcon(new ImageIcon(image.getScaledInstance(width, PopupFrame.COMPONENTS_HEIGHT, Image.SCALE_DEFAULT)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        button.addActionListener(e -> this.homeScreen());
        MenuBarListeners.buttonClickedListener(button);
        return button;
    }

    /**
     * @return the height of components
     */
    public static int getComponentsHeight(){
        return COMPONENTS_HEIGHT;
    }

}
