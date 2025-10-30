package SprSecu.demo.controller;

import SprSecu.demo.dto.SignupDTO;
import SprSecu.demo.service.SignupService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class SignupController {
    private final SignupService signupService;

    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    @PostMapping("/signup")
    public String signup(SignupDTO signupDTO) {
        signupService.signup(signupDTO);
        return "success";
    }
}