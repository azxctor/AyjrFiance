/*
 * Project Name: kmfex-platform
 * File Name: DateTimeUtil.java
 * Class Name: DateTimeUtil
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.fund.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class Name: DateUtils Description: TODO
 * 
 * @author congzhou
 * 
 */

public class DateUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

	public static Date getDate(String dateStr, String format) {
		if (StringUtils.isBlank(dateStr)) {
			return null;
		}
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		try {
			return dateformat.parse(dateStr);
		} catch (ParseException e) {
			// 出生年月
			if (dateStr.matches("\\d{4}-\\d{2}")) {
				dateStr = dateStr + "-01";
				return getDate(dateStr, format);
			}
			e.printStackTrace();
		}
		return new Date();
	}
    
    public static Date formatYYYYMMDDHHmmss(Date yyyyMMddDate){
        String workDateStr = DateUtils.formatDate(yyyyMMddDate, "yyyy-MM-dd");
        String hhmmss = DateUtils.formatDate(new Date(), "HH:mm:ss");
        String lastPayTsStr = workDateStr+" "+hhmmss;
        return DateUtils.getDate(lastPayTsStr, "yyyy-MM-dd HH:mm:ss");
    }

	public static String formatDate(Date date, String format) {
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		return dateformat.format(date);
	}

	public static Date maxDate(Date a, Date b) {
		return a.after(b) ? a : b;
	}

	public static Long date2yyyyMMddLong(Date date) {
		return Long.valueOf(formatDate(date, "yyyyMMdd"));
	}

	public static boolean isGreaterAndEqualsyyyyMMdd(Date effectDate, Date workDate) {
		Long resvEffectDateLong = date2yyyyMMddLong(effectDate);
		Long workDateLong = date2yyyyMMddLong(workDate);
		return resvEffectDateLong.compareTo(workDateLong)>=0;
	}

	public static Date addHours(Date date, int hour) {
		return add(date, Calendar.HOUR_OF_DAY, hour);
	}

	public static Date add(Date date, int calendarField, int amount) {
		if (date == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}

	public static Date getStartDate(Date date) throws ParseException {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat startFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss:SSS");
		return startFormat.parse(dateformat.format(date) + " 00:00:00:000");
	}

	public static Date getEndDate(Date date) throws ParseException {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat startFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss:SSS");
		return startFormat.parse(dateformat.format(date) + " 23:59:59:999");
	}

	public static int betweenDays(Date startDate, Date endDate) {
		Calendar beginCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();

		beginCalendar.setTime(startDate);
		endCalendar.setTime(endDate);

		if (beginCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR)) {
			return endCalendar.get(Calendar.DAY_OF_YEAR)
					- beginCalendar.get(Calendar.DAY_OF_YEAR);
		} else {
			if (beginCalendar.getTimeInMillis() < endCalendar.getTimeInMillis()) {
				int days = beginCalendar.getActualMaximum(Calendar.DAY_OF_YEAR)
						- beginCalendar.get(Calendar.DAY_OF_YEAR)
						+ endCalendar.get(Calendar.DAY_OF_YEAR);
				for (int i = beginCalendar.get(Calendar.YEAR) + 1; i < endCalendar
						.get(Calendar.YEAR); i++) {
					Calendar c = Calendar.getInstance();
					c.set(Calendar.YEAR, i);
					days += c.getActualMaximum(Calendar.DAY_OF_YEAR);
				}
				return days;
			} else {
				int days = endCalendar.getActualMaximum(Calendar.DAY_OF_YEAR)
						- endCalendar.get(Calendar.DAY_OF_YEAR)
						+ beginCalendar.get(Calendar.DAY_OF_YEAR);
				for (int i = endCalendar.get(Calendar.YEAR) + 1; i < beginCalendar
						.get(Calendar.YEAR); i++) {
					Calendar c = Calendar.getInstance();
					c.set(Calendar.YEAR, i);
					days += c.getActualMaximum(Calendar.DAY_OF_YEAR);
				}
				return days;
			}
		}
	}

	public static Date getNearestHourDate(long time) {
		long second = time / 1000;
		Date date = new Date(second / 3600 * 3600 * 1000);
		if (second % 3600 >= 1800) {
			date = addHours(date, 1);
		}
		return date;
	}
	
	public static void main(String[] args){
		
		Date workDate = DateUtils.getDate("2014-07-17", "yyyy-MM-dd");
		
		Date date = DateUtils.add(workDate, Calendar.DATE, 15);
		
		System.out.println(DateUtils.formatDate(date, "yyyy-MM-dd"));
		
	}
	
	public static Date getFirstDateOfMonth(String yearMonth) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(yearMonth + "-01");
		} catch (ParseException e) {
			LOGGER.debug("[" + yearMonth + "] 不是一个有效的年月格式。有效的合法值日：2014-09");
			return null;
		}
	}
	
	public static Date getLastDateOfMonth(String yearMonth) {
		if (yearMonth == null) {
			return null;
		}
		
		if (yearMonth.split("-").length != 2) {
			LOGGER.debug("[" + yearMonth + "] 不是一个有效的年月格式。有效的合法值日：2014-09");
			return null;
		}
		
		try {
			int year = Integer.valueOf(yearMonth.split("-")[0]);
			int month = Integer.valueOf(yearMonth.split("-")[1]);
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month - 1, 1);
			int maxDay = calendar.getActualMaximum(Calendar.DATE);
			return new SimpleDateFormat("yyyy-MM-dd").parse(yearMonth + "-" + maxDay);
		} catch (NumberFormatException e) {
			LOGGER.debug("[" + yearMonth + "] 不是一个有效的年月格式。有效的合法值日：2014-09");
			return null;
		} catch (ParseException e) {
			LOGGER.debug("[" + yearMonth + "] 不是一个有效的年月格式。有效的合法值日：2014-09");
			return null;
		}
	}
}
