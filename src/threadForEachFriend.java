import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class threadForEachFriend implements Runnable {
    private ServerSocket serversocket;
    private sharedPlayList sharedPlayList;
    private int id;
    private boolean flag;
    private ArrayList<PlayList> playLists;


    threadForEachFriend(ServerSocket socket , sharedPlayList sharedPlayList, int id, ArrayList<PlayList> playLists){
        this.playLists = playLists;
        this.sharedPlayList = sharedPlayList;
        this.serversocket = socket;
        this.id = id;
        flag = false;
    }
    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serversocket.accept();
                DataInputStream WhatsWrong = new DataInputStream(
                        new BufferedInputStream(socket.getInputStream()));
                Scanner getWhatsWrong = new Scanner(WhatsWrong);
                String subjectRequest = getWhatsWrong.next();
                getWhatsWrong.close();
                WhatsWrong.close();
                System.out.println(subjectRequest);

                if (subjectRequest.equals("newMusic")){
                    String ip = "";
                    for (int i = 1; i <socket.getRemoteSocketAddress().toString().length() ; i++) {
                        if (socket.getRemoteSocketAddress().toString().charAt(i)==':')
                            break;
                        ip = ip + socket.getRemoteSocketAddress().toString().charAt(i);

                    }
                    socket.close();
                    socket = new Socket(ip,7070);
                    OutputStream response = socket.getOutputStream();
                    PrintWriter setOutTheResponse = new PrintWriter(response);
                    setOutTheResponse.println("ImReady");
                    setOutTheResponse.close();
                    response.close();
                    socket.close();

                    socket = serversocket.accept();
                    ObjectInputStream musicReceiver = new
                            ObjectInputStream(socket.getInputStream());
                    Music newMusic = (Music) musicReceiver.readObject();
                    newMusic.setPlayLists(this.playLists);
                    musicReceiver.close();

                    newMusic.ChangeDirectory("NotDownloadedYet");
                    sharedPlayList.AddToNet(newMusic);
                    socket.close();

                }
                else if(subjectRequest.equals("RemoveMusic")){

                    String ip = "";
                    for (int i = 1; i <socket.getRemoteSocketAddress().toString().length() ; i++) {
                        if (socket.getRemoteSocketAddress().toString().charAt(i)==':')
                            break;
                        ip = ip + socket.getRemoteSocketAddress().toString().charAt(i);

                    }
                    socket.close();
                    socket = new Socket(ip,7070);
                    OutputStream response = socket.getOutputStream();
                    PrintWriter setOutTheResponse = new PrintWriter(response);
                    setOutTheResponse.println("Ok");
                    setOutTheResponse.close();
                    response.close();
                    socket.close();

                    socket = serversocket.accept();
                    DataInputStream whatsTitle = new DataInputStream(
                            new BufferedInputStream(socket.getInputStream()));
                    Scanner getWhatsTitle = new Scanner(whatsTitle);
                    String Title = getWhatsTitle.next();
                    whatsTitle.close();
                    getWhatsTitle.close();
                    socket.close();
                    Music m = sharedPlayList.GetMusic(Title);
                    if(m != null)
                        sharedPlayList.Delete(m);
                }
                else if(subjectRequest.equals("ChangeLastMusicPlayedByPeople")){
                    String ip = "";
                    for (int i = 1; i <socket.getRemoteSocketAddress().toString().length() ; i++) {
                        if (socket.getRemoteSocketAddress().toString().charAt(i)==':')
                            break;
                        ip = ip + socket.getRemoteSocketAddress().toString().charAt(i);

                    }
                    socket.close();
                    socket = new Socket(ip,7070);
                    OutputStream response = socket.getOutputStream();
                    PrintWriter setOutTheResponse = new PrintWriter(response);
                    setOutTheResponse.println("Ok");
                    setOutTheResponse.close();
                    response.close();
                    socket = serversocket.accept();
                    DataInputStream in = new DataInputStream(
                            new BufferedInputStream(socket.getInputStream()));
                    Scanner sc = new Scanner(in);
                    String name = sc.next();
                    String Title = sc.next();
                    Music m = sharedPlayList.GetMusic(Title);
                    if(m != null)
                        sharedPlayList.setLastMusicOfPerson(name, m);
                    else
                        System.out.println("SomeThing is wrong");
                }
                else {
                    Music wantedMusic = sharedPlayList.GetMusic(subjectRequest);
                    File fileOfMusic;

                    if (wantedMusic != null && wantedMusic.getOwnerId() == id)
                        fileOfMusic = new File(wantedMusic.getPath());
                    else
                        fileOfMusic = null;

                    String ip = "";
                    for (int i = 1; i <socket.getRemoteSocketAddress().toString().length() ; i++) {
                        if (socket.getRemoteSocketAddress().toString().charAt(i)==':')
                            break;
                        ip = ip + socket.getRemoteSocketAddress().toString().charAt(i);

                    }
                    socket.close();
                    socket = new Socket(ip,7070);
                    OutputStream lenOfMusicSender = socket.getOutputStream();
                    PrintWriter setOutThelenOfmusic = new PrintWriter(lenOfMusicSender);
                    setOutThelenOfmusic.println(fileOfMusic.length());
                    setOutThelenOfmusic.close();
                    lenOfMusicSender.close();
                    socket.close();

                    byte[] musicSenderbuffer = new byte[(int) fileOfMusic.length()];
                    BufferedInputStream fileReader = new BufferedInputStream(new FileInputStream(fileOfMusic));
                    fileReader.read(musicSenderbuffer, 0, musicSenderbuffer.length);
                    fileReader.close();

                    socket = new Socket(ip,5050);
                    OutputStream musicSenderGate = socket.getOutputStream();
                    musicSenderGate.write(musicSenderbuffer, 0, musicSenderbuffer.length);
                    musicSenderGate.flush();
                    musicSenderGate.close();
                    socket.close();
                }

            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
                if(flag)
                    return;

            }
        }


    }


    public void end(){
        this.flag = true;
    }
}
