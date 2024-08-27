package org.dynapi.squirtle.core.functions;

import org.dynapi.squirtle.core.interfaces.FunctionSqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.functions.AggregateFunction;

public class DistinctOptionFunction extends AggregateFunction implements FunctionSqlAble {
    protected boolean distinct;

    public DistinctOptionFunction(DistinctOptionFunction original) {
        super(original);
        this.distinct = original.distinct;
    }

    public DistinctOptionFunction(String name, Object... args) {
        super(name, args);
        this.distinct = false;
    }

    public DistinctOptionFunction as(String alias) {
        this.alias = alias;
        return this;
    }

    public String getFunctionSql(SqlAbleConfig config) {
        String s = super.getFunctionSql(config);

        int n = name.length() + 1;
        if (distinct) {
            return s.substring(0, n) + "DISTINCT " + s.substring(n);
        }
        return s;
    }

    public DistinctOptionFunction distinct() {
        return distinct(true);
    }

    public DistinctOptionFunction distinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }
}
