package com.nalutbae.jpa.criteria.filter;

/**
 * {@link Float} 속성 필드에 사용할 수 있는 쿼리 필터.
 *
 * @see RangeFilter
 */
public class FloatFilter extends RangeFilter<Float> {

    private static final long serialVersionUID = 1L;

    public FloatFilter() {
    }

    public FloatFilter(FloatFilter filter) {
        super(filter);
    }

    public FloatFilter copy() {
        return new FloatFilter(this);
    }

}
