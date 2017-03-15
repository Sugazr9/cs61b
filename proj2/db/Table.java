package db;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.nio.*;

public class Table {
    private final HashMap<String, ArrayList> columns;
    final ArrayList<String[]> types;

    Table(ArrayList<String[]> a) {
        columns = null;
        types = a;
    }
    /* takes an array of Arraylists and inserts each value of the Arraylist into the corresponding rows
    after type checking
     */
    String insert(ArrayList[] d) {return null;}

    // prints out table in string form
    String print() { return null;}

    //joins two tables non-destructively given a command string and return the new table
    static Table join(Table t, Table uddah_table, String command) {return null;}
}
