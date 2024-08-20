package org.dynapi.squirtle.core.functions;

import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.functions.Function;

import java.util.concurrent.locks.Condition;

public class StringFunctions {
    public static class Ascii extends Function {
        public Ascii(Ascii original) {
            super(original);
        }

        public Ascii(String alias, Term term) {
            super(alias, "ASCII", term);
        }
    }

    public static class NullIf extends Function {
        public NullIf(NullIf original) {
            super(original);
        }

        public NullIf(String alias, Term term, Condition condition) {
            super(alias, "NULLIF", term, condition);
        }
    }

    public static class Bin extends Function {
        public Bin(Bin original) {
            super(original);
        }

        public Bin(String alias, Term term) {
            super(alias, "BIN", term);
        }
    }

    public static class Concat extends Function {
        public Concat(Concat original) {
            super(original);
        }

        public Concat(String alias, Term... terms) {
            super(alias, "CONCAT", (Object[]) terms);
        }
    }

    public static class Insert extends Function {
        public Insert(Insert original) {
            super(original);
        }

        public Insert(String alias, Term term, Integer start, Integer stop, Term subterm) {
            super(alias, "INSERT", term, start, start, subterm);
        }
    }

    public static class Length extends Function {
        public Length(Length original) {
            super(original);
        }

        public Length(String alias, Term term) {
            super(alias, "LENGTH", term);
        }
    }

    public static class Upper extends Function {
        public Upper(Upper original) {
            super(original);
        }

        public Upper(String alias, Term term) {
            super(alias, "UPPER", term);
        }
    }

    public static class Lower extends Function {
        public Lower(Lower original) {
            super(original);
        }

        public Lower(String alias, Term term) {
            super(alias, "LOWER", term);
        }
    }

    public static class Substring extends Function {
        public Substring(Substring original) {
            super(original);
        }

        public Substring(String alias, Term term, Integer start, Integer stop) {
            super(alias, "SUBSTRING", term, start, stop);
        }
    }

    public static class Reverse extends Function {
        public Reverse(Reverse original) {
            super(original);
        }

        public Reverse(String alias, Term term) {
            super(alias, "REVERSE", term);
        }
    }

    public static class Trim extends Function {
        public Trim(Trim original) {
            super(original);
        }

        public Trim(String alias, Term term) {
            super(alias, "TRIM", term);
        }
    }

    public static class SplitPart extends Function {
        public SplitPart(SplitPart original) {
            super(original);
        }

        public SplitPart(String alias, Term term, String delimiter, Integer index) {
            super(alias, "SPLIT_PART", term, delimiter, index);
        }
    }

    public static class RegexpMatches extends Function {
        public RegexpMatches(RegexpMatches original) {
            super(original);
        }

        public RegexpMatches(String alias, Term term, String pattern, String modifiers) {
            super(alias, "REGEXP_MATCHES", term, pattern, modifiers);
        }
    }

    public static class RegexpLike extends Function {
        public RegexpLike(RegexpLike original) {
            super(original);
        }

        public RegexpLike(String alias, Term term, String pattern, String modifiers) {
            super(alias, "REGEXP_LIKE", term, pattern, modifiers);
        }
    }

    public static class Replace extends Function {
        public Replace(Replace original) {
            super(original);
        }

        public Replace(String alias, Term term, String findString, String replaceWith) {
            super(alias, "REPLACE", term, findString, replaceWith);
        }
    }
}
