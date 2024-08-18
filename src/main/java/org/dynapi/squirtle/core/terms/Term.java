package org.dynapi.squirtle.core.terms;

import lombok.Getter;
import lombok.Setter;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.enums.ArithmeticOperation;
import org.dynapi.squirtle.core.enums.Enumerator;
import org.dynapi.squirtle.core.enums.Equality;
import org.dynapi.squirtle.core.enums.Matching;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.queries.Table;
import org.dynapi.squirtle.core.terms.criterion.*;
import org.dynapi.squirtle.core.functions.Arithmetic;
import org.dynapi.squirtle.core.terms.values.JSON;
import org.dynapi.squirtle.core.terms.values.NullValue;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

import java.util.*;

public class Term implements Node, SqlAble {
    @Override
    public Boolean isAggregate() {
        return false;
    }
    public Enumerator getArithmeticOperation() { return null; }

    @Getter
    @Setter
    protected String alias;

    public Term(String alias) {
        this.alias = alias;
    }

    public Term as(String alias) {
        this.alias = alias;
        return this;
    }

    public Set<Table> getTables() {
        return new HashSet<>(find(Table.class));
    }

    public Set<Field> getFields() {
        return new HashSet<>(find(Field.class));
    }

    public static Term wrapConstant(Object value) {
        return wrapConstant(value, null);
    }

    public static Term wrapConstant(Object value, Class<? extends ValueWrapper> wrapperClass) {
        switch (value) {
            case null -> {
                return new NullValue();
            }
            case Node node -> {
                return (Term) value;
            }
            // kinda confusing that List=>Array and array=>Tuple
            case List<?> list -> {  // List -> mutable -> Array
                return new Array(list);
            }
            case Collection<?> tuple -> {
                return new Array(tuple);
            }
//        value.getClass().isArray()  // marked
            case Object[] tuple -> {  // array -> immutable -> Tuple
                return new Tuple(tuple);
            }
            default -> {}
        }

        if (wrapperClass == null)
            wrapperClass = ValueWrapper.class;
        return Utils.newInstance(wrapperClass, new Object[]{ value }, new Class<?>[] { Object.class });
    }

    public static Node wrapJson(Object value) {
        return wrapJson(value, null);
    }

    public static Node wrapJson(Object value, Class<? extends ValueWrapper> wrapperClass) {
        switch (value) {
            case null -> {
                return new NullValue();
            }
            case Term term -> {
                return term;
            }
//            case QueryBuilder builder -> {
//                return builder;
//            }
            case Interval interval -> {
                return interval;
            }
            default -> {}
        }
        if (value instanceof String || value instanceof Integer || value instanceof Boolean)
            return Utils.newInstance(wrapperClass, new Object[]{ value });

        return new JSON(null, value);
    }

    /**
     * Replaces all occurrences of the specified table with the new table. Useful when reusing fields across queries.
     * The base implementation returns self because not all terms have a table property.
     *
     * @param currentTable The table to be replaced
     * @param newTable The table to replace with
     * @return this
     */
    public Term replaceTable(Table currentTable, Table newTable) {
        return this;
    }

    public NullCriterion isNull() {
        return new NullCriterion(null, this);
    }

    public Not notNull() {
        return isNull().negate();
    }

    public NotNullCriterion isNotNull() {
        return new NotNullCriterion(null, this);
    }

    public ArithmeticExpression add(Object other) {
        return new ArithmeticExpression(null, ArithmeticOperation.ADD, this, wrapConstant(other));
    }

    public ArithmeticExpression sub(Object other) {
        return new ArithmeticExpression(null, ArithmeticOperation.SUB, this, wrapConstant(other));
    }

    public ArithmeticExpression mul(Object other) {
        return new ArithmeticExpression(null, ArithmeticOperation.MUL, this, wrapConstant(other));
    }

    public ArithmeticExpression div(Object other) {
        return new ArithmeticExpression(null, ArithmeticOperation.DIV, this, wrapConstant(other));
    }

    public Arithmetic.Pow pow(float other) {
        return new Arithmetic.Pow(null, this, other);
    }

    public Arithmetic.Mod mod(float other) {
        return new Arithmetic.Mod(null, this, other);
    }

    public BasicCriterion eq(Object other) {
        return new BasicCriterion(null, Equality.EQ, this, wrapConstant(other));
    }

    public BasicCriterion ne(Object other) {
        return new BasicCriterion(null, Equality.NE, this, wrapConstant(other));
    }

    public BasicCriterion gt(Object other) {
        return new BasicCriterion(null, Equality.GT, this, wrapConstant(other));
    }

    public BasicCriterion gte(Object other) {
        return new BasicCriterion(null, Equality.GTE, this, wrapConstant(other));
    }

    public BasicCriterion lt(Object other) {
        return new BasicCriterion(null, Equality.LT, this, wrapConstant(other));
    }

    public BasicCriterion lte(Object other) {
        return new BasicCriterion(null, Equality.LTE, this, wrapConstant(other));
    }

    public BasicCriterion glob(String expr) {
        return new BasicCriterion(null, Matching.GLOB, this, wrapConstant(expr));
    }

    public BasicCriterion like(String expr) {
        return new BasicCriterion(null, Matching.LIKE, this, wrapConstant(expr));
    }

    public BasicCriterion not_like(String expr) {
        return new BasicCriterion(null, Matching.NOT_LIKE, this, wrapConstant(expr));
    }

    public BasicCriterion ilike(String expr) {
        return new BasicCriterion(null, Matching.ILIKE, this, wrapConstant(expr));
    }

    public BasicCriterion not_ilike(String expr) {
        return new BasicCriterion(null, Matching.NOT_ILIKE, this, wrapConstant(expr));
    }

    public BasicCriterion rlike(String expr) {
        return new BasicCriterion(null, Matching.RLIKE, this, wrapConstant(expr));
    }

    public BasicCriterion regex(String expr) {
        return new BasicCriterion(null, Matching.REGEX, this, wrapConstant(expr));
    }

    public BasicCriterion regexp(String expr) {
        return new BasicCriterion(null, Matching.REGEXP, this, wrapConstant(expr));
    }

    public BasicCriterion binRegex(String pattern) {
        return new BasicCriterion(null, Matching.BIN_REGEX, this, wrapConstant(pattern));
    }

    public BetweenCriterion between(Object lower, Object upper) {
        return new BetweenCriterion(null, this, wrapConstant(lower), wrapConstant(upper));
    }

    public PeriodCriterion fromTo(Object start, Objects end) {
        return new PeriodCriterion(null, this, wrapConstant(start), wrapConstant(end));
    }

    public BasicCriterion asOf(String expr) {
        return new BasicCriterion(null, Matching.AS_OF, this, wrapConstant(expr));
    }

    public All all() {
        return new All(null, this);
    }

    public ContainsCriterion isIn(List<?> args) {
        List<Term> wrapped = args.stream().map(Term::wrapConstant).toList();
        return new ContainsCriterion(null, this, new Tuple(wrapped));
    }

    public ContainsCriterion isIn(Term arg) {
        return new ContainsCriterion(null, this, arg);
    }

    public ContainsCriterion notIn(List<?> arg) {
        return isIn(arg).negated();
    }

    public Not negate() {
        return new Not(null, this);
    }

    public BitwiseAndCriterion bitwiseAnd(int value) {
        return new BitwiseAndCriterion(null, this, wrapConstant(value));
    }

    public ArithmeticExpression lshift(Object other) {
        return new ArithmeticExpression(null, ArithmeticOperation.LSHIFT, this, wrapConstant(other));
    }

    public ArithmeticExpression rshift(Object other) {
        return new ArithmeticExpression(null, ArithmeticOperation.RSHIFT, this, wrapConstant(other));
    }

    @Override
    public String toString() {
        SqlAbleConfig config = SqlAbleConfig.builder()
                .quoteChar("\"")
                .secondaryQuoteChar("'")
                .build();
        return getSql(config);
    }

    @Override
    public int hashCode() {
        SqlAbleConfig config = SqlAbleConfig.builder()
                .withAlias(true)
                .withNamespace(true)
                .build();
        return getSql(config).hashCode();
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        throw new RuntimeException("Not Implemented");
    }
}
