package Game.View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class MenuUIDefaults extends FlatLightLaf {

    private static final Color LIGHTCOLOR = new Color(230, 216, 202);
    private static final Color DARKCOLOR = new Color(26, 14, 5);
    private static final Font FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 17);

    public MenuUIDefaults(){
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        UIManager.put("Button.background", DARKCOLOR);
        UIManager.put("Button.foreground", LIGHTCOLOR);
        UIManager.put("Button.font", FONT);
        UIManager.put("Panel.background", LIGHTCOLOR);
        UIManager.put("Panel.font", FONT);
        UIManager.put("Label.font", FONT);
    }
}
