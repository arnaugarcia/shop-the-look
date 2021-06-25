package com.klai.stl.cucumber;

import com.klai.stl.ShopTheLookApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = ShopTheLookApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
