package byow.Core;

import java.io.Serializable;

//Every room includes position((x, y)) and size(width and height).
public class Room implements Serializable {
    int x;
    int y;
    int height;
    int width;
    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + width + ", " + height + "]";
    }
}
