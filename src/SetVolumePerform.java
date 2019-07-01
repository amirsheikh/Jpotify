import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class SetVolumePerform implements ActionListener , Serializable {
    JSlider volumeValue;

    public SetVolumePerform(JSlider volumeValue){
        this.volumeValue=volumeValue;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Mixer.Info [] mixers = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : mixers)
        {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info [] lineInfos = mixer.getTargetLineInfo(); // target, not source
            for (Line.Info lineInfo : lineInfos)
            {
                Line line = null;
                boolean opened = true;
                try
                {
                    line = mixer.getLine(lineInfo);
                    opened = line.isOpen() || line instanceof Clip;
                    if (!opened)
                    {
                        line.open();
                    }
                    FloatControl volCtrl = (FloatControl)line.getControl(FloatControl.Type.VOLUME);
                    volCtrl.setValue(volumeValue.getValue());
                }
                catch (LineUnavailableException b)
                {
                    break;
                }
                catch (IllegalArgumentException iaEx)
                {
                    break;
                }
            }
        }

    }

    public static void setVolumeValue(float i) {

        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : mixers) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] lineInfos = mixer.getTargetLineInfo(); // target, not source
            for (Line.Info lineInfo : lineInfos) {
                Line line = null;
                boolean opened = true;
                try {
                    line = mixer.getLine(lineInfo);
                    opened = line.isOpen() || line instanceof Clip;
                    if (!opened) {
                        line.open();
                    }
                    FloatControl volCtrl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
                    volCtrl.setValue(i);
                } catch (LineUnavailableException b) {
                    break;
                } catch (IllegalArgumentException iaEx) {
                    break;
                }
            }

        }
    }
}
