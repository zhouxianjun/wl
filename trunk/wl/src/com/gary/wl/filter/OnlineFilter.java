package com.gary.wl.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.AntPathMatcher;

import com.gary.framework.config.ApplicationContextHolder;
import com.gary.framework.result.ExecuteResult;
import com.gary.framework.result.Result;
import com.gary.wl.entity.User;
import com.gary.wl.error.MyErrorCode;
import com.gary.wl.service.UserService;

/**
 * Servlet Filter implementation class OnlineFilter
 */
@WebFilter(filterName = "online", urlPatterns = { "*.do" })
public class OnlineFilter implements Filter {
	private UserService userService;
	private AntPathMatcher antPathMatcher;
	private String[] nofilter;
    /**
     * Default constructor. 
     */
    public OnlineFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest req = (HttpServletRequest)request;
		String requestURI = req.getRequestURL().toString();
		boolean isFilter = true;
		for (String pattern : nofilter) {
			if(antPathMatcher.match(pattern, requestURI)){
				isFilter = false;
				continue;
			}
		}
		if(!isFilter){
			chain.doFilter(request, response);
		}else{
			if(userService == null){
				userService = ApplicationContextHolder.getBean("userService", UserService.class);
			}
			String userid = req.getParameter("_userid");
			User user = null;
			if(userid != null){
				user = userService.get(userid);
			}
			if(user == null || !user.getLogin().getOnline()){
				response.setContentType("application/json;charset=utf-8");
				ObjectMapper objectMapper = ApplicationContextHolder.getBean("objectMapper", ObjectMapper.class);
				Result result = new Result();
				result.setExecuteResult(new ExecuteResult(MyErrorCode.NO_LOGIN));
				result.setSuccess(false);
				objectMapper.writeValue(response.getOutputStream(), result);
			}else{
				chain.doFilter(request, response);
			}
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		antPathMatcher = new AntPathMatcher();
		nofilter = new String[]{
			"**/login.do*",
			"**/reg.do*",
			"**/getConfig.do*",
			"**/upload.do*",
			"**/now.do*",
			"**/onlines.do*"
		};
	}

}
