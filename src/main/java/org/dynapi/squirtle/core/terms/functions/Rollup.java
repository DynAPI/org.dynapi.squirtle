package org.dynapi.squirtle.core.terms.functions;

import org.dynapi.squirtle.core.terms.Term;

public class Rollup extends Function {
    public Rollup(Term... terms) {
        super(null, "ROLLUP", (Object[]) terms);
    }
}
