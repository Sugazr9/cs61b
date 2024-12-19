package db;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Datum {
    protected String type;
    private final Object val;
    private final Pattern STRING_PATTERN = Pattern.compile("^'(.*)'\\z");
    private final String[] STRING_NONOS = new String[]{"\n", "\t", ",", "'", "\""};
    private static final ArrayList<String> SPECIAL_TYPES = new ArrayList<>() {
        {
            add("NaN");
            add("NOVALUE");
        }};


    protected Datum(String type, String x, boolean parsing) throws Exception {
        this.type = type;
        if (x.equals("NOVALUE")) {
            val = x;
        }
        else {
            switch (type) {
                case "int" -> val = Integer.parseInt(x);
                case "float" -> val = floatValueChecker(x);
                case "string" -> val = strValueChecker(x);
                default -> throw new RuntimeException("Invalid type for datum instance");
            }
        }
    }

    protected Datum(String type, Object x) throws Exception {
        this.type = type;
        if (SPECIAL_TYPES.contains(x)) {
            this.val = x;
            if (type.equals("string") && x.equals("NaN")) {
                throw new RuntimeException("Cannot have a string type NaN value");
            }
        }
        else if (type.equals("float") || type.equals("int")) {
            this.val = x;
        }
        else if (type.equals("string")) {
            this.val = strValueChecker((String) x);
        }
        else {
            throw new RuntimeException("Invalid type for datum instance");
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////ARITHMETIC OPERATIONS///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////

    protected static Datum add(Datum x, Datum y) throws Exception {
        if ((x.isString() || y.isString()) && !(x.isString() && y.isString())) {
            throw new RuntimeException("Cannot add string type to float or int type!");
        }
        if (x.isNoValue() && y.isNoValue()) {
            if (x.isString()) {
                return new Datum("string", "NOVALUE");
            }
            else if (x.type.equals("float") || y.type.equals("float")) {
                return new Datum("float", "NOVALUE");
            }
            else {
                return new Datum("int", "NOVALUE");
            }
        }
        else if (x.isNoValue()) {
            return add(x.convertNoValue(), y);
        }
        else if (y.isNoValue()) {
            return add(x, y.convertNoValue());
        }
        if (x.isString()) {
            String x_string = x.val.toString();
            String y_string = y.val.toString();
            String x_core = x_string.substring(1, x_string.length() - 1);
            String y_core = y_string.substring(1, y_string.length() - 1);

            return new Datum("string","'" + x_core + y_core + "'");
        }
        if (x.val.equals("NaN")) {
            return new Datum(x.type, "NaN");
        } else if(y.val.equals("NaN")) {
            return new Datum(y.type, "NaN");
        }

        return switch (x.val) {
            case Float v when y.val instanceof Float -> new Datum("float", v + (Float) y.val);
            case Integer i when y.val instanceof Integer -> new Datum("int", i + (Integer) y.val);
            case Float v when y.val instanceof Integer -> new Datum("float", v + (Integer) y.val);
            case Integer i when y.val instanceof Float -> new Datum("float", i + (Float) y.val);
            case null, default -> throw new RuntimeException("Datums have invalid types");
        };
    }

    protected static Datum subtract(Datum x, Datum y) throws Exception {
        if (x.isString() || y.isString()) {
            throw new RuntimeException("Cannot perform subtraction on string types");
        }
        if (x.val.equals("NaN")) {
            return new Datum(x.type, "NaN");
        } else if(y.val.equals("NaN")) {
            return new Datum(y.type, "NaN");
        }
        if (x.isNoValue() && y.isNoValue()) {
            if (x.type.equals("float") || y.type.equals("float")) {
                return new Datum("float", "NOVALUE");
            }
            else {
                return new Datum("int", "NOVALUE");
            }
        }
        if (x.isNoValue()) {
            return subtract(x.convertNoValue(), y);
        }
        if (y.isNoValue()) {
            return subtract(x, y.convertNoValue());
        }
        return switch (x.val) {
            case Float v when y.val instanceof Float -> new Datum("float", v - (Float) y.val);
            case Integer i when y.val instanceof Integer -> new Datum("int", i - (Integer) y.val);
            case Float v when y.val instanceof Integer -> new Datum("float", v - (Integer) y.val);
            case Integer i when y.val instanceof Float -> new Datum("float", i - (Float) y.val);
            case null, default -> throw new RuntimeException("Datums have invalid types");
        };
    }

    protected static Datum multiply(Datum x, Datum y) throws Exception {
        if (x.isString() || y.isString()) {
            throw new RuntimeException("Cannot perform multiplication on string types");
        }
        if (x.val.equals("NaN")) {
            return new Datum(x.type, "NaN");
        } else if(y.val.equals("NaN")) {
            return new Datum(y.type, "NaN");
        }
        if (x.isNoValue() && y.isNoValue()) {
            if (x.type.equals("float") || y.type.equals("float")) {
                return new Datum("float", "NOVALUE");
            }
            else {
                return new Datum("int", "NOVALUE");
            }
        }
        if (x.isNoValue()) {
            return multiply(x.convertNoValue(), y);
        }
        if (y.isNoValue()) {
            return multiply(x, y.convertNoValue());
        }

        return switch (x.val) {
                case Float v when y.val instanceof Float -> new Datum("float", v * (Float) y.val);
                case Integer i when y.val instanceof Integer -> new Datum("int", i * (Integer) y.val);
                case Float v when y.val instanceof Integer -> new Datum("float", v * (Integer) y.val);
                case Integer i when y.val instanceof Float -> new Datum("float", i * (Float) y.val);
                case null, default -> throw new RuntimeException("Datums have invalid types");
        };

    }

    protected static Datum divide(Datum x, Datum y) throws Exception {
        if (x.isString() || y.isString()) {
            throw new RuntimeException("Cannot perform division on string types");
        }
        if (x.val.equals("NaN")) {
            return new Datum(x.type, "NaN");
        } else if (y.val.equals("NaN")) {
            return new Datum(y.type, "NaN");
        }
        if (x.isNoValue() && y.isNoValue()) {
            if (x.type.equals("float") || y.type.equals("float")) {
                return new Datum("float", "NOVALUE");
            }
            else {
                return new Datum("int", "NOVALUE");
            }
        }
        if (x.isNoValue()) {
            return divide(x.convertNoValue(), y);
        }
        if (y.isNoValue()) {
            return divide(x, y.convertNoValue());
        }

        String datum_type;
        if (x.val instanceof Integer && y.val instanceof Integer) {
            datum_type = "int";
        } else {
            datum_type = "float";
        }
        Datum result;
        try {
            switch (x.val) {
                case Float v when y.val instanceof Float -> result = new Datum(datum_type, v / (Float) y.val);
                case Integer i when y.val instanceof Integer -> result = new Datum(datum_type, i / (Integer) y.val);
                case Float v when y.val instanceof Integer -> result = new Datum(datum_type, v / (Integer) y.val);
                case Integer i when y.val instanceof Float -> result = new Datum(datum_type, i / (Float) y.val);
                case null, default -> throw new RuntimeException("Datums have invalid types");
            }
        } catch (ArithmeticException e) {
            if (e.getMessage().equals("/ by zero")) {
                return new Datum(datum_type, "NaN");
            } else {
                throw e;
            }
        }
        if (datum_type.equals("float") && result.val.toString().equals("Infinity")) {
            return new Datum(datum_type, "NaN");
        }
        else {
            return result;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////COMPARISON FUNCTIONS////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////

    protected static int compare(Datum x, Datum y) throws Exception {
        if ((x.isString() || y.isString()) && !(x.isString() && y.isString())) {
            throw new RuntimeException("Cannot compare string value to int or float value");
        }
        if (x.isNaN() || y.isNaN()) {
            return compareNaN(x, y);
        }
        else if (x.isString()) {
            return x.val.toString().compareTo(y.val.toString());
        }
        else {
            float x_val = Float.parseFloat(x.val.toString());
            float y_val = Float.parseFloat(y.val.toString());
            return Float.compare(x_val, y_val);
        }
    }

    private static int compareNaN(Datum x, Datum y) {
        if (x.val.equals("NaN") && y.val.equals("NaN")) {
            return 0;
        }
        else if(x.val.equals("NaN")) {
            return 1000;
        }
        else {
            return -1000;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////GENERAL HELPER FUNCTIONS AND TOSTRING///////////////////////
    ///////////////////////////////////////////////////////////////////////////////////

    private String strValueChecker (String x) throws Exception {
        if (x.equals("NaN")) {
            throw new RuntimeException("NaN is nit a valid string value.");
        }
        Matcher str_matcher = STRING_PATTERN.matcher(x);
        if(!(str_matcher.matches())) {
            throw new RuntimeException(x + " is not a valid string value. Please make sure there are single quotes around your string values.");
        }
        String val = str_matcher.group(1);
        for (String no : STRING_NONOS) {
            if (val.contains(no)) {
                throw new RuntimeException(x + " contains illegal string characters.");
            }
        }
        return x;
    }

    private float floatValueChecker (String x) throws Exception {
        if (!x.contains(".")) {
            throw new RuntimeException(x + " is not a valid float value. Make sure it contains a decimal point.");
        }
        return Float.parseFloat(x);
    }

    private boolean isString() {
        return type.equals("string");
    }

    protected boolean isNoValue() {
        return val.equals("NOVALUE");
    }

    protected boolean isNaN() {
        return val.equals("NaN");
    }

    private Datum convertNoValue() throws Exception {
        if (type.equals("string")) {
            return new Datum("string", "''");
        }
        else if (type.equals("float")) {
            return new Datum("float", Float.parseFloat("0.0"));
        }
        else {
            return new Datum("int", 0);
        }
    }

    public String toString() {
        if (val instanceof Float) {
            return String.format("%.03f", val);
        } else{
            return val.toString();
        }
    }

    public Datum clone() {
        try {
            return new Datum(type, val);
        } catch (Exception e) {
            return null;
        }
    }

}
