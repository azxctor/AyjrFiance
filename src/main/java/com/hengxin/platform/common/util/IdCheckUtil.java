package com.hengxin.platform.common.util;

/**
 * Utility to check the validity of an numeric id.
 * 
 * @see http://en.wikipedia.org/wiki/Luhn_algorithm
 * 
 * @author yeliangjin
 * 
 */
public final class IdCheckUtil {

	private static final int[][] TABLE = { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
			{ 0, 2, 4, 6, 8, 1, 3, 5, 7, 9 } };

	private IdCheckUtil() {

	}

	private static int luhnMod10(String number) {
		int sum = 0;
		for (int i = number.length() - 1, odd = 1; i >= 0; i--) {
			sum += TABLE[odd][number.charAt(i) - '0'];
			odd = 1 - odd;
		}
		sum = sum % 10;
		return sum == 0 ? 0 : 10 - sum;
	}

	/**
	 * Calculate the check digit for the given id.
	 */
	public static char getCheckDigit(String id) {
		return (char) (luhnMod10(id) + '0');
	}

	/**
	 * Check whether given id is valid.
	 */
	public static boolean checkId(String id) {
		return getCheckDigit(id.substring(0, id.length() - 1)) == id.charAt(id
				.length() - 1);
	}
	
	public static void main(String[] args){
		
		String acctNo = "88002";
		
		System.out.println(acctNo+getCheckDigit(acctNo));
		
	}

}
