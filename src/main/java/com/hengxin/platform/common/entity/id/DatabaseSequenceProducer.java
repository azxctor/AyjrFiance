package com.hengxin.platform.common.entity.id;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Database based sequence generator.
 * 
 * @author yeliangjin
 * 
 */

@Component("sequenceProducer")
public class DatabaseSequenceProducer implements SequenceProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSequenceProducer.class);

    @PersistenceContext(unitName = "default")
    protected EntityManager em;

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Override
	public Sequence produce(String name, String type) {
		return doProduce(name, type);
	}

    /**
     * Produces a sequence number via SP.
     */
    protected Sequence doProduce(String seqName, String type) {
        LOGGER.debug("generating sequence for: {}", seqName);
        StoredProcedureQuery query = em.createStoredProcedureQuery("PKG_UTIL.P_GEN_SEQ");
        query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        query.setParameter(1, seqName);
        query.registerStoredProcedureParameter(2, Long.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(3, Date.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
        query.setParameter(4, type);
        query.execute();
        Long seq = (Long) query.getOutputParameterValue(2);
        Date date = (Date) query.getOutputParameterValue(3);
        LOGGER.debug("generated sequence: {}, date: {}", seq, date);
        return new Sequence(seq, date);
    }

}
