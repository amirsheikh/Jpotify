import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PlayList implements addAndDelete , Serializable{
    List<Music> PlayList;
    HashMap<String,List<String> > TitleToMusics ;
    HashMap<String,Music> TitleToMusic ;
    private String Title;
    byte[] ImagePathOfPlayList;
    int Size;
    private boolean CanChangeName;

    public String getTitle() {
        return Title;
    }


    public PlayList(String Title, boolean canChangeName){
        PlayList = new ArrayList<Music>();
        TitleToMusics = new HashMap<>();
        this.Title = Title;
        ImagePathOfPlayList = null;
        Size =0;
        this.CanChangeName = canChangeName;
        TitleToMusic = new HashMap<>();


    }




    @Override
    public void Add(Music music){

        HashMap<String,Boolean> tmpHash = new HashMap<>();
        PlayList.add(music);
        TitleToMusic.put(music.getTitle(),music);
        Size++;
        String buffer = "";
        for(int i=0;i<music.getTitle().length();i++){
            buffer = buffer + music.getTitle().charAt(i);
            tmpHash.put(buffer,true);
            List<String> tmp = TitleToMusics.get(buffer);
            if(tmp!=null)
                tmp.add(music.getTitle());
            else{
                tmp = new ArrayList<>();
                tmp.add(music.getTitle());
            }
            TitleToMusics.put(buffer, tmp);
        }
        buffer = "";
        for(int i=0;i<music.getArtist().length();i++){
            buffer = buffer + music.getArtist().charAt(i);
            if(tmpHash.get(buffer) == null) {
                List<String> tmp = TitleToMusics.get(buffer);
                if (tmp == null) {
                    tmp = new ArrayList<>();
                    tmp.add(music.getTitle());
                } else
                    tmp.add(music.getTitle());

                TitleToMusics.put(buffer, tmp);
            }
        }
        ImagePathOfPlayList = music.getImage();
    }

    @Override
    public void Delete(Music music){
        Size--;
        if(TitleToMusics.get(music.getTitle()) != null){
            TitleToMusic.remove(music.getTitle());
            String buffer = "";
            for(int i=0;i<music.getTitle().length();i++){
                buffer = buffer + music.getTitle().charAt(i);
                List<String> tmp = TitleToMusics.get(buffer);
                if(tmp!=null)
                    tmp.remove(music.getTitle());
                TitleToMusics.put(buffer, tmp);
            }
            for(int i=0;i<music.getArtist().length();i++){
                buffer = buffer + music.getArtist().charAt(i);
                List<String> tmp = TitleToMusics.get(buffer);
                if(tmp!=null)
                    tmp.remove(music.getTitle());
                TitleToMusics.put(buffer, tmp);
            }
            if(PlayList.get(PlayList.size()-1) == music)
                ImagePathOfPlayList = PlayList.get(PlayList.size()-2).getImage();
            PlayList.remove(music);
        }
    }





    public List<Music> getPlayList() {
        return PlayList;
    }

    public List<String> stringGetMusic(String title){
        return TitleToMusics.get(title);
    }

    public Music GetMusic(int num){
        return PlayList.get(num);
    }
    public Music GetMusic(String title){
        return TitleToMusic.get(title);
    }

    public Music GetStaffMusic(){
        return PlayList.get(new Random().nextInt(PlayList.size()));
    }

    public int getSizeOfPlayList() {
        return Size;
    }
    public void ChangeTitle(String title){
        if(CanChangeName)
            this.Title = title;
    }
    public boolean isCanChangeName(){
        return CanChangeName;
    }

}
