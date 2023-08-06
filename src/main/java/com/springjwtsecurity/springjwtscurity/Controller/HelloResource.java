package com.springjwtsecurity.springjwtscurity.Controller;

import com.springjwtsecurity.springjwtscurity.Util.AuthenticationRequest;
import com.springjwtsecurity.springjwtscurity.Util.AuthenticationResponse;
import com.springjwtsecurity.springjwtscurity.Util.JwtUtil;
import com.springjwtsecurity.springjwtscurity.SecurityConfig.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloResource {

    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    MyUserDetailsService userDetailsService;

    @GetMapping("/hello")
    public ResponseEntity<String> Hello(){
        String re = "Val";
        return  ResponseEntity.ok().body(re);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> jwtResponse(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName()
                    , authenticationRequest.getPassword()));
        }catch (Exception ex){
            throw new Exception("Incorrect user name and password");
        }

       final  UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserName());
       final  String jwtToken = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok().body(new AuthenticationResponse(jwtToken));
    }
}
