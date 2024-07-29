package org.dynapi.squirtle.core.queries;

import lombok.Getter;
import org.dynapi.squirtle.core.enums.JoinType;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.criterion.Field;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class JoinOn extends Join {
    @Getter
    protected Term criterion;
    protected final String collate;

    public JoinOn(Selectable item, JoinType how, Term criteria, String collate) {
        super(item, how);
        this.criterion = criteria;
        this.collate = collate;
    }

    @Override
    public void validate(Collection<Selectable> from, Collection<Join> joins) {
        Set<Selectable> criterionTables = new HashSet<>(criterion.getFields().stream().map(Field::getTable).toList());
        Set<Selectable> availableTables = new HashSet<>();
        availableTables.addAll(from);
        availableTables.addAll(joins.stream().map(join -> join.item).toList());
        availableTables.add(this.item);
        Set<Selectable> missingTables = new HashSet<>(criterionTables);
        missingTables.removeAll(availableTables);
        if (!missingTables.isEmpty())
            throw new RuntimeException();
    }

    @Override
    public JoinOn replaceTable(Table currentTable, Table newTable) {
        item = (currentTable.equals(item)) ? newTable : item;
        criterion = criterion.replaceTable(currentTable, newTable);
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String joinSql = super.getSql(config);
        return String.format(
                "%s ON %s%s",
                joinSql,
                criterion.getSql(config.withSubQuery(true)),
                collate != null ? String.format(" COLLATE %s", collate) : ""
        );
    }
}
