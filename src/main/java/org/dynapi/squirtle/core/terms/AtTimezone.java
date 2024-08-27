package org.dynapi.squirtle.core.terms;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.criterion.Field;

public class AtTimezone extends Term implements SqlAble {
    private final Field field;
    private final String zone;
    private final boolean interval;

    public AtTimezone(AtTimezone original) {
        super(original);
        this.field = CloneUtils.copyConstructorClone(original.field);
        this.zone = original.zone;
        this.interval = original.interval;
    }

    public AtTimezone(Field field, String zone, boolean interval) {
        this.field = field;
        this.zone = zone;
        this.interval = interval;
    }

    public AtTimezone as(String alias) {
        this.alias = alias;
        return this;
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
