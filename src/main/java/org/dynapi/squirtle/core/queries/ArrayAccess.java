package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

import java.util.Objects;

public class ArrayAccess implements SqlAble {
    private final SqlAble array;
    private Integer index = null;
    private Integer[] sliced = null;

    public ArrayAccess(ArrayAccess original) {
        this.array = original.array;
        this.index = original.index;
        this.sliced = original.sliced;
    }

    public ArrayAccess(SqlAble array) {
        this.array = array;
    }

    /**
     * @param index index of the elements that should be accessed
     */
    public ArrayAccess atIndex(Integer index) {
        if (sliced != null)
            throw new IllegalArgumentException("Array-slices already set");
        this.index = index;
        return this;
    }

    /**
     * returns all elements of an array. useful in combination with {@link #subArrayAccess()} <br>
     * shorthand for {@link #sliced(Integer, Integer) sliced(null, null)}
     */
    public ArrayAccess all() {
        return sliced(null, null);
    }

    /**
     * set sliced array access
     * @param start starting index
     * @param end ending index
     */
    public ArrayAccess sliced(Integer start, Integer end) {
        if (index != null)
            throw new IllegalArgumentException("Array-index already set");
        this.sliced = new Integer[]{start, end};
        return this;
    }

    /**
     * returns all elements after {@code start} <br>
     * warning: don't use together with {@link #to(Integer)} for slicing. use {@link #sliced(Integer, Integer)} directly for that.
     * @param start index of the starting element
     */
    public ArrayAccess from(Integer start) {
        return sliced(start, null);
    }

    /**
     * returns all elements before {@code end} <br>
     * warning: don't use together with {@link #from(Integer)} for slicing. use {@link #sliced(Integer, Integer)} directly for that.
     * @param end end of the last element
     */
    public ArrayAccess to(Integer end) {
        return sliced(null, end);
    }

    /**
     * cleaner version to get elements of a nested array
     * <pre>{@code
     * Field field = table.field("name");
     * SqlAble array = new ArrayAccess(field)
     *      .all()
     *      .subArrayAccess()
     *      .sliced(2, 3);
     * array.getSql();  // array[:][2:3]
     * }</pre>
     * @return new {@link ArrayAccess} to multi-dimensional arrays
     */
    public ArrayAccess subArrayAccess() {
        return new ArrayAccess((SqlAble) this);
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String fieldSql = array.getSql(config);

        if (index == null && sliced == null)  // array
            return fieldSql;
        else if (index != null)  // array[0]
            return fieldSql + "[" + index + "]";
        else  // array[:] | array[0:] | array[:0] | array[0:0]
            return fieldSql + "[" + Objects.requireNonNullElse(sliced[0], "") + ":" + Objects.requireNonNullElse(sliced[1], "") + "]";
    }
}
