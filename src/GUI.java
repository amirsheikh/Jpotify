import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class GUI {
    private PlayList allMusics;
    private ArrayList<PlayList> playLists;
    private ArrayList<Album> albums;
    private Boolean isPlaying;
    private Owner owner;
    private Time spentTime;
    private Time totalTime;
    private CustomMusicPlayer playMP3;
    private PlayListMusicPlayer playListPlayer;

    private PlayList searchBarPlaylist;
    private boolean timeFlag;
    private boolean muted;
    private int lastSoundLevel;
    private PlayList displayedPlaylist;


    private JPanel mainPanel;
    private JPanel topBar;
    private JPanel PlayPanel;
    private JPanel AlbumPanel;
    private JPanel networkPanel;
    private JPanel songsPanel;
    private JSlider timLineSlider;
    private JButton play;
    private JPanel timeLine;
    private JPanel SoundPanel;
    private JPanel playingMusicName;
    private JPanel musicIcons;
    private JPanel playPanelButtons;
    private JButton next;
    private JButton previous;
    private JButton shuffleButton;
    private JButton repeatButton;
    private JSlider volumeSlider;
    private JButton volumeLogo;
    private JLabel activityLabel;
    private JPanel homePanel;
    private JLabel homeIcon;
    private JLabel HomeText;
    private JPanel browsePanel;
    private JLabel browseText;
    private JLabel browseIcon;
    private JPanel playListsPanel;
    private JScrollPane playLisrScroll;
    private JPanel playListScrollPanel;
    private JPanel AlbumsPanel;
    private JScrollPane albumsScroll;
    private JPanel albumsScrollPanel;
    private JButton addPlayListButton;
    private JButton addAlbumButton;
    private JLabel searchLogo;
    private JTextField searchBar;
    private JLabel spentTImeLabel;
    private JLabel totalTimeLabel;
    private JButton addFriendButton;
    private JLabel userName;
    private JPanel songsScrollPanel;
    private JScrollPane songsScrollPane;
    private JLabel displayedListLabel;
    private JPanel usersListPanel;
    private GridBagConstraints constraints;
    private JPanel Home;


    public GUI() {
        $$$setupUI$$$();
        isPlaying = new Boolean(false);
        spentTime = new Time();
        totalTime = new Time();
        allMusics = new PlayList("All", false);
        playLists = new ArrayList<PlayList>();
        albums = new ArrayList<Album>();
        owner = new Owner(2, playLists);
        searchBarPlaylist = new PlayList("Searched", false);
        timeFlag = true;
        muted = false;
        userName.setText(owner.getName() + "    " + owner.getIp());
        volumeSlider.setValue(70);
        SetVolumePerform.setVolumeValue((float) 0.7);
        displayedPlaylist = allMusics;


        play.addMouseListener(playAction());
        next.addMouseListener(nextAction());
        previous.addMouseListener(previousAction());
        timLineSlider.addMouseWheelListener(timeLineSliderWheel());
        timLineSlider.addMouseListener(timLineSliderAction());
        homeIcon.addMouseListener(homeAction());
        HomeText.addMouseListener(homeAction());
        browseIcon.addMouseListener(browseAction());
        browseText.addMouseListener(browseAction());
        addPlayListButton.addMouseListener(addPlayListButtonAction(this));
        addAlbumButton.addMouseListener(addAlbumButtonAction());
        playLists.add(new PlayList("favorite", false));
        playLists.add(new sharedPlayList(owner, this));
        allMusics.Add(new Music("C:\\Users\\asus\\Downloads\\Music\\tehran.mp3", owner, playLists));
        playMP3 = new CustomMusicPlayer(allMusics.GetMusic(0), owner, this);
        playListPlayer = new PlayListMusicPlayer(playMP3, allMusics, play);
//        allMusics.Add(new Music("F:\\1.mp3", owner, playLists));
        //       allMusics.Add(new Music("F:\\3.mp3", owner, playLists));
        //      allMusics.Add(new Music("F:\\4.mp3", owner, playLists));
        //      allMusics.Add(new Music("E:\\Musics\\fav from famous artist\\katy perry\\Katy Perry--Teenge Dream.mp3", owner, playLists));
        songsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        makeThreadForTimeLine();

        albumsScrollPanel.removeAll();
        albumsScrollPanel.repaint();
        albumsScrollPanel.revalidate();
        for (PlayList playList : playLists) {

            playListScrollPanel.add(new PlayListWithGraphic(playList, new PlayListForm(this, playList), this).getPanel());//playList.getPanel());
        }
        albumsScrollPanel.revalidate();
        displayListOfMusics(allMusics);

        homePanel.addMouseListener(new MouseAdapter() {
        });
//        searchBar.addActionListener(new searchPerform(allMusics, , searchBar));

        searchBar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchPerform();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchPerform();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchPerform();
            }
        });
        volumeSlider.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                if (notches < 0)
                    volumeSlider.setValue(volumeSlider.getValue() + 5);
                else if (notches > 0)
                    volumeSlider.setValue(volumeSlider.getValue() - 5);
                SetVolumePerform.setVolumeValue(((float) volumeSlider.getValue() / 100));
                if (muted)
                    volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/mute - white.png")));
                else if (volumeSlider.getValue() > 60)
                    volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/sound - white.png")));
                else if (volumeSlider.getValue() > 30)
                    volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/sound1 - white.png")));
                else if (volumeSlider.getValue() >= 0)
                    volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/sound2 - white.png")));

            }
        });
        volumeSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Point p = e.getPoint();
                double percent = p.x / ((double) volumeSlider.getWidth());
                int range = volumeSlider.getMaximum() - volumeSlider.getMinimum();
                double newVal = range * percent;
                int result = (int) (volumeSlider.getMinimum() + newVal);
                volumeSlider.setValue(result);
                SetVolumePerform.setVolumeValue((float) result / 100);
                if (muted)
                    volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/mute - white.png")));
                else if (volumeSlider.getValue() > 60)
                    volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/sound - white.png")));
                else if (volumeSlider.getValue() > 30)
                    volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/sound1 - white.png")));
                else if (volumeSlider.getValue() >= 0)
                    volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/sound2 - white.png")));
            }
        });
        volumeLogo.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (muted)
                    volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/mute - black.png")));
                else
                    volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/sound - black.png")));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (muted) {
                    volumeSlider.setValue(lastSoundLevel);
                    SetVolumePerform.setVolumeValue(((float) volumeSlider.getValue() / 100));
                    if (volumeSlider.getValue() > 60)
                        volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/sound - white.png")));
                    else if (volumeSlider.getValue() > 30)
                        volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/sound1 - white.png")));
                    else if (volumeSlider.getValue() >= 0)
                        volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/sound2 - white.png")));
                    muted = false;
                } else {
                    volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/mute - white.png")));
                    lastSoundLevel = volumeSlider.getValue();
                    SetVolumePerform.setVolumeValue(0);
                    volumeSlider.setValue(0);
                    muted = true;
                }


            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (muted)
                    volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/mute - green.png")));
                volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/sound - green.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (muted)
                    volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/mute - white.png")));
                else if (volumeSlider.getValue() > 60)
                    volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/sound - white.png")));
                else if (volumeSlider.getValue() > 30)
                    volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/sound1 - white.png")));
                else if (volumeSlider.getValue() >= 0)
                    volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/sound2 - white.png")));

            }
        });
        addFriendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                addFriendButton.setIcon(new ImageIcon(getClass().getResource("/Logo/add friend - black.png")));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                addFriendButton.setIcon(new ImageIcon(getClass().getResource("/Logo/add friend - white.png")));
                JFrame frame = new JFrame("addFriendForm");
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
                frame.setContentPane((new addFriendForm(owner, frame)).getMainPanel());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                addFriendButton.setIcon(new ImageIcon(getClass().getResource("/Logo/add friend - green.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                addFriendButton.setIcon(new ImageIcon(getClass().getResource("/Logo/add friend - white.png")));
            }
        });
        shuffleButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                shuffleButton.setIcon(new ImageIcon(getClass().getResource("/Logo/shuffle-black.png")));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (playListPlayer.isShuffling()) {
                    shuffleButton.setIcon(new ImageIcon(getClass().getResource("/Logo/shuffle-white.png")));
                    playListPlayer.setShuffling(false);
                } else {
                    shuffleButton.setIcon(new ImageIcon(getClass().getResource("/Logo/shuffle-green.png")));
                    playListPlayer.setShuffling(true);
                    System.out.println("fkdfdfj");
                }
            }
        });
        repeatButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                repeatButton.setIcon(new ImageIcon(getClass().getResource("/Logo/repeat-black.png")));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (playListPlayer.isRepeating()) {
                    repeatButton.setIcon(new ImageIcon(getClass().getResource("/Logo/repeat-white.png")));
                    playListPlayer.setRepeating(false);
                } else {
                    repeatButton.setIcon(new ImageIcon(getClass().getResource("/Logo/repeat-green.png")));
                    playListPlayer.setRepeating(true);
                }
            }
        });
    }

    private void searchPerform() {
        System.out.println("Hello There");
        searchBarPlaylist.PlayList.clear();
        if (!searchBar.getText().equals("")) {
            if (allMusics.stringGetMusic(searchBar.getText()) != null) {
                java.util.List<String> tmp = allMusics.stringGetMusic(searchBar.getText());
                for (int i = 0; i < tmp.size(); i++) {
                    for (Music music : allMusics.PlayList)
                        if (music.getTitle().equals(tmp.get(i))) {
                            searchBarPlaylist.Add(music);
                            System.out.println(tmp.get(i));
                        }

                }
            } else {
//                        list.add("Dont find any match!");
            }
            displayListOfMusics(searchBarPlaylist);
            songsPanel.revalidate();
        } else if (searchBar.getText().equals("")) {
            displayListOfMusics(allMusics);
            songsPanel.revalidate();

        }
    }

    public JPanel getAlbumsScrollPanel() {
        return albumsScrollPanel;
    }

    private MouseAdapter addPlayListButtonAction(GUI gui) {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                addPlayListButton.setIcon(new ImageIcon(getClass().getResource("/Logo/SquareAdd - black.png")));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                addPlayListButton.setIcon(new ImageIcon(getClass().getResource("/Logo/SquareAdd - white.png")));
                JFrame frame = new JFrame("Enter Name of PlayList");
                frame.setSize(new Dimension(400, 120));
                JDialog dialog = new JDialog(frame, "Enter Name of PlayList", true);
                dialog.setSize(new Dimension(350, 110));
                dialog.setBackground(Color.RED);
                JLabel label = new JLabel("Enter the Name of PlayList");
                label.setPreferredSize(new Dimension(100, 30));
                label.setBackground(new Color(-15198184));
                JButton button = new JButton("OK");
                button.setPreferredSize(new Dimension(80, 20));
                button.setBackground(new Color(-15198184));
                button.setForeground(Color.WHITE);
                JTextField textField = new JTextField();
                textField.setPreferredSize(new Dimension(100, 30));
                dialog.add(label, BorderLayout.NORTH);
                dialog.add(button, BorderLayout.SOUTH);
                dialog.add(textField, BorderLayout.CENTER);

                button.addMouseListener(addAPlayList(frame, textField, label, gui));

                dialog.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseReleased(e);
                addPlayListButton.setIcon(new ImageIcon(getClass().getResource("/Logo/SquareAdd - green.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseReleased(e);
                addPlayListButton.setIcon(new ImageIcon(getClass().getResource("/Logo/SquareAdd - white.png")));
            }
        };

    }

    private MouseAdapter addAlbumButtonAction() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                addAlbumButton.setIcon(new ImageIcon(getClass().getResource("/Logo/SquareAdd - black.png")));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                addAlbumButton.setIcon(new ImageIcon(getClass().getResource("/Logo/SquareAdd - white.png")));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseReleased(e);
                addAlbumButton.setIcon(new ImageIcon(getClass().getResource("/Logo/SquareAdd - green.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseReleased(e);
                addAlbumButton.setIcon(new ImageIcon(getClass().getResource("/Logo/SquareAdd - white.png")));
            }
        };

    }

    private MouseWheelListener timeLineSliderWheel() {
        return new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                timeFlag = false;
                if (notches < 0)
                    timLineSlider.setValue(timLineSlider.getValue() + 50);
                else if (notches > 0)
                    timLineSlider.setValue(timLineSlider.getValue() - 50);
                playMP3.SetPosition((timLineSlider.getValue()) * playMP3.getMusic().getSize() / 10000);
                timeFlag = true;
            }
        };
    }

    private MouseAdapter previousAction() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                previous.setIcon(new ImageIcon(getClass().getResource("/Logo/previous-black.png")));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                previous.setIcon(new ImageIcon(getClass().getResource("/Logo/previous-white.png")));
                if (playMP3.getPosition() < 100) {
                    playListPlayer.previousMusic();
                    playMP3.SetPosition(0);

                } else {
                    playMP3.SetPosition(0);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                previous.setIcon(new ImageIcon(getClass().getResource("/Logo/previous-green.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                previous.setIcon(new ImageIcon(getClass().getResource("/Logo/previous-white.png")));
            }
        };
    }

    private MouseAdapter nextAction() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                next.setIcon(new ImageIcon(getClass().getResource("/Logo/next-black.png")));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                next.setIcon(new ImageIcon(getClass().getResource("/Logo/next-white.png")));
                playListPlayer.nextMusic();
                playMP3.SetPosition(0);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                next.setIcon(new ImageIcon(getClass().getResource("/Logo/next-green.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                next.setIcon(new ImageIcon(getClass().getResource("/Logo/next-white.png")));
            }
        };
    }

    private MouseAdapter playAction() {
        return new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (playMP3.isStopFlag()) {
                    play.setIcon(new ImageIcon(getClass().getResource("/Logo/play-black.png")));
                } else {
                    play.setIcon(new ImageIcon(getClass().getResource("/Logo/pause-black.png")));

                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                if (playMP3.isStopFlag()) {
                    play.setIcon(new ImageIcon(getClass().getResource("/Logo/pause-white.png")));
                    if (!playListPlayer.isThreadMade()) {
                        new Thread(playListPlayer).start();
                    } else {
                        try {
                            playMP3.play();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                    isPlaying = true;

                } else {
                    play.setIcon(new ImageIcon(getClass().getResource("/Logo/play-white.png")));
                    try {
                        playMP3.stop();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    isPlaying = false;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (playMP3.isStopFlag())
                    play.setIcon(new ImageIcon(getClass().getResource("/Logo/play-green.png")));
                else
                    play.setIcon(new ImageIcon(getClass().getResource("/Logo/pause-green.png")));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (playMP3.isStopFlag())
                    play.setIcon(new ImageIcon(getClass().getResource("/Logo/play-white.png")));
                else
                    play.setIcon(new ImageIcon(getClass().getResource("/Logo/pause-white.png")));
            }
        }

                ;
    }

    private MouseAdapter timLineSliderAction() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                timeFlag = false;
                Point p = e.getPoint();
                double percent = p.x / ((double) timLineSlider.getWidth());
                int range = timLineSlider.getMaximum() - timLineSlider.getMinimum();
                double newVal = range * percent;
                int result = (int) (timLineSlider.getMinimum() + newVal);
                timLineSlider.setValue(result);
                playMP3.SetPosition((result) * playMP3.getMusic().getSize() / 10000);
                timeFlag = true;
            }
        };
    }


    private MouseAdapter browseAction() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                browseIcon.setIcon(new ImageIcon(getClass().getResource("/Logo/browse-black.png")));
                browseText.setForeground(Color.BLACK);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                browseIcon.setIcon(new ImageIcon(getClass().getResource("/Logo/browse-gray.png")));
                browseText.setForeground(new Color(-7500403));
                openFileChooser();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                browseIcon.setIcon(new ImageIcon(getClass().getResource("/Logo/browse-white.png")));
                browseText.setForeground(Color.WHITE);

            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                browseIcon.setIcon(new ImageIcon(getClass().getResource("/Logo/browse-gray.png")));
                browseText.setForeground(new Color(-7500403));
            }
        };
    }


    private MouseAdapter homeAction() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                homeIcon.setIcon(new ImageIcon(getClass().getResource("/Logo/Home-black.png")));
                HomeText.setForeground(Color.BLACK);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                homeIcon.setIcon(new ImageIcon(getClass().getResource("/Logo/Home-gray.png")));
                HomeText.setForeground(new Color(-7500403));
                displayListOfMusics(allMusics);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                homeIcon.setIcon(new ImageIcon(getClass().getResource("/Logo/Home-white.png")));
                HomeText.setForeground(Color.WHITE);

            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                homeIcon.setIcon(new ImageIcon(getClass().getResource("/Logo/Home-gray.png")));
                HomeText.setForeground(new Color(-7500403));
            }
        };
    }

    public void revalidateSongsPanel() {
        songsPanel.revalidate();
    }

    public void revalidatePlayListScrollPanel() {
        playListScrollPanel.revalidate();
    }

    public JPanel getPlayListScrollPanel() {
        return playListScrollPanel;
    }

    private MouseAdapter addAPlayList(JFrame frame, JTextField textField, JLabel label, GUI gui) {
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (textField.getText().equals(""))
                    label.setText("Name Can Not Be Empty");
                else if (playListIsAlreadyHere(textField.getText()))
                    label.setText("Name Already Exists in PlayLists");
                else {
                    PlayList tempPL = new PlayList(textField.getText(), true);
                    playLists.add(tempPL);
                    playListScrollPanel.add(new PlayListWithGraphic(tempPL, new PlayListForm(gui, tempPL), gui).getPanel());
                    playListScrollPanel.revalidate();
                    System.out.println(textField.getText());
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                }

            }
        };
    }

    public static ImageIcon resiseImageIcon(int width, int height, String path) {
        BufferedImage srcImg = null;
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
        }
        BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, width, height, null);
        g2.dispose();

        return new ImageIcon(resizedImg);
    }

    private boolean playListIsAlreadyHere(String name) {
        for (PlayList playList : this.playLists)
            if (playList.getTitle().equals(name))
                return true;
        return false;

    }

    public PlayList getAllMusics() {
        return allMusics;
    }

    public JPanel getNetworkPanel() {
        return networkPanel;
    }

    public ArrayList<PlayList> getPlayLists() {
        return playLists;
    }

    private void openFileChooser() {
        try {
            boolean musicExist = false;
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jfc.setMultiSelectionEnabled(true);
            jfc.setFileFilter(new FileNameExtensionFilter("mp3", "mp3"));
            int returnVal = jfc.showOpenDialog(null);
            File[] selectedFiles = new File[100];
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                selectedFiles = jfc.getSelectedFiles();
                for (File file : selectedFiles) {
                    for (Music music : allMusics.PlayList)
                        if (music.getPath().equals(file.getAbsolutePath()))
                            musicExist = true;
                    if (!musicExist)
                        allMusics.Add(new Music(file.getAbsolutePath(), owner, playLists));
                    musicExist = false;
                }
                displayListOfMusics(allMusics);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void startProgram() {
        JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getUsersListPanel() {
        return usersListPanel;
    }

    public JPanel getPlayingMusicName() {
        return playingMusicName;
    }

    public void displayListOfMusics(PlayList playList) {
        songsScrollPanel.removeAll();
        songsScrollPanel.repaint();
        songsScrollPanel.revalidate();
        for (Music music : playList.PlayList) {
            songsScrollPanel.add(new MusicWithGraphic(music, new MusicForm(music.getPath()), playMP3, playListPlayer, playList, play, isPlaying).getMusicPanel());
        }
        songsScrollPane.getVerticalScrollBar().setValue(0);
        songsScrollPanel.revalidate();
        displayedPlaylist = playList;
        displayedListLabel.setText(displayedPlaylist.getTitle());
        displayedListLabel.revalidate();
    }


    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void makeThreadForTimeLine() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int spentFrames, totalFrames;
                while (true) {
                    try {
                        spentFrames = playMP3.getPosition();
                        totalFrames = playMP3.getMusic().getSize();
                        spentTime.setTime(spentFrames);
                        totalTime.setTime(totalFrames);
                        totalTimeLabel.setText(totalTime.getTime());
                        spentTImeLabel.setText(spentTime.getTime());
//                        if (!timeFlag)
//                            System.out.println("ldj");
                        if (timeFlag) {
                            timLineSlider.setValue((int) (((double) spentFrames / totalFrames) * 10000));
                            timLineSlider.revalidate();
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }).start();
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
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, 0));
        mainPanel.setBackground(new Color(-15198184));
        mainPanel.putClientProperty("html.disable", Boolean.FALSE);
        topBar = new JPanel();
        topBar.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        topBar.setBackground(new Color(-15592942));
        mainPanel.add(topBar, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(850, 50), new Dimension(1280, 50), new Dimension(1920, -1), 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        topBar.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-15592942));
        topBar.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(150, -1), new Dimension(230, -1), new Dimension(320, -1), 0, false));
        searchLogo = new JLabel();
        searchLogo.setForeground(new Color(-855310));
        searchLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/search logo - white.png")));
        searchLogo.setText("");
        topBar.add(searchLogo, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searchBar = new JTextField();
        searchBar.setBackground(new Color(-15592942));
        searchBar.setForeground(new Color(-855310));
        topBar.add(searchBar, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(250, -1), new Dimension(250, -1), 0, false));
        userName = new JLabel();
        userName.setBackground(new Color(-15592942));
        userName.setForeground(new Color(-855310));
        userName.setText("");
        topBar.add(userName, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        PlayPanel = new JPanel();
        PlayPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        PlayPanel.setBackground(new Color(-14145496));
        mainPanel.add(PlayPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(600, 100), new Dimension(1740, 100), new Dimension(1920, 100), 0, false));
        timeLine = new JPanel();
        timeLine.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        timeLine.setBackground(new Color(-14145496));
        PlayPanel.add(timeLine, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(350, 50), new Dimension(590, 50), new Dimension(1200, 50), 0, false));
        timLineSlider = new JSlider();
        timLineSlider.setAutoscrolls(false);
        timLineSlider.setBackground(new Color(-14145496));
        timLineSlider.setInheritsPopupMenu(false);
        timLineSlider.setInverted(false);
        timLineSlider.setMajorTickSpacing(0);
        timLineSlider.setMaximum(10000);
        timLineSlider.setPaintLabels(false);
        timLineSlider.setPaintTicks(false);
        timLineSlider.setPaintTrack(true);
        timLineSlider.setSnapToTicks(true);
        timLineSlider.setValueIsAdjusting(true);
        timLineSlider.setVerifyInputWhenFocusTarget(true);
        timeLine.add(timLineSlider, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(300, 20), new Dimension(450, 20), new Dimension(1000, 20), 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        timeLine.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        totalTimeLabel = new JLabel();
        totalTimeLabel.setBackground(new Color(-14013910));
        totalTimeLabel.setForeground(new Color(-855310));
        totalTimeLabel.setText("00:00");
        totalTimeLabel.setToolTipText("Total Time");
        timeLine.add(totalTimeLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spentTImeLabel = new JLabel();
        spentTImeLabel.setBackground(new Color(-14013910));
        spentTImeLabel.setForeground(new Color(-855310));
        spentTImeLabel.setText("00:00");
        spentTImeLabel.setToolTipText("Spent Time");
        timeLine.add(spentTImeLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        SoundPanel = new JPanel();
        SoundPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        SoundPanel.setBackground(new Color(-14145496));
        PlayPanel.add(SoundPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 3, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(250, 100), new Dimension(345, 100), new Dimension(360, 100), 0, false));
        volumeSlider = new JSlider();
        volumeSlider.setBackground(new Color(-14145496));
        SoundPanel.add(volumeSlider, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(150, -1), new Dimension(150, -1), 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        SoundPanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        volumeLogo = new JButton();
        volumeLogo.setBackground(new Color(-14145496));
        volumeLogo.setBorderPainted(false);
        volumeLogo.setContentAreaFilled(false);
        volumeLogo.setFocusPainted(false);
        volumeLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/sound - white.png")));
        volumeLogo.setText("");
        volumeLogo.setToolTipText("Sound Level");
        SoundPanel.add(volumeLogo, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(30, 30), new Dimension(30, 30), new Dimension(30, 30), 0, false));
        playingMusicName = new JPanel();
        playingMusicName.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        playingMusicName.setBackground(new Color(-14145496));
        PlayPanel.add(playingMusicName, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 3, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(250, 100), new Dimension(345, 100), new Dimension(360, 100), 0, false));
        musicIcons = new JPanel();
        musicIcons.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        musicIcons.setBackground(new Color(-14145496));
        PlayPanel.add(musicIcons, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(350, 50), new Dimension(590, 50), new Dimension(1200, 50), 0, false));
        playPanelButtons = new JPanel();
        playPanelButtons.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), 5, -1, true, false));
        playPanelButtons.setBackground(new Color(-14145496));
        playPanelButtons.setEnabled(false);
        musicIcons.add(playPanelButtons, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, 35), new Dimension(250, 35), new Dimension(350, 35), 0, false));
        play = new JButton();
        play.setBackground(new Color(-14145496));
        play.setBorderPainted(false);
        play.setContentAreaFilled(false);
        play.setEnabled(true);
        play.setFocusPainted(false);
        play.setHideActionText(true);
        play.setIcon(new ImageIcon(getClass().getResource("/Logo/play-white.png")));
        play.setText("");
        play.setToolTipText("Play / Pause");
        play.putClientProperty("hideActionText", Boolean.TRUE);
        playPanelButtons.add(play, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(35, 35), new Dimension(35, 35), new Dimension(35, 35), 0, false));
        next = new JButton();
        next.setBackground(new Color(-14145496));
        next.setBorderPainted(false);
        next.setContentAreaFilled(false);
        next.setFocusPainted(false);
        next.setIcon(new ImageIcon(getClass().getResource("/Logo/next-white.png")));
        next.setText("");
        next.setToolTipText("Next");
        playPanelButtons.add(next, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(30, 30), new Dimension(30, 30), new Dimension(30, 30), 0, false));
        previous = new JButton();
        previous.setBackground(new Color(-14145496));
        previous.setBorderPainted(false);
        previous.setContentAreaFilled(false);
        previous.setFocusPainted(false);
        previous.setIcon(new ImageIcon(getClass().getResource("/Logo/previous-white.png")));
        previous.setText("");
        previous.setToolTipText("Previous");
        playPanelButtons.add(previous, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(30, 30), new Dimension(30, 30), new Dimension(30, 30), 0, false));
        shuffleButton = new JButton();
        shuffleButton.setBackground(new Color(-14145496));
        shuffleButton.setBorderPainted(false);
        shuffleButton.setContentAreaFilled(false);
        shuffleButton.setFocusPainted(false);
        shuffleButton.setIcon(new ImageIcon(getClass().getResource("/Logo/shuffle-white.png")));
        shuffleButton.setText("");
        shuffleButton.setToolTipText("Shuffle");
        playPanelButtons.add(shuffleButton, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(30, 30), new Dimension(30, 30), new Dimension(30, 30), 0, false));
        repeatButton = new JButton();
        repeatButton.setBackground(new Color(-14145496));
        repeatButton.setBorderPainted(false);
        repeatButton.setContentAreaFilled(false);
        repeatButton.setFocusPainted(false);
        repeatButton.setIcon(new ImageIcon(getClass().getResource("/Logo/repeat-white.png")));
        repeatButton.setText("");
        repeatButton.setToolTipText("Repeat");
        playPanelButtons.add(repeatButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(30, 30), new Dimension(30, 30), new Dimension(30, 30), 0, false));
        AlbumPanel = new JPanel();
        AlbumPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        AlbumPanel.setAutoscrolls(false);
        AlbumPanel.setBackground(new Color(-15592942));
        AlbumPanel.setFocusTraversalPolicyProvider(false);
        AlbumPanel.setFocusable(true);
        AlbumPanel.setOpaque(true);
        mainPanel.add(AlbumPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(200, 500), new Dimension(165, 641), new Dimension(250, -1), 0, true));
        homePanel = new JPanel();
        homePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        homePanel.setBackground(new Color(-15461356));
        AlbumPanel.add(homePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, -1), new Dimension(200, -1), new Dimension(200, -1), 0, false));
        homeIcon = new JLabel();
        homeIcon.setBackground(new Color(-15592942));
        homeIcon.setEnabled(true);
        homeIcon.setIcon(new ImageIcon(getClass().getResource("/Logo/Home-gray.png")));
        homeIcon.setText("");
        homeIcon.setToolTipText("Go to Home Library");
        homePanel.add(homeIcon, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        homePanel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        HomeText = new JLabel();
        HomeText.setBackground(new Color(-15592942));
        Font HomeTextFont = this.$$$getFont$$$("Inconsolata", Font.BOLD, 18, HomeText.getFont());
        if (HomeTextFont != null) HomeText.setFont(HomeTextFont);
        HomeText.setForeground(new Color(-7500403));
        HomeText.setText("Home");
        HomeText.setToolTipText("Go to Home Library");
        homePanel.add(HomeText, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        browsePanel = new JPanel();
        browsePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        browsePanel.setBackground(new Color(-15461356));
        AlbumPanel.add(browsePanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, -1), new Dimension(200, -1), new Dimension(200, -1), 0, false));
        browseIcon = new JLabel();
        browseIcon.setBackground(new Color(-15592942));
        browseIcon.setIcon(new ImageIcon(getClass().getResource("/Logo/browse-gray.png")));
        browseIcon.setText("");
        browseIcon.setToolTipText("Browse Files");
        browsePanel.add(browseIcon, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        browsePanel.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        browseText = new JLabel();
        browseText.setBackground(new Color(-15592942));
        Font browseTextFont = this.$$$getFont$$$("Inconsolata", Font.BOLD, 18, browseText.getFont());
        if (browseTextFont != null) browseText.setFont(browseTextFont);
        browseText.setForeground(new Color(-7500403));
        browseText.setText("Browse");
        browseText.setToolTipText("Browse Files");
        browsePanel.add(browseText, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        playListsPanel = new JPanel();
        playListsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 5, new Insets(0, 0, 0, 0), -1, -1));
        playListsPanel.setBackground(new Color(-15592942));
        AlbumPanel.add(playListsPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 300), new Dimension(220, -1), 0, false));
        playLisrScroll = new JScrollPane();
        playLisrScroll.setBackground(new Color(-15592942));
        playLisrScroll.setHorizontalScrollBarPolicy(30);
        playLisrScroll.setInheritsPopupMenu(false);
        playLisrScroll.setVerticalScrollBarPolicy(20);
        playListsPanel.add(playLisrScroll, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(280, -1), new Dimension(280, 300), new Dimension(290, -1), 0, false));
        playLisrScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null));
        playListScrollPanel = new JPanel();
        playListScrollPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 5));
        playListScrollPanel.setAutoscrolls(true);
        playListScrollPanel.setBackground(new Color(-15592942));
        playListScrollPanel.setForeground(new Color(-855310));
        playListScrollPanel.setMaximumSize(new Dimension(600, 32767));
        playListScrollPanel.setMinimumSize(new Dimension(200, 400));
        playListScrollPanel.setPreferredSize(new Dimension(220, 10));
        playLisrScroll.setViewportView(playListScrollPanel);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Lucida Sans Typewriter", Font.BOLD, 14, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-855310));
        label1.setText("PlayLists");
        playListsPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addPlayListButton = new JButton();
        addPlayListButton.setBackground(new Color(-15592942));
        addPlayListButton.setBorderPainted(false);
        addPlayListButton.setContentAreaFilled(false);
        addPlayListButton.setFocusPainted(false);
        addPlayListButton.setForeground(new Color(-855310));
        addPlayListButton.setIcon(new ImageIcon(getClass().getResource("/Logo/SquareAdd - white.png")));
        addPlayListButton.setText("");
        playListsPanel.add(addPlayListButton, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer6 = new com.intellij.uiDesigner.core.Spacer();
        playListsPanel.add(spacer6, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        AlbumsPanel = new JPanel();
        AlbumsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        AlbumsPanel.setBackground(new Color(-15592942));
        AlbumPanel.add(AlbumsPanel, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 300), null, 0, false));
        albumsScroll = new JScrollPane();
        albumsScroll.setBackground(new Color(-15592942));
        albumsScroll.setDoubleBuffered(true);
        albumsScroll.setInheritsPopupMenu(false);
        albumsScroll.setOpaque(true);
        albumsScroll.setVerticalScrollBarPolicy(20);
        AlbumsPanel.add(albumsScroll, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(200, 300), null, 0, false));
        albumsScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null));
        albumsScrollPanel = new JPanel();
        albumsScrollPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        albumsScrollPanel.setBackground(new Color(-15592942));
        albumsScrollPanel.setVisible(true);
        albumsScroll.setViewportView(albumsScrollPanel);
        final com.intellij.uiDesigner.core.Spacer spacer7 = new com.intellij.uiDesigner.core.Spacer();
        albumsScrollPanel.add(spacer7, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setForeground(new Color(-1));
        label2.setText("Label");
        albumsScrollPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("Lucida Sans Typewriter", Font.BOLD, 14, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setForeground(new Color(-855310));
        label3.setText("Albums");
        AlbumsPanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addAlbumButton = new JButton();
        addAlbumButton.setBackground(new Color(-15592942));
        addAlbumButton.setBorderPainted(false);
        addAlbumButton.setContentAreaFilled(false);
        addAlbumButton.setFocusPainted(false);
        addAlbumButton.setIcon(new ImageIcon(getClass().getResource("/Logo/SquareAdd - white.png")));
        addAlbumButton.setText("");
        AlbumsPanel.add(addAlbumButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        networkPanel = new JPanel();
        networkPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        networkPanel.setBackground(new Color(-15592942));
        mainPanel.add(networkPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(200, 500), new Dimension(230, 641), new Dimension(250, -1), 0, false));
        activityLabel = new JLabel();
        activityLabel.setBackground(new Color(-15198184));
        Font activityLabelFont = this.$$$getFont$$$("Inconsolata", Font.BOLD, 18, activityLabel.getFont());
        if (activityLabelFont != null) activityLabel.setFont(activityLabelFont);
        activityLabel.setForeground(new Color(-855310));
        activityLabel.setText("Friend Activity");
        activityLabel.setVerifyInputWhenFocusTarget(true);
        networkPanel.add(activityLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
        final com.intellij.uiDesigner.core.Spacer spacer8 = new com.intellij.uiDesigner.core.Spacer();
        networkPanel.add(spacer8, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        usersListPanel = new JPanel();
        usersListPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        usersListPanel.setBackground(new Color(-15592942));
        networkPanel.add(usersListPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(220, -1), new Dimension(220, -1), new Dimension(220, -1), 0, false));
        addFriendButton = new JButton();
        addFriendButton.setBackground(new Color(-15592942));
        addFriendButton.setBorderPainted(false);
        addFriendButton.setContentAreaFilled(false);
        addFriendButton.setFocusCycleRoot(true);
        addFriendButton.setFocusPainted(false);
        addFriendButton.setIcon(new ImageIcon(getClass().getResource("/Logo/add friend - white.png")));
        addFriendButton.setText("");
        networkPanel.add(addFriendButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 12, false));
        songsPanel = new JPanel();
        songsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        songsPanel.setAutoscrolls(false);
        songsPanel.setBackground(new Color(-15198184));
        songsPanel.setForeground(new Color(-855310));
        mainPanel.add(songsPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(450, 500), new Dimension(820, 550), new Dimension(870, 680), 0, false));
        songsScrollPane = new JScrollPane();
        songsScrollPane.setBackground(new Color(-15198184));
        songsPanel.add(songsScrollPane, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(1080, -1), new Dimension(1420, 641), new Dimension(1420, 1080), 0, false));
        songsScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null));
        songsScrollPanel = new JPanel();
        songsScrollPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 40, 20));
        songsScrollPanel.setBackground(new Color(-15198184));
        songsScrollPanel.setMaximumSize(new Dimension(1020, 32767));
        songsScrollPanel.setMinimumSize(new Dimension(1020, 32767));
        songsScrollPanel.setPreferredSize(new Dimension(1020, 32767));
        songsScrollPane.setViewportView(songsScrollPanel);
        displayedListLabel = new JLabel();
        displayedListLabel.setBackground(new Color(-15198184));
        Font displayedListLabelFont = this.$$$getFont$$$(null, -1, 14, displayedListLabel.getFont());
        if (displayedListLabelFont != null) displayedListLabel.setFont(displayedListLabelFont);
        displayedListLabel.setForeground(new Color(-855310));
        displayedListLabel.setText("");
        songsPanel.add(displayedListLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}






