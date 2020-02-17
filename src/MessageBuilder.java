public class MessageBuilder {

    public MessageBuilder() {}

    protected final String separator = ":";

    public String constructMessage(int playerID, String... message) {
        String result =  playerID + separator;
        for(int i = 0; i < message.length; i++) {
            result += message[i];
            if(i < message.length - 1)
                result += separator;
        }
        return result; 
    }

    public int getID(String message) { return Integer.parseInt(getWord(message, 0)); }

    // Each word is separated with a separator
    public String getWord(String message, int wordIndex) {
        String words[] = message.split(separator);
        return words[wordIndex];
    }

    public int getNumber(String message, int numberIndex) {
        return Integer.parseInt(getWord(message, numberIndex)); 
    }
}