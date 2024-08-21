package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.Term;

import java.util.List;

public class Tuple extends Criterion {
    protected List<Term> values;

    public Tuple(Tuple original) {
        super(original);
        this.values = CloneUtils.copyConstructorCloneCollection(original.values);
    }

    public Tuple(List<?> values) {
        this.values = values.stream().map(Tuple::wrapConstant).toList();
    }

    public Tuple(Object... values) {
        this(List.of(values));
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String argList = String.join(",", values.stream().map(value -> value.getSql(config)).toList());
        String sql = String.format("(%s)", argList);
        return Utils.formatAliasSql(sql, alias, config);
    }

    @Override
    public Boolean isAggregate() {
        return Utils.resolveIsAggregate(values.stream().map(Node::isAggregate).toList());
    }

    @Override
    public Tuple replaceTable(Table currentTable, Table newTable) {
        values = values.stream().map(value -> value.replaceTable(currentTable, newTable)).toList();
        return this;
    }
}
