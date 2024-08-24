package org.dynapi.squirtle.core.dialects.postgresql;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.*;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.criterion.*;
import org.dynapi.squirtle.core.terms.functions.Function;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;
import org.dynapi.squirtle.errors.QueryException;

import java.util.*;

public class PostgreSQLQueryBuilder extends QueryBuilder {
    public String sqlAbleAliasQuoteChar() { return "\""; }
    public Class<? extends Query> sqlAbleQueryClass() { return PostgreSQLQuery.class; }

    protected List<Term> returns = new ArrayList<>();
    protected boolean returnStar = false;

    protected boolean onConflict = false;
    protected List<Term> onConflictFields = new ArrayList<>();
    protected boolean onConflictDoNothing = false;
    protected List<OnConflictDoUpdateEntry> onConflictDoUpdates = new ArrayList<>();
    protected Criterion onConflictWheres = null;
    protected Criterion onConflictDoUpdatesWheres = null;

    protected List<Term> distinctOn = new ArrayList<>();

    protected boolean forUpdateNowait = false;
    protected boolean forUpdateSkipLocked = false;
    protected Set<String> forUpdateOf = new HashSet<>();

    public PostgreSQLQueryBuilder(PostgreSQLQueryBuilder original) {
        super(original);
        this.returns = CloneUtils.copyConstructorCloneCollection(original.returns);
        this.returnStar = original.returnStar;

        this.onConflict = original.onConflict;
        this.onConflictFields = CloneUtils.copyConstructorCloneCollection(original.onConflictFields);
        this.onConflictDoNothing = original.onConflictDoNothing;
        this.onConflictDoUpdates = new ArrayList<>(original.onConflictDoUpdates);
        this.onConflictWheres = CloneUtils.copyConstructorClone(original.onConflictWheres);
        this.onConflictDoUpdatesWheres = CloneUtils.copyConstructorClone(original.onConflictDoUpdatesWheres);

        this.distinctOn = CloneUtils.copyConstructorCloneCollection(original.distinctOn);

        this.forUpdateNowait = original.forUpdateNowait;
        this.forUpdateSkipLocked = original.forUpdateSkipLocked;
        this.forUpdateOf = new HashSet<>(original.forUpdateOf);
    }

    public PostgreSQLQueryBuilder(Config config) {
        super(config.toBuilder()
                .dialect(Dialects.POSTGRESQL)
                .build()
        );
    }

    public PostgreSQLQueryBuilder distinctOn(Term... fields) {
        this.distinctOn.addAll(List.of(fields));
        return this;
    }

    public PostgreSQLQueryBuilder onConflict(Term... targetFields) {
        if (insertTable == null)
            throw new RuntimeException("onConflict only applies to insert query");

        this.onConflict = true;
        onConflictFields.addAll(List.of(targetFields));
        return this;
    }

    public PostgreSQLQueryBuilder onConflictDoNothing() {
        if (!onConflictDoUpdates.isEmpty())
            throw new RuntimeException("Can not have two conflict handlers");
        this.onConflictDoNothing = true;
        return this;
    }

    public PostgreSQLQueryBuilder onUpdate(Field updateField, Object updateValue) {
        if (onConflictDoNothing)
            throw new QueryException("Can not have two conflict handlers");

        this.onConflictDoUpdates.add(new OnConflictDoUpdateEntry(updateField, updateValue == null ? null : new ValueWrapper(updateValue)));
        return this;
    }

    public PostgreSQLQueryBuilder where(Criterion criterion) {
        if (!onConflict)
            return (PostgreSQLQueryBuilder) super.where(criterion);

        if (criterion instanceof EmptyCriterion)
            return this;

        if (onConflictDoNothing)
            throw new QueryException("DO NOTHING does not support WHERE");

        if (!onConflictFields.isEmpty() && !onConflictDoUpdates.isEmpty()) {
            onConflictDoUpdatesWheres = (onConflictDoUpdatesWheres == null) ? criterion : onConflictDoUpdatesWheres.and(criterion);
        } else if (!onConflictFields.isEmpty()) {
            onConflictWheres = (onConflictWheres == null) ? criterion : onConflictWheres.and(criterion);
        } else
            throw new QueryException("Can not have fieldless ON CONFLICT WHERE");

        return this;
    }

    public PostgreSQLQueryBuilder using(Table table) {
        using.add(table);
        return this;
    }

    protected String distinctSql(SqlAbleConfig config) {
        if (!distinctOn.isEmpty())
            return String.format(
                    "DISTINCT ON(%s) ",
                    String.join(",", distinctOn.stream().map(term -> term.getSql(config)).toList())
            );

        return super.distinctSql(config);
    }

    protected Field conflictFieldStr(String term) {
        if (insertTable != null)
            return new Field(term, insertTable);
        return null;
    }

    protected String onConflictSql(SqlAbleConfig config) {
        if (!onConflictDoNothing && onConflictDoUpdates.isEmpty()) {
            if (onConflictFields.isEmpty()) return "";
            throw new QueryException("No handler defined for onConflict");
        }

        if (!onConflictDoUpdates.isEmpty() && onConflictFields.isEmpty())
            throw new QueryException("Can not have fieldless onConflictDoUpdates");

        String sql = " ON CONFLICT";
        if (!onConflictFields.isEmpty())
            sql += String.format(
                    " (%s)",
                    String.join(", ", onConflictFields.stream().map(field -> field.getSql(config.withWithAlias(true))).toList())
            );

        if (onConflictWheres != null)
            sql += String.format(" WHERE %s", onConflictWheres.getSql(config.withSubQuery(true)));

        return sql;
    }

    protected String forUpdateSql(SqlAbleConfig config) {
        String sql = "";

        if (forUpdate) {
            sql =" FOR UPDATE";
            if (forUpdateOf != null)
                sql += String.format(
                        " OF %s",
                        String.join(", ", forUpdateOf.stream().map(item -> new Table(item).getSql(config)).toList())
                );
            if (forUpdateNowait)
                sql += " NOWAIT";
            else if (forUpdateSkipLocked)
                sql += " SKIP LOCKED";
        }

        return sql;
    }

    protected String onConflictActionSql(SqlAbleConfig config) {
        if (onConflictDoNothing)
            return " DO NOTHING";
        else if (!onConflictDoUpdates.isEmpty()) {
            List<String> updates = new ArrayList<>();

            for (OnConflictDoUpdateEntry entry : onConflictDoUpdates) {
                if (entry.value != null) {
                    updates.add(String.format(
                            "%s=%s",
                            entry.field.getSql(config),
                            entry.value.getSql(config.withWithAlias(true))
                    ));
                } else {
                    String fieldSql = entry.field.getSql(config);
                    updates.add(String.format("%s=EXCLUDED.%s", fieldSql, fieldSql));
                }
            }

            String actionSql = String.format(" DO UPDATE SET %s", String.join(",", updates));

            if (onConflictDoUpdatesWheres != null)
                actionSql += String.format(
                        " WHERE %s",
                        onConflictDoUpdatesWheres.getSql(config.withSubQuery(true).withWithNamespace(true))
                );
            return actionSql;
        }

        return "";
    }

    public PostgreSQLQueryBuilder returning(Object... terms) {
        for (Object term : terms) {
            if (term instanceof Field field)
                returnField(field);
            else if (term instanceof String string)
                returnFieldString(string);
            else if (term instanceof Function function) {
                if (function.isAggregate())
                    throw new QueryException("Aggregate functions are not allowed in returning");
                returnOther(function);
            } else if (term instanceof ArithmeticExpression expression) {
                if (expression.isAggregate())
                    throw new QueryException("Aggregate expressions are not allowed in returning");
                returnOther(expression);
            } else
                returnOther(wrapConstant(term));
        }
        return this;
    }

    protected void validateReturningTerm(Term term) {
        for (Field field : term.getFields()) {
            if (insertTable == null && updateTable == null && deleteFrom == null)
                throw new QueryException("Returning can't be used in this query");

            boolean tableIsInsertOrUpdateTable = Objects.equals(field.getTable(), insertTable) || Objects.equals(field.getTable(), updateTable);
            Set<Table> joinTables = new HashSet<>();
            for (Join join : joins) {
                if (join instanceof JoinOn joinOn)
                    joinTables.addAll(joinOn.getCriterion().getTables());
                else
                    System.err.println("Bad Join " + join);
            }

            Set<Table> joinAndBaseTables = new HashSet<>();
            joinAndBaseTables.addAll(joinTables);
            joinAndBaseTables.addAll(joinTables);
            Set<Table> diff = new HashSet<>(term.getTables());
            diff.removeAll(joinAndBaseTables);
            boolean tableNotBaseOrJoin = !diff.isEmpty();
            if (!tableIsInsertOrUpdateTable && tableNotBaseOrJoin)
                throw new QueryException("You can't return from other tables");
        }
    }

    protected void setReturnsForStar() {
        // xxx: not sure with this as replacement for `hasattr(obj, 'table')`
        this.returns = returns.stream().filter(returning -> returning instanceof Field).toList();
        this.returnStar = true;
    }

    protected void returnField(Field field) {
        if (returnStar) return;

        validateReturningTerm(field);

        if (field instanceof Star)
            setReturnsForStar();

        returns.add(field);
    }

    protected void returnFieldString(String string) {
        if (string.equals("*")) {
            setReturnsForStar();
            returns.add(new Star());
            return;
        }

        if (insertTable != null)
            returnField(new Field(string, insertTable));
        else if (updateTable != null)
            returnField(new Field(string, updateTable));
        else if (deleteFrom != null)
            returnField(new Field(string, from.get(0)));
        else
            throw new QueryException("Returning can't be used in this query");
    }

    protected void returnOther(Term term) {
        validateReturningTerm(term);
        returns.add(term);
    }

    protected String returningSql(SqlAbleConfig config) {
        return String.format(
                " RETURNING %s",
                String.join(",", returns.stream().map(term -> term.getSql(config.withWithAlias(true))).toList())
        );
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        config = getSqlAbleConfigWithDefaults(config);

        String queryString = super.getSql(config);

        queryString += onConflictSql(config);
        queryString += onConflictActionSql(config);

        if (!returns.isEmpty())
            queryString += returningSql(config.withWithNamespace(updateTable != null && !from.isEmpty()));
        return queryString;
    }

    protected record OnConflictDoUpdateEntry(Field field, ValueWrapper value) {}
}
