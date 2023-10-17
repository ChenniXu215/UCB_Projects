package byow.Core;

import byow.TileEngine.TETile;

import java.io.IOException;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 2) {
            System.out.println("Can only have two arguments - the flag and input string");
            System.exit(0);
        } else if (args.length == 2) {
            Engine engine = new Engine();
            TETile[][] addWorld = new TETile[0][];
            addWorld = engine.interactWithInputString(args[1]);
            engine.getRenderer().initialize(Engine.WIDTH, Engine.HEIGHT);
            engine.getRenderer().renderFrame(addWorld);
        // DO NOT CHANGE THESE LINES YET ;)
//        } else if (args.length == 2 &&nq args[0].equals("-p")) {
//            System.out.println("Coming soon.");
        } else {
            Engine engine = new Engine();
            engine.interactWithKeyboard();
        }
    }
}
