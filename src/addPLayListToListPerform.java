import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.HashMap;

public class addPLayListToListPerform implements ActionListener , Serializable {
    private List list;
    private TextField textField;
    private HashMap<String,PlayList> titleToPlayList;
    private GUI gui;

    public addPLayListToListPerform(List list ,TextField textField,HashMap<String,PlayList> titlePlayList, GUI gui){
        this.list = list;
        this.textField = textField;
        this.titleToPlayList = titlePlayList;
        this.gui = gui;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        list.add(textField.getText());
        PlayList tmp = new PlayList(textField.getText(),true );
        titleToPlayList.put(textField.getText(),tmp);
    }
}
