package org.dynapi.squirtle.core.terms.values;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.enums.JSONOperators;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.criterion.Array;
import org.dynapi.squirtle.core.terms.criterion.BasicCriterion;

import java.util.List;
import java.util.Map;

public class JSON extends Term {
    private Object value;

    public JSON(String alias, Object value) {
        super(alias);
        this.value = value;
    }

    private String getRecursiveSql(Object value, SqlAbleConfig config) {
        if (value instanceof Map<?,?>)
            return getMappingSql(value, config);
        if (value instanceof List<?>)
            return getListSql(value, config);
        if (value instanceof String)
            return getStringSql(value, config);
        return value.toString();
    }

    private String getMappingSql(Object value, SqlAbleConfig config) {

    }

    private String getListSql(Object value, SqlAbleConfig config) {

    }

    private String getStringSql(Object value, SqlAbleConfig config) {

    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String sql = Utils.formatQuotes(getRecursiveSql(value, config), config.getSecondaryQuoteChar());
        return Utils.formatAliasSql(sql, alias, config);
    }

    public BasicCriterion getJsonValue(String key) {
        return new BasicCriterion(null, JSONOperators.GET_JSON_VALUE, this, wrapConstant(key));
    }

    public BasicCriterion getJsonValue(Integer index) {
        return new BasicCriterion(null, JSONOperators.GET_JSON_VALUE, this, wrapConstant(index));
    }

    public BasicCriterion getTextValue(String key) {
        return new BasicCriterion(null, JSONOperators.GET_TEXT_VALUE, this, wrapConstant(key));
    }

    public BasicCriterion getTextValue(Integer index) {
        return new BasicCriterion(null, JSONOperators.GET_TEXT_VALUE, this, wrapConstant(index));
    }

    public BasicCriterion getPathJsonValue(String pathJson) {
        return new BasicCriterion(null, JSONOperators.GET_PATH_JSON_VALUE, this, wrapConstant(pathJson));
    }

    public BasicCriterion getPathTextValue(String pathJson) {
        return new BasicCriterion(null, JSONOperators.GET_PATH_TEXT_VALUE, this, wrapConstant(pathJson));
    }

    public BasicCriterion hasKey(String key) {
        return new BasicCriterion(null, JSONOperators.HAS_KEY, this, wrapConstant(key));
    }

    public BasicCriterion contains(Object other) {
        return new BasicCriterion(null, JSONOperators.HAS_KEY, this, (Term) wrapJson(other));
    }

    public BasicCriterion containedBy(Object other) {
        return new BasicCriterion(null, JSONOperators.HAS_KEY, this, (Term) wrapJson(other));
    }

    public BasicCriterion hasKeys(Object... other) {
        return new BasicCriterion(null, JSONOperators.HAS_KEYS, this, new Array(other));
    }

    public BasicCriterion hasAnyKeys(Object... other) {
        return new BasicCriterion(null, JSONOperators.HAS_ANY_KEYS, this, new Array(other));
    }
}
