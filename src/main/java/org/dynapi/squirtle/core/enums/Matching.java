package org.dynapi.squirtle.core.enums;

public class Matching extends Comparator {
    public final static Matching NOT_LIKE = new Matching(" NOT LIKE ");
    public final static Matching LIKE = new Matching(" LIKE ");
    public final static Matching NOT_ILIKE = new Matching(" NOT ILIKE ");
    public final static Matching ILIKE = new Matching(" ILIKE ");
    public final static Matching RLIKE = new Matching(" RLIKE ");
    public final static Matching REGEX = new Matching(" REGEX ");
    public final static Matching REGEXP = new Matching(" REGEXP ");
    public final static Matching BIN_REGEX = new Matching(" REGEX BINRARY ");
    public final static Matching AS_OF = new Matching(" AS OF ");
    public final static Matching GLOB = new Matching(" GLOB ");

    public Matching(String value) {
        super(value);
    }
}
