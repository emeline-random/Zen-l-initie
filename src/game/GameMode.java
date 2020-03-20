package game;


public class GameMode {

    private String mode;
    private GraphicalInterface graphicalInterface;

    public GameMode(){
        this.mode = "classic";
    }

    public GameMode(String mode){
        this.mode = mode;
    }

    public GameMode(String mode, GraphicalInterface graphicalInterface){
        this.mode = mode;
        this.graphicalInterface = graphicalInterface;
    }

    public GraphicalInterface getGraphicalInterface() {
        return graphicalInterface;
    }

    public String  getMode() {
        return mode;
    }
}
