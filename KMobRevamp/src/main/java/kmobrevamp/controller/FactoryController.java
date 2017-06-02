package kmobrevamp.controller;

import java.util.List;

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

import kmobrevamp.model.User;
import kmobrevamp.service.UserService;

@Controller
public class FactoryController {

	@Autowired
	private UserService userService;
	
	
	@RequestMapping(value="/factory/home", method = RequestMethod.GET)
	public ModelAndView factoryhome(){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", "Welcome " + user.getName() + " (" + user.getEmail() + ")");
		modelAndView.setViewName("factory/home");
		return modelAndView;
	}
	
	@RequestMapping(value="/factory/registration", method=RequestMethod.GET)
	public ModelAndView registration()
	{
		ModelAndView modelAndView=new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", "Welcome " + user.getName() + " (" + user.getEmail() + ")");
		User newUser=new User();
		modelAndView.addObject("user", newUser);
		modelAndView.setViewName("factory/registration");
		return modelAndView;
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/factory/registration", method=RequestMethod.POST)
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
			modelAndView.addObject("userName", "Welcome " + user1.getName() + " (" + user1.getEmail() + ")");
			modelAndView.setViewName("factory/registration");
		}
		else{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user1 = userService.findUserByEmail(auth.getName());
			List<GrantedAuthority> list=(List<GrantedAuthority>) auth.getAuthorities();
			userService.saveServiceCenterUser(user);
			modelAndView.addObject("userName", "Welcome " + user1.getName() + " (" + user1.getEmail() + ")");
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("factory/registration");
		}
		
		return modelAndView;
	}
	
	
}
