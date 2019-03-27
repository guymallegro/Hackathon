package sample;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import net.sourceforge.javaflacencoder.FLACFileWriter;

public class Main extends Application {
    private final String GOOGLE_KEY="AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw";
    public TextArea response;
    private GSpeechDuplex duplex;
    private Microphone mic;
    public Button stop;
    public Button start;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hackathon!");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void Start(ActionEvent actionEvent) {
        mic = new Microphone(FLACFileWriter.FLAC);
        duplex = new GSpeechDuplex(GOOGLE_KEY);
        duplex.setLanguage("en");
        duplex.addResponseListener(new GSpeechResponseListener() {
            public void onResponse(GoogleResponse gr) {
                String output="";
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
        start.setDisable(true);
        stop.setDisable(false);
    }

    public void Stop(ActionEvent actionEvent) {
        mic.close();
        duplex.stopSpeechRecognition();
        start.setDisable(false);
        stop.setDisable(true);
    }
}
