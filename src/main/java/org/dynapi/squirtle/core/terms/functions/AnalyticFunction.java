package org.dynapi.squirtle.core.terms.functions;

import org.dynapi.squirtle.core.enums.Order;
import org.dynapi.squirtle.core.interfaces.FunctionSqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.criterion.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnalyticFunction extends AggregateFunction implements FunctionSqlAble {
    @Override
    public Boolean isAggregate() { return false; }
    public Boolean isAnalytic() { return true; }

    protected List<Term> filters;
    protected List<Object> partition;
    protected List<OrderByEntry> orderBys;
    protected boolean includeFilter;
    protected boolean includeOver;

    public AnalyticFunction(String alias, String name, Object... args) {
        super(alias, name, args);
        this.filters = new ArrayList<>();
        this.partition = new ArrayList<>();
        this.orderBys = new ArrayList<>();
        this.includeFilter = false;
        this.includeOver = false;
    }

    public AnalyticFunction over(Object... terms) {
        includeOver = true;
        partition.addAll(List.of(terms));
        return this;
    }

    public AnalyticFunction orderBy(Field... fields) {
        return orderBy((OrderByEntry[]) Arrays.stream(fields).map(field -> new OrderByEntry(field, null)).toArray());
    }

    public AnalyticFunction orderBy(Field field, Order orient) {
        return orderBy(new OrderByEntry(field, orient));
    }

    public AnalyticFunction orderBy(OrderByEntry... orders) {
        includeFilter = true;
        orderBys.addAll(Arrays.asList(orders));
        return this;
    }

    protected String getOrderByField(Field field, Order orient, SqlAbleConfig config) {
        if (orient == null) return field.getSql(config);

        return String.format("%s %s", field.getSql(config), orient.value);
    }

    public String getPartitionSql(SqlAbleConfig config) {
        List<String> terms = new ArrayList<>();

        if (partition != null) {
            List<String> mappedPartition = partition
                    .stream()
                    .map(p -> (p instanceof SqlAble)
                        ? ((SqlAble) p).getSql(config)
                        : p.toString()
                    )
                    .toList();
            terms.add(String.format("PARTITION BY %s", String.join(",", mappedPartition)));
        }

        if (orderBys != null) {
            List<String> mappedOrderBy = orderBys
                    .stream()
                    .map(orderEntry -> getOrderByField(orderEntry.field, orderEntry.orient, config))
                    .toList();
            terms.add(String.format("ORDER BY %s", String.join(",", mappedOrderBy)));
        }

        return String.join(" ", terms);
    }

    @Override
    public String getFunctionSql(SqlAbleConfig config) {
        String sql = super.getFunctionSql(config);
        if (includeOver) {
            sql += String.format(" OVER(%s)", getPartitionSql(config));
        }
        return sql;
    }

    public record OrderByEntry(Field field, Order orient) {}
}
