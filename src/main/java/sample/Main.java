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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.sourceforge.javaflacencoder.FLACFileWriter;

import java.io.IOException;

public class Main extends Application {
    private final String GOOGLE_KEY = "AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw";
    public TextArea response;
    private GSpeechDuplex duplex;
    private Microphone mic;
    public ImageView taptospeak;
    public ImageView listening;
    public Button stopbutton;
    public ChoiceBox lang;
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hackathon!");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void Listen() {

        taptospeak.setVisible(false);
        listening.setVisible(true);
        stopbutton.setVisible(true);
        Parent subtitlesWindow;
        try {
            subtitlesWindow = FXMLLoader.load(getClass().getClassLoader().getResource("subtitles.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(primaryScreenBounds.getMinX());
            stage.setY(primaryScreenBounds.getMinY());
            stage.setWidth(primaryScreenBounds.getWidth());
            stage.setHeight(primaryScreenBounds.getHeight()/15);

            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(subtitlesWindow, 450, 450));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        mic = new Microphone(FLACFileWriter.FLAC);
        duplex = new GSpeechDuplex(GOOGLE_KEY);
        duplex.setLanguage("he");
        duplex.addResponseListener(new GSpeechResponseListener() {
            public void onResponse(GoogleResponse gr) {
                String output = "";
                output = gr.getResponse();
                if (gr.getResponse() == null) {
                    return;
                }
                response.setText("");
                response.setText(output);
            }
        });
        new Thread(() -> {
            try {
                duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
        taptospeak.setDisable(true);
        stopbutton.setDisable(false);
    }

    public void Stop(ActionEvent actionEvent) {
        mic.close();
        duplex.stopSpeechRecognition();
        taptospeak.setDisable(false);
        stopbutton.setDisable(true);
    }
}
