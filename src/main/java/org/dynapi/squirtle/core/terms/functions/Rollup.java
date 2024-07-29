package org.dynapi.squirtle.core.terms.functions;

import org.dynapi.squirtle.core.terms.Term;

import java.util.List;

public class Rollup extends Function {
    public Rollup(Term... terms) {
        super(null, "ROLLUP", (Object[]) terms);
    }

    public Rollup addArgs(Term... terms) {
        this.args.addAll(List.of(terms));
        return this;
    }
}
