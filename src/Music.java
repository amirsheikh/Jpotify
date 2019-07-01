import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Character.toLowerCase;


public class Music implements Serializable {
    private String Title;
    private String Path;
    private String Artist;
    private byte[] imageData;
    private int size;
    private String ownerIP;
    private int ownerId;
    private Boolean onSharedList;
    private transient ArrayList<PlayList> playLists;


    public Music(String path, Owner owner , ArrayList<PlayList> playLists){
        onSharedList = false;
        this.Path=path;
        this.Artist="Unknown";
        this.Title = "Unknown";
        imageData = null;
        size = new CustomMusicPlayer(this,owner ,null).size();
        this.playLists = playLists;
        setMetaData();
        this.ownerIP = owner.getIp();
        this.ownerId = owner.getId();

    }

    public ArrayList<PlayList> getPlayLists() {
        return playLists;
    }

    public static List<String> getSongLyrics(String band, String songTitle) throws IOException {
        String songLyricsURL = "http://www.songlyrics.com";
        List<String> lyrics= new ArrayList<String>();
        String url = songLyricsURL+ "/"+band.replace(" ", "-").toLowerCase()+"/"+songTitle.replace(" ", "-").toLowerCase()+"-lyrics/";
        System.out.println(url);
//        Connection connection = Jsoup.connect(url);
//        connection.userAgent("Mozilla/5.0");
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://www.google.com")
                .get(); ;//= Jsoup.connect(url).get();
        String title = doc.title();
        System.out.println(title);
        Element p = doc.select("p.songLyricsV14").get(0);
        for (Node e: p.childNodes()) {
            if (e instanceof TextNode) {
                lyrics.add(((TextNode)e).getWholeText());
            }
        }
        return lyrics;
    }



    boolean isInFavorite(ArrayList<PlayList> playLists, Music thisMusic){
        for(PlayList playList : playLists)
            if (playList.getTitle().equals("favorite"))
                for(Music music : playList.getPlayList())
                    if(music.Title.equals(thisMusic.Title)  &&  music.Artist.equals((thisMusic.Artist)))
                        return true;
         return false;
    }
    public String getOwnerIP() {
        return ownerIP;
    }

    public void ChangeDirectory(String Path){
        this.Path = Path;
    }

    public String getArtist() {
        return Artist;
    }

    public String getPath() {
        return Path;
    }

    public String getTitle() {
        return Title;
    }

    public byte[] getImage(){
        return imageData;
    }

    public int getSize() {
        return size;
    }

    private void setMetaData(){
        if(this.Path.equals("NotDownloadedYet"))
            return;
        try {
            int count =0;
            int charAt;
            FileReader input = new FileReader(new File(getPath()));
            while(input.read() != -1){
                count++;
            }
            input = new FileReader(new File(getPath()));
            input.skip(count-128);
            if(Character.isLetter(input.read())) {
                Title = "";
                input.skip(2);
                count = 30;
                while ((charAt = input.read()) != -1 && (count--) != 0) {
                    Title = Title + toLowerCase((char) charAt);
                }
                Artist = toLowerCase((char) charAt) + "";
                count = 29;
                while ((charAt = input.read()) != -1 && (count--) != 0) {
                    Artist = Artist + toLowerCase((char) charAt);
                }
            }

            Mp3File song = new Mp3File(this.getPath());
            if (song.hasId3v2Tag()){
                ID3v2 id3v2tag = song.getId3v2Tag();
                this.imageData = id3v2tag.getAlbumImage();


            }
            System.out.println(Title);
            System.out.println(Artist);
            System.out.println();
        }catch (Exception e){
        }
    }

    public void setPlayLists(ArrayList<PlayList> playLists) {
        this.playLists = playLists;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public Boolean isOnSharedMusic(){
        return onSharedList;
    }

    public void setOnSharedList(Boolean onSharedList) {
        this.onSharedList = onSharedList;
    }

    public byte[] getImageData() {
        return imageData;
    }
}
