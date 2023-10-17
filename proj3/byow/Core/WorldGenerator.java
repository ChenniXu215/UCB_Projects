package byow.Core;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

//Generate world with random rooms, and store rooms in an ArrayList.
public class WorldGenerator implements Serializable {
    //Largest room's size
    public static final int MIN_ROOMS = 4;
    public static final int MAX_ROOMS = 14;
    public static final int MIN_ROOM_SIZE = 5;
    public static final int MAX_ROOM_SIZE = Integer.MAX_VALUE;

    public static void genInterface() {
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 60);
        StdDraw.setFont(fontBig);
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT - 10, "CS61B: THE GAME");
        Font fontSmall = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontSmall);
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT / 2, "New Game (N)");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT / 2 - 2, "Load Game (L)");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT / 2 - 4, "Quit (Q)");
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT / 2 - 6, "Replay (R)");
        StdDraw.show();
    }

    //Generate random rooms and store them in ArrayList
    public static ArrayList<Room> genRooms(Random r) {
        int roomNumbers = RandomUtils.uniform(r, MIN_ROOMS, MAX_ROOMS);
        ArrayList<Room> rooms = new ArrayList<Room>();
        int size = 1;
        rooms.add(0, new Room(0, 0, Engine.WIDTH, Engine.HEIGHT));
        while (size < roomNumbers) {
            int index = 0;
            Room removed = rooms.remove(index);
            boolean isVerticalCut = RandomUtils.bernoulli(r);
            if ((isVerticalCut && removed.width / 2 < MIN_ROOM_SIZE)
                    || (!isVerticalCut && removed.height / 2 < MIN_ROOM_SIZE)) {
                rooms.add(removed);
                continue;
            }
            if (isVerticalCut) {
                int split = removed.width / 2;
                Room next1 = new Room(removed.x, removed.y, split, removed.height);
                Room next2 = new Room(removed.x + split,
                        removed.y,
                        removed.width - split,
                        removed.height);
                rooms.add(next1);
                rooms.add(next2);
                size += 1;
            } else {
                int split = removed.height / 2;
                Room next1 = new Room(removed.x, removed.y, removed.width, split);
                Room next2 = new Room(removed.x,
                        removed.y + split,
                        removed.width,
                        removed.height - split);
                rooms.add(next1);
                rooms.add(next2);
                size += 1;
            }

        }
        for (int i = 0; i < rooms.size(); i++) {
            int shrinkWidth = 0;
            int shrinkHeight = 0;
            if (rooms.get(i).width == MIN_ROOM_SIZE) {
                shrinkWidth = rooms.get(i).width;
            } else {
                shrinkWidth = RandomUtils.uniform(r, MIN_ROOM_SIZE, rooms.get(i).width);
                shrinkWidth = Math.min(shrinkWidth, MAX_ROOM_SIZE);
            }
            if (rooms.get(i).height == MIN_ROOM_SIZE) {
                shrinkHeight = rooms.get(i).height;
            } else {
                shrinkHeight = RandomUtils.uniform(r, MIN_ROOM_SIZE, rooms.get(i).height);
                shrinkHeight = Math.min(shrinkHeight, MAX_ROOM_SIZE);
            }
            int shrinkX = rooms.get(i).x + (rooms.get(i).width - shrinkWidth) / 2;
            int shrinkY = rooms.get(i).y + (rooms.get(i).height - shrinkHeight) / 2;
            rooms.set(i, new Room(shrinkX, shrinkY, shrinkWidth, shrinkHeight));
        }
        for (Room rr : rooms) {
            System.out.println(rr);
        }
        return rooms;
    }

    public static ArrayList<Hallway> genHallways(Random r, ArrayList<Room> rooms) {
        ArrayList<Room> notConnectedRooms = new ArrayList<>(rooms);
        ArrayList<Hallway> res = new ArrayList<Hallway>();
        while (notConnectedRooms.size() > 1) {
            ArrayList<Room> nextRoundRooms = new ArrayList<>();
            while (notConnectedRooms.size() > 1) {
                //Randomly removes two rooms from NotConnectedRooms
                int index1 = r.nextInt(0, notConnectedRooms.size());
                int index2 = r.nextInt(0, notConnectedRooms.size());
                while (index1 == index2) {
                    index2 = r.nextInt(0, notConnectedRooms.size());
                }
                Room room1 = notConnectedRooms.get(index1);
                Room room2 = notConnectedRooms.get(index2);
                Hallway newHallway = new Hallway(room1.x + room1.width / 2,
                        room1.y + room1.height / 2,
                        room2.x + room2.width / 2,
                        room2.y + room2.height / 2, r);
                //Add the Hallway to res
                res.add(newHallway);
                notConnectedRooms.remove(room1);
                notConnectedRooms.remove(room2);
                //Randomly pick one of the two room and add it to nextRoundRooms
                if (RandomUtils.bernoulli(r)) {
                    nextRoundRooms.add(room1);
                } else {
                    nextRoundRooms.add(room2);
                }
            }
            if (notConnectedRooms.size() == 1) {
                nextRoundRooms.add(notConnectedRooms.remove(0));
            }
            notConnectedRooms = nextRoundRooms;
        }
        return  res;
    }

}
