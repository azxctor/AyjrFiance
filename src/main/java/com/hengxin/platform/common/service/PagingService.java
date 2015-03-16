package com.hengxin.platform.common.service;

import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.hengxin.platform.common.paging.EOperator;
import com.hengxin.platform.common.paging.SearchCriteria;
import com.hengxin.platform.common.paging.SubCriteria;

public class PagingService<T> extends BaseService{
    private static final Logger LOGGER = LoggerFactory.getLogger(PagingService.class);
    @Deprecated
    protected Specification<T> createSpecification(final SearchCriteria criteria) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                    CriteriaQuery<?> query, CriteriaBuilder builder) {
                query.distinct(true);
                Set<Entry<String, SubCriteria>> set = criteria.getMap().entrySet();
                Predicate where = builder.conjunction();
                for (Entry<String, SubCriteria> entry : set) {
                    String attribute = entry.getKey();
                    if (attribute.indexOf(".") > 0) {
                        /** multiple tables. **/
                        String[] attrs = attribute.split("\\.");
                        /** create path expression. **/
                        Path<String> exp = null;
                        for (int i = 0; i < attrs.length - 1; i++) {
                            exp = root.get(attrs[i]);
                        }
                        exp = exp.get(attrs[attrs.length - 1]);
                        SubCriteria sc = entry.getValue();
                        if (EOperator.EQ == sc.getOperator()) {
                            where = builder.and(where, builder.equal(exp, sc.getValue()));
                        } else if (EOperator.LIKE == sc.getOperator()){
                            where = builder.and(where, builder.like(exp, sc.getValue().toString()));
                        }
                    } else {
                        /** single table. **/
                        SubCriteria sc = entry.getValue();
                        Path<Object> exp = root.get(attribute);
                        Class<? extends Object> javaType = exp.getJavaType();
                        if(javaType.isEnum()){
                            Field[] fields = javaType.getFields();
                            for(Field field:fields){
                                String name = field.getName();
                                if(name.equalsIgnoreCase(sc.getValue().toString())){
                                    try {
                                        Object object = field.get(exp);
                                        if (EOperator.EQ == sc.getOperator()) {
                                            where = builder.and(where, builder.equal(exp, object));
                                        }
                                    } catch (IllegalArgumentException e) {
                                        LOGGER.error("");
                                    } catch (IllegalAccessException e) {
                                        LOGGER.error("");
                                    }

                                }
                            }
                        }else {
                            if (EOperator.EQ == sc.getOperator()) {
                                where = builder.and(where, builder.equal(exp, sc.getValue()));
                            }
                        }

                        //                        if (EOperator.EQ == sc.getOperator()) {
                        //                            where = builder.and(where, builder.equal(exp, sc.getValue()));
                        //                        }
                        //                        else if (EOperator.LIKE == sc.getOperator()){
                        //                            where = builder.and(where, builder.like(exp, sc.getValue().toString()));
                        //                        }
                    }
                }
                query.where(where);
                return null;
            }
        };
    }

}
