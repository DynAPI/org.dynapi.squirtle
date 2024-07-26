package org.dynapi.squirtle.core.terms;

import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.criterion.Field;

public class AtTimezone extends Term implements SqlAble {
    private final Field field;
    private final String zone;
    private final boolean interval;

    public AtTimezone(Field field, String zone, boolean interval, String alias) {
        super(alias);
        this.field = field;
        this.zone = zone;
        this.interval = interval;
    }

    public String getSql(SqlAbleConfig config) {
        String sql = String.format(
                "%s AT TIME ZONE %s'%s'",
                field.getSql(config),
                interval ? "INTERVAL " : "",
                zone
        );
        return Utils.formatAliasSql(sql, alias, config);
    }
}
