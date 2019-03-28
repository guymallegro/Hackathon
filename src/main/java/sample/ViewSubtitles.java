package sample;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import javafx.scene.control.TextArea;
import net.sourceforge.javaflacencoder.FLACFileWriter;

public class ViewSubtitles {
    public TextArea shownText;
    private GSpeechDuplex duplex;
    private Microphone mic;

    public void initialize(){
        shownText.setStyle("-fx-font-size: 50 Thaoma; -fx-font-weight: bold");
        mic = new Microphone(FLACFileWriter.FLAC);
        duplex = new GSpeechDuplex(Main.GOOGLE_KEY);
        duplex.setLanguage("he");
        duplex.addResponseListener(new GSpeechResponseListener() {
            public void onResponse(GoogleResponse gr) {
                try {
                    String output = "";
                    output = gr.getResponse();
                    if (gr.getResponse() == null) {
                        return;
                    }
                    shownText.setText(output);
                }catch (Exception e){

                }
            }
        });
        new Thread(() -> {
            try {
                duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}
