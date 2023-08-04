package com.springjwtsecurity.springjwtscurity.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloResoure {

    @GetMapping("/")
    public ResponseEntity<String> Hello(){
        String re = "Val";
        return  ResponseEntity.ok().body(re);
    }
}
