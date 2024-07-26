package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

public class PeriodFor implements SqlAble {
    private final String name;
    public final Column startColumn;
    public final Column endColumn;

    public PeriodFor(String name, String startColumn, String endColumn) {
        this(name, new Column(startColumn), new Column(endColumn));
    }

    public PeriodFor(String name, Column startColumn, Column endColumn) {
        this.name = name;
        this.startColumn = startColumn;
        this.endColumn = endColumn;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        return String.format(
                "PERIOD FOR %s (%s,%s)",
                Utils.formatQuotes(name, config.getQuoteChar()),
                startColumn.getNameSql(config),
                endColumn.getNameSql(config)
        );
    }
}
