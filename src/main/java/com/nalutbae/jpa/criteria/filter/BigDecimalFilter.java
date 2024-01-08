package com.nalutbae.jpa.criteria.filter;

import java.math.BigDecimal;

/**
 * {@link BigDecimal} 속성 필드에 사용할 수 있는 쿼리 필터
 *
 * @see RangeFilter
 */
public class BigDecimalFilter extends RangeFilter<BigDecimal> {

    private static final long serialVersionUID = 1L;

    public BigDecimalFilter() {
    }

    public BigDecimalFilter(BigDecimalFilter filter) {
        super(filter);
    }

    public BigDecimalFilter copy() {
        return new BigDecimalFilter(this);
    }
}
