package sample;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import javafx.scene.control.TextArea;
import net.sourceforge.javaflacencoder.FLACFileWriter;

import java.util.HashMap;

public class ViewSubtitles {
    public TextArea shownText;
    private GSpeechDuplex duplex;
    private Microphone mic;
    static HashMap<String,Integer> wordsFreq;

    public void initialize(){
        wordsFreq=new HashMap<String,Integer>();
        shownText.setStyle("-fx-font-size: 50 Thaoma; -fx-font-weight: bold");
        mic = new Microphone(FLACFileWriter.FLAC);
        duplex = new GSpeechDuplex(Main.GOOGLE_KEY);
        duplex.setLanguage("en");
        duplex.addResponseListener(new GSpeechResponseListener() {
            public void onResponse(GoogleResponse gr) {
                try {
                    String output = "";
                    output = gr.getResponse();
                    if (gr.getResponse() == null) {
                        return;
                    }

                    shownText.setText(output);
                    String [] words = output.split(" ");
                    for (String w :
                         words) {
                        if(wordsFreq.containsKey(w)){
                            int num = wordsFreq.get(w);
                            wordsFreq.remove(w);
                            wordsFreq.put(w,num+1);
                        }
                        else
                            wordsFreq.put(w,1);
                    }

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

public static HashMap<String,Integer> getDictionary(){
        return wordsFreq;
}

}
