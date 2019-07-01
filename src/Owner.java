import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Owner implements Serializable {
    private int id;
    private String ip;
    private String name;

    private List<String> listOfFriendName= new ArrayList<>();
    private List<ServerSocket> listOfServersocket = new ArrayList<>();
    private HashMap<String,ServerSocket> hashOfSerever = new HashMap<>();
    private HashMap<String,threadForEachFriend> hashOfThread = new HashMap<>();
    private List<String> listOfIP = new ArrayList<>();
    private List<Integer> listOfID = new ArrayList<>();
    private HashMap<String,String> hashOfIP = new HashMap<>();
    private HashMap<String,Integer> hashOfID = new HashMap<>();
    private ArrayList<PlayList> playLists;

    private sharedPlayList sharedPlayList;

    public String getName() {
        return name;
    }

    public Owner(int id, ArrayList<PlayList> playLists){
        this.playLists = playLists;
        this.id = id;
        com.sun.security.auth.module.NTSystem NTSystem = new com.sun.security.auth.module.NTSystem();
        name = NTSystem.getName();
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            ip = (localhost.getHostAddress()).trim();
            // ip = "192.168.43.32";
            ServerSocket tmp1 = new ServerSocket(id);
            Owner owner = this;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Socket socket = tmp1.accept();
                            DataInputStream WhatsWrong = new DataInputStream(
                                    new BufferedInputStream(socket.getInputStream()));
                            Scanner getWhatsWrong = new Scanner(WhatsWrong);
                            String subjectRequest = getWhatsWrong.next();
                            String[] parts = subjectRequest.split("_");
                            String requestedName = parts[0];
                            String requestedIP = parts[1];
                            String requestedID = parts[2];
                            System.out.println("Name: " + requestedName + "\nIP: " + requestedIP + "\nID: " + requestedID);
                            String ip = "";
                            for (int i = 1; i <socket.getRemoteSocketAddress().toString().length() ; i++) {
                                if (socket.getRemoteSocketAddress().toString().charAt(i)==':')
                                    break;
                                ip = ip + socket.getRemoteSocketAddress().toString().charAt(i);

                            }
                            System.out.println(ip);
                            socket = new Socket(ip,7070);
                            OutputStream response = socket.getOutputStream();
                            PrintWriter setOutTheRequest = new PrintWriter(response);
                            JFrame frame = new JFrame("Friend Request");
                            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                            RequestForm requestForm = new RequestForm(frame , setOutTheRequest , owner, requestedName, requestedIP, requestedID);
                            frame.setLocation(dim.width/2-requestForm.getMainPanel().getSize().width/2, dim.height/2-requestForm.getMainPanel().getSize().height/2);
                            requestForm.getRequestLabel().setText("Do you want to add " + requestedName + " as your friend??!");
                            frame.setContentPane(requestForm.getMainPanel());
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            frame.pack();
                            frame.setVisible(true);
                            getWhatsWrong.close();
                            WhatsWrong.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            System.out.println(name + " " + ip);
        }catch(Exception e){
            System.out.println(e);
            return;
        }
    }

    public void accept(int id, String name, String ip){
        try {
            listOfFriendName.add(name);
            sharedPlayList.addPeopleToPlayList(name);
            ServerSocket serverSocket = new ServerSocket(id);
            hashOfIP.put(name, ip);
            hashOfID.put(name, id);
            listOfID.add(id);
            listOfIP.add(ip);
            threadForEachFriend forEachFriend = new threadForEachFriend(serverSocket, sharedPlayList, this.id, playLists);
            hashOfThread.put(name, forEachFriend);
            new Thread(forEachFriend).start();

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void addFriend(String ip,String name,int id) {

        try {
            Socket tmp = new Socket(ip, id);
            OutputStream Request = tmp.getOutputStream();
            PrintWriter setOutTheRequest = new PrintWriter(Request);
            String s = this.name + "_" + this.ip + "_" + this.id;
            setOutTheRequest.println(s);
            setOutTheRequest.close();
            tmp.close();

            ServerSocket tmp1 = new ServerSocket(7070);
            tmp = tmp1.accept();
            DataInputStream result = new DataInputStream(
                    new BufferedInputStream(tmp.getInputStream()));
            Scanner getWhatsWrong = new Scanner(result);
            String resultString = getWhatsWrong.next();
            getWhatsWrong.close();
            setOutTheRequest.close();
            result.close();
            tmp1.close();
            System.out.println(resultString);
            if (resultString.equals("no")) {
                return;
            }
            listOfFriendName.add(name);
            sharedPlayList.addPeopleToPlayList(name);

            tmp.close();
            tmp1 = new ServerSocket(id);
            listOfServersocket.add(tmp1);
            hashOfSerever.put(name, tmp1);

            listOfIP.add(ip);
            listOfID.add(id);

            hashOfIP.put(name, ip);
            hashOfID.put(name, id);
            threadForEachFriend tmp2 = new threadForEachFriend(tmp1, sharedPlayList, this.id, playLists);
            hashOfThread.put(name, tmp2);
            new Thread(tmp2).start();

        } catch (Exception e) {
            System.out.println(e);
            System.out.println(name + ": this person is not registered or online yet");
            return;
        }
    }
    public void removeFriend(String name){
        sharedPlayList.removePeopleFromPlayList(name);
        ServerSocket tmp1 = hashOfSerever.get(name);
        int ID = hashOfID.get(name);
        String IP = hashOfIP.get(name);
        hashOfSerever.remove(name);
        hashOfIP.remove(name);
        hashOfID.remove(name);
        listOfIP.remove(IP);
        listOfID.remove(ID);
        listOfServersocket.remove(tmp1);
        try {
            tmp1.close();
            hashOfThread.get(name).end();
            hashOfThread.remove(name);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public List<ServerSocket> getListOfServersocket() {
        return listOfServersocket;
    }

    public List<String> getListOfip() {
        return listOfIP;
    }
    public List<Integer> getListOfid() {
        return listOfID;
    }

    public int getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public void setSharedPlayList(sharedPlayList sharedPlayList) {
        this.sharedPlayList = sharedPlayList;
    }


}