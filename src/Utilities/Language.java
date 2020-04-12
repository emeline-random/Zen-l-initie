package Utilities;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Language {

    private static File file = new File("Languages/English.txt");

    public static void setLanguage(String language){
        if (language.equals("fr")){
            file = new File("Languages/French.txt");
        } else {
            file = new File("Languages/English.txt");
        }
    }

    public static String getText(String identifier) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(Language.file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String s;
        do {
            assert scanner != null;
            s = scanner.nextLine();
        } while (!s.split("=")[0].trim().equals(identifier.trim()));
        s = s.split("=")[1];
        return s.substring(1).trim();
    }

}
