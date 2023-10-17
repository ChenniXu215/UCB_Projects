package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import static byow.Core.Engine.*;

//Draw the rooms on the tile.
public class World implements Serializable {
    private TETile[][] world;
    private TETile[][] initialWorld;
    private Avatar avatar;
    private ArrayList<Room> rooms;
    private ArrayList<Hallway> hallways;
    Random r;
    boolean isDark = false;
    public static final int VISIBLE_SIZE = 5;
    ArrayList<Character> executedMoves = new ArrayList<Character>();


    public World(long seed) {
        this.world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        this.r = new Random(seed);
        this.rooms = WorldGenerator.genRooms(r);
        drawRooms();
        this.hallways = WorldGenerator.genHallways(r, rooms);
        drawHallways();
        drawWalls();
        this.initialWorld = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                initialWorld[x][y] = world[x][y];
            }
        }
        this.avatar = new Avatar(r, world);
    }


    public void dimWorld() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if ((Math.abs(i - avatar.getX()) * Math.abs(i - avatar.getX())
                        + Math.abs(j - avatar.getY()) * Math.abs(j - avatar.getY()))
                        > VISIBLE_SIZE * VISIBLE_SIZE) {
                    world[i][j] = Tileset.NOTHING;
                } else {
                    world[i][j] = initialWorld[i][j];
                }
            }
        }
        world[avatar.getX()][avatar.getY()] = Tileset.AVATAR;
    }

    public void lightWorld() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = initialWorld[i][j];
            }
        }
    }

    public void replay(int mode, TERenderer ter) {
        // Clean the avatar tile in the destination position
        world[avatar.getX()][avatar.getY()] = Tileset.FLOOR;
        for (char move : executedMoves) {
            if (move == 'W') {
                avatar.setY(avatar.getY() - 1);
            }
            if (move == 'S') {
                avatar.setY(avatar.getY() + 1);
            }
            if (move == 'A') {
                avatar.setX(avatar.getX() + 1);
            }
            if (move == 'D') {
                avatar.setX(avatar.getX() - 1);
            }
            if (move == 'Z') {
                isDark = !isDark;
            }
        }
        world[avatar.getX()][avatar.getY()] = Tileset.AVATAR;
        if (mode == KEYBOARD_MODE) {
            ter.renderFrame(world);
            for (char move : executedMoves) {
                StdDraw.pause(500);
                move(move, true);
                ter.renderFrame(world);
            }
        }
        executedMoves = new ArrayList<>();
    }


    public void move(char direction, boolean isReplaying) {
        direction = Character.toUpperCase(direction);
        world[avatar.getX()][avatar.getY()] = Tileset.FLOOR;
        switch (direction) {
            case 'W':
                if (initialWorld[avatar.getX()][avatar.getY() + 1].equals(Tileset.FLOOR)) {
                    // Record effective moves for the replaying mode
                    if (!isReplaying) {
                        executedMoves.add(direction);
                    }
                    avatar.setY(avatar.getY() + 1);
                    if (isDark) {
                        dimWorld();
                    }
                }
                break;
            case 'S':
                if (initialWorld[avatar.getX()][avatar.getY() - 1].equals(Tileset.FLOOR)) {
                    if (!isReplaying) {
                        executedMoves.add(direction);
                    }
                    avatar.setY(avatar.getY() - 1);
                    if (isDark) {
                        dimWorld();
                    }
                }
                break;
            case 'A':
                if (initialWorld[avatar.getX() - 1][avatar.getY()].equals(Tileset.FLOOR)) {
                    if (!isReplaying) {
                        executedMoves.add(direction);
                    }
                    avatar.setX(avatar.getX() - 1);
                    if (isDark) {
                        dimWorld();
                    }
                }
                break;
            case 'D':
                if (initialWorld[avatar.getX() + 1][avatar.getY()].equals(Tileset.FLOOR)) {
                    if (!isReplaying) {
                        executedMoves.add(direction);
                    }
                    avatar.setX(avatar.getX() + 1);
                    if (isDark) {
                        dimWorld();
                    }
                }
                break;
            case 'Z':
                isDark = !isDark;
                if (!isReplaying) {
                    executedMoves.add(direction);
                }
                if (isDark) {
                    dimWorld();
                } else {
                    lightWorld();
                }
                break;
            default:
                System.out.println("Unrecognized move.");
        }
        world[avatar.getX()][avatar.getY()] = Tileset.AVATAR;
    }


    public void drawRoom(Room room) {
        for (int i = room.x; i < Math.min(room.x + room.width, WIDTH); i++) {
            for (int j = room.y; j < Math.min(room.y + room.height, HEIGHT); j++) {
                world[i][j] = Tileset.FLOOR;
            }
        }
    }

    public void drawRooms() {
        for (int i = 0; i < rooms.size(); i++) {
            drawRoom(rooms.get(i));
        }
    }

    public void drawWalls() {
        for (int i = 1; i < WIDTH - 1; i++) {
            for (int j = 1; j < HEIGHT - 1; j++) {
                if (world[i][j].equals(Tileset.FLOOR)) {
                    if (world[i - 1][j + 1].equals(Tileset.NOTHING)
                            || world[i][j + 1].equals(Tileset.NOTHING)
                            || world[i + 1][j + 1].equals(Tileset.NOTHING)
                            || world[i - 1][j].equals(Tileset.NOTHING)
                            || world[i + 1][j].equals(Tileset.NOTHING)
                            || world[i - 1][j - 1].equals(Tileset.NOTHING)
                            || world[i][j - 1].equals(Tileset.NOTHING)
                            || world[i + 1][j - 1].equals(Tileset.NOTHING)) {
                        world[i][j] = Tileset.WALL;
                    }
                }
            }
        }
        for (int i = 0; i < WIDTH; i++) {
            if (world[i][HEIGHT - 1].equals(Tileset.FLOOR)) {
                world[i][HEIGHT - 1] = Tileset.WALL;
            }
        }
        for (int i = 0; i < WIDTH; i++) {
            if (world[i][0].equals(Tileset.FLOOR)) {
                world[i][0] = Tileset.WALL;
            }
        }
        for (int i = 0; i < HEIGHT; i++) {
            if (world[0][i].equals(Tileset.FLOOR)) {
                world[0][i] = Tileset.WALL;
            }
        }
        for (int i = 0; i < HEIGHT; i++) {
            if (world[WIDTH - 1][i].equals(Tileset.FLOOR)) {
                world[WIDTH - 1][i] = Tileset.WALL;
            }
        }
    }


    public void drawHallway(Hallway hallway) {
        //Determine smallerX and its y, biggerX and its y
        int smallerX = 0;
        int smallerXY = 0;
        int biggerX = 0;
        int biggerXY = 0;
        int smallerY = 0;
        int biggerY = 0;
        if (hallway.startX < hallway.endX) {
            smallerX = hallway.startX;
            smallerXY = hallway.startY;
            biggerX = hallway.endX;
            biggerXY = hallway.endY;
            if (hallway.startY < hallway.endY) {
                smallerY = hallway.startY;
                biggerY = hallway.endY;
            } else {
                smallerY = hallway.endY;
                biggerY = hallway.startY;
            }
        } else {
            smallerX = hallway.endX;
            smallerXY = hallway.endY;
            biggerX = hallway.startX;
            biggerXY = hallway.startY;
            if (hallway.startY < hallway.endY) {
                smallerY = hallway.endY;
                biggerY = hallway.startY;
            } else {
                smallerY = hallway.startY;
                biggerY = hallway.endY;
            }
        }
        //Draw hallways
        if (smallerXY < biggerXY) {
            Room room1 = new Room(smallerX, smallerXY, biggerX - smallerX, hallway.width);
            Room room2 = new Room(biggerX - hallway.width,
                    smallerXY,
                    hallway.width,
                    biggerXY - smallerXY);
            drawRoom(room1);
            drawRoom(room2);
        } else {
            Room room1 = new Room(smallerX, smallerXY, biggerX - smallerX, hallway.width);
            Room room2 = new Room(biggerX,
                    biggerXY,
                    hallway.width,
                    smallerXY + hallway.width - biggerXY);
            drawRoom(room1);
            drawRoom(room2);
        }
    }

    public void cleanWalls() {
        for (int i = 1; i < WIDTH - 1; i++) {
            for (int j = 1; i < HEIGHT - 1; j++) {
                if (world[i][j].equals(Tileset.WALL)) {
                    if ((world[i - 1][j].equals(Tileset.FLOOR))
                            && (world[i + 1][j].equals(Tileset.FLOOR))) {
                        world[i][j] = Tileset.FLOOR;
                    } else if ((world[i][j - 1].equals(Tileset.FLOOR))
                            && (world[i][j + 1].equals(Tileset.FLOOR))) {
                        world[i][j] = Tileset.FLOOR;
                    } else {
                        return;
                    }
                }
            }
        }
    }


    public void drawHallways() {
        for (int i = 0; i < hallways.size(); i++) {
            drawHallway(hallways.get(i));
        }
    }

    public TETile[][] getWorld() {
        return world;
    }

}
