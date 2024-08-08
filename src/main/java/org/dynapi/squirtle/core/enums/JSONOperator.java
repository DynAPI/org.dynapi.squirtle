package org.dynapi.squirtle.core.enums;

public class JSONOperator extends Enumerator {
    public final static JSONOperator HAS_KEY = new JSONOperator("?");
    public final static JSONOperator CONTAINS = new JSONOperator("@>");
    public final static JSONOperator CONTAINED_BY = new JSONOperator("<@");
    public final static JSONOperator HAS_KEYS = new JSONOperator("?&");
    public final static JSONOperator HAS_ANY_KEYS = new JSONOperator("?|");
    public final static JSONOperator GET_JSON_VALUE = new JSONOperator("->");
    public final static JSONOperator GET_TEXT_VALUE = new JSONOperator("->>");
    public final static JSONOperator GET_PATH_JSON_VALUE = new JSONOperator("#>");
    public final static JSONOperator GET_PATH_TEXT_VALUE = new JSONOperator("#>>");

    public JSONOperator(String value) {
        super(value);
    }
}
