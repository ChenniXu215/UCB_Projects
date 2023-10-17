package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Engine implements Serializable {
    private static TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public static final int HEIGHT_ALL = 65;
    private World world;
    public static final int KEYBOARD_MODE = 0;
    public static final int STRING_INPUT_MODE = 1;
    public static final char ENDING_MARK = 0;
    public static final int HUD_HEIGHT = 3;
    public static final File CWD = new File(System.getProperty("user.dir"));
    String inputString;
    int nextCharIdx = 0;
    int interactMode; // KEYBOARD or STRING_INPUT

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public TERenderer getRenderer() {
        return ter;
    }

    public void interactWithKeyboard() {
        interactMode = KEYBOARD_MODE;
        ter.initialize(WIDTH, HEIGHT + HUD_HEIGHT);
        WorldGenerator.genInterface();
        executeProgram();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        interactMode = STRING_INPUT_MODE;
        inputString = input;
        executeProgram();
        return world.getWorld();
    }

    public long getSeed() {
        String seed = "";
        long res = 0;
        char curr = getNextChar();
        while (curr != 's') {
            seed += curr;
            curr = getNextChar();
        }
        try {
            res = Long.parseLong(seed);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return res;
    }

    public char getNextChar() {
        char res;
        if (interactMode == STRING_INPUT_MODE) {
            if (nextCharIdx >= inputString.length()) {
                return ENDING_MARK;
            }
            res = inputString.toCharArray()[nextCharIdx];
            nextCharIdx++;
        } else {
            while (!StdDraw.hasNextKeyTyped()) {
                if (world != null && world.getWorld() != null) {
                    drawHUD();
                }
            }
            res = StdDraw.nextKeyTyped();
        }
        return res;
    }

    public void drawHUD() {
        if (interactMode == KEYBOARD_MODE) {
            StdDraw.clear();
            ter.renderFrame(world.getWorld());
            int x = (int) StdDraw.mouseX();
            int y = (int) StdDraw.mouseY();
            StdDraw.setPenColor(Color.WHITE);
            Font fontBig = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(fontBig);
            if (x < WIDTH && y < HEIGHT) {
                StdDraw.textLeft(0, HEIGHT + HUD_HEIGHT - 1, world.getWorld()[x][y].description());
            }
            StdDraw.show();
        }
    }

    public void executeMoves() {
        while (true) {
            drawHUD();
            char nextMove = getNextChar();
            switch (nextMove) {
                case ENDING_MARK:
                case ':':
                    return;
                default:
                    world.move(nextMove, false);
            }
            if (interactMode == KEYBOARD_MODE) {
                ter.renderFrame(world.getWorld());
            }
        }
    }

    public void executeProgram() {
        char option = Character.toUpperCase(getNextChar());
        switch (option) {
            case 'N':
                long seed = getSeed();
                world = new World(seed);
                break;
            case 'L':
                loadState();
                break;
            case 'Q':
                System.exit(0);
                break;
            case 'R':
                loadState();
                world.replay(interactMode, ter);
                break;
            default:
                return;
        }
        if (interactMode == KEYBOARD_MODE) {
            ter.renderFrame(world.getWorld());
        }
        executeMoves();
        char endChar = getNextChar();
        saveState();
        if (interactMode == KEYBOARD_MODE) {
            System.exit(0);
        }
    }

    public void saveState() {
        File worldFile = Utils.join(CWD, "world.txt");
        if (!worldFile.exists()) {
            try {
                worldFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Utils.writeObject(worldFile, world);
    }

    public void loadState() {
        File worldFile = Utils.join(CWD, "world.txt");
        world = Utils.readObject(worldFile, byow.Core.World.class);
    }

}
