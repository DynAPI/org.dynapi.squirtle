package org.dynapi.squirtle.core.terms.functions;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.interfaces.FunctionSqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.criterion.Criterion;
import org.dynapi.squirtle.core.terms.criterion.CriterionOperations;

import java.util.ArrayList;
import java.util.List;

public class AggregateFunction extends Function implements FunctionSqlAble {
    @Override
    public Boolean isAggregate() {
        return true;
    }

    private boolean include_filter;
    private final List<Term> filters;

    public AggregateFunction(AggregateFunction original) {
        super(original);
        this.include_filter = original.include_filter;
        this.filters = CloneUtils.copyConstructorCloneCollection(original.filters);
    }

    public AggregateFunction(String alias, String name, Object... args) {
        super(alias, name, args);
        this.filters = new ArrayList<>();
        this.include_filter = false;
    }

    public AggregateFunction filter(Term... filters) {
        this.include_filter = true;
        this.filters.addAll(List.of(filters));
        return this;
    }

    protected String getFilterSql(SqlAbleConfig config) {
        if (include_filter) {
            return String.format("WHERE %s", CriterionOperations.all(filters).getSql(config));
        }
        return null;
    }

    @Override
    public String getFunctionSql(SqlAbleConfig config) {
        String sql = super.getFunctionSql(config);
        String filterSql = getFilterSql(config);
        if (filterSql != null) {
            sql += String.format(" FILTER(%s)", filterSql);
        }
        return sql;
    }
}
