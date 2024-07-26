package org.dynapi.squirtle.core.terms.functions;

import org.dynapi.squirtle.core.terms.Term;

public class Pow extends Function {
    public Pow(String alias, Term term, float exponent) {
        super(alias, "POW", term, exponent);
    }
}
