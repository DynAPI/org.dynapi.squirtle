package org.dynapi.squirtle.core.queries;

import lombok.Getter;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.enums.JoinType;
import org.dynapi.squirtle.core.enums.Order;
import org.dynapi.squirtle.core.enums.SetOperations;
import org.dynapi.squirtle.core.interfaces.FinalSqlAble;
import org.dynapi.squirtle.core.interfaces.QueryBuilderAttributes;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.criterion.*;
import org.dynapi.squirtle.core.terms.functions.Rollup;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;
import org.dynapi.squirtle.errors.QueryException;

import java.util.*;

public class QueryBuilder extends Term implements QueryBuilderAttributes, Selectable, FinalSqlAble {
    public String sqlAbleQuoteChar() { return "\""; }
    public String sqlAbleSecondaryQuoteChar() { return "'"; }

    protected List<Selectable> from = new ArrayList<>();
    protected Selectable insertTable = null;
    protected Selectable updateTable = null;
    protected Boolean deleteFrom = null;
    protected boolean replace = false;

    protected List<AliasedQuery> with = new ArrayList<>();
    @Getter
    protected List<Term> selects = new ArrayList<>();
    protected List<Index> forceIndexes = new ArrayList<>();
    protected List<Index> useIndexes = new ArrayList<>();
    protected List<SqlAble> columns = new ArrayList<>();
    protected List<List<Term>> values = new ArrayList<>();
    protected boolean distinct = false;
    protected boolean ignore = false;

    protected boolean forUpdate = false;

    protected Criterion wheres = null;
    protected Criterion preWheres = null;
    protected List<Term> groupBys = new ArrayList<>();
    protected boolean withTotals = false;
    protected Criterion havings = null;
    protected List<OrderByEntry> orderBys = new ArrayList<>();
    protected List<Join> joins = new ArrayList<>();
    protected List<Selectable> unions = new ArrayList<>();
    protected List<Selectable> using = new ArrayList<>();

    protected Integer limit = null;
    protected Integer offset = null;

    protected List<UpdateEntry> updates = new ArrayList<>();

    protected boolean selectStar = false;
    protected Set<Object> selectStarTables = new HashSet<>();
    protected boolean mySqlRollup = false;
    protected boolean selectInto = false;

    protected int subqueryCount = 0;
    protected boolean foreignTable = false;

    @Getter
    protected final Dialects dialect;
    protected final boolean wrapSetOperationQueries;
    protected final boolean asKeyword;

    protected final Class<? extends ValueWrapper> wrapperClass;

    protected final boolean immutable;

    public QueryBuilder() {
        this(null, null, null, null, null);
    }

    public QueryBuilder(Dialects dialect, Boolean wrapSetOperationQueries, Class<? extends ValueWrapper> wrapperClass, Boolean immutable, Boolean asKeyword) {
        super(null);

        this.dialect = dialect;
        this.asKeyword = asKeyword == null ? false : asKeyword;
        this.wrapSetOperationQueries = wrapSetOperationQueries == null ? true : wrapSetOperationQueries;
        this.wrapperClass = wrapperClass;
        this.immutable = immutable == null ? true : immutable;
    }

    // xxx: __copy__

    public QueryBuilder from(String tableName) {
        return from(new Table(null, tableName));
    }

    public QueryBuilder from(Selectable selectable) {
        this.from.add(selectable);

        if ((selectable instanceof QueryBuilder) || (selectable instanceof SetOperation) && selectable.getAlias() == null) {
            int subQueryCount;
            if (selectable instanceof QueryBuilder) subQueryCount = ((QueryBuilder) selectable).subqueryCount;
            else subQueryCount = 0;

            subQueryCount = Math.max(this.subqueryCount, subQueryCount);
            selectable.setAlias(String.format("sq%d", subQueryCount));
            this.subqueryCount = subQueryCount;
        }

        return this;
    }

    @Override
    public QueryBuilder replaceTable(Table currentTable, Table newTable) {
// todo
        return this;
    }

    public QueryBuilder with(Selectable selectable, String name) {
        with.add(new AliasedQuery(name, selectable));
        return this;
    }

    public QueryBuilder into(String tableName) {
        return into(new Table(null, tableName));
    }

    public QueryBuilder into(Table table) {
        if (insertTable != null)
            throw new RuntimeException("'into' method is no longer available");

        if (!selects.isEmpty())
            selectInto = true;

        insertTable = table;

        return this;
    }

    public QueryBuilder select(Object... terms) {
        for (Object term : terms) {
            if (term instanceof Field field) {
                selectField(field);
            } else if (term instanceof String string) {
                selectFieldStr(string);
//            } else if (term instanceof Function || term instanceof ArithmeticExpression) {
//                selectOther(term);
            } else {
                selectOther(wrapConstant(term, wrapperClass));
            }
        }
        return this;
    }

    public QueryBuilder delete() {
        if (deleteFrom || !selects.isEmpty() || updateTable != null)
            throw new RuntimeException("'delete' method is no longer available");

        deleteFrom = true;
        return this;
    }

    public QueryBuilder update(String tableName) {
        return update(new Table(null, tableName));
    }

    public QueryBuilder update(Table table) {
        if (updateTable != null || !selects.isEmpty() || deleteFrom)
            throw new RuntimeException("'update' method is no longer available");

        updateTable = table;
        return this;
    }

    public QueryBuilder columns(Object... terms) {
        if (insertTable != null)
            throw new RuntimeException("'columns' method is no longer available");

        for (Object term : terms) {
            if (term instanceof String fieldName)
                term = new Field(null, fieldName, insertTable);
            columns.add((SqlAble) term);
        }
        return this;
    }

    public QueryBuilder insert(Object... terms) {
        applyTerms(List.of(terms));
        replace = false;
        return this;
    }

    public QueryBuilder forceIndex(Object... terms) {
        for (Object term : terms) {
            if (term instanceof Index index) {
                forceIndexes.add(index);
            } else if (term instanceof String indexName) {
                forceIndexes.add(new Index(null, indexName));
            }
        }
        return this;
    }

    public QueryBuilder useIndex(Object... terms) {
        for (Object term : terms) {
            if (term instanceof Index index) {
                useIndexes.add(index);
            } else if (term instanceof String indexName) {
                useIndexes.add(new Index(null, indexName));
            }
        }
        return this;
    }

    public QueryBuilder distinct() {
        distinct = true;
        return this;
    }

    public QueryBuilder forUpdate() {
        forUpdate = true;
        return this;
    }

    public QueryBuilder ignore() {
        ignore = true;
        return this;
    }

    public QueryBuilder preWhere(Criterion criterion) {
        if (!validateTable(criterion))
            foreignTable = true;

        if (preWheres != null)
            preWheres = preWheres.and(criterion);
        else
            preWheres = criterion;

        return this;
    }

    public QueryBuilder where(Criterion criterion) {
        if (criterion instanceof EmptyCriterion) return this;

        if (!validateTable(criterion))
            foreignTable = true;

        if (wheres != null)
            wheres = wheres.and(criterion);
        else
            wheres = criterion;

        return this;
    }

    public QueryBuilder having(Criterion criterion) {
        if (criterion instanceof EmptyCriterion) return this;

        if (havings != null)
            havings = havings.and(criterion);
        else
            havings = criterion;

        return this;
    }

    public QueryBuilder groupBy(Object... terms) {
        for (Object term : terms) {
            if (term instanceof String fieldName)
                term = new Field(null, fieldName, from.get(0));
            else if (term instanceof Integer fieldIndex)
                term = Term.wrapConstant(fieldIndex);

            groupBys.add((Term) term);
        }
        return this;
    }

    public QueryBuilder withTotals() {
        withTotals = true;
        return this;
    }

    public QueryBuilder rollup(Term... terms) {
        return rollup(terms, null);
    }

    public QueryBuilder rollup(Term[] terms, String vendor) {
        boolean forMySql = Objects.equals(vendor, "mysql");

        if (mySqlRollup)
            throw new RuntimeException("'rollup' method is no longer available");

        if (forMySql) {
            // MySQL rolls up all the dimensions always
            if (terms.length == 0 && groupBys.isEmpty())
                throw new RuntimeException("At least one group is required. Call Query.groupby(term) or pass as parameter to rollup.");

            mySqlRollup = true;
            groupBys.addAll(List.of(terms));
        } else if (!groupBys.isEmpty() && groupBys.get(groupBys.size()-1) instanceof Rollup rollup) {
            rollup.addArgs(terms);
        } else {
            groupBys.add(new Rollup(terms));
        }
        return this;
    }

    public QueryBuilder orderBy(Object... fields) {
        return orderBy(fields, null);
    }

    public QueryBuilder orderBy(Object[] fields, Order order) {
        return orderBy(
                Arrays
                        .stream(fields)
                        .map(field -> field instanceof String fieldName ? new Field(null, fieldName, from.get(0)) : wrapConstant(field))
                        .map(field -> new OrderByEntry(field, order))
                        .toList()
        );
    }

    public QueryBuilder orderBy(List<OrderByEntry> fields) {
        orderBys.addAll(fields);
        return this;
    }

    public Joiner join(Selectable selectable) {
        return join(selectable, JoinType.INNER);
    }
    public Joiner join(Selectable selectable, JoinType how) {
        return new Joiner(this, selectable, how, "subquery");
    }

    public Joiner innerJoin(Selectable selectable) {
        return join(selectable, JoinType.INNER);
    }
    public Joiner leftJoin(Selectable selectable) {
        return join(selectable, JoinType.LEFT);
    }
    public Joiner leftOuterJoin(Selectable selectable) {
        return join(selectable, JoinType.LEFT_OUTER);
    }
    public Joiner rightJoin(Selectable selectable) {
        return join(selectable, JoinType.RIGHT);
    }
    public Joiner rightOuterJoin(Selectable selectable) {
        return join(selectable, JoinType.RIGHT_OUTER);
    }
    public Joiner outerJoin(Selectable selectable) {
        return join(selectable, JoinType.OUTER);
    }
    public Joiner fullOuterJoin(Selectable selectable) {
        return join(selectable, JoinType.FULL_OUTER);
    }
    public Joiner crossJoin(Selectable selectable) {
        return join(selectable, JoinType.CROSS);
    }
    public Joiner hashJoin(Selectable selectable) {
        return join(selectable, JoinType.HASH);
    }

    public QueryBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }

    public QueryBuilder offset(int offset) {
        this.offset = offset;
        return this;
    }

    public SetOperation union(QueryBuilder other) {
        return new SetOperation(null, this, other, SetOperations.UNION, wrapperClass);
    }

    public SetOperation unionAll(QueryBuilder other) {
        return new SetOperation(null, this, other, SetOperations.UNION_ALL, wrapperClass);
    }

    public SetOperation intersect(QueryBuilder other) {
        return new SetOperation(null, this, other, SetOperations.INTERSECT, wrapperClass);
    }

    public SetOperation exceptOf(QueryBuilder other) {
        return new SetOperation(null, this, other, SetOperations.EXCEPT_OF, wrapperClass);
    }

    public SetOperation minus(QueryBuilder other) {
        return new SetOperation(null, this, other, SetOperations.MINUS, wrapperClass);
    }

    public QueryBuilder set(String fieldName, Objects value) {
        return set(new Field(null, fieldName, null), value);
    }
    public QueryBuilder set(Field field, Objects value) {
        updates.add(new UpdateEntry(field, Utils.newInstance(wrapperClass, null, value)));
        return this;
    }

    protected List<String> listAliases(Collection<Field> fieldSet, String quoteChar) {
        SqlAbleConfig config = SqlAbleConfig.builder().quoteChar(quoteChar).build();
        return fieldSet.stream().map(field -> field.getAlias() != null ? field.getAlias() : field.getSql(config)).toList();
    }

    protected void selectFieldStr(String term) {
        if (from.isEmpty())
            throw new QueryException(String.format("Cannot select '%s', no FROM table specified", term));

        if ("*".equals(term)) {
            selectStar = true;
            selects = List.of(new Star());
        } else {
            selectField(new Field(null, term, from.get(0)));
        }
    }

    protected void selectField(Field term) {
        if (selectStar) return;

        if (selectStarTables.contains(term.getTable())) return;

        if (term instanceof Star) {
            this.selects = selects.stream()
                            // todo: verify if field is only with table
                            .filter(select -> select instanceof Field field && term.getTable() != field.getTable())
                            .toList();
            selectStarTables.add(term.getTable());
        } else {
            this.selects.add(term);
        }
    }

    protected void selectOther(Term term) {
        this.selects.add(term);
    }

    public List<Field> fields() {
        // Don't return anything here. Subqueries have their own fields.
        return List.of();
    }

    public void doJoin(Join join) {
        List<Selectable> baseTables = new ArrayList<>();
        baseTables.addAll(from);
        baseTables.add(updateTable);
        baseTables.addAll(with);

        join.validate(baseTables, joins);

        boolean tableInQuery = baseTables.stream().anyMatch(clause -> clause instanceof Table && baseTables.contains(join.item));
        if (join.item instanceof Table joinItem && joinItem.getAlias() == null && tableInQuery) {
            joinItem.setAlias(joinItem.getTableName() + "2");
        }
    }

    public boolean isJoined(Table table) {
        return joins.stream().anyMatch(join -> Objects.equals(table, join.item));
    }

    protected boolean validateTable(Term term) {
        // Returns False if the term references a table not already part of the FROM clause or JOINS and True otherwise.
        List<Selectable> baseTables = new ArrayList<>();
        baseTables.addAll(from);
        baseTables.add(updateTable);

        for (Field field : term.getFields()) {
            boolean tableInBaseTables = baseTables.contains(field.getTable());
            boolean tableInJoins = joins.stream().anyMatch(join -> Objects.equals(join.item, field.getTable()));
            if (
                    field.getTable() != null
                    && !tableInBaseTables
                    && !tableInJoins
                    && !Objects.equals(field.getTable(), updateTable)
            ) {
                return false;
            }
        }
        return true;
    }

    protected void tagSubquery(QueryBuilder subquery) {
        subquery.setAlias(String.format("sq%d", subqueryCount));
        subqueryCount += 1;
    }

    protected void applyTerms(List<Object>... terms) {
        if (insertTable == null)
            throw new RuntimeException("this is currently not available");

        for (List<Object> values : terms) {
            this.values.add(values.stream().map(value -> (value instanceof Term term) ? term : wrapConstant(value)).toList());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof QueryBuilder other)) return false;

        return Objects.equals(alias, other.alias);
    }

    @Override
    public int hashCode() {
        return alias.hashCode() + from.stream().map(Object::hashCode).reduce(0, Integer::sum);
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        // todo?: defaults
        // todo: pop defaults from config
        config = getSqlAbleConfigWithDefaults(config);

        if (!(!selects.isEmpty() || insertTable != null || deleteFrom != null || updateTable != null))
            return "";
        if (insertTable != null && !(!selects.isEmpty() || !values.isEmpty()))
            return "";
        if (updateTable != null && updates.isEmpty())
            return "";

        boolean hasJoins = !joins.isEmpty();
        boolean hashMultipleFromClause = from.size() > 1;
        boolean hasSubqueryFromClause = !from.isEmpty() && from.get(0) instanceof QueryBuilder;
        boolean hasReferenceToForeignTable = foreignTable;
        boolean hasUpdateFrom = updateTable != null && !from.isEmpty();

        config = config.withWithNamespace(hasJoins || hashMultipleFromClause || hasSubqueryFromClause || hasReferenceToForeignTable || hasUpdateFrom);

        String queryString = "";

        if (updateTable != null) {
            if (!with.isEmpty())
                queryString = withSql(config);

            queryString += updateSql(config);

            if (!joins.isEmpty()) {
                SqlAbleConfig joinConfig = config;
                queryString += " " + String.join(" ", joins.stream().map(join -> join.getSql(joinConfig)).toList());
            }

            queryString += setSql(config);

            if (!from.isEmpty())
                queryString += fromSql(config);

            if (wheres != null)
                queryString += whereSql(config);

            if (limit != null)
                queryString += limitSql(config);

            return queryString;
        }

        if (deleteFrom) {
            queryString = deleteSql(config);
        } else if (!selectInto && insertTable != null) {
            if (!with.isEmpty())
                queryString = withSql(config);

            if (replace)
                queryString += replaceSql(config);
            else
                queryString += insertSql(config);

            if (!columns.isEmpty())
                queryString += columnsSql(config);

            if (!values.isEmpty()) {
                queryString += valuesSql(config);
                return queryString;
            } else
                queryString += " " + selectSql(config);
        } else {
            if (!with.isEmpty())
                queryString += withSql(config);

            queryString += selectSql(config);

            if (insertTable != null)
                queryString += intoSql(config);
        }

        if (!from.isEmpty())
            queryString += fromSql(config);

        if (!using.isEmpty())
            queryString += usingSql(config);

        if (!forceIndexes.isEmpty())
            queryString += forceIndexSql(config);

        if (useIndexes != null)
            queryString += useIndexSql(config);

        if (!joins.isEmpty()) {
            SqlAbleConfig joinConfig = config;
            queryString += " " + String.join(" ", joins.stream().map(join -> join.getSql(joinConfig)).toList());
        }

        if (preWheres != null)
            queryString += preWhereSql(config);

        if (wheres != null)
            queryString += whereSql(config);

        if (!groupBys.isEmpty()) {
            queryString += groupSql(config);
            if (mySqlRollup)
                queryString += rollupSql(config);
        }

        if (havings != null)
            queryString += havingSql(config);

        if (!orderBys.isEmpty())
            queryString += orderBySql(config);

        queryString += paginationSql(config);

        if (forUpdate)
            queryString += forUpdateSql(config);

        if (config.isSubQuery())
            queryString = String.format("(%s)", queryString);

        if (config.isWithAlias()) {
            config = config.withAliasQuoteChar(
                    sqlAbleQueryAliasQuoteChar() == null ? sqlAbleAliasQuoteChar() : sqlAbleQueryAliasQuoteChar()
            );
            return Utils.formatAliasSql(queryString, alias, config);
        }

        return queryString;
    }

    protected String paginationSql(SqlAbleConfig config) {
        String queryString = "";

        if (limit != null)
            queryString += limitSql(config);

        if (offset != null)
            queryString += offsetSql(config);

        return queryString;
    }

    protected String withSql(SqlAbleConfig config) {
        SqlAbleConfig withConfig = config.withSubQuery(false).withWithAlias(false);
        return "WITH " + String.join(",", with.stream().map(
                clause -> String.format("%s AS (%s)", clause.getName(), clause.getSql(withConfig))
        ).toList());
    }

    protected String distinctSql(SqlAbleConfig config) {
        return distinct ? "DISTINCT " : "";
    }

    protected String forUpdateSql(SqlAbleConfig config) {
        return forUpdate ? " FOR UPDATE" : "";
    }

    protected String selectSql(SqlAbleConfig config) {
        SqlAbleConfig selectsConfig = config = config.withWithAlias(true).withSubQuery(true);
        return String.format(
                "SELECT %s%s",
                distinctSql(config),
                String.join(",", selects.stream().map(term -> term.getSql(selectsConfig)).toList())
        );
    }

    protected String insertSql(SqlAbleConfig config) {
        return String.format(
                "INSERT %sINTO %s",
                ignore ? "IGNORE " : "",
                insertTable.getSql(config)
        );
    }

    protected String replaceSql(SqlAbleConfig config) {
        return String.format(
                "REPLACE INTO %s",
                insertTable.getSql(config)
        );
    }

    protected String deleteSql(SqlAbleConfig config) {
        return "DELETE";
    }

    protected String updateSql(SqlAbleConfig config) {
        return String.format(
                "UPDATE %s",
                updateTable.getSql(config)
        );
    }

    protected String columnsSql(SqlAbleConfig config) {
        SqlAbleConfig columnsConfig = config.withWithNamespace(false);
        return String.format(
                " (%s)",
                String.join(",", columns.stream().map(term -> term.getSql(columnsConfig)).toList())
        );
    }

    protected String valuesSql(SqlAbleConfig config) {
        SqlAbleConfig valuesConfig = config.withWithAlias(true).withSubQuery(true);
        return String.format(
                " VALUES (%s)",
                String.join(
                        "),(",
                        values.stream().map(
                            value -> String.join(",", value.stream().map(row -> row.getSql(valuesConfig)).toList()
                            )
                        ).toList()
                )
        );
    }

    protected String intoSql(SqlAbleConfig config) {
        return String.format(
                " INTO %s",
                insertTable.getSql(config.withWithAlias(false))
        );
    }

    protected String fromSql(SqlAbleConfig config) {
        SqlAbleConfig fromConfig = config.withSubQuery(true).withWithAlias(true);
        return String.format(
                " FROM %s",
                String.join(",", from.stream().map(term -> term.getSql(fromConfig)).toList())
        );
    }

    protected String usingSql(SqlAbleConfig config) {
        SqlAbleConfig usingConfig = config.withSubQuery(true).withWithAlias(true);
        return String.format(
                " USING %s",
                String.join(",", using.stream().map(clause -> clause.getSql(usingConfig)).toList())
        );
    }

    protected String forceIndexSql(SqlAbleConfig config) {
        return String.format(
                " FORCE INDEX (%s)",
                String.join(",", forceIndexes.stream().map(index -> index.getSql(config)).toList())
        );
    }

    protected String useIndexSql(SqlAbleConfig config) {
        return String.format(
                " USE INDEX (%s)",
                String.join(",", useIndexes.stream().map(index -> index.getSql(config)).toList())
        );
    }

    protected String preWhereSql(SqlAbleConfig config) {
        return String.format(
                " PREWHERE %s",
                preWheres.getSql(config.withSubQuery(true))
        );
    }

    protected String whereSql(SqlAbleConfig config) {
        return String.format(
                " WHERE %s",
                wheres.getSql(config.withSubQuery(true))
        );
    }

    protected String groupSql(SqlAbleConfig config) {
        config = config.withGroupByAlias(true);
        List<String> clauses = new ArrayList<>();
        Set<String> selectedAliases = new HashSet<>();
        for (Term s : selects)
            selectedAliases.add(s.getAlias());

        for (Term field : groupBys) {
            if (config.isGroupByAlias() && field.getAlias() != null && selectedAliases.contains(field.getAlias()))
                clauses.add(Utils.formatQuotes(field.getAlias(), config.getAliasQuoteChar() != null ? config.getAliasQuoteChar() : config.getQuoteChar()));
            else
                clauses.add(field.getSql(config));
        }

        String sql = " GROUP BY " + String.join(",", clauses);

        if (withTotals)
            return sql + " WITH TOTALS";

        return sql;
    }

    protected String orderBySql(SqlAbleConfig config) {
        List<String> clauses = new ArrayList<>();
        Set<String> selectedAliases = new HashSet<>();
        for (Term s : selects)
            selectedAliases.add(s.getAlias());

        for (OrderByEntry entry : orderBys) {
            String termSql = (
                    config.isOrderByAlias() && entry.field.getAlias() != null && selectedAliases.contains(entry.field.getAlias())
                    ? Utils.formatQuotes(entry.field.getAlias(), config.getAliasQuoteChar() != null ? config.getAliasQuoteChar() : config.getQuoteChar())
                    : entry.field.getSql(config)
            );

            clauses.add(
                    String.format("%s %s", termSql, entry.order.value)
            );
        }

        return " ORDER BY " + String.join(",", clauses);
    }

    protected String rollupSql(SqlAbleConfig ignored) {
        return " WITH ROLLUP";
    }

    protected String havingSql(SqlAbleConfig config) {
        return " HAVING " + havings.getSql(config);
    }

    protected String offsetSql(SqlAbleConfig config) {
        return String.format(" OFFSET %d", offset);
    }

    protected String limitSql(SqlAbleConfig config) {
        return String.format(" LIMIT %d", limit);
    }

    protected String setSql(SqlAbleConfig config) {
        return " SET " + String.join(",", updates.stream().map(entry -> (
                String.format("%s=%s", entry.field.getSql(config.withWithNamespace(false)), entry.value.getSql(config))
                )).toList());
    }

    public record OrderByEntry(Term field, Order order) {}
    public record UpdateEntry(Field field, SqlAble value) {}
}
