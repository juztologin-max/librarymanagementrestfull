package com.hms.api.login;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginLogoutController {
	private final JwtService jwtService;
	private final AuthenticationManager authManager;

	public LoginLogoutController(JwtService jwtService, AuthenticationManager authManager) {
		this.jwtService = jwtService;
		this.authManager = authManager;
	}

	@PostMapping("/login")
	String apiLoginHandler(@RequestBody LoginRequestData data) {
		authManager.authenticate(new UsernamePasswordAuthenticationToken(data.username(), data.password()));
		return jwtService.generateToken(data.username());

	}

	@GetMapping("/login/test")
	String loginTestHandler() {
		return "Success";
	}

}

record LoginRequestData(String username, String password) {
}
