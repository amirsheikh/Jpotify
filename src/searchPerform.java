import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.Serializable;

public class searchPerform implements ActionListener, Serializable {
    private PlayList playList;
    private List list;
    private TextField textField;


    public searchPerform(PlayList playList, List list, TextField textField) {
        this.playList = playList;
        this.list = list;
        this.textField = textField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //list.
        if (textField.getText() != "") {
            list.setVisible(true);
            list.removeAll();
            if (playList.stringGetMusic(textField.getText()) != null) {
                java.util.List<String> tmp = playList.stringGetMusic(textField.getText());
                    for (int i = 0; i < tmp.size(); i++) {
                    list.add(tmp.get(i));
                }
            } else {
                list.add("Dont find any match!");
            }
        } else
            list.setVisible(false);
    }


}
