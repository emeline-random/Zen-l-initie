package utilities;

import game.view.PopupFrame;
import game.view.Scheme;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Class that allows to perform basics operations on view components, principally on images.
 */
public class ViewUtilities {

    /**
     * Allows to get a BufferedImage object that is a fully loaded image from the ImageIO.read() method.
     *
     * @param path the path of the image to get (needs to begin with a '/').
     * @return the image oat the corresponding path or null if it does not exists
     */
    public static BufferedImage getImage(String path) {
        try {
            return ImageIO.read(ViewUtilities.class.getResourceAsStream(path));
        } catch (IOException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Allows to get an Image object that has a defined dimension.
     *
     * @param image  the original image
     * @param width  the desired width
     * @param height the desired height
     * @return the scaled image
     */
    public static Image getImage(Image image, int width, int height) {
        return image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    /**
     * Allows to get an Image object that has a defined dimension
     *
     * @param path   the path of the image to get (needs to begin with a '/').
     * @param width  the desired width
     * @param height the desired height
     * @return the image with a personalized dimension
     */
    public static Image getImage(String path, int width, int height) {
        return getImage(Objects.requireNonNull(getImage(path)), width, height);
    }

    /**
     * Allows to get an ImageIcon object of a personalized dimension.
     *
     * @param path   the path of the image to get (needs to begin with a '/').
     * @param width  the desired width
     * @param height the desired height
     * @return the icon with a personalized dimension
     */
    public static ImageIcon getIcon(String path, int width, int height) {
        return new ImageIcon(getImage(path, width, height));
    }

    /**
     * Allows to get an ImageIcon object of a personalized dimension.
     *
     * @param imageIcon the original ImageIcon
     * @param width  the desired width
     * @param height the desired height
     * @return the icon with a personalized dimension
     */
    public static ImageIcon getIcon(ImageIcon imageIcon, int width, int height) {
        return new ImageIcon(getImage(imageIcon.getImage(), width, height));
    }

    /**
     * Allows to get an icon that depends on the current scheme. The icon has the dimension of
     * the component height defined in the PopupFrame class (the width is adapted). If the current
     * scheme is dark, the image is searched in /pictures/icons/dark, else in /pictures/icons/light and
     * if the icon is ot found then it is searched in /pictures/icons.
     * @param iconName the path of the icon from the directories defined higher (often simply imageName.png)
     * @return the version of the icon in the current scheme
     */
    public static ImageIcon getSchemeIcon(String iconName, boolean manageSize) {//TODO javadoc update
        Image image;
        if (Scheme.isDark()) image = getImage("/pictures/icons/dark/" + iconName);
        else image = getImage("/pictures/icons/light/" + iconName);
        if (image == null) image = getImage("/pictures/icons/" + iconName);
        assert image != null;
        if (manageSize){
            int width = PopupFrame.getComponentsHeight() * image.getWidth(null) / image.getHeight(null);
            return getIcon(new ImageIcon(image), width, PopupFrame.getComponentsHeight());
        }
        return new ImageIcon(image);//getIcon(new ImageIcon(image), width, PopupFrame.getComponentsHeight());
    }

    /**
     * Allows to show a JDialog at the center of a frame. A title and a size can be defined.
     * @param dialog the dialog to show
     * @param title the title of the dialog
     * @param width the width of the dialog
     * @param height the height of the dialog
     */
    public static void showDialog(JDialog dialog, String title, int width, int height) {
        if (title != null) dialog.setTitle(title);
        if (width > 0 && height > 0) dialog.setSize(width, height);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    /**
     * Allows to add a component at the center of a panel and to eventually add glue after it.
     *
     * @param panel     the panel to add the component into
     * @param component the component to be added
     * @param addGlue true if the glue needs to be added, false otherwise
     */
    public static void addCenteredComponent(Container panel, JComponent component, boolean addGlue) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (component instanceof JTextField) ((JTextField) component).setHorizontalAlignment(SwingConstants.CENTER);
        else if (component instanceof JLabel) ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
        if (addGlue) panel.add(Box.createVerticalGlue());
        panel.add(component);
    }

    /**
     * Allows to add a list of components to a container
     * @param container the container to add the components in
     * @param components the components to add
     */
    public static void addAll(Container container, Component ... components){
        for (Component component : components) {
            container.add(component);
        }
    }

    /**
     * Allows to add a list of components at the center of a frame, glue can be add between components.
     * @param container the container to add the components to
     * @param glue true if glue needs to be added, false otherwise
     * @param components the components to add
     */
    public static void addAllCentered(Container container, boolean glue, JComponent... components) {
        for (JComponent component : components) {
            addCenteredComponent(container, component, glue);
        }
    }

    /**
     * Allows to add an action listener to some buttons
     * @param listener the listener to add
     * @param buttons the buttons to add the listener to
     */
    public static void addActionListener(ActionListener listener, JButton ... buttons) {
        for (JButton button : buttons) {
            button.addActionListener(listener);
        }
    }
}
