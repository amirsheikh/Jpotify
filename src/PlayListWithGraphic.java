import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayListWithGraphic {
    private PlayList playList;
    private PlayListForm playListForm;
    private GUI gui;

    public PlayListWithGraphic(PlayList playList, PlayListForm playListForm, GUI gui) {
        this.playList = playList;
        this.playListForm = playListForm;
        this.gui = gui;
        playListForm = new PlayListForm(gui , playList);
        makeGraphic();
    }


    private void makeGraphic(){


        try {
            playListForm.getNameButton().setText(playList.getTitle());
            playListForm.getNameButton().addMouseListener(nameButtonAction(playListForm.getNameButton() , playList));

            playListForm.getAddIcon().setIcon(new ImageIcon(getClass().getResource("/Logo/circleAddOutline - white.png")));
//            playListForm.getAddIcon().addMouseListener(addToPlayList(playListForm.getAddIcon() , this));


        }catch (Exception e){
            System.out.println(e);
        }
        optionsListeners(this);
    }

    public JPanel getPanel() {
        return playListForm.getMainPanel();
    }

    private MouseAdapter nameButtonAction(JButton button, PlayList playList){
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                button.setContentAreaFilled(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                button.setContentAreaFilled(false);
                gui.displayListOfMusics(playList);

            }
        };
    }


    private void optionsListeners(PlayListWithGraphic playListWithGraphic) {
        playListForm.getOptionButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                playListForm.getOptionButton().setIcon(new ImageIcon(getClass().getResource("/Logo/option-black.png")));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                playListForm.getOptionButton().setIcon(new ImageIcon(getClass().getResource("/Logo/option-white.png")));
                JFrame frame = new JFrame("choose option");
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
                frame.setContentPane((new PlayListOption(gui, playListWithGraphic, frame)).getMainPanel());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                playListForm.getOptionButton().setIcon(new ImageIcon(getClass().getResource("/Logo/option-green.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                playListForm.getOptionButton().setIcon(new ImageIcon(getClass().getResource("/Logo/option-white.png")));
            }
        });
    }


    public PlayList getPlayList() {
        return playList;
    }

    public PlayListForm getPlayListForm() {
        return playListForm;
    }
}
