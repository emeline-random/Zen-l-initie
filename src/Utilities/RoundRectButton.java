package Utilities;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class RoundRectButton extends JButton {

    private boolean isMouseIn = false;
    private int arc = 10;

    public RoundRectButton(String label, int arc) {
        super(label);
        // This call causes the JButton not to paint the background.
        // This allows us to paint a round background.
        if (arc >= 0) this.arc = arc;
        setContentAreaFilled(false);
        setFocusPainted(true);
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

    @Override
    // Paint the button with the fillRoundRect() method, if it id selected, the background color is lightGray, else it is the button's color
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(Color.lightGray);
        } else {
            g.setColor(this.getBackground());
        }
        g.fillRoundRect(0, 0, getSize().width - 1, getSize().height - 1, this.arc, this.arc);
        super.paintComponent(g);
    }

    @Override
    // Paint the border of the drawRoundRect() method and the foreground color of the button
    protected void paintBorder(Graphics g) {
        if (this.isMouseIn){
            this.paintMouseInBorder(g);
        } else {
            g.setColor(this.getForeground());
            g.drawRoundRect(0, 0, getSize().width - 1, getSize().height - 1, this.arc, this.arc);
        }
    }

    private void paintMouseInBorder(Graphics g){
        g.setColor(this.getForeground());
        g.drawRoundRect(2, 2, this.getWidth() - 6, this.getHeight() - 6, this.arc, this.arc);
    }
}