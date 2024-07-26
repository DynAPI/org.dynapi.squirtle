package org.dynapi.squirtle.core.dialects.mysql;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Query;
import org.dynapi.squirtle.core.queries.QueryBuilder;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.criterion.Field;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;
import org.dynapi.squirtle.errors.QueryException;

import java.util.*;

public class MySQLQueryBuilder extends QueryBuilder {
    public static String QUOTE_CHAR = "`";
    public static Class<? extends Query> QUERY_CLASS = MySQLQuery.class;

    protected List<DuplicateUpdateEntry> duplicateUpdates = new ArrayList<>();
    protected boolean ignoreDuplicates = false;
    protected List<String> modifiers = new ArrayList<>();

    protected boolean forUpdateNoWait = false;
    protected boolean forUpdateSkipLocked = false;
    protected Set<String> forUpdateOf = new HashSet<>();

    public MySQLQueryBuilder(Boolean wrapSetOperationQueries, Class<? extends ValueWrapper> wrapperClass, Boolean immutable, Boolean asKeyword) {
        super(Dialects.MYSQL, wrapSetOperationQueries, wrapperClass, immutable, asKeyword);
    }

    public MySQLQueryBuilder forUpdate(boolean noWait, boolean skipLocked, Collection<String> of) {
        this.forUpdate = true;
        this.forUpdateSkipLocked = skipLocked;
        this.forUpdateNoWait = noWait;
        this.forUpdateOf = new HashSet<>(of);
        return this;
    }

    public MySQLQueryBuilder onDuplicateKeyUpdate(Field field, SqlAble value) {
        if (ignoreDuplicates)
            throw new QueryException("Can not have two conflict handlers");

        this.duplicateUpdates.add(new DuplicateUpdateEntry(field, value));
        return this;
    }

    public MySQLQueryBuilder onDuplicateKeyIgnore() {
        if (!duplicateUpdates.isEmpty())
            throw new QueryException("Can not have two conflict handlers");

        this.ignoreDuplicates = true;
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        config = getSqlAbleConfigWithDefaults(config);
        String querySql = super.getSql(config);
        if (!querySql.isEmpty()) {
            if (!duplicateUpdates.isEmpty())
                querySql += onDuplicateKeyUpdateSql(config);
            else if (ignoreDuplicates)
                querySql += onDuplicateKeyIgnoreSql(config);
        }
        return querySql;
    }

    protected String forUpdateSql(SqlAbleConfig config) {
        String forUpdate = "";
        if (this.forUpdate) {
            forUpdate = " FOR UPDATE";
            if (!forUpdateOf.isEmpty())
                forUpdate += String.join(", ", forUpdateOf.stream().map(item -> new Table(null, item).getSql(config)).toList());
            if (forUpdateNoWait)
                forUpdate += " NOWAIT";
            else if (forUpdateSkipLocked)
                forUpdate += " SKIP LOCKED";
        }
        return forUpdate;
    }

    protected String onDuplicateKeyUpdateSql(SqlAbleConfig config) {
        return String.format(
                " ON DUPLICATE KEY UPDATE %s",
                String.join(",", duplicateUpdates.stream().map(
                        entry -> String.format("%s=%s", entry.field.getSql(config), entry.value.getSql(config))
                ).toList())
        );
    }

    protected String onDuplicateKeyIgnoreSql(SqlAbleConfig ignored) {
        return " ON DUPLICATE KEY IGNORE";
    }

    public MySQLQueryBuilder modifier(String value) {
        modifiers.add(value);
        return this;
    }

    protected String selectSql(SqlAbleConfig config) {
        SqlAbleConfig selectConfig = config.withWithAlias(true).withSubQuery(true);
        return String.format(
                "SELECT %s%s%s",
                distinct ? "DISTINCT " : "",
                String.join(" ", modifiers) + " ",
                String.join(",", selects.stream().map(term -> term.getSql(selectConfig)).toList())
        );
    }

    public record DuplicateUpdateEntry(Field field, SqlAble value) {}
}
