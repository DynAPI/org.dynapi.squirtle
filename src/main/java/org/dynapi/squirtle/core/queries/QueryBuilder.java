package org.dynapi.squirtle.core.queries;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.With;
import org.dynapi.squirtle.core.CloneUtils;
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
import java.util.stream.Stream;

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
    protected Set<Selectable> selectStarTables = new HashSet<>();
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

    public QueryBuilder(QueryBuilder original) {
        super(original);

        this.from = CloneUtils.copyConstructorCloneCollection(original.from);
        this.insertTable = CloneUtils.copyConstructorClone(original.insertTable);
        this.updateTable = CloneUtils.copyConstructorClone(original.updateTable);
        this.deleteFrom = original.deleteFrom;
        this.replace = original.replace;

        this.with = CloneUtils.copyConstructorCloneCollection(original.with);
        this.selects = CloneUtils.copyConstructorCloneCollection(original.selects);
        this.forceIndexes = CloneUtils.copyConstructorCloneCollection(original.forceIndexes);
        this.useIndexes = CloneUtils.copyConstructorCloneCollection(original.useIndexes);
        this.columns = CloneUtils.copyConstructorCloneCollection(original.columns);
        // todo: better nested clone
        this.values = new ArrayList<>(original.values.stream().map(CloneUtils::copyConstructorCloneCollection).toList());
        this.distinct = original.distinct;
        this.ignore = original.ignore;

        this.forUpdate = original.forUpdate;

        this.wheres = CloneUtils.copyConstructorClone(original.wheres);
        this.preWheres = CloneUtils.copyConstructorClone(original.preWheres);
        this.groupBys = CloneUtils.copyConstructorCloneCollection(original.groupBys);
        this.withTotals = original.withTotals;
        this.havings = CloneUtils.copyConstructorClone(original.havings);
        this.orderBys = CloneUtils.copyConstructorCloneCollection(original.orderBys);
        this.joins = CloneUtils.copyConstructorCloneCollection(original.joins);
        this.unions = CloneUtils.copyConstructorCloneCollection(original.unions);
        this.using = CloneUtils.copyConstructorCloneCollection(original.using);

        this.limit = original.limit;
        this.offset = original.offset;

        this.updates = CloneUtils.copyConstructorCloneCollection(original.updates);

        this.selectStar = original.selectStar;
        this.selectStarTables = CloneUtils.copyConstructorCloneCollection(original.selectStarTables);
        this.mySqlRollup = original.mySqlRollup;
        this.selectInto = original.selectInto;

        this.subqueryCount = original.subqueryCount;
        this.foreignTable = original.foreignTable;

        this.dialect = original.dialect;
        this.wrapSetOperationQueries = original.wrapSetOperationQueries;
        this.asKeyword = original.asKeyword;

        this.wrapperClass = original.wrapperClass;

        this.immutable = original.immutable;
    }

    public QueryBuilder() {
        this(Config.builder().build());
    }

    public QueryBuilder(Config config) {
        this.dialect = config.getDialect();
        this.asKeyword = config.getAsKeyword();
        this.wrapSetOperationQueries = config.getWrapSetOperationQueries();
        this.wrapperClass = config.getWrapperClass();
        this.immutable = config.getImmutable();
    }

    public QueryBuilder as(String alias) {
        this.alias = alias;
        return this;
    }

    // xxx: __copy__

    public QueryBuilder from(String tableName) {
        return from(new Table(tableName));
    }

    public QueryBuilder from(Selectable selectable) {
        this.from.add(selectable);

        if ((selectable instanceof QueryBuilder) || (selectable instanceof SetOperation) && selectable.getAlias() == null) {
            int subQueryCount = Math.max(
                    this.subqueryCount,
                    (selectable instanceof QueryBuilder queryBuilder) ? queryBuilder.subqueryCount : 0
            );
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
        return into(new Table(tableName));
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
        return update(new Table(tableName));
    }

    public QueryBuilder update(Table table) {
        if (updateTable != null || !selects.isEmpty() || deleteFrom)
            throw new RuntimeException("'update' method is no longer available");

        updateTable = table;
        return this;
    }

    public QueryBuilder columns(String... fieldNames) {
        return columns(
                Stream.of(fieldNames)
                        .map(fieldName -> new Field(fieldName, insertTable))
                        .toList()
        );
    }

    public QueryBuilder columns(SqlAble... terms) {
        return columns(Arrays.asList(terms));
    }

    public QueryBuilder columns(Collection<? extends SqlAble> terms) {
        if (insertTable != null)
            throw new RuntimeException("'columns' method is no longer available");

        columns.addAll(terms);
        return this;
    }

    public QueryBuilder insert(Object... terms) {
        return insert(Arrays.asList(terms));
    }

    public QueryBuilder insert(Collection<Object> terms) {
        applyTerm(terms);
        replace = false;
        return this;
    }

    public QueryBuilder forceIndex(String... indexNames) {
        return forceIndex(Stream.of(indexNames).map(Index::new).toList());
    }

    public QueryBuilder forceIndex(Index... indices) {
        return forceIndex(Arrays.asList(indices));
    }

    public QueryBuilder forceIndex(Collection<Index> indices) {
        forceIndexes.addAll(indices);
        return this;
    }

    public QueryBuilder useIndex(String... indexNames) {
        return useIndex(Stream.of(indexNames).map(Index::new).toList());
    }

    public QueryBuilder useIndex(Index... indices) {
        return useIndex(Arrays.asList(indices));
    }

    public QueryBuilder useIndex(Collection<Index> indices) {
        useIndexes.addAll(indices);
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

    public QueryBuilder groupBy(String... fieldNames) {
        Selectable table = from.get(0);
        return groupBy(Stream.of(fieldNames).map(name -> (Term) new Field(name, table)).toList());
    }

    public QueryBuilder groupBy(Integer... fieldIndices) {
        return groupBy(Stream.of(fieldIndices).map(Term::wrapConstant).toList());
    }

    public QueryBuilder groupBy(Term... terms) {
        return groupBy(Arrays.asList(terms));
    }

    public QueryBuilder groupBy(Collection<? extends Term> terms) {
        groupBys.addAll(terms);
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
            groupBys.addAll(Arrays.asList(terms));
        } else if (!groupBys.isEmpty() && groupBys.get(groupBys.size()-1) instanceof Rollup rollup) {
            rollup.addArgs(terms);
        } else {
            groupBys.add(new Rollup(terms));
        }
        return this;
    }

    public QueryBuilder orderBy(String... fieldNames) {
        return orderBy(fieldNames, null);
    }

    public QueryBuilder orderBy(Field... fields) {
        return orderBy(fields, null);
    }

    public QueryBuilder orderBy(Object... fields) {
        return orderBy(fields, null);
    }

    public QueryBuilder orderBy(Object[] fields, Order order) {
        return orderBy(Arrays.asList(fields), order);
    }

    public QueryBuilder orderBy(Collection<Object> fields, Order order) {
        return orderBy(
                fields
                        .stream()
                        .map(field -> new OrderByEntry(
                                field instanceof String fieldName
                                        ? new Field(fieldName, from.get(0))
                                        : wrapConstant(field),
                                order
                        ))
                        .toList()
        );
    }

    public QueryBuilder orderBy(OrderByEntry... orderByEntries) {
        return orderBy(Arrays.asList(orderByEntries));
    }

    public QueryBuilder orderBy(Collection<OrderByEntry> fields) {
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
        return new SetOperation(this, other, SetOperations.UNION, wrapperClass);
    }

    public SetOperation unionAll(QueryBuilder other) {
        return new SetOperation(this, other, SetOperations.UNION_ALL, wrapperClass);
    }

    public SetOperation intersect(QueryBuilder other) {
        return new SetOperation(this, other, SetOperations.INTERSECT, wrapperClass);
    }

    public SetOperation exceptOf(QueryBuilder other) {
        return new SetOperation(this, other, SetOperations.EXCEPT_OF, wrapperClass);
    }

    public SetOperation minus(QueryBuilder other) {
        return new SetOperation(this, other, SetOperations.MINUS, wrapperClass);
    }

    public QueryBuilder set(String fieldName, Objects value) {
        return set(new Field(fieldName, null), value);
    }
    public QueryBuilder set(Field field, Objects value) {
        updates.add(new UpdateEntry(field, Utils.newInstance(wrapperClass, new Object[]{ null, value})));
        return this;
    }

    protected List<String> listAliases(Collection<? extends Field> fieldSet, String quoteChar) {
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
            selectField(new Field(term, from.get(0)));
        }
    }

    protected void selectField(Field term) {
        if (selectStar) return;

        if (selectStarTables.contains(term.getTable())) return;

        if (term instanceof Star star) {
            this.selects = new ArrayList<>(selects.stream()
                            // todo: verify if field is only with table
                            .filter(select -> !(select instanceof Field field) || star.getTable() != field.getTable())
                            .toList());
            selectStarTables.add(star.getTable());
        }
        this.selects.add(term);
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

    protected void applyTerm(Collection<Object> values) {
        if (insertTable == null)
            throw new RuntimeException("this is currently not available");

        this.values.add(values.stream().map(value -> (value instanceof Term term) ? term : wrapConstant(value)).toList());
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

        if (deleteFrom != null) {
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

        if (!useIndexes.isEmpty())
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

        String aliasQuoteChar = config.getAliasQuoteChar();
        for (OrderByEntry entry : orderBys) {
            String termSql = (
                    config.isOrderByAlias() && entry.field.getAlias() != null && selectedAliases.contains(entry.field.getAlias())
                    ? Utils.formatQuotes(entry.field.getAlias(), aliasQuoteChar != null ? aliasQuoteChar : config.getQuoteChar())
                    : entry.field.getSql(config)
            );

            clauses.add(entry.order == null ? termSql : String.format("%s %s", termSql, entry.order.value));
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

    @With
    @Getter
    @ToString
    @Builder(toBuilder = true)
    public static class Config {
        @Builder.Default
        private final Dialects dialect = null;
        @Builder.Default
        private final Boolean wrapSetOperationQueries = true;
        @Builder.Default
        private final Class<? extends ValueWrapper> wrapperClass = ValueWrapper.class;
        @Builder.Default
        private final Boolean immutable = true;
        @Builder.Default
        private final Boolean asKeyword = false;
    }
}
