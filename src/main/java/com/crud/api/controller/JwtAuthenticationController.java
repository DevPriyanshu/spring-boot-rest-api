package com.crud.api.controller;

import com.crud.api.annotations.accessForSuperAdmin;
import com.crud.api.exception.ResourceNotFoundException;
import com.crud.api.model.User;
import com.crud.api.model.authentication.JwtRequest;
import com.crud.api.model.authentication.JwtResponse;
import com.crud.api.model.authentication.UserAuth;
import com.crud.api.repository.jpa.UserRepository;
import com.crud.api.service.JwtUserDetailsService;
import com.crud.api.service.UserService;
import com.crud.api.utility.JwtTokenUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    //@HasRole(UserRoleType.ROLE_SUPER_ADMIN)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        User user = userRepository.findByUsername(authenticationRequest.getUsername());
        if(user == null){
            throw new ResourceNotFoundException("We aren't able to find th user based on provided user name!");
        }
        Authentication authentication = new UserAuth(user.getId(), user.getUsername(), user.getPassword(), user.getRoles());
        System.out.println(authentication.getAuthorities());
        final String token = jwtTokenUtil.generateToken(authentication, user.getId());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/add-admin")
    @accessForSuperAdmin
    public ResponseEntity<?> addUserWithAdminRole(@RequestBody @Valid User user, @RequestHeader Map<String, String> headers) throws Exception {
        String[] jwt = headers.get("authorization").split(" ");
        System.out.println(jwtTokenUtil.getAllClaimsFromToken(jwt[1]));
        User createdUser = userService.saveAdminUser(user, jwt[1]);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}