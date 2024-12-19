package db;


import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

public class Table {
    private final ArrayList<Datum[]> data;
    private final LinkedHashMap<String, String> column_info;
    private final int num_cols;
    private int num_rows;


    private Table(LinkedHashMap<String, String> cols_info) throws Exception {
        for (String col : cols_info.keySet()) {
            colNameCheck(col);
        }
        column_info = cols_info;
        num_cols = cols_info.size();
        if (num_cols == 0) {
            throw new RuntimeException("Cannot create a table with no columns");
        }
        data = new ArrayList<>();
    }
    protected Table(String cols_info) throws Exception {
        column_info = new LinkedHashMap<>();
        String[] cols_array = cols_info.split(",", -2);
        for (String col_info : cols_array) {
            String[] curr_col_info = col_info.split(" ");
            if (curr_col_info.length != 2 ) {
                if (!column_info.isEmpty()) {
                    throw new RuntimeException("Please make sure that all columns have exactly one name and exactly one type");
                } else {
                    throw new RuntimeException("Cannot create a table with no columns");
                }
            }
            String col_name = curr_col_info[0];
            String col_type = curr_col_info[1];
            String[] ACCEPTABLE_TYPES = new String[]{"string", "int", "float"};
            if (!Arrays.asList(ACCEPTABLE_TYPES).contains(col_type)) {
                throw new RuntimeException("Unacceptable type " + col_type + " provided for column!");
            }
            else if (column_info.containsKey(col_name)) {
                throw new RuntimeException("Two columns cannot have the same name!");
            }
            else {
                colNameCheck(col_name);
                column_info.put(col_name, col_type);
            }
        }
        num_cols = column_info.size();
        data = new ArrayList<>();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////ADDROW FUNCTIONS AND HELPERS////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////

    protected void addRow(String[] row_info, boolean insert_command) throws Exception {
        if (row_info.length != num_cols) {
            throw new Exception("Number of values in row is not equal to the number of columns");
        }
        Datum[] row = new Datum[row_info.length];
        for (int i = 0; i <  row_info.length; i++) {
            String col_name = new ArrayList<>(column_info.keySet()).get(i);
            String col_type = column_info.get(col_name);
            Datum val;
            try {
                if (insert_command && row_info[i].equals("NaN")) {
                    throw new Exception("Cannot insert a NaN value via the 'insert into' command");
                }
                val = new Datum(col_type, row_info[i], true);
            } catch (Exception e) {
                if (e instanceof NumberFormatException) {
                    if (col_type.equals("int")) {
                        throw new Exception("Cannot add string or float value to int column");
                    }
                    throw new Exception("Cannot add string value to float column");
                }
                else {
                    throw e;
                }
            }
            row[i] = val;
        }
        typeCheck(row);
        data.add(row);
        num_rows++;
    }

    private void addRow(Datum[] row) throws Exception {
        if (row.length == num_cols) {
            typeCheck(row);
            data.add(row);
            num_rows ++;
        }
        else {
            throw new RuntimeException("Number of values in row is not equal to the number of columns");
        }
    }

    private boolean typeCheck(Datum[] row) throws Exception {
        for (int i = 0; i < num_cols; i++) {
            String col_name = colNameForIndex(i);
            String col_type = column_info.get(col_name);
            Datum d = row[i];
            if (!d.type.equals(col_type)) {
                throw new RuntimeException("Datum type " + d.type + " does not match column type " + col_type + ".");
            }
        }
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////JOIN FUNCTIONS///////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////

    public static Table join(Table table1, Table table2) throws Exception {

        // find common columns
        LinkedHashMap<String, String> table1_cols = table1.column_info;
        LinkedHashMap<String, String> table2_cols = table2.column_info;
        LinkedHashMap<String, String> new_table_cols = new LinkedHashMap<>(table1.column_info);
        ArrayList<String> common_cols = new ArrayList<>();
        for (String col : table2_cols.keySet()) {
            if (table1_cols.containsKey(col)) {
                common_cols.add(col);
            }
            else {
                new_table_cols.put(col, table2.column_info.get(col));
            }
        }

        for (String col : common_cols.reversed()) {
            new_table_cols.remove(col);
            new_table_cols.putFirst(col, table1_cols.get(col));
        }

        // create table
        Table result = new Table(new_table_cols);
        for (int i = 0; i < table1.num_rows; i++) {
            for (int j = 0; j < table2.num_rows; j++) {
                boolean include_row = true;
                for (String col : common_cols) {
                    String t1_val = table1.lookup(i, col).toString();
                    String t2_val = table2.lookup(j, col).toString();
                    if (!t1_val.equals(t2_val)) {
                        include_row = false;
                    }
                }

                if (include_row) {
                    Datum[] new_row = new Datum[result.num_cols];
                    for (int k = 0; k < result.num_cols; k++) {
                        String col_name = (String) result.column_info.keySet().toArray()[k];
                        if (k < table1.num_cols) {
                            new_row[k] = table1.lookup(i, col_name);
                        }
                        else {
                            new_row[k] = table2.lookup(j, col_name);
                        }
                    }
                    result.addRow(new_row);
                }
            }
        }
        return result;
    }

    public static Table join(Table[] tables) throws Exception {
        Table result;
        if (tables.length == 0) {
            throw new Exception("No table provided for join");
        }
        result = tables[0];
        for (Table t : tables) {
                result = Table.join(result, t);
        }
        return result;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////SELECT FUNCTION AND HELPERS/////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    public static Table select(String[] column_exprs, Table[] tables, String[] conditionals) throws Exception {
        Table result = join(tables);
        result = result.selectColumns(column_exprs);
        result = result.selectConditionals(conditionals);
        return result;
    }

    private Table selectColumns(String[] column_exprs) throws Exception {
        Pattern COLUMN_EXPRESSION = Pattern.compile("\\s*(.+?(?:\\s+[\\/*+-]\\s+.+?\\s*)?)(?:\\s+as\\s+(\\S+)\\s*)?$");
        Matcher expr_match;

        if (column_exprs.length == 0) {
            throw new RuntimeException("Cannot select zero columns");
        }
        LinkedHashMap<String, String> new_col_info = new LinkedHashMap<>();
        LinkedHashMap<String, String> cols_and_expressions = new LinkedHashMap<>();
        for (String expr : column_exprs) {
            expr_match = COLUMN_EXPRESSION.matcher(expr);
            String col_name;
            String math;
            if (!expr_match.matches()) {
                System.out.println(expr);
                throw new RuntimeException("Column expression is not valid. Please double-check your command.");
            }
            else if (expr_match.group(2) == null) {
                col_name = expr_match.group(1);
                if (col_name.equals("*")) {
                    math = "";
                    for (Map.Entry<String, String> col_spec : column_info.entrySet()) {
                        String col = col_spec.getKey();
                        String col_type = col_spec.getValue();
                        if (!cols_and_expressions.containsKey(col)) {
                            cols_and_expressions.put(col, col);
                            new_col_info.put(col, col_type);
                        }
                        else {
                            System.out.println("WARNING: Trying to create duplicate columns with the same name, " +
                                    "using expression for first column assignment");
                        }

                    }
                }
                else {
                    math = col_name;
                }
            } else {
                col_name = expr_match.group(2);
                math = expr_match.group(1);
            }
            if (!col_name.equals("*") && !cols_and_expressions.containsKey(col_name)) {
                new_col_info.put(col_name, null);
                cols_and_expressions.put(col_name, math);
            }
            else if (!col_name.equals("*")) {
                System.out.println("WARNING: Trying to create duplicate columns with the same name, using " +
                        "expression for first column assignment");
            }
        }

        Table result = new Table(new_col_info);
        for (int i = 0; i < num_rows; i++) {
            Datum[] new_row = new Datum[new_col_info.size()];
            for (int j = 0; j < new_col_info.size(); j++) {
                String col_name = new ArrayList<>(new_col_info.keySet()).get(j);
                String expr = cols_and_expressions.get(col_name);
                Datum val = evalExper(expr, data.get(i) ,true);
                new_row[j] = val;
                new_col_info.computeIfAbsent(col_name, k -> val.type);
            }
            result.addRow(new_row);
        }
        return result;
    }

    private Datum evalExper(String expression, Datum[] data_row, boolean leftmost) throws Exception {
        expression = expression.trim();
        Pattern COLUMN_EXPRESSION_MD = Pattern.compile("\\s*(.+?)(?:\\s+([\\/*])\\s+(.+?))+");
        Pattern COLUMN_EXPRESSION_AS = Pattern.compile("\\s*(.+?)(?:\\s+([+-])\\s+(.+?))+");
        Matcher expr_match;
        if ((expr_match = COLUMN_EXPRESSION_AS.matcher(expression)).matches()) {
            int oper_start = expr_match.start(2);
            String operation = expr_match.group(2);
            String first_expr = expression.substring(0, oper_start).trim();
            String second_expr = expr_match.group(3).trim();
            Datum first_oper = evalExper(first_expr, data_row, true);
            Datum second_oper = evalExper(second_expr, data_row, false);
            if (operation.equals("+")) {
                return Datum.add(first_oper, second_oper);
            }
            else if (operation.equals("-")) {
                return Datum.subtract(first_oper, second_oper);
            }
            else {
                throw new RuntimeException("Column expression is invalid. Please double-check and try again.");
            }
        }
        else if ((expr_match = COLUMN_EXPRESSION_MD.matcher(expression)).matches()) {
            int oper_start = expr_match.start(2);
            String operation = expr_match.group(2);
            String first_expr = expression.substring(0, oper_start);
            String second_expr = expr_match.group(3);
            Datum first_oper = evalExper(first_expr, data_row, true);
            Datum second_oper = evalExper(second_expr, data_row, false);
            if (operation.equals("/")) {
                 return Datum.divide(first_oper, second_oper);
            }
            else if (operation.equals("*")) {
                return Datum.multiply(first_oper, second_oper);
            }
            else {
                throw new RuntimeException("Column expression is invalid. Please double-check and try again.");
            }
        }
        else {
            if (expression.equals("NaN") || expression.equals("NOVALUE")) {
                throw new RuntimeException("Column expression cannot contain NOVALUE or NaN values");
            }
            Datum operand = operandChecker(expression, data_row);
            if (leftmost && !column_info.containsKey(expression)) {
                throw new RuntimeException("Leftmost value of a column expression must be a valid column name.");
            }
            return operand;
        }
    }

    protected Table selectConditionals(String[] conditionals) throws Exception {
        if (conditionals.length == 0) {
            return clone();
        }
        LinkedHashMap<String, String> new_col_info = (LinkedHashMap<String, String>) column_info.clone();
        Table result = new Table(new_col_info);

        Object[] good_row_is = new Object[data.size()];
        for (int i = 0; i < good_row_is.length; i++) {
            good_row_is[i] = i;
        }

        for (String condition : conditionals) {
            for (int i = 0; i < good_row_is.length; i++) {
                Object row_i = good_row_is[i];
                if (row_i == null) {
                    continue;
                }
                Datum[] curr_row = data.get((int) row_i);
                if (!checkCond(condition, curr_row)) {
                    good_row_is[i] = null;
                }
            }
        }

        for (Object i : good_row_is) {
            if (i != null) {
                result.addRow(data.get((int) i));
            }
        }

        return result;
    }

    private boolean checkCond(String conditional, Datum[] row) throws Exception {
        Pattern cond_checker = Pattern.compile("\\s*(\\S+)\\s+([=><!]{1,2})\\s+('?.+'?)\\s*");

        Matcher cond_matcher;
        if (!(cond_matcher = cond_checker.matcher(conditional)).matches()) {
            throw new RuntimeException("Conditional statement " + conditional + " is invalid! Please double-check it.");
        }
        String oper1 = cond_matcher.group(1);
        String compOper = cond_matcher.group(2);
        String oper2 = cond_matcher.group(3);
        Datum val1 = operandChecker(oper1, row);
        Datum val2 = operandChecker(oper2, row);

        if (val1.isNoValue() || val2.isNoValue()) {
            return false;
        }

        int comparison = Datum.compare(val1, val2);
        return switch (compOper) {
            case "==" -> comparison == 0;
            case "!=" -> comparison != 0;
            case "<" -> comparison < 0;
            case ">" -> comparison > 0;
            case "<=" -> comparison <= 0;
            case ">=" -> comparison >= 0;
            default ->
                    throw new RuntimeException("Invalid comparison operator " + compOper + " . Please double-check your input");
        };
    }

    private Datum operandChecker(String oper, Datum[] data_row) throws Exception {
        if (column_info.containsKey(oper)) {
            return data_row[colIndex(oper)];
        }
        else {
            if (oper.equals("NOVALUE")) {
                return new Datum("string", "NOVALUE");
            }
            if (oper.equals("NaN")) {
                return new Datum("float", "NaN");
            }
            Pattern string_checker = Pattern.compile("^('.+')\\z");
            Matcher string_matcher;
            if ((string_matcher = string_checker.matcher(oper)).matches()) {
                return new Datum("string",  string_matcher.group(1));
            }
            try {
                return new Datum("int", Integer.parseInt(oper));
            } catch (NumberFormatException e) {
                try {
                    return new Datum("float", Float.parseFloat(oper));
                } catch (NumberFormatException err) {
                    throw new RuntimeException("Operand " + oper + " is not a valid operand.");
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////GENERAL HELPER FUNCTIONS AND TOSTRING///////////////////////
    ///////////////////////////////////////////////////////////////////////////////////

    private void colNameCheck(String column_name) throws Exception {
        Pattern name_checker = Pattern.compile("^[a-zA-Z]\\w*$");
        if (!name_checker.matcher(column_name).matches()) {
            throw new RuntimeException("Column name " + column_name + " is invalid! Name must conform to the regex ^[a-zA-Z]\\w*$");
        }
        for (String keyword : Database.DB_KEYWORDS) {
            if (column_name.equals(keyword)) {
                throw new RuntimeException("Column name " + column_name + " contains a keyword! Please think of a different name");
            }
        }
    }

    private int colIndex(String column_name) {
        ArrayList<String> cols_list = new ArrayList<>(this.column_info.keySet());
        return cols_list.indexOf(column_name);
    }

    private String colNameForIndex(int col_index) {
        return new ArrayList<>(this.column_info.keySet()).get(col_index);
    }

    private Datum lookup(int row_number, String col_name) {
        int col_i = colIndex(col_name);
        return data.get(row_number)[col_i];
    }

    public String toString() {
        String return_str = "";
        Object[] col_names = column_info.keySet().toArray();
        for (int i = 0; i < num_cols; i++) {
            String col_name = (String) col_names[i];
            String col_type = column_info.get(col_name);
            return_str += col_name + " " + col_type;
            if (i == num_cols - 1) {
                return_str += "\n";
            }
            else {
                return_str += ",";
            }
        }
        for (int i = 0; i < num_rows; i++) {
            Datum[] curr_row = data.get(i);
            for (int j = 0; j < num_cols; j++) {
                return_str += curr_row[j].toString();
                if (j == num_cols - 1) {
                    return_str += "\n";
                }
                else {
                    return_str += ",";
                }
            }
        }
        return return_str;
    }

    public Table clone() {
        LinkedHashMap<String, String> cloned_cols = new LinkedHashMap<>(column_info);
        try {
            Table cloned = new Table(cloned_cols);
            for (Datum[] row : data) {
                Datum[] cloned_row = new Datum[row.length];
                for (int i = 0; i < row.length; i++) {
                    cloned_row[i] = row[i].clone();
                }
                cloned.addRow(cloned_row);
            }
            return cloned;
        } catch (Exception e) {
            return null;
        }
    }

}
