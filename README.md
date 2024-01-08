# Nalutbae JPA Criteria Query

이 라이브러리는 `org.springframework.data.jpa.domain.Specification`를 사용하여 JPA의 Repository로부터 조회 쿼리를 생성하기 위한 기준 파라미터를 자동으로 생성합니다.
Spring Framework에 의존성을 가지고 있으며 `spring-data-jpa`에서 사용할 수 있습니다.

이 라이브러리의 사용 예시는 다음과 같습니다.

## 필요 라이브러리 추가

JPA meta model을 생성하기 위해 `jpamodelgen` 라이브러리를 필요로 합니다.
아래는 gradle의 예시입니다.

```groovy
dependencies {
    annotationProcessor "org.hibernate.orm:hibernate-jpamodelgen:${hibernateVersion}"
}
```

## 프론트엔드에 노출할 Criteria 클래스 제작

```java
public class ExampleCriteria implements Criteria {
    private LongFilter id;
    private StringFilter stringField;
    private LocalDateTimeFilter dateField;
    private Boolean distinct;
    
    // ... Another {Filter} fieldName;

    // Constructor
    public ExampleCriteria(ExampleCriteria other) {
        this.id = (other.id == null ? null : other.id.copy());
        this.stringField = (other.stringField == null ? null : other.stringField.copy());
        this.dateField = (other.dateField == null ? null : other.dateField.copy());
        this.distinct = other.distinct;
        // Set another fields
    }
    
    // Getters and Setters
}
```

## Entity 조회를 위한 Service 제작

Spring의 `@Service` 어노테이션을 사용하는 예제입니다.

```java
@Service
@Transactional(readOnly = true)
public class ExampleQueryService extends QueryService<Example> {

    private final ExampleRepository exampleRepository;

    public ExampleQueryService(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    public List<ExampleDTO> findByCriteria(ExampleCriteria criteria) {
        final Specification<Example> specification = createSpecification(criteria);
        return exampleRepository.findAll(specification);
    }
    
    protected Specification<Example> createSpecification(ExampleCriteria criteria) {
        Specification<Example> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Example_.id));
            }
			if (criteria.getStringField() != null) {
				specification = specification.and(buildStringSpecification(criteria.getStringField(), Example_.stringField));
			}
            if (criteria.getDateField() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateField(), Example_.dateField));
            }
            // ... Add another field specifications
        }
        return specification;
    }
}
```

## 프론트엔드에서의 사용 예시 

Spring에서 Controller를 사용한다면 다음과 같이 ExampleCriteria를 파라미터로 받도록 구현합니다.

```java
@GetMapping("examples")
public ReturnModel getAll(ExampleCriteria criteria
) {
    List<Example> list = exampleQueryService.findByCriteria(criteria);
    // 반환 로직..
}
```

이 REST API를 호출하는 http의 예시는 다음과 같습니다. 
예시에서 사용된 표현식 외에도 여러 가지 표현식을 각 필드마다 사용할 수 있으며 and 조건으로 적용됩니다.

```
GET {host}/api/examples?id.greaterThanOrEqual=5&stringField.in=someValue&dateField.equals=2024-01-08T20:25:37
```
