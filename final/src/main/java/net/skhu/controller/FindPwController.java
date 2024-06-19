package net.skhu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class FindPwController {
	@GetMapping("/findPw")
	public String Home(Model model) {

		return "findPw";
	}

}
