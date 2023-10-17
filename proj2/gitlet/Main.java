package gitlet;

import static gitlet.Utils.*;

import static gitlet.Repository.GITLET_DIR;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Chenni Xu
 */
public class Main {
    public static Repository restoreRepository() {
        if (!Repository.REPOSITORY_FILE.exists()) {
            throw error("Not a Gitlet repo.");
        }
        // Load the repository object from file.
        return readObject(Repository.REPOSITORY_FILE, Repository.class);
    }

    public static void doInit() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system"
                     + " already exists in the current directory.");
        }
        GITLET_DIR.mkdir();
        Repository r = new Repository();
    }

    public static void doAdd(String[] args) {
        Repository r = restoreRepository();
        r.add(args);
        writeObject(Repository.REPOSITORY_FILE, r);
    }

    public static void doCommit(String[] args) {
        Repository r = restoreRepository();
        r.commit(args);
        writeObject(Repository.REPOSITORY_FILE, r);
    }

    public static void doCheckout(String[] args) {
        Repository r = restoreRepository();
        r.checkout(args);
        writeObject(Repository.REPOSITORY_FILE, r);
    }

    public static void doLog(String[] args) {
        Repository r = restoreRepository();
        r.log(args);
    }

    public static void doStatus() {
        Repository r = restoreRepository();
        r.status();
        return;
    }

    public static void doBranch(String[] args) {
        Repository r = restoreRepository();
        r.branch(args);
        writeObject(Repository.REPOSITORY_FILE, r);
    }

    public  static void doRm(String[] args) {
        Repository r = restoreRepository();
        r.rm(args);
        writeObject(Repository.REPOSITORY_FILE, r);
    }

    public  static void doGlobalLog(String[] args) {
        Repository r = restoreRepository();
        r.globalLog(args);
    }

    public  static void doFind(String[] args) {
        Repository r = restoreRepository();
        r.find(args);
    }

    public  static void doRmBranch(String[] args) {
        Repository r = restoreRepository();
        r.rmBranch(args);
        writeObject(Repository.REPOSITORY_FILE, r);
    }

    public  static void doReset(String[] args) {
        Repository r = restoreRepository();
        r.reset(args);
        writeObject(Repository.REPOSITORY_FILE, r);
    }

    public  static void doMerge(String[] args) {
        Repository r = restoreRepository();
        r.merge(args);
        writeObject(Repository.REPOSITORY_FILE, r);
    }
    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        if (!Repository.REPOSITORY_FILE.exists() && !firstArg.equals("init")) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        switch (firstArg) {
            case "init":
                doInit();
                break;
            case "add":
                doAdd(args);
                break;
            case "commit":
                doCommit(args);
                break;
            case "checkout":
                doCheckout(args);
                break;
            case "log":
                doLog(args);
                break;
            case "status":
                doStatus();
                break;
            case "branch":
                doBranch(args);
                break;
            case "rm":
                doRm(args);
                break;
            case "global-log":
                doGlobalLog(args);
                break;
            case "find":
                doFind(args);
                break;
            case "rm-branch":
                doRmBranch(args);
                break;
            case "reset":
                doReset(args);
                break;
            case "merge":
                doMerge(args);
                break;
            default:
                System.out.println("No command with that name exists.");
        }
    }
}
