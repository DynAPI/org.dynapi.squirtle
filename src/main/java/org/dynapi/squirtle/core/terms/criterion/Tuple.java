package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.Term;

import java.util.Arrays;

public class Tuple extends Criterion {
    protected Term[] values;

    public Tuple(Object... values) {
        super(null);
        this.values = (Term[]) Arrays.stream(values).map(Tuple::wrapConstant).toArray();
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String argList = String.join(",", Arrays.stream(values).map(value -> value.getSql(config)).toList());
        String sql = String.format("(%s)", argList);
        return Utils.formatAliasSql(sql, alias, config);
    }

    @Override
    public Boolean isAggregate() {
        return Utils.resolveIsAggregate(Arrays.stream(values).map(Node::isAggregate).toList());
    }

    @Override
    public Tuple replaceTable(Table currentTable, Table newTable) {
        values = (Term[]) Arrays.stream(values).map(value -> value.replaceTable(currentTable, newTable)).toArray();
        return this;
    }
}
