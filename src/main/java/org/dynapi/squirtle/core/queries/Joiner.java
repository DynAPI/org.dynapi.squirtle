package org.dynapi.squirtle.core.queries;

import lombok.NonNull;
import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.enums.JoinType;
import org.dynapi.squirtle.core.terms.criterion.BasicCriterion;
import org.dynapi.squirtle.core.terms.criterion.Criterion;
import org.dynapi.squirtle.core.terms.criterion.Field;

import java.util.List;

public class Joiner {
    protected final QueryBuilder query;
    protected Selectable item;
    protected JoinType how;
    protected String typeLabel;

    public Joiner(Joiner original) {
        this.query = CloneUtils.copyConstructorClone(original.query);
        this.item = CloneUtils.copyConstructorClone(original.item);
        this.how = original.how;
        this.typeLabel = original.typeLabel;
    }

    public Joiner(QueryBuilder query, Selectable item, JoinType how, String typeLabel) {
        this.query = query;
        this.item = item;
        this.how = how;
        this.typeLabel = typeLabel;
    }

    public QueryBuilder on(@NonNull Criterion criterion, String collate) {
        query.doJoin(new JoinOn(item, how, criterion, collate));
        return this.query;
    }

    public QueryBuilder onField(String... fields) {

        Criterion criterion = null;
        for (String fieldName : fields) {
            BasicCriterion constituent = (new Field(fieldName, query.from.get(0)).eq(new Field(fieldName, item)));
            criterion = criterion == null ? constituent : criterion.and(constituent);
        }

        query.doJoin(new JoinOn(item, how, criterion, null));
        return this.query;
    }

    public QueryBuilder using(Field... fields) {
        query.doJoin(new JoinUsing(item, how, List.of(fields)));
        return this.query;
    }

    public QueryBuilder cross() {
        query.doJoin(new Join(item, JoinType.CROSS));
        return this.query;
    }
}
