import javax.swing.*;
import java.util.Random;

public class PlayListMusicPlayer implements Runnable{

    private CustomMusicPlayer playMp3;
    private PlayList playList;
    private boolean shuffling;
    private boolean innweShuffling=false;
    private boolean repeating;
    private boolean innerRepeating=false;
    private boolean endOfPlayList;
    private int positionInPlayList;
    private boolean threadMade;
    private JButton mainPlayButton;

    public PlayListMusicPlayer(CustomMusicPlayer playMp3, PlayList playList, JButton mainPlayButton) {
        this.playMp3 = playMp3;
        this.playList = playList;
        shuffling = false;
        positionInPlayList = 1;
        endOfPlayList = false;
        threadMade = false;
        this.mainPlayButton = mainPlayButton;
    }

    public void changePlayList(PlayList newPlayList){
        System.out.println(newPlayList.getTitle());
        playList = newPlayList;
        positionInPlayList = 0;
        endOfPlayList = false;
    }

    public void setPositionInPlayList(Music music){
        positionInPlayList = playList.PlayList.indexOf(music)+1;
        playMp3.ChangeMusic(music);
        endOfPlayList = false;
    }

    public boolean isThreadMade() {
        return threadMade;
    }

    public void run(){
        threadMade = true;
        while (true) {
            try {
                playMp3.play(0, Integer.MAX_VALUE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            nextMusic();
                while (endOfPlayList) {
                    mainPlayButton.setIcon(new ImageIcon(getClass().getResource("/Logo/play-white.png")));
                }

        }

    }

    public void nextMusic(){
        if(shuffling){
            Music music = playList.GetStaffMusic();
            setPositionInPlayList(music);
            playMp3.ChangeMusic(music);
        }
        else if (repeating){
            Music music = playMp3.getMusic();
            playMp3.ChangeMusic(music);
        }
        else{
            if (positionInPlayList < playList.PlayList.size()-1) {
                playMp3.ChangeMusic(playList.PlayList.get(positionInPlayList));
                positionInPlayList++;
            }

            else if(positionInPlayList != playList.PlayList.size()){
                playMp3.ChangeMusic(playList.PlayList.get(positionInPlayList));
                endOfPlayList=true;
            }
            else
                endOfPlayList=true;
            if(playList.PlayList.size()==1 && positionInPlayList==0){
                endOfPlayList=false;
            }
        }


        }

    public int getPositionInPlayList() {
        return positionInPlayList;
    }

    public boolean isShuffling() {
        System.out.println(innerRepeating);
        return innweShuffling;
    }

    public boolean isRepeating() {
        return innerRepeating;
    }

    public void previousMusic(){
        if(shuffling){
            Music music = playList.GetStaffMusic();
            setPositionInPlayList(music);
            playMp3.ChangeMusic(music);
        }
        else{
            if (positionInPlayList > 1) {
                positionInPlayList=positionInPlayList-2;
                playMp3.ChangeMusic(playList.PlayList.get(positionInPlayList));
                positionInPlayList++;
            }
            else if(positionInPlayList == 0){
                playMp3.ChangeMusic(playList.PlayList.get(positionInPlayList));
            }
        }


    }


    public void setShuffling(boolean shuffling) {
        if(repeating && shuffling){
            repeating = false;
        }
        this.shuffling = shuffling;
        innweShuffling = shuffling;
    }

    public void setRepeating(boolean repeating) {
        if (shuffling && repeating){
            shuffling = false;
    }
        this.repeating = repeating;
        innerRepeating = repeating;
    }
}
