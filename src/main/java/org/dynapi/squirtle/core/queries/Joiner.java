package org.dynapi.squirtle.core.queries;

import lombok.NonNull;
import org.dynapi.squirtle.core.enums.JoinType;
import org.dynapi.squirtle.core.terms.criterion.Criterion;
import org.dynapi.squirtle.core.terms.criterion.Field;

public class Joiner {
    protected final QueryBuilder query;
    protected Selectable item;
    protected JoinType how;
    protected String typeLabel;

    public Joiner(QueryBuilder query, Selectable item, JoinType how, String typeLabel) {
        this.query = query;
        this.item = item;
        this.how = how;
        this.typeLabel = typeLabel;
    }

    public QueryBuilder on(@NonNull Criterion criterion, String collate) {
        return query.doJoin(new JoinOn(item, how, criterion, collate));
    }

    public QueryBuilder onField(String... fields) {

        Criterion criterion = null;
        for (String fieldName : fields) {
            boolean constituent = (new Field(null, fieldName, query.from.get(0)).equals(new Field(null, fieldName, item)));
            criterion = criterion == null ? constituent : (criterion & constituent);
        }

        return query.doJoin(new JoinOn(item, how, criterion));
    }

    public QueryBuilder using(Field... fields) {
        return query.doJoin(new JoinUsing(item, how, fields));
    }

    public QueryBuilder cross() {
        return query.doJoin(new Join(item, JoinType.CROSS));
    }
}
