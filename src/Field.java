public class Field {

    private final int n = 3;
    private final String player1 = "x";
    private final String player2 = "o";
    private final String empty = "n";

    protected String[][] field;
    
    public Field() {
        field = new String[n][];
        for(int i = 0; i < n; i++)
        field[i] = new String[n];
        
        for(int i = 0; i < n; i++)
            for(int h = 0; h < n; h++)
                field[i][h] = empty;
    }

    
    public void setX(int x, int y) {
        field[y][x] = player1;
    }
    public void setZero(int x, int y) {
        field[y][x] = player2;
    }
    
    public boolean setSymbol(Position p, String s) {
        if(!isEmpty(p))
            return false;
        field[p.y][p.x] = s;
        return true;
    }

    public boolean isEmpty(Position p) {
        return field[p.y][p.x].equals(empty);
    }

    public boolean anyEmptyCells() {
        for(int i = 0; i < n; i++)
            for(int h = 0; h < n; h++)
                if(field[i][h].equals(empty))
                    return true;
        return false;
    }

    // int value = 1 or 0
    public void setValue(int x, int y, int value) {
        if(value == 1)
            setX(y, x);
        else
            setZero(y, x);
    }

    public String valueToSymbol(int value) {
        if(value == 0)
            return player2;
        return player1;
    }

    // -2 for no winner
    // -1 for both
    // 0 or 1 = winner`s index
    public int getWinner() {

        if(checkWinner(0))
            return 0;
        if(checkWinner(1))
            return 1;
        if(!anyEmptyCells())
            return -1;

        return -2;
    }

    boolean checkWinner(int playerIndex) {

        String thisPlayerSymbol = valueToSymbol(playerIndex);

        // Vertical
        for(int i = 0; i < n; i++) {
            int counter = 0;
            for(int h = 0; h < n; h++) {
                if(field[i][h].equals(thisPlayerSymbol))
                    counter++;
            }
            if(counter == 3)
                return true;
        }

        // Horizontal
        for(int i = 0; i < n; i++) {
            int counter = 0;
            for(int h = 0; h < n; h++) {
                if(field[h][i].equals(thisPlayerSymbol))
                    counter++;
            }
            if(counter == 3)
                return true;
        }

        // Diagonal
        {
            int counter = 0;
            for(int i = 0; i < n; i++) {
                if(field[i][i].equals(thisPlayerSymbol))
                counter++;
            }
            if(counter == 3)
            return true;
        } 

        {
            int counter = 0;
            for(int i = n - 1; i >= 0; i--) {
                if(field[i][n-i-1].equals(thisPlayerSymbol))
                counter++;
            }
            if(counter == 3)
            return true;
        } 

        return false;
    }

    @Override
    public String toString() {
        String result = "";
        for(int i = 0; i < n; i++) {
            for(int h = 0; h < n; h++)
                result += field[i][h] + " "; 
            result += "\n";
        }
        return result;
    }
}