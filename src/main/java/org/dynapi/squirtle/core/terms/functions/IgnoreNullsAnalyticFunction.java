package org.dynapi.squirtle.core.terms.functions;

import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

public class IgnoreNullsAnalyticFunction extends AnalyticFunction {
    protected boolean ignoreNulls;

    public IgnoreNullsAnalyticFunction(IgnoreNullsAnalyticFunction original) {
        super(original);
        this.ignoreNulls = original.ignoreNulls;
    }

    public IgnoreNullsAnalyticFunction(String name, Object... values) {
        super(name, values);
        this.ignoreNulls = false;
    }

    public IgnoreNullsAnalyticFunction ignoreNulls() {
        return ignoreNulls(true);
    }
    public IgnoreNullsAnalyticFunction ignoreNulls(boolean ignoreNulls) {
        this.ignoreNulls = ignoreNulls;
        return this;
    }

    @Override
    public String getSpecialParamsSql(SqlAbleConfig config) {
        return ignoreNulls ? "IGNORE NULLS" : null;
    }
}
