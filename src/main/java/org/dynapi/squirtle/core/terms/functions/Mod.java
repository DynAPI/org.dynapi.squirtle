package org.dynapi.squirtle.core.terms.functions;

import org.dynapi.squirtle.core.terms.Term;

public class Mod extends Function {
    public Mod(String alias, Term term, float modulus) {
        super(alias, "MOD", term, modulus);
    }
}
