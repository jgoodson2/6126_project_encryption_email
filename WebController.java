package hello;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import hello.UserService.NonUniqueUsernameException;

@Controller
public class WebController extends WebMvcConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/allMessages").setViewName("allMessages");
	}

	@GetMapping("/")
	public String signinForm(final ModelMap model) {
		model.addAttribute("user", new User());
		return "signin";
	}

	@GetMapping("/register")
	public String registerForm(final ModelMap model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@GetMapping("/receivedMessages")
	public String viewReceivedMessages(final ModelMap model) {

		List<Message> messages = new ArrayList<>();
		User sender = new User("omega@ymail.com", "omg");
		messages.add(new Message(1, "HI", "odssdfosdf34234234", sender));
		messages.add(new Message(2, "HI there world", "odssdfosdasdsdf34234234", sender));
		messages.add(new Message(3, "Hello world", "odssdfossddsfdasdsdf34234234", sender));

		model.addAttribute("messages", messages);

		return "receivedMessages";
	}

	@GetMapping("/sentMessages")
	public String viewSentMessages(final ModelMap model) {

		List<Message> messages = new ArrayList<>();
		User sender = new User("omega@ymail.com", "omg");
		messages.add(new Message(1, "HI", "odssdfosdf34234234", sender));
		messages.add(new Message(2, "HI there world", "odssdfosdasdsdf34234234", sender));
		messages.add(new Message(3, "Hello world", "odssdfossddsfdasdsdf34234234", sender));

		model.addAttribute("messages", messages);

		return "sentMessages";
	}

	@PostMapping("/")
	public String checkPersonInfo(@Valid User user, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "form";
		}

		return "redirect:/messages";
	}

	@PostMapping("/register")
	public String registerUser(@Valid User user, BindingResult bindingResult) {

		// if (bindingResult.hasErrors()) {
		// return "register";
		// }

		try {
			userService.addUser(user);
		} catch (NonUniqueUsernameException e) {
			bindingResult.rejectValue("username", "username", e.getMessage());
			return "register";
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			return "register";
		}
		return "redirect:/receivedMessages";
	}
	
	public String redirectToReceived() {
		return "redirect:/receivedMessages";
	}

	// TEST URL FOR NEW USER
	@GetMapping("/checknewuser")
	public ModelAndView checkNewUser() {
		User user = userService.getUser("user");
		return new ModelAndView("checknewuser", "user", user);
	}
	
	@RequestMapping("/validateuser")
	public ModelAndView displayStuff() {
		String s = null;
		System.out.println("S: " + s);
		User user = userService.getUser("user");
		//System.out.println(user);
		
		//valid user
		s = userService.validateUser("user","$2a$10$j2bNPIpbbERHK/B.ZKSukOzMU1U7/pG/t1g9avfU3QEe5JPBbLvF6");
		
		//non-valid user
		//s = userService.validateUser("user","$2a$10$j2bNPIpbbERHK/B.ZKSukOzMU1U7/pG/t1g9avfU3QEe5");
		
		if(s == null) {s = "0";}
		System.out.println("S: " + s);
		ModelAndView MV = new ModelAndView("/sentMessages");
		if(s == "1") 
			{
			MV = new ModelAndView("/sentMessages");
			}
		else if(s == "0") 
			{
			User UnValuser = userService.getUser("");
			MV = new ModelAndView("signin","user",UnValuser);
			}
		
		return MV;
	}
	
	@RequestMapping("/hello")
	public ModelAndView sayHi() {
		User user = userService.getUser("user");
		return new ModelAndView("signin","user",user);
	}
	
	
	@RequestMapping("/12345")
	public String display2Stuff() {
		return "123";
	}

}