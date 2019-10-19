package com.shangmei.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author zhanggt
 * 
 */
@Target({ ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Deploy {

	public enum ReturnType {
		JSON, STRING, BINARY, WEB
	}

	public enum RequestMethod {
		GET, HEAD, POST, PUT, DELETE, OPTIONS, TRACE
	}

	/**
	 * 参数列表.如果被注解的方法有参数,需要使用该方法设置全部参数的名称,此名称将与接口请求的参数一一对应.<br>
	 * 如果是实体类型的参数,需要实现IServiceParam.<br>
	 * 例如:@Deploy( { "username", "password" })
	 */
	public String[] value() default {};

	/**
	 * 响应数据类型.默认为Json格式,可以根据实际情况改为String或Binary,当返回值用于页面显示时,应选择WEB类型.<br>
	 * 例如:@Deploy(value = { "username", "password" }, returnType =
	 * ReturnType.STRING)
	 */
	public ReturnType returnType() default ReturnType.JSON;

	/**
	 * 文件类型.仅在returnType = ReturnType.BINARY时有效.
	 */
	public String fileType() default "";

	/**
	 * 请求方法
	 */
	public RequestMethod[] method() default { RequestMethod.GET, RequestMethod.HEAD, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,
			RequestMethod.OPTIONS, RequestMethod.TRACE };

}
