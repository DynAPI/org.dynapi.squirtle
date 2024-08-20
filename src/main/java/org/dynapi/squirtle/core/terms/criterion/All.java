package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.Term;

import java.util.ArrayList;
import java.util.List;

public class All extends Criterion {
    protected final Term term;

    public All(All original) {
        super(original);
        this.term = CloneUtils.copyConstructorClone(original.term);
    }

    public All(String alias, Term term) {
        super(alias);
        this.term = term;
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
        String sql = String.format("%s ALL", term.getSql(config));
        return Utils.formatAliasSql(sql, alias, config);
    }
}
