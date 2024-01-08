package com.nalutbae.jpa.criteria.service;

import com.nalutbae.jpa.criteria.filter.*;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

/**
 * 복잡한 조회 쿼리를 구성하고 실행하기 위한 기본 서비스
 *
 * @param <ENTITY> 쿼리할 엔티티
 */
@Transactional(readOnly = true)
public abstract class QueryService<ENTITY> {

	/**
	 * 같음, null, non-null 조건이 지원되는 필드 조회 쿼리를 위한 {@link Specification}을 반환하는 함수
	 *
	 * @param filter 프론터엔드에서 요청한 개별 속성 필터
	 * @param field  필드를 나타내는 JPA 메타모델.
	 * @param <X> 필터링되는 속성의 유형
	 * @return 쿼리 검색 사양
	 */
	protected <X> Specification<ENTITY> buildSpecification(Filter<X> filter, SingularAttribute<? super ENTITY, X> field) {
		return buildSpecification(filter, root -> root.get(field));
	}

	/**
	 * 같음, null, non-null 조건이 지원되는 필드 조회 쿼리를 위한 {@link Specification}을 반환하는 함수
	 *
	 * @param filter 프론터엔드에서 요청한 개별 속성 필터
	 * @param metaclassFunction 현재 엔티티에서 필터가 적용되는 컬럼을 가져오는 함수(람다 표현식)
	 * @param <X> 필터링되는 속성의 유형
	 * @return 쿼리 검색 사양
	 */
	protected <X> Specification<ENTITY> buildSpecification(Filter<X> filter, Function<Root<ENTITY>, Expression<X>> metaclassFunction) {
		if (filter.getEquals() != null) {
			return equalsSpecification(metaclassFunction, filter.getEquals());
		} else if (filter.getIn() != null) {
			return valueIn(metaclassFunction, filter.getIn());
		} else if (filter.getNotIn() != null) {
			return valueNotIn(metaclassFunction, filter.getNotIn());
		} else if (filter.getNotEquals() != null) {
			return notEqualsSpecification(metaclassFunction, filter.getNotEquals());
		} else if (filter.getExists() != null) {
			return byFieldSpecified(metaclassFunction, filter.getExists());
		}
		return null;
	}

	/**
	 * 같음, 포함 및 null, non-null 조건이 지원되는 {@link String} 필드 조회 쿼리를 위한 {@link Specification}을 반환하는 함수
	 *
	 * @param filter 프론터엔드에서 요청한 개별 속성 필터
	 * @param field  필드를 나타내는 JPA 메타모델.
	 * @return 쿼리 검색 사양
	 */
	protected Specification<ENTITY> buildStringSpecification(StringFilter filter, SingularAttribute<? super ENTITY, String> field) {
		return buildSpecification(filter, root -> root.get(field));
	}

	/**
	 * 같음, 포함 및 null, non-null 조건이 지원되는 {@link String} 필드 조회 쿼리를 위한 {@link Specification}을 반환하는 함수
	 *
	 * @param filter 프론터엔드에서 요청한 개별 속성 필터
	 * @param metaclassFunction 현재 엔티티에서 필터가 적용되는 컬럼을 가져오는 함수(람다 표현식)
	 * @return 쿼리 검색 사양
	 */
	protected Specification<ENTITY> buildSpecification(StringFilter filter, Function<Root<ENTITY>, Expression<String>> metaclassFunction) {
		if (filter.getEquals() != null) {
			return equalsSpecification(metaclassFunction, filter.getEquals());
		} else if (filter.getIn() != null) {
			return valueIn(metaclassFunction, filter.getIn());
		} else if (filter.getNotIn() != null) {
			return valueNotIn(metaclassFunction, filter.getNotIn());
		} else if (filter.getContains() != null) {
			return likeUpperSpecification(metaclassFunction, filter.getContains());
		} else if (filter.getDoesNotContain() != null) {
			return doesNotContainSpecification(metaclassFunction, filter.getDoesNotContain());
		} else if (filter.getNotEquals() != null) {
			return notEqualsSpecification(metaclassFunction, filter.getNotEquals());
		} else if (filter.getExists() != null) {
			return byFieldSpecified(metaclassFunction, filter.getExists());
		}
		return null;
	}

	/**
	 * 단일 {@link Comparable} 유형에 대하여 필드 조회 쿼리를 위한 {@link Specification}을 반환하는 함수
	 * (같음, 미만, 초과 및 미만 또는 같음, 이상 또는 같음, null/non-null 조건이 지원됩니다.
	 *
	 * @param <X> 필터링되는 속성의 유형
	 * @param filter 프론터엔드에서 요청한 개별 속성 필터
	 * @param field  필드를 나타내는 JPA 메타모델.
	 * @return 쿼리 검색 사양
	 */
	protected <X extends Comparable<? super X>> Specification<ENTITY> buildRangeSpecification(RangeFilter<X> filter,
																							  SingularAttribute<? super ENTITY, X> field) {
		return buildSpecification(filter, root -> root.get(field));
	}

	/**
	 * 단일 {@link Comparable} 유형에 대하여 필드 조회 쿼리를 위한 {@link Specification}을 반환하는 함수
	 * (같음, 미만, 초과 및 미만 또는 같음, 이상 또는 같음, null/non-null 조건이 지원됩니다.
	 *
	 * @param <X> 필터링되는 속성의 유형
	 * @param filter 프론터엔드에서 요청한 개별 속성 필터
	 * @param metaclassFunction 현재 엔티티에서 필터가 적용되는 컬럼을 가져오는 함수(람다 표현식)
	 * @return 쿼리 검색 사양
	 */
	protected <X extends Comparable<? super X>> Specification<ENTITY> buildSpecification(RangeFilter<X> filter,
																						 Function<Root<ENTITY>, Expression<X>> metaclassFunction) {
		if (filter.getEquals() != null) {
			return equalsSpecification(metaclassFunction, filter.getEquals());
		} else if (filter.getIn() != null) {
			return valueIn(metaclassFunction, filter.getIn());
		}

		Specification<ENTITY> result = Specification.where(null);
		if (filter.getExists() != null) {
			result = result.and(byFieldSpecified(metaclassFunction, filter.getExists()));
		}
		if (filter.getNotEquals() != null) {
			result = result.and(notEqualsSpecification(metaclassFunction, filter.getNotEquals()));
		}
		if (filter.getNotIn() != null) {
			result = result.and(valueNotIn(metaclassFunction, filter.getNotIn()));
		}
		if (filter.getGreaterThan() != null) {
			result = result.and(greaterThan(metaclassFunction, filter.getGreaterThan()));
		}
		if (filter.getGreaterThanOrEqual() != null) {
			result = result.and(greaterThanOrEqualTo(metaclassFunction, filter.getGreaterThanOrEqual()));
		}
		if (filter.getLessThan() != null) {
			result = result.and(lessThan(metaclassFunction, filter.getLessThan()));
		}
		if (filter.getLessThanOrEqual() != null) {
			result = result.and(lessThanOrEqualTo(metaclassFunction, filter.getLessThanOrEqual()));
		}
		return result;
	}

	protected <X> Specification<ENTITY> equalsSpecification(Function<Root<ENTITY>, Expression<X>> metaclassFunction, X value) {
		return (root, query, builder) -> builder.equal(metaclassFunction.apply(root), value);
	}

	protected <X> Specification<ENTITY> notEqualsSpecification(Function<Root<ENTITY>, Expression<X>> metaclassFunction, X value) {
		return (root, query, builder) -> builder.not(builder.equal(metaclassFunction.apply(root), value));
	}

	protected Specification<ENTITY> likeUpperSpecification(Function<Root<ENTITY>, Expression<String>> metaclassFunction,
														   String value) {
		return (root, query, builder) -> builder.like(builder.upper(metaclassFunction.apply(root)), wrapLikeQuery(value));
	}

	protected Specification<ENTITY> doesNotContainSpecification(Function<Root<ENTITY>, Expression<String>> metaclassFunction,
																String value) {
		return (root, query, builder) -> builder.not(builder.like(builder.upper(metaclassFunction.apply(root)), wrapLikeQuery(value)));
	}

	protected <X> Specification<ENTITY> byFieldSpecified(Function<Root<ENTITY>, Expression<X>> metaclassFunction,
														 boolean specified) {
		return specified ?
				(root, query, builder) -> builder.isNotNull(metaclassFunction.apply(root)) :
				(root, query, builder) -> builder.isNull(metaclassFunction.apply(root));
	}

	protected <X> Specification<ENTITY> byFieldEmptiness(Function<Root<ENTITY>, Expression<Set<X>>> metaclassFunction,
														 boolean specified) {
		return specified ?
				(root, query, builder) -> builder.isNotEmpty(metaclassFunction.apply(root)) :
				(root, query, builder) -> builder.isEmpty(metaclassFunction.apply(root));
	}

	protected <X> Specification<ENTITY> valueIn(Function<Root<ENTITY>, Expression<X>> metaclassFunction,
												Collection<X> values) {
		return (root, query, builder) -> {
			In<X> in = builder.in(metaclassFunction.apply(root));
			for (X value : values) {
				in = in.value(value);
			}
			return in;
		};
	}

	protected <X> Specification<ENTITY> valueNotIn(Function<Root<ENTITY>, Expression<X>> metaclassFunction,
												   Collection<X> values) {
		return (root, query, builder) -> {
			In<X> in = builder.in(metaclassFunction.apply(root));
			for (X value : values) {
				in = in.value(value);
			}
			return builder.not(in);
		};
	}

	protected <X extends Comparable<? super X>> Specification<ENTITY> greaterThanOrEqualTo(Function<Root<ENTITY>, Expression<X>> metaclassFunction,
																						   X value) {
		return (root, query, builder) -> builder.greaterThanOrEqualTo(metaclassFunction.apply(root), value);
	}

	protected <X extends Comparable<? super X>> Specification<ENTITY> greaterThan(Function<Root<ENTITY>, Expression<X>> metaclassFunction,
																				  X value) {
		return (root, query, builder) -> builder.greaterThan(metaclassFunction.apply(root), value);
	}

	protected <X extends Comparable<? super X>> Specification<ENTITY> lessThanOrEqualTo(Function<Root<ENTITY>, Expression<X>> metaclassFunction,
																						X value) {
		return (root, query, builder) -> builder.lessThanOrEqualTo(metaclassFunction.apply(root), value);
	}

	protected <X extends Comparable<? super X>> Specification<ENTITY> lessThan(Function<Root<ENTITY>, Expression<X>> metaclassFunction,
																			   X value) {
		return (root, query, builder) -> builder.lessThan(metaclassFunction.apply(root), value);
	}

	protected String wrapLikeQuery(String txt) {
		return "%" + txt.toUpperCase() + '%';
	}

	protected Specification<ENTITY> distinct(boolean distinct) {
		return (root, query, cb) -> {
			query.distinct(distinct);
			return null;
		};
	}

}
