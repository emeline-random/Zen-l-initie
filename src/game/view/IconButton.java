package game.view;


import utilities.ViewUtilities;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

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
     * The path of the icon or its relative name from the icons directory
     */
    private String iconPath;
    /**
     * Needs to be true if the icon has to be search with the getSchemeIcon method
     */
    private boolean schemeIcon;

    /**
     * Constructor of the class that sets the background as needed and that sets the icon or
     * the path of the icon. If the path of the icon is given and if the icon is an icon that
     * depends on the scheme, the boolean schemeIcon needs to be true.
     * The background put depends on whether the current scheme is dark or not.
     *
     * @param imageIcon      the icon of the button
     * @param autoBackground true if the background needs to be automatically set
     * @param iconPath       the path of the icon (this path is used to repaint the image if it is not null, so
     *                       that this quality of the image is always good)
     * @param schemeIcon     needs to be true if the path of the icon has to be searched with the getSchemeIcon() method of
     *                       the ViewUtilities class
     */
    public IconButton(ImageIcon imageIcon, boolean autoBackground, String iconPath, boolean schemeIcon) {
        super();
        if (imageIcon != null || iconPath != null) {
            this.schemeIcon = schemeIcon;
            this.icon = imageIcon;
            this.iconPath = iconPath;
            if (this.iconPath == null) {
                this.setIcon(imageIcon);
            } else {
                if (schemeIcon) this.setIcon(ViewUtilities.getSchemeIcon(this.iconPath, true));
                else this.setIcon(new ImageIcon(Objects.requireNonNull(ViewUtilities.getImage(this.iconPath))));
                this.icon = (ImageIcon) this.getIcon();
            }
            if (autoBackground) {
                if (Scheme.isDark()) this.setBackground(Scheme.DARK_COLOR);
                else this.setBackground(Scheme.LIGHT_COLOR);
            }
        } else throw new IllegalArgumentException("IconButton initialization exception");
    }

    /**
     * Allows to paint the button, if the size of the button does not correspond to the
     * size of the icon, the icon is reset.
     *
     * @param graphics the graphics used to paint the button
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        if (this.getIcon() == null || this.getIcon().getIconHeight() != this.getHeight() - 10) this.repaintIcon();
        super.paintComponent(graphics);
    }

    /**
     * Allows to reset the icon of the appropriate size.
     */
    private void repaintIcon() {
        if (this.iconPath == null) {
            this.setIcon(ViewUtilities.getIcon(this.icon, this.icon.getIconWidth() * (this.getHeight() - 10) /
                    this.icon.getIconHeight(), this.getHeight() - 10));
        } else {
            if (this.schemeIcon) {
                this.setIcon(ViewUtilities.getIcon(ViewUtilities.getSchemeIcon(this.iconPath, false),
                        this.icon.getIconWidth() * (this.getHeight() - 10) / this.icon.getIconHeight(), this.getHeight() - 10));
            } else {
                this.setIcon(ViewUtilities.getIcon(this.iconPath, this.icon.getIconWidth() * (this.getHeight() - 10) /
                        this.icon.getIconHeight(), this.getHeight() - 10));
            }
        }
    }

    /**
     * Allows to change the icon of the button and to repaint the button with the new icon.
     *
     * @param icon       the new icon
     * @param schemeIcon needs to be true if the icon has to be searched with getSchemeIcon() method
     * @param iconPath   the path of the icon
     */
    public void changeIcon(ImageIcon icon, boolean schemeIcon, String iconPath) {
        this.icon = icon;
        this.iconPath = iconPath;
        this.schemeIcon = schemeIcon;
        this.repaintIcon();
    }
}
