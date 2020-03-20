package game;

import javax.swing.*;
import java.awt.*;

public class GraphicalInterface extends JFrame{

    private Pawn[][] pawns;
    protected final Color BoardColor = Color.CYAN;

    public GraphicalInterface(Pawn[][] pawns){
        this.pawns = pawns;
        if(pawns == null) System.out.println("pa");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setBounds(15, 15, 300, 300);
        this.repaint();
        this.setVisible(true);
    }

    public void paint(Graphics graphics){
        for (Pawn[] pawns1 : this.pawns){
            for (Pawn pawn : pawns1){
                if(pawn != null) pawn.paintCircle(graphics);
            }
        }
    }

}
