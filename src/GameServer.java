import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.ArrayList;
import java.*;

public class GameServer extends Thread {

    public static void main(String args[]) throws Exception {
        int newPort = Integer.parseInt(args[0]);

        welcomeSocket = new ServerSocket(newPort);
        GameServer newOne = new GameServer(new MatchFactory());
        newOne.acceptNext();
    }

    public GameServer(MatchFactory mf) { this.mf = mf; }

    public void run() {
        playerAuthorization();
    }

    MatchFactory mf;
    static PlayerManager pm = new PlayerManager();

    static ServerSocket welcomeSocket;
    Socket connectionSocket;

    Player player;
    MessageBuilder mb = new MessageBuilder();

    void playerAuthorization() {

        try {

        connectionSocket = welcomeSocket.accept();
        acceptNext();

        authorize();
        
        while(true) {
            communication();
        }

        } catch(Exception e) {e.printStackTrace();}
    }

    void authorize() throws Exception {
        player = new Player(
            connectionSocket.getInetAddress().toString(), 
            connectionSocket.getLocalPort(), pm.getFreePlayerID());

        player.setInStream(new DataInputStream( 
            new BufferedInputStream(connectionSocket.getInputStream())));
        player.setOutStream(new DataOutputStream(connectionSocket.getOutputStream()));

        pm.players.add(player);
        send(mb.constructMessage(player.getID(), ""));
        System.out.println("New player authorized");
    }

    void communication() throws Exception {

        if(!player.isPlaying()) {

            String received = readString();
            String message = mb.getWord(received, 1);

            System.out.println("Received: " + message);
            System.out.println(player);
            switch(message) {
                case CommunicationOrder.GET_LIST:
                    send(mb.constructMessage(
                        mb.getID(received), 
                        CommunicationOrder.INFO_MESSAGE,
                        pm.players.toString()));
                    System.out.println("Received: " + message);
                break;
                case CommunicationOrder.PLAY:
                    pm.readyToPlay.add(player);
                    // try to create a match
                    // while player is in the match, isPlaying = true
                    createMatch();
                    player.setIsPlaying(true);
                break;
                case CommunicationOrder.LOGOUT:
                    player.getOutStream().close();
                    player.getInStream().close();
                    pm.getPlayers().remove(player);
                    pm.freeIDs.add(player.getID());
                break;
                default:
                break;
            }
        }
    }

    void createMatch() {
        if(pm.readyToPlay.size() >= 2) {

            Player p1 = pm.readyToPlay.get(0);
            Player p2 = pm.readyToPlay.get(1);

            mf.createMatch(p1, p2);

            pm.readyToPlay.remove(p1);
            pm.readyToPlay.remove(p2);
        }
    }

    void send(String s) throws Exception {
        player.send(s);
    }
    String readString() throws Exception {
        return player.readString();
    }

    void acceptNext() {
        new GameServer(mf).start();
    }
}