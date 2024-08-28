package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

import java.util.List;

public class Array extends Tuple {
    public Array(Array original) {
        super(original);
    }

    public Array(List<?> values) {
        super(values);
    }

    public Array(Object... values) {
        super(values);
    }

    public Array as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String argList = String.join(",", values.stream().map(value -> value.getSql(config)).toList());
        String sql = String.format("[%s]", argList);
        if (List.of(Dialects.POSTGRESQL, Dialects.REDSHIFT).contains(config.getDialect()))
            sql = !values.isEmpty() ? String.format("ARRAY[%s]", argList) : "'{}'";
        return Utils.formatAliasSql(sql, alias, config);
    }
}
