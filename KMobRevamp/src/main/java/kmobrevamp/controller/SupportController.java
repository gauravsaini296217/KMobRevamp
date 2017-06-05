package kmobrevamp.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import kmobrevamp.model.Complaint;
import kmobrevamp.model.ServiceCenter;
import kmobrevamp.model.SupportCenter;
import kmobrevamp.model.User;
import kmobrevamp.service.UserService;

@Controller
public class SupportController {

	@Autowired
	private UserService userService;
	
	
	@RequestMapping(value="/support/home", method = RequestMethod.GET)
	public ModelAndView factoryhome(){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		Complaint complaint=new Complaint();
		modelAndView.addObject("userName", "Welcome " + user.getName() + " (" + user.getEmail() + ")");
		modelAndView.addObject("complaint", complaint);
		modelAndView.setViewName("support/home");
		return modelAndView;
	}
		
}
