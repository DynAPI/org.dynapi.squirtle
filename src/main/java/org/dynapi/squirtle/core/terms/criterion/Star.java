package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Selectable;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Node;

import java.util.ArrayList;
import java.util.List;

public class Star extends Field {
    public Star() {
        super(null, "*", null);
    }

    public Star(String tableName) {
        super(null, "*", new Table(null, tableName));
    }

    public Star(Selectable table) {
        super(null, "*", table);
    }

    @Override
    public List<Node> nodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(this);
        if (table != null)
            nodes.addAll(table.nodes());
        return nodes;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        if (table != null && (config.isWithNamespace() || table.getAlias() != null)) {
            String namespace = table.getAlias() != null ? table.getAlias() : table.getTableName();
            return String.format("%s.*", Utils.formatQuotes(namespace, config.getQuoteChar()));
        }
        return "*";
    }
}
