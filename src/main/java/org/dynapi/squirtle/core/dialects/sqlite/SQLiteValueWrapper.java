package org.dynapi.squirtle.core.dialects.sqlite;

import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

public class SQLiteValueWrapper extends ValueWrapper {
    public SQLiteValueWrapper(SQLiteValueWrapper original) {
        super(original);
    }

    public SQLiteValueWrapper(Object value) {
        this(null, value);
    }

    public SQLiteValueWrapper(String alias, Object value) {
        super(alias, value);
    }

    @Override
    public String getValueSql(Object value, SqlAbleConfig config) {
        if (value instanceof Boolean) {
            return (boolean) value ? "1" : "0";
        }
        return super.getValueSql(value, config);
    }
}
