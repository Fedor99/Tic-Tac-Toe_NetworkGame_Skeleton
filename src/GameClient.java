import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.ArrayList;


public class GameClient {

    public static void main(String args[]) throws Exception {

        new GameClient(args[0], Integer.parseInt(args[1]));
    }

    public GameClient(String serverAddress, int port) { 
        this.serverAddress = serverAddress;
        this.port = port;
        start(); 
    }

    String serverAddress = "";
    int port = -1;

    Field field = new Field();
    int playerID = -1;

    DataOutputStream outToServer;
    DataInputStream inFromServer;
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

    MessageBuilder mb = new MessageBuilder();

    void start() {
        
        try {
            Socket socket = new Socket(serverAddress, port);
            outToServer = new DataOutputStream(socket.getOutputStream()); 
            inFromServer = new DataInputStream( 
                new BufferedInputStream(socket.getInputStream())); 

            while(true) {
                //-----------------------------------------------------------------------------------------------------------------
                String received = readString();
                if(playerID == -1) {
                    playerID = mb.getID(received);
                    System.out.println("New ID: " + playerID);
                }

                System.out.println("Connected");

                //-----------------------------------------------------------------------------------------------------------------

                while(true) {
                    playerProcess();
                }
            }
        }
        catch(Exception e) {e.printStackTrace();}
    }

    void playerProcess() throws Exception {
        
        String fromUser = readStringFromUser().trim();
        sendString(mb.constructMessage(playerID, fromUser));

        if(fromUser.equals(CommunicationOrder.LOGOUT)) {
            outToServer.close();
            inFromServer.close();
            System.out.println("Connection closed");
            System.exit(0);
        }
        
        boolean winnerFound = false;
        boolean exit = false;

        while(!winnerFound && !exit) {

            //System.out.println("Receiving");
            String received = readString();
            System.out.println("Received: " + received);
            String message = mb.getWord(received, 1);

            switch(message) {
                case CommunicationOrder.YOUR_TURN:
                    System.out.println("Your turn");

                    sendString(mb.constructMessage(
                                    playerID, 
                                    getPositionFromUser().toString()
                                    ));
                    updateMatrix();
                break;
                case CommunicationOrder.SKIP:
                    updateMatrix();
                break;
                case CommunicationOrder.WINNER:
                {
                    int winner = Integer.parseInt(mb.getWord(received, 2));
                    System.out.println("Winner ID: " + winner);
                    System.out.println("You win!");
                    winnerFound = true;
                }
                break;
                case CommunicationOrder.DEFEATED:
                {
                    int winner = Integer.parseInt(mb.getWord(received, 2));
                    System.out.println("Winner ID: " + winner);
                    System.out.println("You lose");
                    winnerFound = true;
                }
                break;
                case CommunicationOrder.NO_WINNER:
                   exit = true;
                break;
                case CommunicationOrder.INFO_MESSAGE:
                    System.out.println("Info message");
                    exit = true;
                break;
                default:
                break;
            }
        }
    }

    Position getPositionFromUser() throws Exception {
        Position result = new Position().fromString(readStringFromUser());
        while(!field.isEmpty(result)) {
            System.out.println("This position is already taken");
            result = new Position().fromString(readStringFromUser());
        }
        return result;
    }

    void updateMatrix() throws Exception {
        String update = readString();
        Position p = new Position().fromString(mb.getWord(update, 2));

        field.setSymbol(p, mb.getWord(update, 3));

        System.out.println(field);
    }

    // Input should be in format "x y"
    String nextMove() throws Exception {
        return inFromUser.readLine();
    }

    void sendString(String str) throws Exception {
        outToServer.writeUTF(str);
        outToServer.flush();
    }

    String readString() throws Exception {
        return inFromServer.readUTF();
    }

    String readStringFromUser() throws Exception {
        System.out.println("User input : ");
        return inFromUser.readLine();
    }
}