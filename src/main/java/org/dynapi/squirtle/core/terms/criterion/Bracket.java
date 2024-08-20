package org.dynapi.squirtle.core.terms.criterion;

import org.dynapi.squirtle.core.terms.Term;

public class Bracket extends Tuple {
    public Bracket(Bracket original) {
        super(original);
    }

    public Bracket(Term term) {
        super(term);
    }
}
