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

import kmobrevamp.model.SupportCenter;
import kmobrevamp.model.User;
import kmobrevamp.service.UserService;

@Controller
public class ServiceController {

	@Autowired
	private UserService userService;
	
	
	@RequestMapping(value="/service/home", method = RequestMethod.GET)
	public ModelAndView factoryhome(){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", "Welcome " + user.getName() + " (" + user.getEmail() + ")");
		modelAndView.setViewName("service/home");
		return modelAndView;
	}
	
	@RequestMapping(value="/service/registration", method=RequestMethod.GET)
	public ModelAndView registration()
	{
		ModelAndView modelAndView=new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", "Welcome " + user.getName() + " (" + user.getEmail() + ")");
		Set<SupportCenter> supportList=userService.getServiceById(user.getLocation()).getSupportCenters();
		modelAndView.addObject("supports",supportList);
		User newUser=new User();
		modelAndView.addObject("user", newUser);
		modelAndView.setViewName("service/registration");
		return modelAndView;
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/service/registration", method=RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user,BindingResult result)
	{
		ModelAndView modelAndView=new ModelAndView();
		User userExists=userService.findUserByEmail(user.getEmail());
		if(userExists!=null)
		{
			
			result.rejectValue("email", "error.user","There is already a user registered with email provided");
		}
		
		if(result.hasErrors())
		{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user1 = userService.findUserByEmail(auth.getName());
			Set<SupportCenter> supportList=userService.getServiceById(user1.getLocation()).getSupportCenters();
			modelAndView.addObject("supports",supportList);
			modelAndView.addObject("userName", "Welcome " + user1.getName() + " (" + user1.getEmail() + ")");
			modelAndView.setViewName("service/registration");
		}
		else{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user1 = userService.findUserByEmail(auth.getName());
			List<GrantedAuthority> list=(List<GrantedAuthority>) auth.getAuthorities();
			userService.saveSupportCenterUser(user);
			Set<SupportCenter> supportList=userService.getServiceById(user1.getLocation()).getSupportCenters();
			modelAndView.addObject("supports",supportList);
			modelAndView.addObject("userName", "Welcome " + user1.getName() + " (" + user1.getEmail() + ")");
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("service/registration");
		}
		
		return modelAndView;
	}
	
	
}
