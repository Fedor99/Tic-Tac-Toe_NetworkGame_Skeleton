
public class Match extends Thread {

    Player players[] = new Player[2];
    Field field = new Field();
    int startWith = -1;

    MessageBuilder mb = new MessageBuilder();

    public Match(Player player1, Player player2) {
        players[0] = player1;
        players[1] = player2;
        startWith = randomPlayer();

        System.out.println("New match arranged");
    }

    public void run() {
        try {

            int winner = field.getWinner();

            while(winner == -2) {
                Player thisPlayer = players[startWith];
                Player nextPlayer = players[inverse(startWith)];
                thisPlayer.send(
                    mb.constructMessage(thisPlayer.getID(), 
                    CommunicationOrder.YOUR_TURN)
                    );
                nextPlayer.send(
                    mb.constructMessage(nextPlayer.getID(), 
                    CommunicationOrder.SKIP)
                    );

                Position newPosition = receivePosition(thisPlayer);

                System.out.println("new position: " + newPosition);

                String thisPlayerSymbol = field.valueToSymbol(startWith);
                field.setSymbol(newPosition, thisPlayerSymbol);

                System.out.println(field);

                sendUpdateToPlayer(startWith, newPosition, thisPlayerSymbol);
                sendUpdateToPlayer(inverse(startWith), newPosition, thisPlayerSymbol);

                startWith = inverse(startWith);

                // get winner if there is one at this point
                winner = field.getWinner();

                System.out.println("result: " + winner);
            }     
            
            sendWinnerInfo(winner);

        } catch(Exception e) {e.printStackTrace();}

        players[0].setIsPlaying(false);
        players[1].setIsPlaying(false);
    }

    Position receivePosition(Player player) throws Exception {
        String received = player.readString();
        System.out.println("received on match: " + received);

        Position newPosition = 
            new Position().fromString(
                mb.getWord(received, 1));
        return newPosition;
    }

    void sendWinnerInfo(int winner) throws Exception {

        if(winner == -1) {
            for(int i = 0; i < 2; i++) {
                players[i].send(
                    mb.constructMessage(
                        players[i].getID(), 
                        CommunicationOrder.NO_WINNER,
                        winner + "")
                    );
            }
        }

        players[winner].send(
            mb.constructMessage(
                players[winner].getID(), 
                CommunicationOrder.WINNER,
                winner + "")
            );

        for(int i = 0; i < 2; i++) {
            if(i != winner)
            players[i].send(
                mb.constructMessage(
                    players[i].getID(), 
                    CommunicationOrder.DEFEATED,
                    winner + "")
                );
        }
    }

    // playerIndex = 1 or 0
    void sendUpdateToPlayer(int playerIndex, Position newPosition, String symbol) throws Exception {
        players[playerIndex].send(mb.constructMessage(
            players[playerIndex].getID(), 
            CommunicationOrder.UPDATE,
            newPosition.toString(), 
            symbol)
            );
    }

    int randomPlayer() {
        if(Math.random() < 0.5)
            return 0;
        return 1;
    }

    int inverse(int value) {
        if(value == 1)
            return 0;
        return 1;
    }
}