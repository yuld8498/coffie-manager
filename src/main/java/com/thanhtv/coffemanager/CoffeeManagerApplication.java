package com.thanhtv.coffemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CoffeeManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(CoffeeManagerApplication.class, args);
	}

	@GetMapping("/api")
	public ResponseEntity<?>getApi(){
		return new ResponseEntity<>("Hello world",HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?>getApsi(){
		return new ResponseEntity<>("Hello world",HttpStatus.OK);
	}

}
