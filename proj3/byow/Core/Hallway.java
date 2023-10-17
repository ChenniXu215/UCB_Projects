package byow.Core;

import java.io.Serializable;
import java.util.Random;

public class Hallway implements Serializable {
    int startX;
    int startY;
    int endX;
    int endY;
    int width;

    public Hallway(int startX, int startY, int endX, int endY, Random r) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        if (RandomUtils.bernoulli(r)) {
            this.width = 3;
        } else {
            this.width = 4;
        }
    }


}
