package sample;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.*;
import java.text.DecimalFormat;
import static sun.awt.FontConfiguration.verbose;
import java.net.ServerSocket;

public class Main extends Application {
    public final static String GOOGLE_KEY = "AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw";
    private final static int PORT = 1978;
    public ImageView listening;
    HashMap<String, Integer> wordsFreq;
    static HashMap<String, Double> wordDistribution;
    public ImageView play;
    public ImageView pause;
    public ImageView stop;
    public ComboBox lang;
    Stage stage;
    public boolean selected = false;


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

    public void initialize() {
        lang.getItems().add("English");
        lang.getItems().add("Hebrew");
        lang.getItems().add("Arabic");
        new Thread(this::startServer).start();
    }

    private void startServer() {
        try {
            ServerSocket serverConnect = new ServerSocket(PORT);
            System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");

            // we listen until user halts server execution
            while (true) {
                JavaHTTPServer myServer = new JavaHTTPServer(serverConnect.accept());

                if (verbose) {
                    System.out.println("Connecton opened. (" + new Date() + ")");
                }

                // create dedicated thread to manage the client connection
                Thread thread = new Thread(myServer);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }
    }


    public void Listen(MouseEvent actionEvent) {
        if (!selected){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("please choose language before");
            alert.show();
            return;
        }
        Parent subtitlesWindow;
        try {
            subtitlesWindow = FXMLLoader.load(getClass().getClassLoader().getResource("subtitles.fxml"));
            stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            //Controller.setLang(lang.getValue().toString());
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(primaryScreenBounds.getMinX());
            stage.setY(primaryScreenBounds.getMinY());
            stage.setWidth(primaryScreenBounds.getWidth());
            stage.setHeight(primaryScreenBounds.getHeight() / 10);
            play.setVisible(false);
            listening.setVisible(true);
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(subtitlesWindow, 450, 450));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Statistics() {

        wordsFreq = ViewSubtitles.getDictionary();
        wordDistribution = new HashMap<String, Double>();
        int NumOfWords = 0;

        for (String word :
                wordsFreq.keySet()) {

            NumOfWords = NumOfWords + wordsFreq.get(word);
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
            Scene scene2 = new Scene(root2, 500, 650);
            startWindow.setScene(scene2);
            startWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Double> getDistribution() {
        return wordDistribution;
    }

    public void Stop() {
        stage.close();
        play.setVisible(true);
        listening.setVisible(false);
        play.setDisable(false);
    }

    public void setLanguage(ActionEvent actionEvent) {
        Controller.setLang(lang.getValue().toString());
        selected = true;
        pause.setDisable(false);
        stop.setDisable(false);
    }
}
