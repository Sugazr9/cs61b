package db;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Database {

    private final HashMap<String, Table> table_db;
    private static final HashMap<Pattern, String> REGEXES_AND_KEYWORDS = new HashMap<>() {
        {
            put(Pattern.compile("create\\s+table\\s+(.*)"), "create table");
            put(Pattern.compile("load\\s+(.*)"), "load");
            put(Pattern.compile("store\\s+(.*)"), "store");
            put(Pattern.compile("drop\\s+table\\s+(.*)"), "drop table");
            put(Pattern.compile("insert\\s+into\\s+(.*)"), "insert into");
            put(Pattern.compile("print\\s+(.*)"), "print");
            put(Pattern.compile("select\\s+(.*)"), "select");
        }};
    private static final HashMap<String, Pattern> REGEXES_AND_KEYWORDS_EXTRAS = new HashMap<>() {
        {
            put("create new scratch", Pattern.compile("(\\S+)\\s+\\(\\s*((?:\\S+\\s+\\S+\\s*(?:,\\s*\\S+\\s+\\S+\\s*)*)?)\\)\\s*"));
            put("create new select", Pattern.compile("(\\S+)\\s+as select\\s+(.*)"));
            put("insert into info", Pattern.compile("(\\S+)\\s+values\\s+([^,\\s](?:[^,])*\\s*(?:,\\s*[^,\\s](?:[^,])*\\s*)*)\\Z"));
            put("select info", Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+(\\S+\\s*(?:,\\s*\\S+\\s*)*)" +
                    "(?:\\s+where\\s+([\\w\\s+\\-*/'<>=!.]+?(?:\\s+and\\s+[\\w\\s+\\-*/'<>=!.]+?)*))?\\Z"));
        }};

    protected final static String[] DB_KEYWORDS = new String[]{"create", "table", "as", "load", "store", "drop", "insert", "into", "values", "print", "select", "from", "where", "and"};


    public Database() {
        table_db = new HashMap<>();
    }

    /////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////QUERY PARSERS//////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////
    public String transact(String query) {
        found:
        {
            for (Pattern pattern : REGEXES_AND_KEYWORDS.keySet()) {
                Matcher match;
                if ((match = pattern.matcher(query)).matches()) {
                    String cmd = REGEXES_AND_KEYWORDS.get(pattern);
                    switch (cmd) {
                        case "create table" -> {
                            String table_info = match.group(1);
                            if ((match = REGEXES_AND_KEYWORDS_EXTRAS.get("create new scratch").matcher(table_info)).matches()) {
                                String table_name = match.group(1);
                                String columns_info = match.group(2);
                                columns_info = columns_info.trim().replaceAll("\\s+", " ");
                                columns_info = columns_info.replaceAll("\\s*,\\s*", ",");
                                try {
                                    create(table_name, columns_info);
                                    break found;
                                } catch (Exception e) {
                                    return "Could not create table " + table_name + " because of the following error:\n" + e.getMessage() + "\n";
                                }
                            } else if ((match = REGEXES_AND_KEYWORDS_EXTRAS.get("create new select").matcher(table_info)).matches()) {
                                String table_name = match.group(1);
                                String select_info = match.group(2);
                                try {
                                    Table result = selectInfoParser(select_info);
                                    create(table_name, result);
                                    break found;
                                } catch (Exception e) {
                                    return "Could not create table " + table_name + " because of the following error:\n" + e.getMessage() + "\n";
                                }
                            } else {
                                return "Detected malformed create table command. Please double-check it.";
                            }
                        }
                        case "load", "store", "drop table", "print" -> {
                            return simpleParseCommands(cmd, match);
                        }
                        case "insert into" -> {
                            String insert_into_info = match.group(1);
                            if ((match = REGEXES_AND_KEYWORDS_EXTRAS.get("insert into info").matcher(insert_into_info)).matches()) {
                                String table_name = match.group(1);
                                String row_info = match.group(2);
                                try {
                                    String[] row_entries = row_info.trim().split("\\s*,\\s*");
                                    insert_into(table_name, row_entries);
                                    break found;
                                } catch (Exception e) {
                                    return "Could not add new row to table " + table_name + " because of the following error:\n" + e.getMessage() + "\n";
                                }
                            } else {
                                return "'Insert into' command is malformed. Please double check it.";
                            }
                        }
                        case "select" -> {
                            String select_info = match.group(1);
                            Table result;
                            try {
                                result = selectInfoParser(select_info);
                                return result.toString();
                            } catch (Exception e) {
                                return "Could not execute the select clause because of the following error:\n" + e.getMessage() + "\n";
                            }
                        }
                        default -> {
                            return "Could not parse your query. Please make sure it is written correctly!\n";
                        }
                    }
                }
            }
            return "Could not parse your query. Please make sure it is written correctly!\n";
        }
        return "";
    }

    private String simpleParseCommands(String cmd, Matcher match){
        String table_name = match.group(1).trim();
        try {
            switch (cmd) {
                case "load" -> {
                    load(table_name);
                }
                case "store" -> {
                    store(table_name);
                }
                case "drop table" -> {
                    cmd = "drop";
                    drop(table_name);
                }
                case "print" -> {
                    return print(table_name);
                }
                case null, default -> throw new Exception("Command not recognized!!");
            }
        } catch (FileNotFoundException e) {
            return "Could not find table " + table_name + ". Please make sure the table exists.";
        } catch (Exception e) {
            return "Could not " + cmd + " table " + table_name + " because of the following error:\n"+ e.getMessage() + "\n";
        }
        return "";
    }

    private Table selectInfoParser(String select_info) throws Exception {
        Matcher match;
        if((match = REGEXES_AND_KEYWORDS_EXTRAS.get("select info").matcher(select_info)).matches()) {
            String[] expressions = match.group(1).trim().split("\\s*,\\s*");
            String[] tables = match.group(2).trim().split("\\s*,\\s*");
            String[] conditions;
            if (match.group(3) == null) {
                conditions = new String[0];
            }
            else {
                conditions = match.group(3).trim().split("\\s+and\\s+");
            }
            Table[] actualTables = find_tables(tables, false);
            return Table.select(expressions, actualTables, conditions);
        }
        else {
            throw new RuntimeException("'Select' command is malformed. Please double check it.");
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////CREATE AND LOAD FUNCTIONS AND HELPERS//////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////

    private void create(String table_name, String columns_info) throws Exception {
        find_table(table_name, true);
        Table new_table = new Table(columns_info);
        addTable(table_name, new_table);
    }

    private void create(String table_name, Table new_table) throws Exception {
        find_table(table_name, true);
        addTable(table_name, new_table);
    }

    private void load(String table_name) throws Exception {
        String path = "./examples/" + table_name + ".tbl";
        File tbl_file = new File(path);
        Scanner tbl_reader = new Scanner(tbl_file);
        String col_info = tbl_reader.nextLine();
        Table new_table = new Table(col_info);

        // parsing data here
        while (tbl_reader.hasNextLine()) {
            String line = tbl_reader.nextLine();
            String[] line_info = line.split(",");
            new_table.addRow(line_info, false);
        }
        addTable(table_name, new_table);
    }

    private void addTable(String table_name, Table t) throws Exception {
        Pattern name_checker = Pattern.compile("^[a-zA-Z]\\w*$");
        if (!name_checker.matcher(table_name).matches()) {
            throw new RuntimeException("Table name " + table_name + " is invalid! Name must conform to the regex ^[a-zA-Z]\\w*$");
        }
        for (String keyword : Database.DB_KEYWORDS) {
            if (table_name.contains(keyword)) {
                throw new RuntimeException("Table name " + table_name + " contains a keyword! Please think of a different name");
            }
        }
        table_db.put(table_name, t);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    /////////////////////STORE, DROP, PRINT, AND INSERT INTO FUNCTIONS/////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    private void store(String table_name) throws Exception {
        Table curr_table = find_table(table_name, false);
        String contents = curr_table.toString();
        String file_path = "./examples/" + table_name + ".tbl";
        FileWriter writer = new FileWriter(file_path);
        writer.write(contents);
        writer.close();
    }

    private void drop(String table_name) throws Exception {
        find_table(table_name, false);
        table_db.remove(table_name);
    }

    private String print(String table_name) throws Exception {
        Table curr_table = find_table(table_name, false);
        return curr_table.toString();
    }

    private void insert_into(String table_name, String[] row_info) throws Exception {
        find_table(table_name, false);
        table_db.get(table_name).addRow(row_info, true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////SEARCHING HELPER FUNCTIONS///////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////

    private Table find_table(String table_name, boolean adding_new_table) throws Exception {
        Table curr_table = table_db.get(table_name);
        if (curr_table == null && !adding_new_table) {
            throw new Exception("Table " + table_name + " not in database!");
        }
        else if (curr_table != null && adding_new_table) {
            throw new Exception("Table " + table_name + " already in database!");
        }
        else {
            return curr_table;
        }
    }

    private Table[] find_tables(String[] table_names, boolean adding_new_table) throws Exception {
        Table[] tables = new Table[table_names.length];
        for (int i = 0; i < tables.length; i++) {
            tables[i] = find_table(table_names[i], adding_new_table);
        }
        return tables;
    }

    public static void main(String[] x) throws Exception {
        Database new_db = new db.Database();

        //tests for join

        /*

        System.out.println(new_db.transact("load fans"));
        System.out.println(new_db.transact("print fans"));
        System.out.println(new_db.transact("load teams"));
        System.out.println(new_db.transact("print teams"));
        System.out.println(new_db.transact("load records"));
        System.out.println(new_db.transact("print records"));*/


        /*String to_join = String.join(", ", new String[]{"fans", "teams"});
        System.out.println(new_db.transact("select * from " + to_join));
        System.out.println(new_db.transact("select * from fans, teams where Lastname == 'Lee'"));
        System.out.println(new_db.transact("select * from teams where YearEstablished <= 2007 and Sport == 'NFL Football'"));*/
        /*System.out.println(new_db.transact("print fans"));
        System.out.println(new_db.transact("print teams"));
        System.out.println(new_db.transact("print records"));*/
    }
}