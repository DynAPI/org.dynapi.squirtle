package org.dynapi.squirtle.core.dialects.clickhouse;

import lombok.NonNull;
import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

import java.util.ArrayList;
import java.util.List;

public class ClickHouseQueryBuilder extends QueryBuilder {
    public static Class<? extends Query> QUERY_CLASS = ClickHouseQuery.class;

    protected Integer sample = null;
    protected Integer sampleOffset = null;

    public ClickHouseQueryBuilder(ClickHouseQueryBuilder original) {
        super(original);
        this.sample = original.sample;
        this.sampleOffset = original.sampleOffset;
    }

    public ClickHouseQueryBuilder() {
        this(Config.builder().build());
    }

    public ClickHouseQueryBuilder(Config config) {
        super(config.toBuilder()
                .dialect(Dialects.CLICKHOUSE)
                .wrapSetOperationQueries(false)
                .asKeyword(true)
                .build()
        );
    }

    public ClickHouseQueryBuilder sample(@NonNull Integer sample, Integer offset) {
        this.sample = sample;
        this.sampleOffset = offset;
        return this;
    }

    protected String deleteSql(SqlAbleConfig ignored) {
        return "ALTER TABLE";
    }

    protected String updateSql(SqlAbleConfig config) {
        return String.format("ALTER TABLE %s", updateTable.getSql(config));
    }

    protected String fromSql(SqlAbleConfig config) {
        SqlAbleConfig selectConfig = config.withSubQuery(true).withWithAlias(true);
        String selectable = String.join(",", from.stream().map(clause -> clause.getSql(selectConfig)).toList());
        if (deleteFrom != null)
            return String.format(" %s DELETE", selectable);
        List<String> clauses = new ArrayList<>();
        clauses.add(selectable);
        if (sample != null)
            clauses.add(String.format("SAMPLE %d", sample));
        if (sampleOffset != null)
            clauses.add(String.format("OFFSET %d", sampleOffset));
        return String.format(" FROM %s", String.join(" ", clauses));
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        return String.format(
                " UPDATE %s",
                String.join(",", updates.stream().map(
                        entry -> String.format(
                                "%s=%s",
                                entry.field().getSql(config.withWithNamespace(true)),
                                entry.value().getSql(config))
                ).toList())
        );
    }
}
