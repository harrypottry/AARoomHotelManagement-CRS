package com.shangmei.filter;

import com.shangmei.util.Constant;
import com.shangmei.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class LoginFilter implements Filter {

	private Log logger = LogFactory.getLog(this.getClass());

	private String loginUri = "/pages/pms/staff/login.jsp";

	private String skey;

	/**
	 * 不过滤的文件类型
	 */
	private List<String> ignoreType;

	/**
	 * 不过滤的请求元素
	 */
	private List<String> ignoreElement;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String contextPath = request.getContextPath();
		// 获取请求URI
		String requestURI = request.getRequestURI();
		logger.info("request URI：" + requestURI);
		String uri = requestURI.substring(contextPath.length());
		if (ignoreElement.contains(uri) || (uri.startsWith("/houseKeeper") && uri.endsWith(".htm"))) {
			logger.info("Filter:request filter successful,continue.");
			chain.doFilter(req, res);
			return;
		}
		// 获取请求访问的类型
		String actionType = uri.substring(uri.lastIndexOf(".") + 1);
		// 将请求访问类型转为小写字母
		actionType = actionType.toLowerCase();
		if (ignoreType.contains(actionType)) {
			logger.info("Filter:request filter successful,continue.");
			chain.doFilter(req, res);
			return;
		}
		HttpSession session = request.getSession(false);

		// api key :
		String uid = req.getParameter("uid");
		String rstr = req.getParameter("rstr");
		String token = req.getParameter("token");
		if (!StringUtils.isBlank(uid) && !StringUtils.isBlank(token) && !StringUtils.isBlank(rstr)) {
			if (token.equals(MD5Util.MD5(uid + rstr + skey))) {
				logger.info("Filter: auth for api key successful,continue.");
				if (session == null) {
					session = ((HttpServletRequest) req).getSession();
				}
				Map<String, Object> uidMap = new HashMap<String, Object>();
				uidMap.put("name", req.getParameter("__uid"));
				uidMap.put("login_id", req.getParameter("__uid"));
				uidMap.put("id", req.getParameter("__uid"));
				uidMap.put("staffID", req.getParameter("__uid"));
				Enumeration enumeration = req.getParameterNames();
				while (enumeration.hasMoreElements()) {
					String parakey = (String) enumeration.nextElement();
					if (parakey.startsWith("__SESSION_")) {
						uidMap.put(parakey.substring(10), req.getParameter(parakey));
					}
				}
				session.setAttribute(Constant.Session.key, uidMap);
				chain.doFilter(req, res);
				return;
			}
		}

		if (session == null) {
			logger.info("Filter:Session is null,will Redirect to " + contextPath);
			forwardLoginURL(request, response, loginUri);
			return;
		}
		Object object = session.getAttribute(Constant.Session.key);
		if (object == null) {
			logger.info("Filter: Not logged in, Please log in again after the visit. Will Redirect to" + contextPath);
			forwardLoginURL(request, response, loginUri);
			return;
		}
		//1、账务日期是否一致/2、当前班次是否已经交班/3、当前班次是否在交班或夜审(判断是否是同一台机器,同一个人,这一点暂时取消)
		String filterResult=this.atimeClassFilter(request, response);
		if("needLogin".equals(filterResult)){//需要重新登录
			logger.info("Filter:账务日期/班次不一致,will Redirect to " + contextPath);
			forwardLoginURL(request, response, loginUri);
			return;
		}
		logger.info("Filter:Logged in filter successful,continue.");
		chain.doFilter(req, res);
		return;
	}
	
	/***
	 * 判断账务日期/班次是否合法
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	private String atimeClassFilter(ServletRequest req, ServletResponse res){
		//获得数据库中的账务日期
		return "success";
	}

	private void forwardLoginURL(HttpServletRequest request, HttpServletResponse response, String uri) throws IOException, ServletException {
		request.getRequestDispatcher(uri).include(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.ignoreType = this.getInitParameter(filterConfig, "ignoreType", true);
		this.ignoreElement = this.getInitParameter(filterConfig, "ignoreElement", false);
		skey = filterConfig.getInitParameter("skey");
	}

	@Override
	public void destroy() {

	}

	/**
	 * 按照初始化参数名获取初始化参数值，toLower表示是否要把参数值转换为小写
	 * 
	 * @param fConfig
	 * @param paramName
	 * @param toLower
	 *            是否要把参数值转换为小写
	 * @return
	 */
	private List<String> getInitParameter(FilterConfig fConfig, String paramName, boolean toLower) {
		List<String> list = new ArrayList<String>();
		String paramValue = fConfig.getInitParameter(paramName);
		if (paramValue != null && !paramValue.trim().equals("")) {
			if (toLower) {
				// 将初始化参数值转为小写字母
				paramValue = paramValue.toLowerCase();
			}
			String[] paramValues = paramValue.split(";");
			list = Arrays.asList(paramValues);
		}
		return list;
	}
}
