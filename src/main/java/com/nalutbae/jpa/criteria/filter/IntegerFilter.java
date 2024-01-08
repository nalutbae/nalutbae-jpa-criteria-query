package com.nalutbae.jpa.criteria.filter;

/**
 * {@link Integer} 속성 필드에 사용할 수 있는 쿼리 필터.
 *
 * @see RangeFilter
 */
public class IntegerFilter extends RangeFilter<Integer> {

    private static final long serialVersionUID = 1L;

    public IntegerFilter() {
    }

    public IntegerFilter(IntegerFilter filter) {
        super(filter);
    }

    public IntegerFilter copy() {
        return new IntegerFilter(this);
    }

}
