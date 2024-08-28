package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.Term;

import java.util.ArrayList;
import java.util.List;

public class Not extends Criterion {
    protected Term term;

    public Not(Not original) {
        super(original);
        this.term = CloneUtils.copyConstructorClone(original.term);
    }

    public Not(Term term) {
        this.term = term;
    }

    public Not as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public List<Node> nodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(this);
        nodes.addAll(term.nodes());
        return nodes;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        config = config.withSubCriterion(true);
        String sql = String.format("NOT %s", term.getSql(config));
        return Utils.formatAliasSql(sql, alias, config);
    }

    // todo: __getattr__ magic

    @Override
    public Not replaceTable(Table currentTable, Table newTable) {
        term = term.replaceTable(currentTable, newTable);
        return this;
    }
}
