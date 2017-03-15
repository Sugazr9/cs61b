package db;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.nio.*;

public class Database {
    private final HashMap<String, Table> storage;
    private final Keywords keywords = new Keywords();
    public Database() {
        storage = new HashMap<>();
    }

    //takes in a query and interprets and opertates on it if necessary
    public String transact(String query) throws Exception{
        Scanner x = new Scanner(query);
        String command = x.next();
        if (command.equals("insert") || command.equals("create")) {
            command = command + "" + x.next();
        }
        switch(command) {
            case "create table":
                return createTable(x.next(), x.toString());
            case "load":
                return load(x.toString());
            case "store":
                return store(x.toString());
            case "drop":
                return dropTable(x.next());
            case "insert into":
                return insertInto(x.next(), x.toString());
            default:
                throw new Exception("invalid command");
        }
    }


    // creates a table given the name and the right commands (commands can be null)
    private String createTable(String name, String command){return null;}
    // loads a table from a file (proj 0 might be helpful)
    private String load(String file) throws Exception{
        try {
            File filename = new File(file + ".tbl");s
            Scanner a = new Scanner(filename);

        } catch (FileNotFoundException f) {
            throw new Exception("ERROR: file not found in current directory");
        }
    }
    // takes a table and creates a tbl file (overwrites if already exists)
    private String store(String name) throws Exception{
        if (storage.containsKey(name)) {
            return "";
        }
        throw new Exception("ERROR: table not found");
    }
    // drops a table given the table name
    private String dropTable(String name) throws Exception{
        if (storage.containsKey(name)) {
            storage.remove(name);
            return "";
        }
        throw new Exception("ERROR: table not found");
    }
    // inserts values into a given table
    private String insertInto(String name, String values) throws Exception{
        if (storage.containsKey(name)) {
            return "";
        }
        throw new Exception("ERROR: table not found");
    }

}
