package sample;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.sourceforge.javaflacencoder.FLACFileWriter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class Main extends Application {
    public final static String GOOGLE_KEY = "AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw";
    public TextArea response;
    public ImageView listening;
    public ImageView taptospeak;
    public Button stopbutton;
    private GSpeechDuplex duplex;
    public ImageView play;
    public ImageView pause;
    public ImageView stop;
    private Microphone mic;
    HashMap<String,Integer> wordsFreq;
    static HashMap<String,Double> wordDistribution;
    public ComboBox lang;
    public Button buttonstatistic;
    Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("style.css");
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hackathon!");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void initialize (){
        lang.getItems().add("English");
        lang.getItems().add("Hebrew");
        lang.getItems().add("Arabic");


    }


    public void Listen(MouseEvent actionEvent) {
        Parent subtitlesWindow;

        try {
            subtitlesWindow = FXMLLoader.load(getClass().getClassLoader().getResource("subtitles.fxml"));
            stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(primaryScreenBounds.getMinX());
            stage.setY(primaryScreenBounds.getMinY());
            stage.setWidth(primaryScreenBounds.getWidth());
            stage.setHeight(primaryScreenBounds.getHeight()/10);
            play.setVisible(false);
            listening.setVisible(true);
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(subtitlesWindow, 450, 450));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Statistics(){

        wordsFreq=ViewSubtitles.getDictionary();
        wordDistribution=new HashMap<String,Double> ();
        int NumOfWords=0;

        for (String word:
                wordsFreq.keySet()) {

            NumOfWords=NumOfWords+wordsFreq.get(word);
        }

        List<String> keys = new ArrayList(wordsFreq.keySet());
        Collections.sort(keys, (o2, o1) -> (int) (new Integer(wordsFreq.get(o1).compareTo(wordsFreq.get(o2)))));

        for(int i=0;i<Math.min(10,keys.size());i++){
            double x= (double)wordsFreq.get(keys.get(i))/NumOfWords;
            x=x*100;
            x =Double.parseDouble(new DecimalFormat("##.##").format(x));

            wordDistribution.put(keys.get(i),new Double(x));
        }


        try {
            Stage startWindow;
            startWindow = new Stage();
            startWindow.initModality(Modality.APPLICATION_MODAL);
            startWindow.setTitle("Welcome");

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("statistics.fxml"));
            Parent root2 = loader.load();
            Scene scene2 = new Scene(root2, 450, 450);
            startWindow.setScene(scene2);
            startWindow.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static HashMap<String,Double> getDistribution(){return wordDistribution; };

    public void Stop() {
        stage.close();
        listening.setVisible(false);
        play.setVisible(true);
        //buttonstatistic.setDisable(false);
//        duplex.stopSpeechRecognition();
    }
}
