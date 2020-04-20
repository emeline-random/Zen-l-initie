package Game.View;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class SwitchScheme {

    private static Scheme currentScheme;

    enum Scheme {
        LIGHT,
        DARK,
        NIMBUS,
        SYSTEM,
        MENU
    }

    public static void switchScheme(Scheme scheme) {
        switch (scheme) {
            case DARK:
                try {
                    UIManager.setLookAndFeel(new FlatDarculaLaf());
                    updateUIDefaults();
                    currentScheme = scheme;
                } catch (Exception ex) {
                    switchScheme(Scheme.SYSTEM);
                }
                break;
            case LIGHT:
                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    updateUIDefaults();
                    currentScheme = scheme;
                } catch (Exception ex) {
                    switchScheme(Scheme.SYSTEM);
                }
                break;
            case NIMBUS:
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        try {
                            UIManager.setLookAndFeel(info.getClassName());
                            updateUIDefaults();
                            currentScheme = scheme;
                        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                            switchScheme(Scheme.LIGHT);
                        }
                        break;
                    }
                }
                break;
            case SYSTEM:
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    updateUIDefaults();
                    currentScheme = scheme;
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                break;
            case MENU:
                try {
                    UIManager.setLookAndFeel(new MenuUIDefaults());
                    currentScheme = scheme;
                } catch (UnsupportedLookAndFeelException e) {
                    switchScheme(Scheme.LIGHT);
                }
        }
    }

    public static void updateUIDefaults() {
        UIDefaults defaults = UIManager.getLookAndFeel().getDefaults();
        UIManager.put("Button.background", defaults.get("Button.background"));
        UIManager.put("Button.foreground", defaults.get("Button.foreground"));
        UIManager.put("Button.font", defaults.get("Button.font"));
        UIManager.put("Panel.background", defaults.get("Panel.background"));
        UIManager.put("Panel.font", defaults.get("Panel.font"));
        UIManager.put("Label.font", defaults.get("Label.font"));
    }

    public static Scheme getCurrentScheme() {
        return SwitchScheme.currentScheme;
    }

    public static Scheme[] getSchemes() {
        return new Scheme[]{Scheme.LIGHT, Scheme.DARK, Scheme.NIMBUS, Scheme.SYSTEM};
    }

}
