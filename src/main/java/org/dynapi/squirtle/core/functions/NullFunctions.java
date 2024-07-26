package org.dynapi.squirtle.core.functions;

import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.functions.Function;

import java.util.concurrent.locks.Condition;

public class NullFunctions {
    public static class IsNull extends Function {
        public IsNull(String alias, Term term) {
            super(alias, "ISNULL", term);
        }
    }

    public static class Coalesce extends Function {
        public Coalesce(String alias, Term term, Object... defaultValues) {
            super(alias, "COALESCE", term, defaultValues);
        }
    }

    public static class IfNull extends Function {
        public IfNull(String alias, Condition condition, Term term) {
            super(alias, "IFNULL", condition, term);
        }
    }

    public static class NVL extends Function {
        public NVL(String alias, Condition condition, Term term) {
            super(alias, "NVL", condition, term);
        }
    }
}
