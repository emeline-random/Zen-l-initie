package game.view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Java class that allows to create a JButton object that has a rounded rectangular shape
 * and that follows the size of the JFrame where it is (ie. the bigger the JFrame is, the bigger
 * the button gets). The background, the foreground and the font are set to correspond to the
 * menu scheme at the construction of the object (but can still be changed using the appropriate
 * methods).
 */
public class RoundRectButton extends JButton {

    /**
     * True if the mouse is in the button
     */
    private boolean isMouseIn = false;
    /**
     * Arc for the round rectangle
     */
    private final int arc;
    /**
     * initial size of the font
     */
    private int fontSize;
    /**
     * indicates how much the size of the font depends on the JFrame
     */
    private final int fontPercentage;
    /**
     * The JFrame where the button is
     */
    private final JFrame master;
    /**
     * The actual font used in the button
     */
    private Font font;
    private final String label;

    /**
     * Constructor of the class, allows to create a RoundRectButton object
     * by calling the superclass constructor with the specified label and
     * by setting the arc, the font percentage and the frame where the button is.
     * If fontPercentage or frame are not valid, the fontPercentage is set to -1.
     *
     * @param label          the text to display on the button
     * @param arc            the arc of the round rectangular shape
     * @param fontPercentage the percentage that the font will follow
     * @param frame          the frame where the button is
     */
    public RoundRectButton(String label, int arc, int fontPercentage, JFrame frame) {
        super();
        this.label = label;
        this.font = this.getFont();
        this.setBackground(Scheme.DARK_COLOR);
        this.setForeground(Scheme.LIGHT_COLOR);
        if (arc >= 0 && fontPercentage >= 0 && frame != null) {
            this.arc = arc;
            this.fontPercentage = fontPercentage;
            this.master = frame;
            this.configButton();
        } else {
            throw new IllegalArgumentException("RoundRectButton constructor exception");
        }
    }

    /**
     * Allows to initialize the font size and to add a MouseListener
     * that will be used to change the borders if the mouse is on the
     * button. Set focusPainted to false so that we can redraw our own
     * button.
     */
    private void configButton() {
        this.fontSize = this.getFont().getSize();
        this.setContentAreaFilled(false);
        this.setFocusable(false);
        this.setFocusPainted(false);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                RoundRectButton.this.isMouseIn = true;
                RoundRectButton.this.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                RoundRectButton.this.isMouseIn = false;
                RoundRectButton.this.repaint();
            }
        });
    }

    /**
     * Paint the button by filling a round rectangle with it's background color or light gray
     * color if the button is pressed and by setting the size of the font.
     * Then calls the superclass paintComponent method.
     *
     * @param g the graphics of the button
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) g.setColor(Color.lightGray);
        else g.setColor(this.getBackground());
        if (this.font.getSize() != (int) (this.fontSize + (this.fontPercentage / 1000d) * this.master.getWidth() * 2)) {
            this.font = new Font("Arial Rounded MT Bold", Font.PLAIN, (int) (this.fontSize +
                    (this.fontPercentage / 1000d) * this.master.getWidth() * 2));
            this.setFont(this.font);
            this.setText(this.label);
        } else {
            g.fillRoundRect(0, 0, getSize().width - 1, getSize().height - 1, this.arc, this.arc);
            g.setColor(this.getForeground());
            g.drawRoundRect(0, 0, getSize().width - 1, getSize().height - 1, this.arc, this.arc);
            super.paintComponent(g);
            this.paintBorder(g);
        }
    }

    /**
     * Allows to paint the border of the button, these are not the same depending on
     * whether the mouse is in the button or not.
     *
     * @param g the graphics of the button
     */
    @Override
    protected void paintBorder(Graphics g) {
        if (this.isMouseIn) this.paintMouseInBorder(g);
    }

    /**
     * Allows to paint specific borders by drawing a round rectangle a little bit smaller
     * than the button shape.
     *
     * @param g the graphics of the button
     */
    private void paintMouseInBorder(Graphics g) {
        g.setColor(this.getForeground());
        g.drawRoundRect(2, 2, this.getWidth() - 6, this.getHeight() - 6, this.arc, this.arc);
    }
}