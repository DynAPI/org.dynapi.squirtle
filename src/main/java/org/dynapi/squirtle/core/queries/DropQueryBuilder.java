package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.criterion.Index;

public class DropQueryBuilder implements SqlAble {
    public static String QUOTE_CHAR = "\"";
    public static String SECONDARY_QUOTE_CHAR = "'";
    public static String ALIAS_QUOTE_CHAR = null;
    public static Class<? extends Query> QUERY_CLASS = Query.class;

    protected Object dropTargetKind = null;
    protected Object dropTarget = "";
    protected Boolean ifExists = null;
    protected Dialects dialect;

    public DropQueryBuilder() {
        this(null);
    }

    public DropQueryBuilder(Dialects dialect) {
        this.dialect = dialect;
    }

    protected SqlAbleConfig getSqlAbleConfigWithDefaults(SqlAbleConfig config) {
        SqlAbleConfig.SqlAbleConfigBuilder builder = config.toBuilder();
        if (config.getQuoteChar() == null) builder.quoteChar(QUOTE_CHAR);
        if (config.getSecondaryQuoteChar() == null) builder.secondaryQuoteChar(SECONDARY_QUOTE_CHAR);
        if (config.getDialect() == null) builder.dialect(dialect);
        return builder.build();
    }

    public DropQueryBuilder dropDatabase(Database database) {
        setTarget("DATABASE", database);
        return this;
    }

    public DropQueryBuilder dropTable(Table table) {
        setTarget("TABLE", table);
        return this;
    }

    public DropQueryBuilder dropUser(String user) {
        setTarget("USER", user);
        return this;
    }

    public DropQueryBuilder dropView(String view) {
        setTarget("VIEW", view);
        return this;
    }

    public DropQueryBuilder dropIndex(Index index) {
        setTarget("INDEX", index);
        return this;
    }

    public DropQueryBuilder ifExists() {
        return ifExists(true);
    }

    public DropQueryBuilder ifExists(Boolean ifExists) {
        this.ifExists = ifExists;
        return this;
    }

    protected void setTarget(String kind, Object target) {
        if (dropTarget != null)
            throw new RuntimeException("'DropQuery' object already has attribute dropTarget");
        this.dropTargetKind = kind;
        this.dropTarget = target;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        config = setSqlAbleConfigDefaults(config.toBuilder()).build();

        String ifExistsSql = ifExists ? "IF EXISTS " : "";
        String targetName = "";

        if (dropTarget instanceof SqlAble)
            targetName = ((SqlAble) dropTarget).getSql(config);
        else
            targetName = Utils.formatQuotes(dropTarget.toString(), config.getQuoteChar());

        return String.format("DROP %s %s%s", dropTargetKind, ifExistsSql, targetName);
    }
}
