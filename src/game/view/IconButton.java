package game.view;


import utilities.ViewUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * Allows to create a button that has an icon which is resized depending on the size of the button.
 * The background can be automatically set.
 */
public class IconButton extends JButton {

    /**
     * The icon that will be on the button
     */
    private ImageIcon icon;

    /**
     * Constructor of the class that sets the background as needed and that sets the icon.
     * The background put depends on wether the current scheme is dark or not.
     * @param imageIcon the icon of the button
     * @param autoBackground true if the background needs to be automatically set
     */
    public IconButton(ImageIcon imageIcon, boolean autoBackground) {
        super();
        if (imageIcon != null) {
            this.icon = imageIcon;
            this.setIcon(imageIcon);
            if (autoBackground) {
                if (Scheme.isDark()) this.setBackground(Scheme.DARK_COLOR);
                else this.setBackground(Scheme.LIGHT_COLOR);
            }
        } else throw new IllegalArgumentException("IconButton initialization exception");
    }

    /**
     * Allows to paint the button, if the size of the button does not correspond to the
     * size of the icon, the icon is reset.
     * @param graphics the graphics used to paint the button
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        if (this.getIcon() == null || this.getIcon().getIconHeight() != this.getHeight() - 10) {
            this.repaintIcon();
        }
        super.paintComponent(graphics);
    }

    /**
     * Allows to reset the icon of the appropriate size.
     */
    private void repaintIcon() {
        this.setIcon(ViewUtilities.getIcon(this.icon, this.icon.getIconWidth() * (this.getHeight() - 10) /
                this.icon.getIconHeight(), this.getHeight() - 10));
    }

    /**
     * Allows to change the icon of the button and to repaint the button with the new icon.
     * @param icon the new icon
     */
    public void changeIcon(ImageIcon icon) {
        this.icon = icon;
        this.repaintIcon();
    }
}
