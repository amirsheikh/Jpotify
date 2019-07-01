import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;
import java.util.HashMap;

public class playListListPreform implements ItemListener , Serializable {
    private List list;
    private HashMap<String,PlayList> titleToPlayList;
    public playListListPreform(List list, HashMap<String,PlayList> titlePlayList){
        this.list = list;
        titleToPlayList = titlePlayList;
    }
    @Override
    public void itemStateChanged(ItemEvent e) {
        String title = list.getSelectedItem();
        list.remove(title);
        list.add(title);
        PlayList tmp = titleToPlayList.get(title);
        //coding
    }
}
