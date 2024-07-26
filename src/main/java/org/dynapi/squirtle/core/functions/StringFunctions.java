package org.dynapi.squirtle.core.functions;

import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.functions.Function;

import java.util.concurrent.locks.Condition;

public class StringFunctions {
    public static class Ascii extends Function {
        public Ascii(String alias, Term term) {
            super(alias, "ASCII", term);
        }
    }

    public static class NullIf extends Function {
        public NullIf(String alias, Term term, Condition condition) {
            super(alias, "NULLIF", term, condition);
        }
    }

    public static class Bin extends Function {
        public Bin(String alias, Term term) {
            super(alias, "BIN", term);
        }
    }

    public static class Concat extends Function {
        public Concat(String alias, Term... terms) {
            super(alias, "CONCAT", terms);
        }
    }

    public static class Insert extends Function {
        public Insert(String alias, Term term, Integer start, Integer stop, Term subterm) {
            super(alias, "INSERT", term, start, start, subterm);
        }
    }

    public static class Length extends Function {
        public Length(String alias, Term term) {
            super(alias, "LENGTH", term);
        }
    }

    public static class Upper extends Function {
        public Upper(String alias, Term term) {
            super(alias, "UPPER", term);
        }
    }

    public static class Lower extends Function {
        public Lower(String alias, Term term) {
            super(alias, "LOWER", term);
        }
    }

    public static class Substring extends Function {
        public Substring(String alias, Term term, Integer start, Integer stop) {
            super(alias, "SUBSTRING", term, start, stop);
        }
    }

    public static class Reverse extends Function {
        public Reverse(String alias, Term term) {
            super(alias, "REVERSE", term);
        }
    }

    public static class Trim extends Function {
        public Trim(String alias, Term term) {
            super(alias, "TRIM", term);
        }
    }

    public static class SplitPart extends Function {
        public SplitPart(String alias, Term term, String delimiter, Integer index) {
            super(alias, "SPLIT_PART", term, delimiter, index);
        }
    }

    public static class RegexpMatches extends Function {
        public RegexpMatches(String alias, Term term, String pattern, String modifiers) {
            super(alias, "REGEXP_MATCHES", term, pattern, modifiers);
        }
    }

    public static class RegexpLike extends Function {
        public RegexpLike(String alias, Term term, String pattern, String modifiers) {
            super(alias, "REGEXP_LIKE", term, pattern, modifiers);
        }
    }

    public static class Replace extends Function {
        public Replace(String alias, Term term, String findString, String replaceWith) {
            super(alias, "REPLACE", term, findString, replaceWith);
        }
    }
}
