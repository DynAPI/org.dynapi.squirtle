package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.errors.CaseException;

import java.util.ArrayList;
import java.util.List;

public class Case extends Criterion {
    protected List<Entry> cases;
    protected Term otherwise;

    public Case(Case original) {
        super(original);
        this.cases = new ArrayList<>(original.cases);
        this.otherwise = CloneUtils.copyConstructorClone(otherwise);
    }

    public Case() {
        cases = new ArrayList<>();
        otherwise = null;
    }

    public Case as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public List<Node> nodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(this);
        for (Entry entry : cases) {
            nodes.addAll(entry.criterion.nodes());
            nodes.addAll(entry.term.nodes());
        }
        return nodes;
    }

    @Override
    public Boolean isAggregate() {
        List<Boolean> values = new ArrayList<>();
        for (Entry entry : cases) {
            values.add(entry.criterion.isAggregate());
            values.add(entry.term.isAggregate());
        }
        if (otherwise != null)
            values.add(otherwise.isAggregate());
        return Utils.resolveIsAggregate(values);
    }

    @Override
    public Case replaceTable(Table currentTable, Table newTable) {
        cases = cases
                .stream()
                .map(cas -> new Entry(
                        cas.criterion.replaceTable(currentTable, newTable),
                        cas.term.replaceTable(currentTable, newTable)
                ))
                .toList();
        otherwise = otherwise.replaceTable(currentTable, newTable);
        return this;
    }

    public Case when(Criterion criterion, Term term) {
        cases.add(new Entry(criterion, term));
        return this;
    }

    public Case otherwise(Term term) {
        this.otherwise = term;
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        if (cases.isEmpty())
            throw new CaseException("At least one 'when' case is required for a CASE statement.");

        List<String> formattedCases = cases
                .stream()
                .map(cas -> String.format(
                        "WHEN %s THEN %s",
                        cas.criterion.getSql(config),
                        cas.term.getSql(config)
                ))
                .toList();
        String casesSql = String.join(" ", formattedCases);
        String otherwiseSql = otherwise != null ? String.format("ELSE %s", otherwise.getSql(config)) : "";

        String sql = String.format("CASE %s%s END", casesSql, otherwiseSql);

        if (config.isWithAlias())
            return Utils.formatAliasSql(sql, alias, config);
        return sql;
    }

    public record Entry(Criterion criterion, Term term) {}
}
