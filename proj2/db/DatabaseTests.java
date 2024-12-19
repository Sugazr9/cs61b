package db;

import java.io.File;
import java.util.Scanner;

public class DatabaseTests {
    Database test_db;

    protected DatabaseTests() {
        test_db = new Database();
    }

    private void testsCreate() {
        System.out.println();
        System.out.println();
        System.out.println("**************************************************************************");
        System.out.println("Running 'create table' test");
        System.out.println("**************************************************************************\n");
        test_db = new Database();
        execute("create test (col1 int1, col2 float, col3 string)"); // incorrect create command
        execute("create table test (col1 int, col2 float, col3 string,)"); //malformed create command
        execute("create table test (col1 int1, col2 float, col3 string)"); //testing incorrect typing
        execute("create table test ()"); //create table with no columns
        execute("create table test (col1 int,      col2 float,col54   string)   "); //testing spacing
        execute("create table table_1 (col1 int,      col2 float,col54   string)   "); //testing table name with keyword
        execute("create table test (col1 int, col2 string, col3 float)"); //creating table that already exists
        execute("create table test2 (col5 float, col10 string)");
        execute("create table test3 as select * from test, test2"); //creating via join
        execute("print test");
        execute("print test2");
        execute("print test3");
    }

    private void testsLoad() {
        test_db = new Database();
        System.out.println();
        System.out.println();
        System.out.println("**************************************************************************");
        System.out.println("Running 'load' test");
        System.out.println("**************************************************************************\n");
        test_db = new Database();
        execute("load fake_file"); //trying to load non-existent file
        execute("create table fans (col1 int, col2 float, col3 string)");
        execute("print fans");
        execute("load   fans   "); //loading and replacing existing table
        execute("print fans");
        execute("load teams_err"); //table has a row with wrong number of entries
        execute("print teams_err");
        execute("load fans_err"); //table has int value in string column;
        execute("print fans_err");
        execute("load fans_err_float"); //table has float value in string column;
        execute("print fans_err_float");
        execute("load records_err"); //table has string value in int column
        execute("print records_err");
        execute("load records_err4"); //table has float value in int column
        execute("print records_err4");
        execute("load records_err2"); //table has string value in float column;
        execute("print recors_err2");
        execute("load records_err3"); //table has int value in float column;
        execute("print recors_err3");
    }

    private void testsStore() {
        System.out.println();
        System.out.println();
        System.out.println("**************************************************************************");
        System.out.println("Running 'store' test");
        System.out.println("**************************************************************************\n");
        test_db = new Database();
        execute("load fans");
        execute("create table fans_test as select * from fans where Lastname == 'Lee'");
        String fans_test_1a = execute("print fans_test");
        execute("store fans_test,"); //malformed store command
        execute("store fans_test");
        execute("load fans_test");
        String fans_test_1b = execute("print fans_test");
        System.out.println(fans_test_1a.equals(fans_test_1b)); //checks to see if data of stored table is accurate
        execute("drop table fans_test");
        execute("create table fans_test as select * from fans where Firstname == 'Mitas'");
        String fans_test_2a = execute("print fans_test");
        execute("store fans_test"); //overwriting fans_test file
        execute("load fans_test");
        String fans_test_2b = execute("print fans_test");
        System.out.println(fans_test_1b.equals(fans_test_2b)); //checking to see if data of stored table is different from previous store
        System.out.println(fans_test_2b.equals(fans_test_2a)); //checking to see if data of stored table is accurate
    }

    private void testsDropTable() {
        System.out.println();
        System.out.println();
        System.out.println("**************************************************************************");
        System.out.println("Running 'drop table' test");
        System.out.println("**************************************************************************\n");
        test_db = new Database();
        execute("drop table fans"); // dropping non-existent table
        execute("load fans");
        execute("print fans");
        execute("drop fans"); // malformed drop
        execute("drop table fans,"); // malformed drop
        execute("drop table fans"); // dropping existing table
        execute("print fans");
        execute("drop table fans"); // dropping the same table again
    }

    private void testsInsertInto() {
        System.out.println();
        System.out.println();
        System.out.println("**************************************************************************");
        System.out.println("Running 'insert into' test");
        System.out.println("**************************************************************************\n");
        test_db = new Database();
        execute("create table test (col1 string, col2 int, col3 float)");
        execute("print test");
        execute("insert into test values    'Golden Bears'  ,4,    4.556    "); //testing spacing
        execute("print test");
        execute("insert into test 'Golden Bears'  ,4,    4.556    "); //malformed insert command
        execute("print test");
        execute("insert test values    'Golden Bears'  ,4,    4.556    "); //malformed insert command
        execute("print test");
        execute("insert into test values    'Golden Bears'  ,4,    4.556,    "); //malformed insert command
        execute("print test");
        execute("insert into test values    'Golden Bears'  ,4,    4.556, 4.54    "); //inserting more values than columns
        execute("print test");
        execute("insert into test values 4.5,4, 4.556"); //float in string
        execute("print test");
        execute("insert into test values 4,4, 4.556"); //int in string
        execute("print test");
        execute("insert into test values 'Golden Bears',4.55,4.556"); //float in int
        execute("print test");
        execute("insert into test values 'Golden Bears','hahaha', 4.556"); //string in int
        execute("print test");
        execute("insert into test values 'Golden Bears',4, 'hahaha'"); //string in float
        execute("print test");
        execute("insert into test values 'Golden Bears',4, 65"); //int in float
        execute("print test");
        execute("insert into hahaha values 'Golden Bears',4, 65"); //inserting into nonexistent table
        execute("print test");
    }

    private void testsPrint() throws Exception {
        System.out.println();
        System.out.println();
        System.out.println("**************************************************************************");
        System.out.println("Running 'print' test");
        System.out.println("**************************************************************************\n");
        test_db = new Database();
        File tbl_file = new File("./examples/teams.tbl");
        Scanner tbl_reader = new Scanner(tbl_file);
        String from_file = "";
        while (tbl_reader.hasNextLine()) {
            from_file += tbl_reader.nextLine() + "\n";
        }
        System.out.println(from_file);
        execute("load teams");
        String from_db = execute("print      teams    ");
        System.out.println(from_db.equals(from_file)); //tests to make sure that print is the same as the file
        execute("create table test (col1 string, col2 int, col3 float)");
        execute("insert into test values 'Golden Bears',4,4.556");
        String test_print = execute("print test");
        System.out.println(test_print);
        String test_str = "col1 string,col2 int,col3 float\n'Golden Bears',4,4.556\n";
        System.out.println(test_str.equals(test_print)); //tests to make sure that print is the same as table data
    }

    private void testsSelect() {
        System.out.println();
        System.out.println();
        System.out.println("**************************************************************************");
        System.out.println("Running 'select' test");
        System.out.println("**************************************************************************\n");
        test_db = new Database();
        execute("load teams");
        execute("load records");
        String teams = execute("print teams");
        String sel = execute("select * from teams");
        System.out.println(teams.equals(sel));
        execute("select from teams"); // malformed
        execute("select from where"); // malformed
        execute("select from where and"); // malformed
        execute("select from "); // malformed
        execute("select "); // malformed
        execute("select TeamName from teams where"); // malformed
        execute("select TeamName, from teams"); // malformed
        execute("select TeamName from teams");
        execute("select TeamName, Losses from teams"); // testing select on columns that don't exist
        execute("select Losses from teams"); // testing select on columns that don't exist
        execute("select TeamName from teams where YearEstablished >= 1962"); // making sure conditionals are evaluated last
        execute("select TeamName from teams where TeamName == 'Steelers'");
        execute("select * from teams, records");
        execute("select Sport, City, TeamName, * from teams, records"); //testing order
        execute("select    *    from    teams,    records    where    Wins >= 10");
        execute("select    *    from    teams,    records    where    Wins >= 10 and "); // malformed
        execute("select    *    from    teams,    records    where    Wins >= 10 and TeamName == 'Steelers'");
        execute("select Wins + Losses as Games, 0 as Games from teams, records"); // creating two different columns with the same name
        execute("select Wins + Losses as Wins from teams, records"); // overwriting an existing col
        execute("select Wins + Losses as Wins, * from teams, records"); // overwriting an existing col and adding rest of table
        execute("select    Wins + Losses + Ties as TotalGames    from    teams,    records    where    Wins >= 10 and TeamName == 'Steelers'"); // checking that using cols in col expr does not affect conds
        execute("select    Wins + Losses + Ties as TotalGames, *    from    teams,    records    where    Wins >= 10 and TeamName == 'Steelers'"); // just to see what happens
    }

    private void testsTableColNames() {
        System.out.println();
        System.out.println();
        System.out.println("**************************************************************************");
        System.out.println("Running column names test");
        System.out.println("**************************************************************************\n");
        test_db = new Database();
        execute("create table table1 (4col1 int,      col2 float,col3   string)   "); //bad column name
        execute("create table 3table1 (col1 int,      col2 float,col3   string)   "); //bad table name
        execute("create table table1 (col1 int,      col2$ float,col3   string)   "); //bad column name
        execute("create table table1$ (col1 int,      col2 float,col3   string)   "); //bad table name
        // testing keywords as table and column names
        String[] DB_KEYWORDS = new String[]{"create", "table", "as", "load", "store", "drop", "insert", "into", "values", "print", "select", "from", "where", "and", };
        for (int i = 0; i < DB_KEYWORDS.length; i++) {
            String keyw = DB_KEYWORDS[i];
            execute("create table " + keyw + " (col1 int, col2 string, col3 float)");
            execute("create table test" + i + " (" + keyw + " int, col2 string, col3 float)");
        }
    }

    private void testsLiterals() {
        System.out.println();
        System.out.println();
        System.out.println("**************************************************************************");
        System.out.println("Running literals test");
        System.out.println("**************************************************************************\n");
        test_db = new Database();
        execute("create table test (col1 string, col2 int, col3 float)");
        execute("insert into test values 'Golden Bears', 4, 5.");
        //testing illegal string characters
        String[] STRING_NONOS = new String[]{"\n", "\t", ",", "'", "\""};
        for (String no : STRING_NONOS) {
            execute("insert into test values '" + no + "', 10, 15.");
            execute("select col1 + '" + no + "' as col4 from test");
            execute("select '" + no + "' as col4 from test");
            execute("select * from test where col1 == '" + no + "'");
        }
        //testing compatible literals (but similar literals)
        execute("select col1 + '4' as col4 from test");
        execute("select col1 + '4.5' as col4 from test");
        execute("select col2 + 4 as col4 from test");
        execute("select col3 + 4.5 as col4 from test");
        //testing incompatible literals
        execute("select col1 + 4 as col4 from test");
        execute("select col1 as col4 from test where col4 >= 4");
        execute("select col1 + 4.5 as col4 from test");
        execute("select col1 as col4 from test where col4 >= 4.5");
        execute("select col2 + '4' as col4 from test");
        execute("select col2 as col4 from test where col4 >= '4'");
        execute("select col3 + '4.5' as col4 from test");
        execute("select col3 as col4 from test where col4 >= '4.5'");
    }

    private void testsTypes() {
        System.out.println();
        System.out.println();
        System.out.println("**************************************************************************");
        System.out.println("Running types test");
        System.out.println("**************************************************************************\n");
        test_db = new Database();
        execute("create table test (col1 int, col2 string, col3 float)");
        execute("insert into test values 4,'Golden Bears',5.");
        String test_print = execute("print test");
        String test_str = "col1 int,col2 string,col3 float\n4,'Golden Bears',5.000\n";
        System.out.println(test_str.equals(test_print)); //making sure floats print 3 decimal places
        //testing illegal string characters
        String[] STRING_NONOS = new String[]{"\n", "\t", ",", "'", "\""};
        for (String no : STRING_NONOS) {
            execute("insert into test values 10,'" + no + "',.43");
        }
        execute("insert into test values 10, '', .43");
        execute("insert into test values 10,'hi',.43"); //testing different decimal placements of floats
        execute("insert into test values 10,'hi',4.3"); //testing different decimal placements of floats
        execute("insert into test values 10,'hi',43."); //testing different decimal placements of floats
        execute("insert into test values -10,'hi',-.3"); //testing negatives for ints and floats
        execute("print test");
    }

    private void testsColExprs() {
        System.out.println();
        System.out.println();
        System.out.println("**************************************************************************");
        System.out.println("Running column expressions test");
        System.out.println("**************************************************************************\n");
        test_db = new Database();
        execute("create table test (col1 int, col2 string, col3 float)");
        execute("insert into test values 4,'Golden Bears',5.");
        execute("select col1 + col3 from test"); // invalid column expression, no alias given
        execute("select * from test");
        execute("select col2 + col2 as col2dup, col2dup as check from test"); // cannot select newly created cols
        // illegal operands
        execute("select col5 as col6 from test");
        execute("select 4 as col7 from test");
        execute("select 'hi' as col85 from test");
        execute("select 4.5 as col42 from test");
        // illegal expressions with strings
        execute("select col1 + col2 as col1a from test");
        execute("select col3 + col2 as col1a from test");
        execute("select col2 - col2 as col1a from test");
        execute("select col2 / col2 as col1a from test");
        execute("select col2 * col2 as col1a from test");
        execute("select col2 + 4 as col1a from test");
        execute("select col2 + .4 as col1a from test");
        // string expressions
        execute("select col2 + col2 as col1a from test");
        execute("select col2 + 'hahahaha' as col1a from test");
        // illegal expressions (literal cannot come first)
        execute("select 4 + col1 as col1a from test");
        execute("select 4.5 + col1 as col1a from test");
        execute("select 'hihi' + col1 as col1a from test");
        execute("select 4.5 * col1 + 5 as col1a from test");
        execute("select 4.5 * col1 + col3 as col1a from test");
        execute("select 4.5 * 5 + col3 as col1a from test");
        // int and float expressions
        execute("select col1 * col1 as col1a from test");
        execute("select col1 - col1 as col1a from test");
        execute("select col1 + col1 as col1a from test");
        execute("select col1 / col1 as col1a from test");
        execute("select col3 * col3 as col1a from test");
        execute("select col3 / col3 as col1a from test");
        execute("select col3 + col3 as col1a from test");
        execute("select col3 - col3 as col1a from test");
        execute("select col1 + col3 as col1a from test");
        execute("select col1 - col3 / col3 as col1a from test");
        execute("select col1 / col3 - col3 as col1a from test");
        execute("select col1 * col3 / col3 as col1a from test");
        execute("select col1 / col3 + col3 as col1a from test");
        execute("select col1 / col3 - 4 as col1a from test");
        execute("select col1 / col3 * 4 as col1a from test");
        execute("select col1 / col3 + 4 as col1a from test");
        execute("select col1 / col3 / 4 as col1a from test");
        execute("select (col1 / col3) + 3 as col1a from test"); //illegal operators
        execute("select col1**2 / col3 + 3 as col1a from test"); //illegal operators
    }

    private void testsCondExprs() {
        System.out.println();
        System.out.println();
        System.out.println("**************************************************************************");
        System.out.println("Running conditional expressions test");
        System.out.println("**************************************************************************\n");
        test_db = new Database();
        execute("load teams");
        execute("load records");
        execute("select * from teams, records");
        execute("select * from teams, records where Sport == 'NFL Football'");
        execute("select * from teams, records where Sport == 'NFL Football' and City == 'Pittsburgh'");
        execute("select * from teams, records where Sport == 'NFL Football' and City == 'Pittsburgh' and Wins > 9");
        //errors
        execute("select Sport from teams, records where City == 'Pittsburgh'"); //testing that conditional evaled last
        execute("select Sport from teams, records where City ");
        execute("select Sport from teams, records where City == ");
        execute("select City from teams, records where City == 9");
        execute("select City from teams, records where City == 9.5");
        execute("select City from teams, records where City == -9");
        execute("select City from teams, records where City == -9.5");
        execute("select Wins from teams, records where Wins == '-9.5'");
        execute("select Wins / Losses as ratio from teams, records where ratio == '-9.5'");
        // testing the different operations
        execute("select * from teams, records where Wins > 10");
        execute("select * from teams, records where Wins >= 10");
        execute("select * from teams, records where Wins == 10");
        execute("select * from teams, records where Wins != 10");
        execute("select * from teams, records where Wins < 10");
        execute("select * from teams, records where Wins <= 10");
        execute("select * from teams, records where Wins > 10.0");
        execute("select * from teams, records where Wins >= 10.0");
        execute("select * from teams, records where Wins == 10.0");
        execute("select * from teams, records where Wins != 10.0");
        execute("select * from teams, records where Wins < 10.0");
        execute("select * from teams, records where Wins <= 10.0");
        execute("select * from teams, records where Wins > Losses");
        execute("select * from teams, records where Wins >= Losses");
        execute("select * from teams, records where Wins == Losses");
        execute("select * from teams, records where Wins != Losses");
        execute("select * from teams, records where Wins < Losses");
        execute("select * from teams, records where Wins <= Losses");
        execute("select * from teams, records where City > 'New England'");
        execute("select * from teams, records where City >= 'New England'");
        execute("select * from teams, records where City == 'New England'");
        execute("select * from teams, records where City != 'New England'");
        execute("select * from teams, records where City < 'New England'");
        execute("select * from teams, records where City <= 'New England'");
    }

    private void testsSpecialVals() {
        System.out.println();
        System.out.println();
        System.out.println("**************************************************************************");
        System.out.println("Running special values test");
        System.out.println("**************************************************************************\n");
        test_db = new Database();
        // tests for NaN
        execute("create table test (col1 int, col2 float, col3 int)");
        execute("insert into test values 1, 2.0, 3");
        execute("print test");
        execute("create table test2 as select col1 / 0 as col1n, col2 / 0 as col2n from test");
        execute("print test2");
        execute("select * from test2 where col1n == col2n");
        execute("create table test3 as select col1n + 3 as col1a, col1n - 3 as col1s, col1n / 4 as col1d, col1n * 4 as col1m from test2");
        execute("create table test4 as select col2n + 3 as col2a, col2n - 3 as col2s, col2n / 4 as col2d, col2n * 4 as col2m from test2");
        execute("print test3");
        execute("print test4");
        execute("select * from test2 where col1n <= 4.5"); //testing typing
        execute("select * from test2 where col2n <= 4"); //testing typing
        execute("select * from test2 where col1n == 'NaN'"); //comparing to a string
        execute("select * from test2 where col2n == 'NaN'"); //comparing to a string
        execute("select * from test2 where col1n <= 4");
        execute("select * from test2 where col1n > 4");
        execute("select * from test2 where col1n == NaN");
        execute("select col2n + NaN as colee from test2"); // cannot use NaN in col expr
        execute("insert into test values NaN, 4.5, 10"); // cannot insert NaN value
        execute("insert into test values 4, NaN, 4"); // cannot insert NaN value
        // test for NOVALUE
        execute("create table test5 (col1 int, col2 float, col3 string, val int)");
        execute("insert into test5 values NOVALUE, NOVALUE, NOVALUE, 4");
        execute("print test5");
        execute("select col1 + 3 as col1 from test5");
        execute("select col2 + 3 as col1 from test5");
        execute("select col3 + '3' as col1 from test5");
        execute("select col1 + col1 as col1 from test5");
        execute("select col1 - col1 as col1 from test5");
        execute("select col1 * col1 as col1 from test5");
        execute("select col1 / col1 as col1 from test5");
        execute("select col2 + col2 as col1 from test5");
        execute("select col2 - col2 as col1 from test5");
        execute("select col2 * col2 as col1 from test5");
        execute("select col2 / col2 as col1 from test5");
        execute("select col1 + col2 as col1 from test5");
        execute("select col3 + col3 as col1 from test5");
        execute("select col3 - col3 as col1 from test5");
        execute("select col3 * col3 as col1 from test5");
        execute("select col3 / col3 as col1 from test5");
        //conditionals with NOVALUE are always false
        execute("select col1 from test5 where col1 > 4");
        execute("select col1 from test5 where col1 < 4");
        execute("select col1 from test5 where col1 <= 4");
        execute("select col1 from test5 where col1 >= 4");
        execute("select col1 from test5 where col1 == 4");
        execute("select col1 from test5 where col1 != 4");
        execute("select val from test5 where val > NOVALUE");
        execute("select val from test5 where val < NOVALUE");
        execute("select val from test5 where val <= NOVALUE");
        execute("select val from test5 where val >= NOVALUE");
        execute("select val from test5 where val == NOVALUE");
        execute("select val from test5 where val != NOVALUE");
        execute("select col3 + NOVALUE as done from test5"); // cannot use NOVALUE in column epr
    }

    private void testsJoin() {
        System.out.println();
        System.out.println();
        System.out.println("**************************************************************************");
        System.out.println("Running join test");
        System.out.println("**************************************************************************\n");
        test_db = new Database();
        execute("load t1");
        execute("load t2");
        execute("load t4");
        execute("load t5");
        //testing natural inner join
        execute("select * from t1, t2");
        execute("create table t3 as select * from t1, t2");
        // testing natural inner join with cartesian product
        String three_join = execute("select * from t1, t2, t4");
        String step_join = execute("select * from t3, t4");
        System.out.println(three_join.equals(step_join));
        // testing natural inner join with no common rows
        execute("select * from t3, t4, t5");
    }

    private String execute(String command) {
        System.out.println("Running the following command: " + command);
        String output = test_db.transact(command);
        System.out.println("Result: \n" + output);
        return output;
    }

    public static void main(String[] args) throws Exception {
        DatabaseTests testing = new DatabaseTests();
//        testing.testsCreate(); //test passed
//        testing.testsLoad(); //test passed
//        testing.testsStore(); //test passed
//        testing.testsDropTable(); //test passed
//        testing.testsInsertInto(); //test passed
//        testing.testsPrint(); //test passed
//        testing.testsSelect(); //test passed
//        testing.testsTableColNames(); //test passed
//        testing.testsLiterals(); //test passed
//        testing.testsTypes(); //test passed
//        testing.testsColExprs(); //test passed
//        testing.testsCondExprs(); //test passed
//        testing.testsSpecialVals(); //test passed
//        testing.testsJoin(); //test passed
    }
}
