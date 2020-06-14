package utilities;

import game.view.PopupFrame;
import game.view.SwitchScheme;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class ViewUtilities {

    public static Image getImage(String path) {
        try {
            return ImageIO.read(ViewUtilities.class.getResourceAsStream(path));
        } catch (IOException e) {
            return Toolkit.getDefaultToolkit().getImage(ViewUtilities.class.getResource(path));
        }
    }

    public static Image getImage(Image image, int width, int height) {
        return image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    public static Image getImage(String path, int width, int height) {
        return getImage(getImage(path), width, height);
    }

    public static ImageIcon getImageIcon(Image image) {
        return new ImageIcon(image);
    }

    public static ImageIcon getImageIcon(String path, int width, int height) {
        return new ImageIcon(getImage(path, width, height));
    }

    public static ImageIcon getImageIcon(ImageIcon imageIcon, int width, int height) {
        return new ImageIcon(getImage(imageIcon.getImage(), width, height));
    }

    public static ImageIcon getImageIcon(Image image, int width, int height) {
        return new ImageIcon(getImage(image, width, height));
    }

    public static ImageIcon getSchemeIcon(String iconName) {
        Image image;
        if (SwitchScheme.isDark()) image = getImage("/pictures/icons/dark/" + iconName);
        else image = getImage("/pictures/icons/light/" + iconName);
        int width = PopupFrame.getComponentsHeight() * image.getWidth(null) / image.getHeight(null);
        return getImageIcon(getImageIcon(image), width, PopupFrame.getComponentsHeight());
    }

    /**
     * Allows to add a component at the center of a panel
     *
     * @param panel     the panel to add the component into
     * @param component the component to be add
     */
    public static void addCenteredComponent(Container panel, JComponent component, boolean addGlue) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (component instanceof JTextField) ((JTextField) component).setHorizontalAlignment(SwingConstants.CENTER);
        else if (component instanceof JLabel) ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
        if (addGlue) panel.add(Box.createVerticalGlue());
        panel.add(component);
    }

    public static void showDialog(JDialog dialog, String title, int width, int height){
        if (title != null) dialog.setTitle(title);
        if (width > 0 && height > 0) dialog.setSize(width, height);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public static void addAll(Container container, List<Component> components){
        for (Component component : components){
            container.add(component);
        }
    }

    public static void addAllCentered(Container container, List<JComponent> components, boolean glue){
        for (JComponent component : components){
            addCenteredComponent(container, component, glue);
        }
    }

    public static void addActionListener(ActionListener listener, List<JButton> buttons){
        for (JButton button : buttons){
            button.addActionListener(listener);
        }
    }
}
