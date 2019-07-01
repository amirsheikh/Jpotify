import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MusicWithGraphic {
    private Music music;
    private transient MusicForm musicForm;
    private CustomMusicPlayer playMP3;
    private PlayListMusicPlayer playListPlayer;
    private PlayList displayedPlaylist;
    private JButton mainPlayButton;
    private Boolean isPlaying;


    public MusicWithGraphic(Music music, MusicForm musicForm, CustomMusicPlayer playMP3,
                            PlayListMusicPlayer playListPlayer, PlayList displayedPlaylist, JButton mainPlayButton, Boolean isPlaying) {
        this.music = music;
        this.musicForm = musicForm;
        this.playMP3 = playMP3;
        this.playListPlayer = playListPlayer;
        musicForm = new MusicForm(music.getPath());
        this.displayedPlaylist = displayedPlaylist;
        this.mainPlayButton = mainPlayButton;
        this.isPlaying = isPlaying;
        makeGraphic(music.getPlayLists());
    }
    public void makeGraphic(ArrayList<PlayList> playLists){


        musicForm.getDetailsLabel().setText(music.getTitle() + " - " + music.getArtist());
        musicForm.getLikeButton().setIcon(new ImageIcon(getClass().getResource("/Logo/favoriteOutline - red.png")));
        musicForm.getLikeButton().addMouseListener(likeAction(musicForm.getLikeButton(), playLists, music));

        musicForm.getPlayButton().setIcon(new ImageIcon(getClass().getResource("/Logo/play-white.png")));
        musicForm.getPlayButton().addMouseListener(playAction(musicForm.getPlayButton(), playLists, music));

        musicForm.getLyricsButton().setIcon(new ImageIcon(getClass().getResource("/Logo/lyrics-white.png")));
        musicForm.getLyricsButton().addMouseListener(lyricsAction(musicForm.getLyricsButton(), playLists, music));
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(music.getImageData()));
            BufferedImage resizedImg = new BufferedImage(160, 160, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(img, 0, 0, 160, 160, null);
            g2.dispose();
            musicForm.getImageLabel().setIcon(new ImageIcon(resizedImg));
            musicForm.getMainPanel().revalidate();
        }catch (IOException e){
            e.printStackTrace();
        }



    }



    private MouseAdapter lyricsAction(JButton button, ArrayList<PlayList> playLists, Music thisMusic){
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                musicForm.getLyricsButton().setIcon(new ImageIcon(getClass().getResource("/Logo/lyrics-black.png")));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                musicForm.getLyricsButton().setIcon(new ImageIcon(getClass().getResource("/Logo/lyrics-white.png")));
                try {
                    System.out.println(music.getSongLyrics("U2", "With or Without You"));
                }catch (IOException e2){
                    e2.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                musicForm.getLyricsButton().setIcon(new ImageIcon(getClass().getResource("/Logo/lyrics-green.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                musicForm.getLyricsButton().setIcon(new ImageIcon(getClass().getResource("/Logo/lyrics-white.png")));
            }
        };
    }

    private MouseAdapter playAction(JButton button, ArrayList<PlayList> playLists, Music thisMusic){
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                button.setIcon(new ImageIcon(getClass().getResource("/Logo/play-black.png")));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                button.setIcon(new ImageIcon(getClass().getResource("/Logo/play-white.png")));

                playListPlayer.changePlayList(displayedPlaylist);
                playListPlayer.setPositionInPlayList(thisMusic);
                if (!playListPlayer.isThreadMade()) {
                    new Thread(playListPlayer).start();
                    isPlaying = true;
                }

                else
                    if(isPlaying)
                        playMP3.SetPosition(0);
                isPlaying = true;
                mainPlayButton.setIcon(new ImageIcon(getClass().getResource("/Logo/pause-white.png")));


            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                button.setIcon(new ImageIcon(getClass().getResource("/Logo/play-green.png")));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                button.setIcon(new ImageIcon(getClass().getResource("/Logo/play-white.png")));
            }
        };
    }



    private MouseAdapter likeAction(JButton button, ArrayList<PlayList> playLists, Music thisMusic){
        return new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (music.isInFavorite(playLists, thisMusic))
                    button.setIcon(new ImageIcon(getClass().getResource("/Logo/favorite - red.png")));
                else
                    button.setIcon(new ImageIcon(getClass().getResource("/Logo/favoriteOutline - red.png")));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (music.isInFavorite(playLists, thisMusic))
                    button.setIcon(new ImageIcon(getClass().getResource("/Logo/favoriteOutline - white.png")));
                else
                    button.setIcon(new ImageIcon(getClass().getResource("/Logo/favorite - white.png")));

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (music.isInFavorite(playLists, thisMusic)) {
                    button.setIcon(new ImageIcon(getClass().getResource("/Logo/favoriteOutline - red.png")));
                    for (PlayList playList : playLists)
                        if(playList.getTitle().equals("favorite")) {
                            playList.PlayList.remove(thisMusic);
                            break;
                        }
                }
                else{
                    button.setIcon(new ImageIcon(getClass().getResource("/Logo/favorite - red.png")));
                    for (PlayList playList : playLists)
                        if(playList.getTitle().equals("favorite")) {
                            playList.Add(thisMusic);
                            break;
                        }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (music.isInFavorite(playLists, thisMusic))
                    button.setIcon(new ImageIcon(getClass().getResource("/Logo/favorite - black.png")));
                else
                    button.setIcon(new ImageIcon(getClass().getResource("/Logo/favoriteOutline - green.png")));

            }
        };
    }

    public JPanel getMusicPanel() {
        return musicForm.getMainPanel();
    }


}
