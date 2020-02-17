import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;
import java.*;

public class Player {

    protected boolean _isPlaying = false;

    protected DataOutputStream out;
    protected DataInputStream in;

    protected Match match;

    private String playerIP;
    private int playerPort;
    private int playerID;
    public Player(String playerIP, int playerPor, int playerID) {
        this.playerIP = playerIP;
        this.playerPort = playerPort;
        this.playerID = playerID;
    }

    public String getIP() { return playerIP; }
    public int getPort() { return playerPort; }
    public int getID() { return playerID; }

    public void send(String s) throws Exception {
        out.writeUTF(s);
        out.flush();
    }
    public String readString() throws Exception {
        return in.readUTF();
    }

    public synchronized void setMatch(Match match) {this.match = match;}
    public synchronized Match getMatch() { return match; }

    public synchronized void setIsPlaying(boolean value) { 
        _isPlaying = value; 
        System.out.println(this);
    }
    public synchronized boolean isPlaying() {return _isPlaying; }

    public synchronized void setOutStream(DataOutputStream out) {
        this.out = out;
    }
    public synchronized void setInStream(DataInputStream in) {
        this.in = in;
    }
    public synchronized DataOutputStream getOutStream() { return out; }
    public synchronized DataInputStream getInStream() { return in; }


    @Override
    public String toString() {
        return "Player (playerIP: " + 
        playerIP + ", playerPort: " +
        playerPort + ", playerID: " + 
        playerID + ", isPlaying: " + isPlaying() + ")";
    }

    @Override
    public boolean equals(Object o) {   
        if (o == this) { 
            return true; 
        } 
  
        if (!(o instanceof Player)) { 
            return false; 
        } 

        Player c = (Player) o; 
        return playerIP.equals(c.getIP()) && 
                playerPort == c.getPort() &&
                playerID == c.getID();
    }
}