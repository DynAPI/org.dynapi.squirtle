package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

import java.util.Arrays;
import java.util.List;

public class Array extends Tuple {
    public Array(Object... values) {
        super(values);
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String argList = String.join(",", Arrays.stream(values).map(value -> value.getSql(config)).toList());
        String sql = String.format("[%s]", argList);
        if (List.of(Dialects.POSTGRESQL, Dialects.REDSHIFT).contains(config.getDialect()))
            sql = values.length > 0 ? String.format("ARRAY[%s]", argList) : "'{}'";
        return Utils.formatAliasSql(sql, alias, config);
    }
}
