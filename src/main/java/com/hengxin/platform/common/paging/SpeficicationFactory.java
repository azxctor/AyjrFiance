package com.hengxin.platform.common.paging;

import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

/**
 * Create a speficication for JPA search criteria.
 * @author Gregory
 *
 * @param <T>
 */
public final class SpeficicationFactory<T> {

	public Specification<T> create(final SearchCriteria criteria) {
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Set<Entry<String, SubCriteria>> set = criteria.getMap().entrySet();
				Predicate where = cb.conjunction();
				for (Entry<String, SubCriteria> entry : set) {
					String attribute = entry.getKey();
					if (attribute.indexOf(".") > 0) {
						/** multiple tables. **/
						String[] attrs = attribute.split("\\.");
						if (attrs.length > 2) {
							throw new RuntimeException("Not support yet");
						}
						Join<T, ?> root2 = root.join(attrs[0], JoinType.INNER);
						Path<String> exp = root2.get(attrs[1]);
						SubCriteria sc = entry.getValue();
						if (EOperator.EQ == sc.getOperator()) {
							where = cb.and(where, cb.equal(exp, sc.getValue()));
						} else if (EOperator.LIKE == sc.getOperator()){
							where = cb.and(where, cb.like(exp, sc.getValue().toString()));
						}
					} else {
						/** single table. **/
						SubCriteria sc = entry.getValue();
						Path<String> exp = root.get(attribute);
						if (EOperator.EQ == sc.getOperator()) {
							where = cb.and(where, cb.equal(exp, sc.getValue()));
						} else if (EOperator.LIKE == sc.getOperator()){
							where = cb.and(where, cb.like(exp, sc.getValue().toString()));
						}
					}
				}
				query.where(where);
				return null;
			}
		};
	}
	
}
