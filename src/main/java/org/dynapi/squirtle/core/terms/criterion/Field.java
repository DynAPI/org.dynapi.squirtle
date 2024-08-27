package org.dynapi.squirtle.core.terms.criterion;

import lombok.Getter;
import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Selectable;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.values.JSON;

import java.util.ArrayList;
import java.util.List;

public class Field extends JSON implements CriterionOperations, SqlAble {
    @Getter
    protected Selectable table;
    protected final String name;

    public Field(Field original) {
        super(original);
        this.table = CloneUtils.copyConstructorClone(original.table);
        this.name = original.name;
    }

    public Field(String name) {
        this(name, null);
    }

    public Field(String name, Selectable table) {
        this.name = name;
        this.table = table;
    }

    public Field as(String alias) {
        this.alias = alias;
        return this;
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
    public Field replaceTable(Table currentTable, Table newTable) {
        if (table == currentTable)
            table = newTable;
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String fieldSql = Utils.formatQuotes(name, config.getQuoteChar());

        if (table != null && (config.isWithNamespace() || table.getAlias() != null)) {
            fieldSql = String.format(
                    "%s.%s",
                    Utils.formatQuotes(table.getTableName(), config.getQuoteChar()),
                    fieldSql
            );
        }

        if (config.isWithAlias())
            return Utils.formatAliasSql(fieldSql, alias, config);
        return fieldSql;
    }
}
