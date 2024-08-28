package org.dynapi.squirtle.core.functions;

import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.functions.Function;

import java.util.concurrent.locks.Condition;

public class StringFunctions {
    public static class Ascii extends Function {
        public Ascii(Ascii original) {
            super(original);
        }

        public Ascii(Term term) {
            super("ASCII", term);
        }

        public Ascii as(String alias) {
            this.alias = alias;
            return this;
        }
    }

    public static class NullIf extends Function {
        public NullIf(NullIf original) {
            super(original);
        }

        public NullIf(Term term, Condition condition) {
            super("NULLIF", term, condition);
        }

        public NullIf as(String alias) {
            this.alias = alias;
            return this;
        }
    }

    public static class Bin extends Function {
        public Bin(Bin original) {
            super(original);
        }

        public Bin(Term term) {
            super("BIN", term);
        }

        public Bin as(String alias) {
            this.alias = alias;
            return this;
        }
    }

    public static class Concat extends Function {
        public Concat(Concat original) {
            super(original);
        }

        public Concat(Term... terms) {
            super("CONCAT", (Object[]) terms);
        }

        public Concat as(String alias) {
            this.alias = alias;
            return this;
        }
    }

    public static class Insert extends Function {
        public Insert(Insert original) {
            super(original);
        }

        public Insert(Term term, Integer start, Integer stop, Term subterm) {
            super("INSERT", term, start, start, subterm);
        }

        public Insert as(String alias) {
            this.alias = alias;
            return this;
        }
    }

    public static class Length extends Function {
        public Length(Length original) {
            super(original);
        }

        public Length(Term term) {
            super("LENGTH", term);
        }

        public Length as(String alias) {
            this.alias = alias;
            return this;
        }
    }

    public static class Upper extends Function {
        public Upper(Upper original) {
            super(original);
        }

        public Upper(Term term) {
            super("UPPER", term);
        }

        public Upper as(String alias) {
            this.alias = alias;
            return this;
        }
    }

    public static class Lower extends Function {
        public Lower(Lower original) {
            super(original);
        }

        public Lower(Term term) {
            super("LOWER", term);
        }

        public Lower as(String alias) {
            this.alias = alias;
            return this;
        }
    }

    public static class Substring extends Function {
        public Substring(Substring original) {
            super(original);
        }

        public Substring(Term term, Integer start, Integer stop) {
            super("SUBSTRING", term, start, stop);
        }

        public Substring as(String alias) {
            this.alias = alias;
            return this;
        }
    }

    public static class Reverse extends Function {
        public Reverse(Reverse original) {
            super(original);
        }

        public Reverse(Term term) {
            super("REVERSE", term);
        }

        public Reverse as(String alias) {
            this.alias = alias;
            return this;
        }
    }

    public static class Trim extends Function {
        public Trim(Trim original) {
            super(original);
        }

        public Trim(Term term) {
            super("TRIM", term);
        }

        public Trim as(String alias) {
            this.alias = alias;
            return this;
        }
    }

    public static class SplitPart extends Function {
        public SplitPart(SplitPart original) {
            super(original);
        }

        public SplitPart(Term term, String delimiter, Integer index) {
            super("SPLIT_PART", term, delimiter, index);
        }

        public SplitPart as(String alias) {
            this.alias = alias;
            return this;
        }
    }

    public static class RegexpMatches extends Function {
        public RegexpMatches(RegexpMatches original) {
            super(original);
        }

        public RegexpMatches(Term term, String pattern, String modifiers) {
            super("REGEXP_MATCHES", term, pattern, modifiers);
        }

        public RegexpMatches as(String alias) {
            this.alias = alias;
            return this;
        }
    }

    public static class RegexpLike extends Function {
        public RegexpLike(RegexpLike original) {
            super(original);
        }

        public RegexpLike(Term term, String pattern, String modifiers) {
            super("REGEXP_LIKE", term, pattern, modifiers);
        }

        public RegexpLike as(String alias) {
            this.alias = alias;
            return this;
        }
    }

    public static class Replace extends Function {
        public Replace(Replace original) {
            super(original);
        }

        public Replace(Term term, String findString, String replaceWith) {
            super("REPLACE", term, findString, replaceWith);
        }

        public Replace as(String alias) {
            this.alias = alias;
            return this;
        }
    }
}
