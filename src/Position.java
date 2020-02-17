
public class Position {

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position() {
        x = -1; y = -1;
    }

    public final int x, y;

    // For a string in format 'x y'
    public Position fromString(String str) {
        int newX, newY;
        String parts[] = str.split(" ");

        newX = Integer.parseInt(parts[0]);
        newY = Integer.parseInt(parts[1]);
        return new Position(newX, newY);
    }

    @Override
    public String toString() {
        return x + " " + y;
    }
}