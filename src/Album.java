import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album extends PlayList implements addAndDelete , Serializable {
    private String artist;
    private int year;

    public Album(String artist, int year, String title, boolean canChangeTheName , JPanel panel, GUI gui){
        super(title,canChangeTheName);
        this.artist = artist;
        this.year = year;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public void Add(Music music) {
        String buffer = "";
        PlayList.add(music);
        TitleToMusic.put(music.getTitle(),music);
        Size++;
        for(int i=0;i<music.getTitle().length();i++){
            buffer = buffer + music.getTitle().charAt(i);
            List<String> Tmp = TitleToMusics.get(buffer);
            if(Tmp==null) {
                Tmp = new ArrayList<>();
                Tmp.add(music.getTitle());
            }
            else
                Tmp.add(music.getTitle());
            TitleToMusics.put(buffer, Tmp);
        }
    }

    @Override
    public void Delete(Music music) {
        Size--;
        if (TitleToMusics.get(music.getTitle()) != null) {
            String buffer = music.getTitle().charAt(0) + "";
            TitleToMusic.remove(music.getTitle());
            for (int i = 1; i < music.getTitle().length(); i++) {
                List<String> tmp = TitleToMusics.get(buffer);
                buffer = buffer + music.getTitle().charAt(i);
                if (tmp != null)
                    tmp.remove(music.getTitle());
                TitleToMusics.put(buffer, tmp);
            }
            List<String> tmp = TitleToMusics.get(buffer);
            if (tmp != null)
                tmp.remove(music.getTitle());
            TitleToMusics.put(buffer, tmp);
        }
        if(PlayList.get(PlayList.size()-1) == music)
            ImagePathOfPlayList = PlayList.get(PlayList.size()-2).getImage();
        PlayList.remove(music);
    }

    public int getYear() {
        return year;
    }
}
