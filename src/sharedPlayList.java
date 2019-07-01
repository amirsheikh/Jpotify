import javax.swing.*;
import javax.swing.text.html.parser.Entity;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class sharedPlayList extends PlayList implements addAndDelete, Serializable {
    private Owner owner;
    private List<String> PeopleInThisPlayList = new ArrayList<>();
    private HashMap<String,Music> LastMusicOfPerson = new HashMap<>();
    private HashMap<String, FriendForm> listOfFriendForms = new HashMap<>();
    private List<FriendForm> friendForms = new ArrayList<>();
    private GUI gui;

    public sharedPlayList(Owner owner , GUI gui){
        super("Shared Playlist",false);
        this.owner = owner;
        owner.setSharedPlayList(this);
        this.gui = gui;
    }
    @Override
    public void Add(Music music) {
        super.Add(music);
        System.out.println("Added By Shared");
        music.setOnSharedList(true);
        List<String> listOfIPFriendToPost = owner.getListOfip();
        List<Integer> listOfIDFriendToPost = owner.getListOfid();

        for(int i=0;i<listOfIPFriendToPost.size();i++){
            String IPtmp = listOfIPFriendToPost.get(i);
            System.out.println(IPtmp);
            int IDtmp = listOfIDFriendToPost.get(i);
            try {
                Socket tmp = new Socket(IPtmp,owner.getId());
                OutputStream request = tmp.getOutputStream();
                PrintWriter outRequest = new PrintWriter(request);
                outRequest.println("newMusic");
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
                Servertmp.close();

                if(Response.equals("ImReady")){
                    tmp = new Socket(IPtmp,owner.getId());
                    ObjectOutputStream SenderMusic = new
                            ObjectOutputStream(tmp.getOutputStream());
                    SenderMusic.writeObject(music);
                    SenderMusic.close();
                    tmp.close();

                }
            }catch(Exception e){
                System.out.println("Socket:"  + "is Sleep Via error: " + e);
            }
        }
    }

    @Override
    public void Delete(Music music) {
        super.Delete(music);
        music.setOnSharedList(false);
        if(music.getOwnerIP().equals(this.owner.getIp())){
            List<String> listOfIPFriendToPost = owner.getListOfip();
            for(int i=0;i<listOfIPFriendToPost.size();i++){
                String IPtmp = listOfIPFriendToPost.get(i);
                try {
                    Socket tmp = new Socket(IPtmp,owner.getId());
                    OutputStream request = tmp.getOutputStream();
                    PrintWriter outRequest = new PrintWriter(request);
                    outRequest.println("RemoveMusic");
                    request.close();
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
                    tmp.close();
                    if(Response.equals("Ok")){
                        tmp = new Socket(IPtmp,owner.getId());
                        request = tmp.getOutputStream();
                        outRequest = new PrintWriter(request);
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
    }
    public void AddToNet(Music music){super.Add(music);}
    public void addPeopleToPlayList(String name){
        this.PeopleInThisPlayList.add(name);
        FriendForm tmpFriendForm = new FriendForm(name, "none");
        listOfFriendForms.put(name , tmpFriendForm);
        friendForms.add(tmpFriendForm);
        System.out.println("1");
        gui.getUsersListPanel().removeAll();
        System.out.println("31");
        gui.getUsersListPanel().repaint();
        System.out.println("133");
        gui.getUsersListPanel().revalidate();
        System.out.println("144");
        for (FriendForm f : friendForms){
            gui.getUsersListPanel().add(f.getMainPanel());
            System.out.println("551");
        }
        System.out.println("16");
        gui.getUsersListPanel().revalidate();


    }
    public void removePeopleFromPlayList(String name){
        this.PeopleInThisPlayList.remove(name);
    }
    public void setLastMusicOfPerson(String Name,Music music){
        LastMusicOfPerson.put(Name,music);
        listOfFriendForms.get(Name).changeLastMusic(music.getTitle());

    }
    public Music getLastMusicOfPerson(String name){
        return LastMusicOfPerson.get(name);
    }
}