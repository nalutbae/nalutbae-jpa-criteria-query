package com.nalutbae.jpa.criteria.filter;

import java.util.UUID;

/**
 * {@link UUID} 속성 필드에 사용할 수 있는 쿼리 필터.
 *
 * @see Filter
 */
public class UUIDFilter extends Filter<UUID> {

    private static final long serialVersionUID = 1L;

    public UUIDFilter() {
    }

    public UUIDFilter(UUIDFilter filter) {
        super(filter);
    }

    public UUIDFilter copy() {
        return new UUIDFilter(this);
    }

}
