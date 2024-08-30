package org.dynapi.squirtle.core.terms.values;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.enums.JSONOperator;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.criterion.Array;
import org.dynapi.squirtle.core.terms.criterion.BasicCriterion;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class JSON extends Term {
    private final Object value;

    public JSON(JSON original) {
        super(original);
        this.value = CloneUtils.copyConstructorClone(original.value);
    }

    public JSON() {
        this((Object) null);
    }

    public JSON(Object value) {
        this.value = value;
    }

    public JSON as(String alias) {
        this.alias = alias;
        return this;
    }


    private String getRecursiveSql(Object value, SqlAbleConfig config) {
        if (value instanceof Map<?,?> map)
            return getMappingSql(map, config);
        if (value instanceof List<?> list)
            return getListSql(list, config);
        if (value instanceof CharSequence charSequence)
            return getStringSql(charSequence, config);
        return value.toString();
    }

    private String getMappingSql(Map<?, ?> map, SqlAbleConfig config) {
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        for (Map.Entry<?, ?> item : map.entrySet()) {
            joiner.add(String.format(
                    "%s:%s",
                    getRecursiveSql(item.getKey(), config),
                    getRecursiveSql(item.getValue(), config)
            ));
        }
        return joiner.toString();
    }

    private String getListSql(List<?> list, SqlAbleConfig config) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (Object item : list) {
            joiner.add(getRecursiveSql(item, config));
        }
        return joiner.toString();
    }

    private String getStringSql(CharSequence value, SqlAbleConfig config) {
        return Utils.formatQuotes(value.toString(), config.getQuoteChar());
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String sql = Utils.formatQuotes(getRecursiveSql(value, config), config.getSecondaryQuoteChar());
        return Utils.formatAliasSql(sql, alias, config);
    }

    public BasicCriterion getJsonValue(String key) {
        return new BasicCriterion(JSONOperator.GET_JSON_VALUE, this, wrapConstant(key));
    }

    public BasicCriterion getJsonValue(Integer index) {
        return new BasicCriterion(JSONOperator.GET_JSON_VALUE, this, wrapConstant(index));
    }

    public BasicCriterion getTextValue(String key) {
        return new BasicCriterion(JSONOperator.GET_TEXT_VALUE, this, wrapConstant(key));
    }

    public BasicCriterion getTextValue(Integer index) {
        return new BasicCriterion(JSONOperator.GET_TEXT_VALUE, this, wrapConstant(index));
    }

    public BasicCriterion getPathJsonValue(String pathJson) {
        return new BasicCriterion(JSONOperator.GET_PATH_JSON_VALUE, this, wrapConstant(pathJson));
    }

    public BasicCriterion getPathTextValue(String pathJson) {
        return new BasicCriterion(JSONOperator.GET_PATH_TEXT_VALUE, this, wrapConstant(pathJson));
    }

    public BasicCriterion hasKey(String key) {
        return new BasicCriterion(JSONOperator.HAS_KEY, this, wrapConstant(key));
    }

    public BasicCriterion contains(Object other) {
        return new BasicCriterion(JSONOperator.HAS_KEY, this, (Term) wrapJson(other));
    }

    public BasicCriterion containedBy(Object other) {
        return new BasicCriterion(JSONOperator.HAS_KEY, this, (Term) wrapJson(other));
    }

    public BasicCriterion hasKeys(Object... other) {
        return new BasicCriterion(JSONOperator.HAS_KEYS, this, new Array(other));
    }

    public BasicCriterion hasAnyKeys(Object... other) {
        return new BasicCriterion(JSONOperator.HAS_ANY_KEYS, this, new Array(other));
    }
}
