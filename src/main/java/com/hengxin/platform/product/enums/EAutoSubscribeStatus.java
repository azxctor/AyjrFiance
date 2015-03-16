package com.hengxin.platform.product.enums;

public enum EAutoSubscribeStatus {

	   ALLOW("Y"), DOESNOT("N"), PUBLISH("P");

	    private String code;

	    EAutoSubscribeStatus(String code) {
	        this.code = code;
	    }

		/**
		 * @return the code
		 */
		public String getCode() {
			return code;
		}
	    
}
