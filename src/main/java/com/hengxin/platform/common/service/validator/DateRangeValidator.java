package com.hengxin.platform.common.service.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateRangeValidator extends BaseValidator implements
		ConstraintValidator<DateRangeCheck, Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateRangeValidator.class);
	
	private String startDate;
    private String endDate;
    private String messageTemplete;
    
	@Override
	public void initialize(DateRangeCheck constraintAnnotation) {
        this.startDate = constraintAnnotation.startDate();
        this.endDate = constraintAnnotation.endDate();
        this.messageTemplete = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		LOGGER.info("DateRangeValidator isValid() invoked");
        try {
            final String startDate = BeanUtils.getProperty(object, this.startDate);
            final String endDate = BeanUtils.getProperty(object, this.endDate);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date sd = df.parse(startDate);
            Date ed = df.parse(endDate);
            if (sd.after(ed)) {
                bindNode(context, this.endDate, this.messageTemplete);
                return false;
            }
        } catch (NoSuchMethodException ex) {
        	LOGGER.error("NoSuchMethodException ex, please check below fields {}, {} ", this.startDate, this.endDate);
        } catch (ParseException ex) {
        	LOGGER.error("It cannot parse by dateformat yyyy-MM-dd, {}", ex);
        } catch (Exception ex) {
        	LOGGER.error("ex {}", ex);
        }
        return true;
	}

}
