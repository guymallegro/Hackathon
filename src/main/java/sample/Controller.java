package sample;

public class Controller {

    private static String lang;

    public static String getLang (){
        return lang;
    }

    public static void setLang (String language){
        switch (language){
            case "English":
                lang = "en";
            case "Hebrew":
                lang = "he";
            case "Arabic":
                lang = "ar";
        }

    }
}
