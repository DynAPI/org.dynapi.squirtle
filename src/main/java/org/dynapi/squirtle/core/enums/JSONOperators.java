package org.dynapi.squirtle.core.enums;

public class JSONOperators extends Enumerator {
    public final static JSONOperators HAS_KEY = new JSONOperators("?");
    public final static JSONOperators CONTAINS = new JSONOperators("@>");
    public final static JSONOperators CONTAINED_BY = new JSONOperators("<@");
    public final static JSONOperators HAS_KEYS = new JSONOperators("?&");
    public final static JSONOperators HAS_ANY_KEYS = new JSONOperators("?|");
    public final static JSONOperators GET_JSON_VALUE = new JSONOperators("->");
    public final static JSONOperators GET_TEXT_VALUE = new JSONOperators("->>");
    public final static JSONOperators GET_PATH_JSON_VALUE = new JSONOperators("#>");
    public final static JSONOperators GET_PATH_TEXT_VALUE = new JSONOperators("#>>");

    public JSONOperators(String value) {
        super(value);
    }
}
