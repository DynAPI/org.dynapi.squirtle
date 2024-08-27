package org.dynapi.squirtle.core.functions;

import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.functions.Function;

import java.util.concurrent.locks.Condition;

public class NullFunctions {
    public static class IsNull extends Function {
        public IsNull(IsNull original) {
            super(original);
        }

        public IsNull(Term term) {
            super("ISNULL", term);
        }

        public IsNull as(String alias) {
            this.alias = alias;
            return this;
        }
    }

    public static class Coalesce extends Function {
        public Coalesce(Coalesce original) {
            super(original);
        }

        public Coalesce(Term term, Object... defaultValues) {
            super("COALESCE", term, defaultValues);
        }

        public Coalesce as(String alias) {
            this.alias = alias;
            return this;
        }
    }

    public static class IfNull extends Function {
        public IfNull(IfNull original) {
            super(original);
        }

        public IfNull(Condition condition, Term term) {
            super("IFNULL", condition, term);
        }
    }

    public static class NVL extends Function {
        public NVL(NVL original) {
            super(original);
        }

        public NVL(Condition condition, Term term) {
            super("NVL", condition, term);
        }

        public NVL as(String alias) {
            this.alias = alias;
            return this;
        }
    }
}
