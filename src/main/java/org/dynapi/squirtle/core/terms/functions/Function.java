package org.dynapi.squirtle.core.terms.functions;

import lombok.NonNull;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.FunctionSqlAble;
import org.dynapi.squirtle.core.interfaces.SpecialParamsSqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Schema;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.criterion.Criterion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Function extends Criterion implements SpecialParamsSqlAble, FunctionSqlAble {
    protected final String name;
    protected List<Node> args;
    protected final Schema schema;

    public Function(String alias, @NonNull String name, Object... args) {
        this(alias, name, null, args);
    }

    public Function(String alias, @NonNull String name, Schema schema, Object... args) {
        super(alias);
        this.name = name;
        this.args = Arrays.stream(args).map(arg -> wrapConstant(arg, null)).toList();
        this.schema = schema;
    }

    @Override
    public List<Node> nodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(this);
        for (Node arg : args) {
            nodes.addAll(arg.nodes());
        }
        return nodes;
    }

    public Boolean isAggregate() {
        return Utils.resolveIsAggregate(args.stream().map(Node::isAggregate).toList());
    }

    @Override
    public Function replaceTable(Table currentTable, Table newTable) {
        args = args
                .stream()
                // todo: not sure if this cast breaks something
                .map(arg -> (arg instanceof Term) ? ((Term) arg).replaceTable(currentTable, newTable) : arg)
                .toList();
        return this;
    }

    @Override
    public String getSpecialParamsSql(SqlAbleConfig config) {
        return null;
    }

    private static String getArgSql(Node arg, SqlAbleConfig config) {
        return arg instanceof SqlAble ? ((SqlAble) arg).getSql(config.withWithAlias(false)) : arg.toString();
    }

    @Override
    public String getFunctionSql(SqlAbleConfig config) {
        String specialParamsSql = getSpecialParamsSql(config);

        SqlAbleConfig getSqlConfig = config.withWithAlias(false).withSubQuery(true);

        List<String> mappedArgs = args
                .stream()
                .map(arg -> (arg instanceof SqlAble)
                        ? ((SqlAble) arg).getSql(getSqlConfig)
                        : getArgSql(arg, config)
                )
                .toList();
        String parametersStr = String.join(",", mappedArgs);
        String specialParamsStr = specialParamsSql != null ? " " + specialParamsSql : "";

        return String.format("%s(%s%s)", name, parametersStr, specialParamsStr);
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        SqlAbleConfig functionConfig = SqlAbleConfig.builder()
                .withNamespace(config.isWithNamespace())
                .quoteChar(config.getQuoteChar())
                .dialect(config.getDialect())
                .build();

        String functionSql = getFunctionSql(functionConfig);

        if (schema != null)
            functionSql += String.format("%s.%s", schema.getSql(config), functionSql);

        if (config.isWithAlias())
            return Utils.formatAliasSql(functionSql, alias, config);
        return functionSql;
    }
}
