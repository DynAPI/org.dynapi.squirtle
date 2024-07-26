package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.enums.Arithmetic;
import org.dynapi.squirtle.core.enums.Enumerator;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.Term;

import java.util.ArrayList;
import java.util.List;

public class ArithmeticExpression extends Term {
    protected static List<Arithmetic> ADD_ORDER = List.of(Arithmetic.ADD, Arithmetic.SUB);

    public final Arithmetic operator;
    protected Term left;
    protected Term right;

    public ArithmeticExpression(String alias, Arithmetic operator, Term left, Term right) {
        super(alias);
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public List<Node> nodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(this);
        nodes.addAll(left.nodes());
        nodes.addAll(right.nodes());
        return nodes;
    }

    @Override
    public Boolean isAggregate() {
        return Utils.resolveIsAggregate(left.isAggregate(), right.isAggregate());
    }

    @Override
    public Arithmetic getOperator() {
        return operator;
    }

    @Override
    public Term replaceTable(Table currentTable, Table newTable) {
        left = left.replaceTable(currentTable, newTable);
        right = right.replaceTable(currentTable, newTable);
        return this;
    }

    protected boolean leftNeedsParens(Arithmetic currentOperator, Enumerator leftOperator) {
        if (leftOperator == null)
            return false;
        if (ADD_ORDER.contains(currentOperator))
            return false;
        return leftOperator instanceof Arithmetic && ADD_ORDER.contains((Arithmetic) leftOperator);
    }

    protected boolean rightNeedsParens(Arithmetic currentOperator, Enumerator rightOperator) {
        if (rightOperator == null)
            return false;
        if (currentOperator == Arithmetic.ADD)
            return false;
        if (currentOperator == Arithmetic.DIV)
            return true;
        return rightOperator instanceof Arithmetic && ADD_ORDER.contains((Arithmetic) rightOperator);
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        Enumerator leftOperator = left.getOperator();
        Enumerator rightOperator = right.getOperator();

        String leftSql = String.format(
                leftNeedsParens(operator, leftOperator) ? "(%s)" : "%s",
                left.getSql(config)
        );
        String rightSql = String.format(
                rightNeedsParens(operator, rightOperator) ? "(%s)" : "%s",
                right.getSql(config)
        );
        String sql = String.format("%s%s%s", leftSql, operator, rightSql);

        if (config.isWithAlias())
            return Utils.formatAliasSql(sql, alias, config);

        return sql;
    }
}
