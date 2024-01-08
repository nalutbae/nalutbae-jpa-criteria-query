package com.nalutbae.jpa.criteria.filter;

/**
 * {@link Boolean} 속성 필드에 사용할 수 있는 쿼리 필터.
 * 본 필터를 이용하여 다음과 같은 쿼리를 사용할 수 있습니다.
 * <pre>
 *      fieldName.equals=true
 *      fieldName.notEquals=true
 *      fieldName.specified=true
 *      fieldName.specified=false
 *      fieldName.in=true,false
 *      fieldName.notIn=true,false
 * </pre>
 */
public class BooleanFilter extends Filter<Boolean> {

    private static final long serialVersionUID = 1L;

    public BooleanFilter() {
    }

    public BooleanFilter(BooleanFilter filter) {
        super(filter);
    }

    /** {@inheritDoc} */
    @Override
    public BooleanFilter copy() {
        return new BooleanFilter(this);
    }

}
