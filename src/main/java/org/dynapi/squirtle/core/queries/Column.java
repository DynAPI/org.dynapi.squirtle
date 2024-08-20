package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

public class Column implements SqlAble {
    private final String name;
    private final String columnType;
    private final Boolean nullable;
    private final Term defaultValue;

    public Column(Column original) {
        this.name = original.name;
        this.columnType = original.columnType;
        this.nullable = original.nullable;
        this.defaultValue = CloneUtils.copyConstructorClone(original.defaultValue);
    }

    public Column(String name) {
        this(name, null, null, null);
    }

    public Column(String name, String columnType) {
        this(name, columnType, null, null);
    }

    public Column(String name, String columnType, Boolean nullable, Object defaultValue) {
        this.name = name;
        this.columnType = columnType;
        this.nullable = nullable;
        this.defaultValue = (defaultValue instanceof Term) ? (Term) defaultValue : new ValueWrapper(null, defaultValue);
    }

    public String getNameSql(SqlAbleConfig config) {
        return Utils.formatQuotes(name, config.getQuoteChar());
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String sql = getNameSql(config);

        if (columnType != null)
            sql += " " + columnType;

        if (nullable != null)
            sql += nullable ? " NULL" : " NOT NULL";

        if (defaultValue != null)
            sql += String.format(" DEFAULT %s", defaultValue.getSql(config));

        return sql;
    }

    // xxx: toString
}
