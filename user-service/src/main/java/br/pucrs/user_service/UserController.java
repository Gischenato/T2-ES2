package br.pucrs.user_service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.pucrs.user_service.models.AuthDTO;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser (@RequestBody User user) {
        try {
            userService.save(user);
            return ResponseEntity.ok().body("User registered!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/auth")
    public String authUser (@RequestBody AuthDTO user) {
        try {
            return userService.authUser(user);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
