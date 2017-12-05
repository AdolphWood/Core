package com.adolph.handler;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import com.adolph.entity.User;
import com.adolph.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@RequestMapping("/user")
@Controller
public class UserHandler {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ResourceBundleMessageSource messageSource;

	/**
	 * 1. 关于如何避免表单的重复提交. 实际上最简单的方式是: 不使用转发来响应页面. 而使用重定向的方式来响应页面.
	 * 2. 若使用重定向的方式来响应页面, 则如何向页面传递 key-value 呢 ?
	 * 1). 把数据放入到 HttpSession 中. 到页面后再从 HttpSession 中删除. 
	 * 
	 * 3. SpringMVC 提供的处理方式: 
	 * 1). 在目标方法的参数列表中添加 RedirectAttributes 类型的参数
	 * 2). 在方法体中, 调用 RedirectAttributes 的 addFlashAttribute(key, val) 来加入键值对.
	 * 3). 重定向到目标页面.
	 * 注意: 不能直接重定向到物理的目标页面. 而是需要重定向到由 SpringMVC 路由的页面. 
	 * 不能达到目的: return "redirect:/index.jsp";
	 * 可以的: return "redirect:/index";
	 * <mvc:view-controller path="/index" view-name="index"/>
	 * 
	 * 4. 如何把消息加入到国际化资源文件中 ?
	 * 1). 在 SpringMVC 的配置文件中来配置国际化资源文件
	 * <bean id="messageSource" 
	 * 	class="org.springframework.context.support.ResourceBundleMessageSource">
	 * 		<property name="basename" value="i18n"></property>	
	 * </bean>
	 * 2). 在 Handler 中装配 ResourceBundleMessageSource 类型的参数
	 * 3). 在国际化资源文件中加入 key=val
	 * 4). 在 Handler 方法中调用 ResourceBundleMessageSource 的 getMessage(code, args, locale)
	 * 获取国际化资源文件中的消息. 
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "password", required = false) String password,
			HttpSession session,
			RedirectAttributes attributes, 
			Locale locale) {
		User user = userService.login(name, password);

		if (user != null) {
			session.setAttribute("user", user);
			return "success";
		}
		String code = "error.user.login";
		String [] args = null;
		String message = messageSource.getMessage(code, args, locale);
		attributes.addFlashAttribute("message", message);
		return "redirect:/index";
	}
}
