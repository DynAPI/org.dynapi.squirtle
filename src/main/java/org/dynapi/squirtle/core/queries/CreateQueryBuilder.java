package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.enums.ReferenceOption;
import org.dynapi.squirtle.core.interfaces.QueryBuilderAttributes;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CreateQueryBuilder implements QueryBuilderAttributes, SqlAble {
    public String sqlAbleQuoteChar() { return "\""; }
    public String sqlAbleSecondaryQuoteChar() { return "'"; }

    protected Table createTable = null;
    protected boolean temporary = false;
    protected boolean unlogged = false;
    protected QueryBuilder asSelect = null;
    protected List<Column> columns = new ArrayList<>();
    protected List<PeriodFor> periodFors = new ArrayList<>();
    protected boolean withSystemVersioning = false;
    protected List<Column> primaryKey = null;
    protected List<List<Column>> uniques = new ArrayList<>();
    protected boolean ifNotExists = false;
    protected Dialects dialect;
    protected List<Column> foreignKey = null;
    protected List<Column> foreignKeyReference = null;
    protected Table foreignKeyReferenceTable = null;
    protected ReferenceOption foreignKeyOnUpdate = null;
    protected ReferenceOption foreignKeyOnDelete = null;

    public CreateQueryBuilder(CreateQueryBuilder original) {
        this.createTable = CloneUtils.copyConstructorClone(original.createTable);
        this.temporary = original.temporary;
        this.unlogged = original.unlogged;
        this.asSelect = CloneUtils.copyConstructorClone(original.asSelect);
        this.columns = CloneUtils.copyConstructorCloneCollection(original.columns);
        this.periodFors = CloneUtils.copyConstructorCloneCollection(original.periodFors);
        this.withSystemVersioning = original.withSystemVersioning;
        this.primaryKey = CloneUtils.copyConstructorCloneCollection(original.primaryKey);
        // todo: better nested clone
        this.uniques = new ArrayList<>(original.uniques.stream().map(CloneUtils::copyConstructorCloneCollection).toList());
        this.ifNotExists = original.ifNotExists;
        this.dialect = original.dialect;
        this.foreignKey = CloneUtils.copyConstructorCloneCollection(original.foreignKey);
        this.foreignKeyReference = original.foreignKeyReference;
        this.foreignKeyReferenceTable = CloneUtils.copyConstructorClone(original.foreignKeyReferenceTable);
        this.foreignKeyOnUpdate = original.foreignKeyOnUpdate;
        this.foreignKeyOnDelete = original.foreignKeyOnDelete;
    }

    public CreateQueryBuilder() {
        this((Dialects) null);
    }

    public CreateQueryBuilder(Dialects dialect) {
        this.dialect = dialect;
    }

    public CreateQueryBuilder createTable(String tableName) {
        return createTable(new Table(null, tableName));
    }

    public CreateQueryBuilder createTable(Table table) {
        if (createTable != null)
            throw new RuntimeException("'Query' object already has attribute createTable");

        createTable = table;
        return this;
    }

    public CreateQueryBuilder temporary() {
        return temporary(true);
    }

    public CreateQueryBuilder temporary(boolean temporary) {
        this.temporary = temporary;
        return this;
    }

    public CreateQueryBuilder unlogged() {
        return unlogged(true);
    }

    public CreateQueryBuilder unlogged(boolean unlogged) {
        this.unlogged = unlogged;
        return this;
    }

    public CreateQueryBuilder withSystemVersioning() {
        return unlogged(true);
    }

    public CreateQueryBuilder withSystemVersioning(boolean withSystemVersioning) {
        this.withSystemVersioning = withSystemVersioning;
        return this;
    }

    public CreateQueryBuilder columns(String... columns) {
        return columns((Column[]) Arrays.stream(columns).map(Column::new).toArray());
    }

    public CreateQueryBuilder columns(Column... columns) {
        if (asSelect != null)
            throw new RuntimeException("'Query' object already has attribute asSelect");

        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public CreateQueryBuilder periodFor(String name, String startColumn, String endColumn) {
        return periodFor(name, new Column(startColumn), new Column(endColumn));
    }

    public CreateQueryBuilder periodFor(String name, Column startColumn, Column endColumn) {
        this.periodFors.add(new PeriodFor(name, startColumn, endColumn));
        return this;
    }

    public CreateQueryBuilder unique(String... uniques) {
        return unique((Column[]) Arrays.stream(uniques).map(Column::new).toArray());
    }

    public CreateQueryBuilder unique(Column... uniques) {
        this.uniques.add(List.of(uniques));
        return this;
    }

    public CreateQueryBuilder primaryKey(String... primaryKey) {
        return primaryKey((Column[]) Arrays.stream(primaryKey).map(Column::new).toArray());
    }

    public CreateQueryBuilder primaryKey(Column... primaryKey) {
        if (this.primaryKey != null)
            throw new RuntimeException("'Query' object already has attribute primaryKey");

        this.primaryKey = List.of(primaryKey);
        return this;
    }

    public CreateQueryBuilder foreignKey(Collection<Column> columns, Table referenceTable, List<Column> referencedColumns, ReferenceOption onUpdate, ReferenceOption onDelete) {
        if (this.foreignKey != null)
            throw new RuntimeException("'Query' object already has attribute foreignKey");

        this.foreignKey = new ArrayList<>(columns);
        this.foreignKeyReferenceTable = referenceTable;
        this.foreignKeyReference = referencedColumns;
        this.foreignKeyOnUpdate = onUpdate;
        this.foreignKeyOnDelete = onDelete;
        return this;
    }

    public CreateQueryBuilder asSelect(QueryBuilder asSelect) {
        if (!this.columns.isEmpty())
            throw new RuntimeException("'Query' object already has attribute columns");

        this.asSelect = asSelect;
        return this;
    }

    public CreateQueryBuilder ifNotExists() {
        return ifNotExists(true);
    }

    public CreateQueryBuilder ifNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        config = getSqlAbleConfigWithDefaults(config);

        if (createTable == null)
            return "";

        if (columns.isEmpty() && asSelect == null)
            return "";

        String createTableSql = this.createTableSql(config);

        if (asSelect != null)
            return createTableSql + asSelectSql(config);

        String bodySql = bodySql(config);
        String tableOptionsSql = tableOptionsSql(config);

        return String.format("%s (%s)%s", createTableSql, bodySql, tableOptionsSql);
    }

    public String createTableSql(SqlAbleConfig config) {
        String tableType = "";
        if (temporary)
            tableType = "TEMPORARY ";
        if (unlogged)
            tableType = "UNLOGGED ";

        String ifNotExists = "";
        if (this.ifNotExists)
            ifNotExists = "IF NOT EXISTS ";

        return String.format("CREATE %sTABLE %s %s", tableType, ifNotExists, createTable.getSql(config));
    }

    protected String tableOptionsSql(SqlAbleConfig ignored) {
        return this.withSystemVersioning ? " WITH SYSTEM_VERSIONING" : "";
    }

    protected List<String> columnClauses(SqlAbleConfig config) {
        return columns.stream().map(col -> col.getSql(config)).toList();
    }

    protected List<String> periodForClauses(SqlAbleConfig config) {
        return periodFors.stream().map(col -> col.getSql(config)).toList();
    }

    protected List<String> uniqueKeyClauses(SqlAbleConfig config) {
        return uniques.stream().map(
                unique -> String.format(
                        "UNIQUE (%s)",
                        String.join(",", unique.stream().map(col -> col.getNameSql(config)).toList())
                )
        ).toList();
    }

    protected String primaryKeyClause(SqlAbleConfig config) {
        return String.format(
                "PRIMARY KEY (%s)",
                String.join(",", primaryKey.stream().map(col -> col.getSql(config)).toList())
        );
    }

    protected String foreignKeyClause(SqlAbleConfig config) {
        String clause = String.format(
                "FOREIGN KEY (%s) REFERENCES %s (%s)",
                String.join(",", foreignKey.stream().map(col -> col.getNameSql(config)).toList()),
                foreignKeyReferenceTable.getSql(config),
                String.join(",", foreignKeyReference.stream().map(col -> col.getNameSql(config)).toList())
        );

        if (foreignKeyOnDelete != null)
            clause += " ON DELETE " + foreignKeyOnDelete.value;
        if (foreignKeyOnUpdate != null)
            clause += " ON UPDATE " + foreignKeyOnUpdate.value;

        return clause;
    }

    protected String bodySql(SqlAbleConfig config) {
        List<String> clauses = new ArrayList<>();
        clauses.addAll(columnClauses(config));
        clauses.addAll(periodForClauses(config));
        clauses.addAll(uniqueKeyClauses(config));

        if (!primaryKey.isEmpty())
            clauses.add(primaryKeyClause(config));
        if (!foreignKey.isEmpty())
            clauses.add(foreignKeyClause(config));

        return String.join(",", clauses);
    }

    protected String asSelectSql(SqlAbleConfig config) {
        return String.format(
                " AS (%s)",
                asSelect.getSql(config)
        );
    }
}
