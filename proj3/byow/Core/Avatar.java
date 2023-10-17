package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

public class Avatar implements Serializable {
    private int x;
    private int y;

    public Avatar(Random r, TETile[][] world) {
        this.x = r.nextInt(0, Engine.WIDTH);
        this.y = r.nextInt(0, Engine.HEIGHT);
        while (!world[x][y].equals(Tileset.FLOOR)) {
//            this.x = RandomUtils.uniform(r, 0, r.nextInt(0, Engine.WIDTH););
//            this.y = RandomUtils.uniform(r, 0, Engine.HEIGHT);
            this.x = r.nextInt(0, Engine.WIDTH);
            this.y = r.nextInt(0, Engine.HEIGHT);
        }
        world[x][y] = Tileset.AVATAR;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

}
