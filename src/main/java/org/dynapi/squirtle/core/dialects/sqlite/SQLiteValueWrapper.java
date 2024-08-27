package org.dynapi.squirtle.core.dialects.sqlite;

import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

public class SQLiteValueWrapper extends ValueWrapper {
    public SQLiteValueWrapper(SQLiteValueWrapper original) {
        super(original);
    }

    public SQLiteValueWrapper(Object value) {
        super(value);
    }

    public SQLiteValueWrapper as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String getValueSql(Object value, SqlAbleConfig config) {
        if (value instanceof Boolean booleanValue) {
            return booleanValue ? "1" : "0";
        }
        return super.getValueSql(value, config);
    }
}
