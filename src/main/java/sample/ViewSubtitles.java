package sample;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import javafx.scene.control.TextArea;
import net.sourceforge.javaflacencoder.FLACFileWriter;

import java.io.*;
import java.util.HashMap;

public class ViewSubtitles {
    public TextArea shownText;
    private GSpeechDuplex duplex;
    private Microphone mic;
    static HashMap<String, Integer> wordsFreq;

    public void initialize() {
        wordsFreq = new HashMap<String, Integer>();
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
                    addToHtml(shownText.getText());
                    String[] words = output.split(" ");
                    for (String w :
                            words) {
                        if (wordsFreq.containsKey(w)) {
                            int num = wordsFreq.get(w);
                            wordsFreq.remove(w);
                            wordsFreq.put(w, num + 1);
                        } else
                            wordsFreq.put(w, 1);
                    }

                } catch (Exception e) {

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

    public static HashMap<String, Integer> getDictionary() {
        return wordsFreq;
    }

    private void addToHtml(String text) throws IOException {
        String utfString = convertToUTF8(text);
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"he\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <meta http-equiv=\"refresh\" content=\"1\"/>\n" +
                "</head>\n" +
                "\n";
        PrintWriter pw = new PrintWriter("index.html");
        pw.close();
        OutputStream os = new FileOutputStream(new File("index.html"), true);
        os.write((html + utfString).getBytes(), 0, ((html + utfString)).length());
        os.close();
    }

    private static String convertToUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }
}
