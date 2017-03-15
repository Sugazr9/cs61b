package db;

import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.nio.*;

public class Keywords {
    HashMap<String, Pattern> cache = new HashMap<>();
    Keywords() {
        cache.put(" as ", Pattern.compile("as", 2));
        cache.put(" from ", Pattern.compile("from", 2));
        cache.put(" where ", Pattern.compile("where", 2));
        cache.put(" values ", Pattern.compile("values", 2));
    }
}
