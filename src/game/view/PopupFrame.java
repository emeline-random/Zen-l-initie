package game.view;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class PopupFrame extends JDialog {

    private final static int COMPONENTS_HEIGHT = 70;
    private final List<JComponent> components;

    public PopupFrame(JFrame mainFrame, List<JComponent> components) {
        super(mainFrame, true);
        this.setLayout(new GridLayout(1, components.size()));
        this.components = components;
        for (JComponent component : components) {
            this.add(component);
        }
        this.pack();
    }

    public void showFrame() {
        this.setLocationRelativeTo(this.getRootPane());
        this.setVisible(true);
    }

    private void homeScreen() {
        this.getContentPane().removeAll();
        for (JComponent component : this.components) {
            this.add(component);
        }
        this.revalidate();
        this.repaint();
    }

    public JButton getHomeButton() {
        JButton button = new JButton();
        try {
            Image image = ImageIO.read(GraphicalInterface.class.getResourceAsStream("/pictures/icons/return.png"));
            int width = PopupFrame.COMPONENTS_HEIGHT * image.getWidth(null) / image.getHeight(null);
            button.setIcon(new ImageIcon(image.getScaledInstance(width, PopupFrame.COMPONENTS_HEIGHT, Image.SCALE_DEFAULT)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        button.addActionListener((ActionEvent action) -> this.homeScreen());
        return button;
    }

    public static int getComponentsHeight(){
        return COMPONENTS_HEIGHT;
    }

}
