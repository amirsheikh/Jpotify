import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class PlayListCheckBox {

    private PlayList allMusics;
    private PlayList playList;
    private ArrayList<MyCheckBox> listOfCheckBox;
    private JPanel mainPanel;
    private JScrollPane scrollPanel;
    private JPanel checkBoxPanel;
    private JButton oK;
    private GridBagConstraints constraints;

    public PlayListCheckBox(PlayList playList, PlayList allMusics, JFrame frame) {
        $$$setupUI$$$();
        listOfCheckBox = new ArrayList<MyCheckBox>();
        this.allMusics = allMusics;
        this.playList = playList;
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.gridx = 0;
        makeCheckBoxList();
        oK.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                oK.setBackground(new Color(-14145496));
//                playList.PlayList.clear();
                for (MyCheckBox checkBox : listOfCheckBox) {
                    if (checkBox.isSelected())
                        playList.Add(checkBox.music);
                    else {
                        if (playList.PlayList.contains(checkBox.music))
                            playList.Delete(checkBox.music);
                    }
                }

                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void makeCheckBoxList() {

        constraints.gridy = 0;
        MyCheckBox tmpCheckBox;
        for (Music musicInAll : allMusics.PlayList) {
            tmpCheckBox = new MyCheckBox(musicInAll);
            tmpCheckBox.setBackground(new Color(-14145496));
            tmpCheckBox.setForeground(new Color(-855310));
            tmpCheckBox.setText(musicInAll.getTitle());
            tmpCheckBox.setFocusPainted(false);
            tmpCheckBox.setBorderPainted(false);
            for (Music musicInPlaylist : playList.PlayList) {
                if (musicInPlaylist.getTitle().equals(musicInAll.getTitle())) {
                    tmpCheckBox.setSelected(true);
                    break;
                }
            }
            listOfCheckBox.add(tmpCheckBox);
            constraints.gridy++;
            constraints.gridx = 0;
            checkBoxPanel.add(tmpCheckBox, constraints);
            checkBoxPanel.revalidate();
        }
        constraints.gridy = 0;

    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setBackground(new Color(-14145496));
        final JLabel label1 = new JLabel();
        label1.setBackground(new Color(-14145496));
        Font label1Font = this.$$$getFont$$$("Droid Sans Mono Dotted", Font.BOLD, 14, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-1));
        label1.setText("Check Musics to Add to the PlayList");
        mainPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrollPanel = new JScrollPane();
        scrollPanel.setBackground(new Color(-14145496));
        mainPanel.add(scrollPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(400, -1), new Dimension(400, -1), new Dimension(400, -1), 0, false));
        checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new GridBagLayout());
        checkBoxPanel.setBackground(new Color(-14145496));
        scrollPanel.setViewportView(checkBoxPanel);
        oK = new JButton();
        oK.setBackground(new Color(-14145496));
        oK.setBorderPainted(false);
        oK.setContentAreaFilled(true);
        oK.setFocusPainted(false);
        oK.setForeground(new Color(-855310));
        oK.setText("OK");
        mainPanel.add(oK, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
class MyCheckBox extends JCheckBox{
    Music music;

    public MyCheckBox(Music music){
        this.music = music;
    }
}