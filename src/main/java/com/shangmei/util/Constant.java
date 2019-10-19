package com.shangmei.util;

/**
 * 
 * @author shangmei
 * 
 */
public class Constant {

	public interface Session {

		String key = "staff_session_key";
		
	}

	public interface Cookie {

		String shop_id_key = "shopID";
		
		String customer_id_key = "customer_id";
		
		String staff_id_key = "staffID";
		
		String staff_name_key = "staffName";

		String classset_id_key = "classsetID";

		int max_age = 60 * 60 * 24 * 30;

	}
}
