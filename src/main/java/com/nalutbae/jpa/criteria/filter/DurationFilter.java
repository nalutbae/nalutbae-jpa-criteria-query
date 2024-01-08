package com.nalutbae.jpa.criteria.filter;

import java.time.Duration;

/**
 * {@link Duration} 속성 필드에 사용할 수 있는 쿼리 필터.
 *
 * @see Filter
 */
public class DurationFilter extends RangeFilter<Duration> {

    private static final long serialVersionUID = 1L;

    public DurationFilter() {
    }

    public DurationFilter(DurationFilter filter) {
        super(filter);
    }

    public DurationFilter copy() {
        return new DurationFilter(this);
    }

}
