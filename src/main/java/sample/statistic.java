package sample;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class statistic {

    public Label Word1;
    public Label Word2;
    public Label Word3;
    public Label Word4;
    public Label Word5;
    public Label Word6;
    public Label Word7;
    public Label Word8;
    public Label Word9;
    public Label Word10;
    public Label MostCommon;


    public void initialize (){


        HashMap<String,Double> dist=Main.getDistribution();
        List<String> keys = new ArrayList(dist.keySet());
        Collections.sort(keys, (o1, o2) -> (int) (new Integer(dist.get(o1).compareTo(dist.get(o2)))));

        MostCommon.setText(keys.get(0)+" "+ dist.get(keys.get(0)));
        Word1.setText(keys.get(1)+" "+ dist.get(keys.get(1)));
        Word2.setText(keys.get(2)+" "+ dist.get(keys.get(2)));
        Word3.setText(keys.get(3)+" "+ dist.get(keys.get(3)));
        Word4.setText(keys.get(4)+" "+ dist.get(keys.get(4)));
        Word5.setText(keys.get(5)+" "+ dist.get(keys.get(5)));
        Word6.setText(keys.get(6)+" "+ dist.get(keys.get(6)));
        Word7.setText(keys.get(7)+" "+ dist.get(keys.get(7)));
        Word8.setText(keys.get(8)+" "+ dist.get(keys.get(8)));
        Word9.setText(keys.get(9)+" "+ dist.get(keys.get(9)));





    }

}
