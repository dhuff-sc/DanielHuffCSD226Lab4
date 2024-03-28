package csd226.lab2.controllers;

import csd226.lab2.data.Account;
import csd226.lab2.repositories.AccountRepository;
import csd226.lab2.security.JwtTokenUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import csd226.lab2.data.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;


    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtTokenUtil jwtUtil;

    @PostMapping("/test_form")
    public String test_form(@ModelAttribute Account account, Model model) {
        model.addAttribute("email", account);
        return "result";
    }
    @PostMapping(path="/auth/login")
    public ResponseEntity<?> login(@ModelAttribute Account acc, Model model) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            acc.getEmail(), acc.getPassword())
            );


            Account account = (Account) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(account);

            AuthResponse response = new AuthResponse(account.getEmail(), accessToken);

            return ResponseEntity.ok().body(response + "<script>alert('setting var \\\"accessToken\\\"');var accessToken='" + accessToken + "';</script>");
        } catch( Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("/login")
    public ResponseEntity<String> getLogin(){ // map a URL to a method
        String s=

                "<form hx-post=\"/auth/login\" hx-target=\"this\" hx-swap=\"outerHTML\">" +
                "    <div>" +
                "        <label>Username</label>" +
                "        <input type=\"text\" name=\"email\" value=\"fred.carella@gmail.com\">" +
                "    </div>" +
                "    <div class=\"form-group\">" +
                "        <label>Password</label>" +
                "        <input type=\"password\" name=\"password\" value=\"fredspassword\">" +
                "    </div>" +
                "    <button class=\"btn\">Submit</button>" +
                "    <button class=\"btn\" hx-get=\"/login\">Cancel</button>" +
                "</form>";
        return ResponseEntity.ok(s);
    }
    @GetMapping("/signin")
    public ResponseEntity<String> getSignin(){ // map a URL to a method
        String s="<form hx-post=\"/signup\" hx-target=\"this\" hx-swap=\"outerHTML\">" +
                "    <div>" +
                "        <label>First Name</label>" +
                "        <input type=\"text\" name=\"firstname\" value=\"Fred\">" +
                "    </div>" +
                "    <div class=\"form-group\">" +
                "        <label>Last Name</label>" +
                "        <input type=\"text\" name=\"lastname\" value=\"Carella\">" +
                "    </div>" +
                "    <div class=\"form-group\">" +
                "        <label>Email Address</label>" +
                "        <input type=\"email\" name=\"email\" value=\"fred.carella@gmail.com\">" +
                "    </div>" +
                "    <div class=\"form-group\">" +
                "        <label>Password</label>" +
                "        <input type=\"password\" name=\"password\" value=\"fredspassword\">" +
                "    </div>" +
                "    <div class=\"form-group\">" +
                "        <label>Confirm Password</label>" +
                "        <input type=\"password\" name=\"confirmPassword\" value=\"fredspassword\">" +
                "    </div>" +
                "    <button class=\"btn\">Submit</button>" +
                "    <button class=\"btn\" hx-get=\"/signin\">Cancel</button>" +
                "</form>";
        return ResponseEntity.ok(s);
    }


    @PostMapping("/signup")
    public ResponseEntity<String> createAccount(@RequestBody Account signUpFormData) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(signUpFormData.getPassword());

        signUpFormData.setPassword(password);

        Account savedAccount = accountRepository.save(signUpFormData);

        return ResponseEntity.ok("createAccount(): " + signUpFormData.getEmail());
    }


}
