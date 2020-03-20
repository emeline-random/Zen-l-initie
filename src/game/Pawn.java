package game;


import java.awt.*;

public class Pawn {

    private final static int RADIUS = 5;
    private final static int DIMENSION = GameBoard.getDIMENSION();
    private int columnPosition;
    private int linePosition;
    private Color color;
    private String colorString;
    private int number;
    private Player player;

    public Pawn(Color color, Player player, int number) {
        if (color != null && player != null && number >= 0) {
            this.color = color;
            this.number = number;
            this.player = player;
            setColorString();
        }
    }

    public Pawn(){
        this.color = Color.red;
        this.number = -1;
        this.setColorString();
    }

    private void setColorString(){
        if(this.color == Color.RED) this.colorString = "\u001B[31m";
        else if (this.color == Color.WHITE) this.colorString = "\u001B[37m";
        else if(this.color == Color.CYAN) this.colorString = "\u001B[36m";
        else if(this.color == Color.PINK) this.colorString = "\u001B[35m";
        else if (this.color == Color.GREEN) this.colorString = "\u001B[32m";
        else if(this.color == Color.BLUE) this.colorString = "\u001B[34m";
        else if(this.color == Color.YELLOW) this.colorString = "\u001B[33m";
        else this.colorString = "\u001B[0m";
    }

    protected void movePawn(int linePosition, int columnPosition) {
        if (columnPosition < DIMENSION && columnPosition >= 0 && linePosition < DIMENSION && linePosition >= 0) {
            this.columnPosition = columnPosition;
            this.linePosition = linePosition;
        }
    }

    @Override
    public String toString() {
        if(this.number != -1) return ""+this.number;
        else return "z";
    }

    protected void paintCircle(Graphics g) {
        g.setColor(this.color);
        g.fillOval((int) ((this.columnPosition + 1) * RADIUS * 2.5), (int) ((this.linePosition + 1) * RADIUS * 2.5 + 40), RADIUS * 2, RADIUS * 2);
    }

    public int getNumber() {
        return this.number;
    }

    public int getColumnPosition() {
        return this.columnPosition;
    }

    public int getLinePosition() {
        return this.linePosition;
    }

    public Color getColor(){
        return this.color;
    }

    public String getColorString(){
        return this.colorString;
    }

    public void setPosition(int linePosition, int columnPosition) {
        this.columnPosition = columnPosition;
        this.linePosition = linePosition;
    }
}
