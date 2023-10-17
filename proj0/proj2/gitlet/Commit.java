package gitlet;

import static gitlet.Utils.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;


/** Represents a gitlet commit object.
 *  @author Chenni Xu
 */
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */


    public static final String LOG_FORMAT = "===\ncommit %s\nDate: %s\n%s\n";
    /** The message of this Commit. */
    private String message;
    private String timestamp;
    private HashMap<String, String> fileMap; // file name -> blob hash id
    private String parent; // parent commit hashcode
    private String hash;

    private static String getCurrentTimestamp() {
        String timestamp = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z").format(new Date());
        return timestamp;
    }

    public Commit(String message, HashMap<String, String> fileMap, String parent) {
        this.message = message;
        this.fileMap = fileMap;
        this.parent = parent;
        this.timestamp = getCurrentTimestamp();
        this.hash = sha1(message, String.valueOf(System.currentTimeMillis()));
    }
    public HashMap<String, String> getFileMap() {
        return fileMap;
    }

    public String getHash() {
        return hash;
    }

    public String toString() {
        return String.format(LOG_FORMAT, hash, timestamp.toString(), message);
    }

    public String getParent() {
        return parent;
    }

    public String getMessage() {
        return message;
    }
}
