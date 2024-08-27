package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.Term;

import java.util.ArrayList;
import java.util.List;

public class BitwiseAndCriterion extends Criterion {
    protected Term term;
    protected Node value;

    public BitwiseAndCriterion(BitwiseAndCriterion original) {
        super(original);
        this.term = CloneUtils.copyConstructorClone(original.term);
        this.value = CloneUtils.copyConstructorCloneNoFail(original.value);
    }

    public BitwiseAndCriterion(Term term, Node value) {
        this.term = term;
        this.value = value;
    }

    public BitwiseAndCriterion as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public List<Node> nodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(this);
        nodes.addAll(term.nodes());
        nodes.addAll(value.nodes());
        return nodes;
    }

    @Override
    public BitwiseAndCriterion replaceTable(Table currentTable, Table newTable) {
        term = term.replaceTable(currentTable, newTable);
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String sql = String.format(
                "(%s & %s)",
                term.getSql(config),
                value
        );
        return Utils.formatAliasSql(sql, alias, config);
    }
}
