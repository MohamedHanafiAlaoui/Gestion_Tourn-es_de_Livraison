package com.example.Livraison.conroller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatientController
{
    @GetMapping("/name")
    public String  printPatinName()
    {
        return  "Hello World";
    }
}
