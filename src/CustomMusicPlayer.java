import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;

public class CustomMusicPlayer implements Serializable {
    private Bitstream bitstream;
    private Decoder decoder;
    private AudioDevice audio;
    private boolean closed = false;
    private boolean complete = false;
    private InputStream stream;
    private String File;
    private int Position;
    private boolean StopFlag;
    private Music music;
    private Owner owner;
    private boolean innerStopFlag;
    private GUI gui;


    public boolean isClosed() {
        return closed;
    }

    public CustomMusicPlayer(Music music, Owner owner, GUI gui) {
        this.owner = owner;
        this.gui = gui;
        try {
            this.innerStopFlag = false;
            File = music.getPath();
            StopFlag = true;
            stream = new FileInputStream(File);
            bitstream = new Bitstream(stream);
            audio = FactoryRegistry.systemRegistry().createAudioDevice();
            audio.open(decoder = new Decoder());
            this.music = music;
        } catch (Exception e) {
        }
    }


    public  void SetPosition(int i) {
        makePlayingForm();
        innerStopFlag = true;
        AudioDevice out = audio;
        if (out != null) {
            closed = true;
            audio = null;
            out.close();
            try {
                bitstream.close();
            } catch (BitstreamException ex) {
            }
        }
        try {

            Position = i;
            stream = new FileInputStream(File);
            bitstream = new Bitstream(stream);
            System.gc();
            audio = FactoryRegistry.systemRegistry().createAudioDevice();
            audio.open(decoder = new Decoder());

            for (int j = 0; j < i; j++)
                skipFrame();
        } catch (Exception e) {
            System.out.println(e);
        }
        innerStopFlag = false;
    }
    public void play() throws JavaLayerException {
        StopFlag = false;
    }

    public boolean play(int frames) throws JavaLayerException {
        boolean ret = true;

        if(File.equals("NotDownloadedYet")){
            System.out.println("Wait for downloading the Music");
            String ip = music.getOwnerIP();
            try{
                Socket tmp = new Socket(ip,owner.getId());
                OutputStream request = tmp.getOutputStream();
                PrintWriter outRequest = new PrintWriter(request);
                outRequest.println(music.getTitle());
                System.out.println(music.getTitle());
                outRequest.close();
                tmp.close();

                ServerSocket servertmp = new ServerSocket(7070);
                tmp = servertmp.accept();
                DataInputStream in = new DataInputStream(
                        new BufferedInputStream(tmp.getInputStream()));
                Scanner sc = new Scanner(in);
                int len = Integer.parseInt(sc.next());
                tmp.close();
                System.out.println(len);
                in.close();
                sc.close();
                byte[] MusicReaderBuffer = new byte[len];
                servertmp.close();
                servertmp = new ServerSocket(5050);
                tmp = servertmp.accept();
                InputStream musicGeter = tmp.getInputStream();
                System.out.println("C:\\"+music.getTitle()+".mp3");
                System.out.println((music.getTitle() + ".mp3").replaceAll("\\s+", ""));
                FileOutputStream fileForMusic = new FileOutputStream((music.getSize()+".mp3").replaceAll("\\s+", ""));
                BufferedOutputStream fileWriter = new BufferedOutputStream(fileForMusic);
                System.out.println(len);
                int bytesRead = musicGeter.read(MusicReaderBuffer, 0, MusicReaderBuffer.length);
                int current = bytesRead;
                do {
                    bytesRead = musicGeter.read(MusicReaderBuffer, current, (MusicReaderBuffer.length-current));
                    if(bytesRead >= 0) current += bytesRead;
                    System.out.println(current + "------    " + bytesRead);
                } while(bytesRead > 0 );

                System.out.println(bytesRead);
                System.out.println(MusicReaderBuffer.length);
                fileWriter.write(MusicReaderBuffer, 0, MusicReaderBuffer.length);
                System.out.println(len);
//                fileWriter.close();
//                System.out.println("joho");
//                servertmp.close();
//                tmp.close();

                music.ChangeDirectory((music.getSize()+".mp3").replaceAll("\\s+", ""));

                ChangeMusic(music);
                play(0, Integer.MAX_VALUE);
                System.out.println("joho");
            }catch (Exception e){
                System.out.println(e);
                System.out.println("The Owner of this music is offline!");
                return false;
            }
        }
        if(music.isOnSharedMusic()){
            List<String> listOfIPFriendToPost = owner.getListOfip();
            List<Integer> listOfIDFriendToPost = owner.getListOfid();
            for(int i=0;i<listOfIPFriendToPost.size();i++){
                String IPtmp = listOfIPFriendToPost.get(i);
                int IDtmp = listOfIDFriendToPost.get(i);
                try {
                    Socket tmp = new Socket(IPtmp,owner.getId());
                    OutputStream request = tmp.getOutputStream();
                    PrintWriter outRequest = new PrintWriter(request);
                    outRequest.println("ChangeLastMusicPlayedByPeople");
                    outRequest.close();
                    tmp.close();

                    ServerSocket Servertmp = new ServerSocket(7070);
                    tmp = Servertmp.accept();
                    DataInputStream getResponse = new DataInputStream(
                            new BufferedInputStream(tmp.getInputStream()));
                    Scanner sc = new Scanner(getResponse);
                    String Response = sc.next();
                    getResponse.close();
                    sc.close();
                    request.close();
                    tmp.close();
                    if(Response.equals("Ok")){
                        tmp = new Socket(IPtmp,owner.getId());
                        request = tmp.getOutputStream();
                        outRequest = new PrintWriter(request);
                        com.sun.security.auth.module.NTSystem NTSystem = new com.sun.security.auth.module.NTSystem();
                        String name = NTSystem.getName();
                        outRequest.println(name);
                        outRequest.println(music.getTitle());
                        outRequest.close();
                        tmp.close();
                        Servertmp.close();
                    }
                }catch(Exception e){
                    System.out.println("Socket:" + "is Sleep Via error: " + e);
                }
            }
        }


        while (ret) {
            ret = decodeFrame();
        }

//    if (!ret)
        {
            // last frame, ensure all data flushed to the audio device.
            AudioDevice out = audio;
            if (out != null) {

                out.flush();

                synchronized (this) {
                    complete = (!closed);
                    close();
                }
            }
        }
        return ret;
    }

    public synchronized void close() {
        AudioDevice out = audio;
        if (out != null) {
            closed = true;
            audio = null;
            out.close();
            try {
                bitstream.close();
            } catch (BitstreamException ex) {
            }
        }
    }

    public boolean isStopFlag() {
        return StopFlag;
    }
    private void makePlayingForm(){
        try {
            gui.getPlayingMusicName().removeAll();
            gui.getPlayingMusicName().repaint();
            gui.getPlayingMusicName().revalidate();
            gui.getPlayingMusicName().add(new PlayingMusicForm(music).getMainPanel());
            gui.getPlayingMusicName().revalidate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void ChangeMusic(Music music) {
        this.music = music;
        makePlayingForm();
        closed = false;
        Position = 0;
        try {
            File = music.getPath();
            StopFlag = false;
            stream = new FileInputStream(File);
            bitstream = new Bitstream(stream);
            audio = FactoryRegistry.systemRegistry().createAudioDevice();
            audio.open(decoder = new Decoder());
            System.gc();
            this.music = music;
        } catch (Exception e) {
        }
    }

    protected synchronized boolean decodeFrame() throws JavaLayerException {
        if (!StopFlag && !innerStopFlag) {
            try {
                Position++;
                AudioDevice out = audio;
                if (out == null) return false;

                Header h = bitstream.readFrame();
                if (h == null) return false;
                // sample buffer set when decoder constructed
                SampleBuffer output = (SampleBuffer) decoder.decodeFrame(h, bitstream);
                synchronized (this) {
                    out = audio;
                    if (out != null) {
                        out.write(output.getBuffer(), 0, output.getBufferLength());
                    }
                }
                bitstream.closeFrame();
            } catch (RuntimeException ex) {
            }
        }
        return true;
    }

    public boolean skipFrame() throws JavaLayerException {
        Header h = bitstream.readFrame();
        if (h == null) return false;
        bitstream.closeFrame();
        return true;
    }

    public boolean play(final int start, final int end) throws JavaLayerException {
        makePlayingForm();
        closed = false;
        Position = 0;
        try {
            File = music.getPath();
            StopFlag = false;
            stream = new FileInputStream(File);
            bitstream = new Bitstream(stream);
            audio = FactoryRegistry.systemRegistry().createAudioDevice();
            audio.open(decoder = new Decoder());
            System.gc();
            this.music = music;
        } catch (Exception e) {
        }
        boolean ret = true;
        int offset = start;
        while (offset-- > 0 && ret) ret = skipFrame();
        return play(end - start);
    }

    public void stop() {
        StopFlag = true;
    }

    public int getPosition() {
        return Position;
    }

    public int size() {
        try {
            while (true) {
                Header h = bitstream.readFrame();
                Position++;
                if (h == null)
                    return Position;
                bitstream.closeFrame();
            }
        } catch (Exception e) {
        }
        return -1;
    }

    public Music getMusic() {
        return music;
    }
}