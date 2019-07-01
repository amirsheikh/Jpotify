import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;

public class resultSearchListPerform implements ItemListener , Serializable {
    private List list;
    private PlayList playList;

    public resultSearchListPerform(List list,PlayList playList){
        this.playList = playList;
        this.list = list;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        String title = list.getSelectedItem();
        Music musicSlected = playList.GetMusic(title);
        /// coding !!!!
    }
}
