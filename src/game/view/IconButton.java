package game.view;


import javax.swing.*;
import java.awt.*;

public class IconButton extends JButton {

    private ImageIcon icon;
    private final static Color BACKGROUND_DARK = new Color(26, 14, 5);
    private final static Color BACKGROUND_LIGHT = new Color(230, 216, 202);

    public IconButton(ImageIcon imageIcon, boolean autoBackground) {
        super();
        if (imageIcon != null) {
            this.icon = imageIcon;
            this.setIcon(imageIcon);
            if (autoBackground) {
                if (SwitchScheme.isDark()) this.setBackground(BACKGROUND_DARK);
                else this.setBackground(BACKGROUND_LIGHT);
            }
        } else throw new IllegalArgumentException("IconButton initialization exception");
    }

    @Override
    public void paintComponent(Graphics graphics) {
        if (this.getIcon() == null || this.getIcon().getIconHeight() != this.getHeight() - 10) {
            this.repaintIcon();
        }
        super.paintComponent(graphics);
    }

    private void repaintIcon() {
        this.setIcon(new ImageIcon(this.icon.getImage().getScaledInstance(this.icon.getIconWidth() *
                (this.getHeight() - 10) / this.icon.getIconHeight(), this.getHeight() - 10, Image.SCALE_DEFAULT)));
    }

    public void changeIcon(ImageIcon icon) {
        this.icon = icon;
        this.repaintIcon();
    }
}
