package web.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import web.daos.UserRepository;
import web.models.User;
import web.models.UserType;

@RestController
public class LoginController {

	@Autowired
	UserRepository uRepo;

	@Autowired
	PasswordEncoder pwEncd;

	@RequestMapping(value = { "login" }, method = RequestMethod.GET)
	public ModelAndView login(Principal principal, HttpServletResponse res) throws IOException {
		System.out.println("login get");
		ModelAndView mv = new ModelAndView();
		if (principal == null) {
			mv.setViewName("login");
		} else {
			res.sendRedirect("home");
			return null;
		}
		return mv;
	}
	
	@RequestMapping(value = "home", method = RequestMethod.GET)
	public ModelAndView home(Principal principal, HttpServletRequest req, HttpServletResponse res) {
		System.out.println("home");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("welcome");
//		String token = req.getHeader(jwtVariable.getHEADER());
//		System.out.println("Token: " + token);
//		if (token != null && tokenHelper.validateToken(token)) {
//			Claims cl = tokenHelper.getClaimsFromToken(token);
//			System.out.println(cl.getIssuedAt());
//			System.out.println(cl.getExpiration());
//			
//		}
		if (principal != null) {
			User us = uRepo.findByUserName(principal.getName());
			if (us != null) {
				mv.addObject("name", us.getName());
			}
//			Cookie[] cookies = req.getCookies();
//			if (cookies != null) {
//				for (Cookie ck : cookies) {
//					if (ck.getName().equals("JSESSIONID")) {
//						ck.setMaxAge(3 * 60 * 60);
//						res.addCookie(ck);
//						break;
//					}
//				}
//			}
			//res.setHeader("api-token", pwEncd.encode(us.getId()));
			
//			HttpSession ss = req.getSession();
//			Enumeration<String> es = ss.getAttributeNames();
//			while (es.hasMoreElements()) {
//				System.out.println(es.nextElement());
//			}			
//			System.out.println(ss.getAttribute("SPRING_SECURITY_CONTEXT"));
		} else {
			mv.addObject("name", "hmm");
		}
		return mv;
	}

//	@RequestMapping(value = "create", method = RequestMethod.GET)
//	public ModelAndView createPage(Principal principal, HttpServletResponse res) throws IOException {
//		ModelAndView mv = new ModelAndView();
//		if (principal == null) {
//			mv.setViewName("createUser");
//		} else {
//			res.sendRedirect("home");
//			return null;
//		}
//		return mv;
//	}

	@RequestMapping(value = "create-user", method = RequestMethod.POST)
	public String createUser(@RequestBody User user, Principal principal) {
		if  (principal != null ) {
			return "Hi " + principal.getName() + ", you have to log out first!";
		}
		System.out.println("create");
		if (user != null) {
			System.out.println(user.toString());
			if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getName()) || 
					StringUtils.isEmpty(user.getPassword())) {
				return "Invalid data!";
			}
			if (user.getUserName().length() < 5) {
				return "Username must have at least 5 characters!";
			}
			if (!checkValidUsername(user.getUserName())) {
				return "Invalid username!";
			}
			if (user.getPassword().length() < 5) {
				return "Password must have at least 5 characters!";
			}
			if (user.getName().length() < 5) {
				return "Name must have at least 5 characters!";
			}
			User tmp = new User();
			tmp.setUserName(user.getUserName());
			tmp.setName(user.getName());
			tmp.setPassword(pwEncd.encode(user.getPassword()));
			tmp.setType(UserType.USER);
			uRepo.insert(tmp);
			return tmp.toString();
		}
		return "";
	}
	
	private boolean checkValidUsername(String un) {
		Pattern p = Pattern.compile("[^a-zA-Z0-9]");
		Matcher m = p.matcher(un);
		if (m.find()) {
			System.out.println(m.group());
			return false;			
		}
		return true;
	}

	@RequestMapping(value = "denied", method = RequestMethod.GET)
	public ModelAndView denied() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("accessDenied");
		return mv;
	}

}
