package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.enums.Order;
import org.dynapi.squirtle.core.enums.SetOperations;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.criterion.Field;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

import java.util.*;

public class SetOperation extends Term implements Selectable, SqlAble {
    protected final QueryBuilder baseQuery;
    protected final List<SetOperationEntry> setOperations;
    protected final List<OrderByEntry> orderBys;
    protected Integer limit;
    protected Integer offset;
    protected final Class<? extends ValueWrapper> wrapperClass;

    public SetOperation(SetOperation original) {
        super(original);
        this.baseQuery = CloneUtils.copyConstructorClone(original.baseQuery);
        this.setOperations = CloneUtils.copyConstructorCloneCollection(original.setOperations);
        this.orderBys = CloneUtils.copyConstructorCloneCollection(original.orderBys);
        this.limit = original.limit;
        this.offset = original.offset;
        this.wrapperClass = original.wrapperClass;
    }

    public SetOperation(String alias, QueryBuilder baseQuery, QueryBuilder setOperationQuery, SetOperations setOperations, Class<? extends ValueWrapper> wrapperClass) {
        super(alias);
        this.baseQuery = baseQuery;
        this.setOperations = new ArrayList<>();
        this.setOperations.add(new SetOperationEntry(setOperations, setOperationQuery));

        this.orderBys = new ArrayList<>();
        this.limit = null;
        this.offset = null;

        this.wrapperClass = wrapperClass;
    }

    public SetOperation orderBy(Field... fields) {
        return orderBy((OrderByEntry[]) Arrays.stream(fields).map(field -> new OrderByEntry(field, null)).toArray());
    }

    public SetOperation orderBy(OrderByEntry... fields) {
        orderBys.addAll(Arrays.asList(fields));
        return this;
    }

    public SetOperation limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public SetOperation offset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public SetOperation union(Selectable other) {
        this.setOperations.add(new SetOperationEntry(SetOperations.UNION, other));
        return this;
    }

    public SetOperation union_all(Selectable other) {
        this.setOperations.add(new SetOperationEntry(SetOperations.UNION, other));
        return this;
    }

    public SetOperation intersect(Selectable other) {
        this.setOperations.add(new SetOperationEntry(SetOperations.UNION, other));
        return this;
    }

    public SetOperation exceptOf(Selectable other) {
        this.setOperations.add(new SetOperationEntry(SetOperations.UNION, other));
        return this;
    }

    public SetOperation minus(Selectable other) {
        this.setOperations.add(new SetOperationEntry(SetOperations.UNION, other));
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        if (config.getDialect() == null)
            config = config.withDialect(baseQuery.getDialect());
        if (config.getQuoteChar() == null)
            config = config.withQuoteChar(baseQuery.sqlAbleQuoteChar());

        String querySql = baseQuery.getSql(config.withSubQuery(baseQuery.wrapSetOperationQueries));

        if (!orderBys.isEmpty())
            querySql += orderBySql(config);

        if (limit != null)
            querySql += limitSql(config);

        if (offset != null)
            querySql += offsetSql(config);

        if (config.isSubQuery())
            querySql = String.format("(%s)", querySql);

        if (config.isWithAlias())
            return Utils.formatAliasSql(querySql, alias != null ? alias : getTableName(), config);

        return querySql;
    }

    protected String orderBySql(SqlAbleConfig config) {
        List<String> clauses = new ArrayList<>();
        Set<String> selectedAliases = new HashSet<>();
        for (var select : baseQuery.getSelects())
            selectedAliases.add(select.getAlias());

        for (var orderBy : orderBys) {
            Field field = orderBy.field;
            Order orient = orderBy.orient;
            String termSql = selectedAliases.contains(field.getAlias())
                    ? Utils.formatQuotes(field.getAlias(), config.getQuoteChar())
                    : field.getSql(config);

            clauses.add(
                    (orient == null)
                    ? termSql
                    : String.format("%s %s", termSql, orient.value)
            );
        }

        return String.format(" ORDER BY %s", String.join(",", clauses));
    }

    protected String limitSql(SqlAbleConfig ignored) {
        return String.format(" LIMIT %d", limit);
    }

    protected String offsetSql(SqlAbleConfig ignored) {
        return String.format(" OFFSET %d", offset);
    }

    public record OrderByEntry(Field field, Order orient) {}
    public record SetOperationEntry(SetOperations setOperations, Selectable setOperationQuery) {}
}
