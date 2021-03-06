package adrianliz.shared.infrastructure.hibernate;

import adrianliz.shared.domain.criteria.Criteria;
import adrianliz.shared.domain.criteria.Filter;
import adrianliz.shared.domain.criteria.FilterOperator;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import javax.persistence.criteria.*;

public final class HibernateCriteriaConverter<T> {

  private final CriteriaBuilder builder;
  private final HashMap<FilterOperator, BiFunction<Filter, Root<T>, Predicate>>
      predicateTransformers =
          new HashMap<>() {
            {
              put(
                  FilterOperator.EQUAL,
                  HibernateCriteriaConverter.this::equalsPredicateTransformer);
              put(
                  FilterOperator.NOT_EQUAL,
                  HibernateCriteriaConverter.this::notEqualsPredicateTransformer);
              put(
                  FilterOperator.GT,
                  HibernateCriteriaConverter.this::greaterThanPredicateTransformer);
              put(
                  FilterOperator.LT,
                  HibernateCriteriaConverter.this::lowerThanPredicateTransformer);
              put(
                  FilterOperator.CONTAINS,
                  HibernateCriteriaConverter.this::containsPredicateTransformer);
              put(
                  FilterOperator.NOT_CONTAINS,
                  HibernateCriteriaConverter.this::notContainsPredicateTransformer);
            }
          };

  public HibernateCriteriaConverter(final CriteriaBuilder builder) {
    this.builder = builder;
  }

  public CriteriaQuery<T> convert(final Criteria criteria, final Class<T> aggregateClass) {
    final CriteriaQuery<T> hibernateCriteria = builder.createQuery(aggregateClass);
    final Root<T> root = hibernateCriteria.from(aggregateClass);

    hibernateCriteria.where(formatPredicates(criteria.filters().filters(), root));

    if (criteria.order().hasOrder()) {
      final Path<Object> orderBy = root.get(criteria.order().orderBy().value());
      final Order order =
          criteria.order().orderType().isAsc() ? builder.asc(orderBy) : builder.desc(orderBy);

      hibernateCriteria.orderBy(order);
    }

    return hibernateCriteria;
  }

  private Predicate[] formatPredicates(final List<Filter> filters, final Root<T> root) {
    final List<Predicate> predicates =
        filters.stream().map(filter -> formatPredicate(filter, root)).toList();

    Predicate[] predicatesArray = new Predicate[predicates.size()];
    predicatesArray = predicates.toArray(predicatesArray);

    return predicatesArray;
  }

  private Predicate formatPredicate(final Filter filter, final Root<T> root) {
    final BiFunction<Filter, Root<T>, Predicate> transformer =
        predicateTransformers.get(filter.operator());

    return transformer.apply(filter, root);
  }

  private Predicate equalsPredicateTransformer(final Filter filter, final Root<T> root) {
    return builder.equal(root.get(filter.field().value()), filter.value().value());
  }

  private Predicate notEqualsPredicateTransformer(final Filter filter, final Root<T> root) {
    return builder.notEqual(root.get(filter.field().value()), filter.value().value());
  }

  private Predicate greaterThanPredicateTransformer(final Filter filter, final Root<T> root) {
    return builder.greaterThan(root.get(filter.field().value()), filter.value().value());
  }

  private Predicate lowerThanPredicateTransformer(final Filter filter, final Root<T> root) {
    return builder.lessThan(root.get(filter.field().value()), filter.value().value());
  }

  private Predicate containsPredicateTransformer(final Filter filter, final Root<T> root) {
    return builder.like(
        root.get(filter.field().value()), String.format("%%%s%%", filter.value().value()));
  }

  private Predicate notContainsPredicateTransformer(final Filter filter, final Root<T> root) {
    return builder.notLike(
        root.get(filter.field().value()), String.format("%%%s%%", filter.value().value()));
  }
}
