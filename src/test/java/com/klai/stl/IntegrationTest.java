package com.klai.stl;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(TYPE)
@Retention(RUNTIME)
@SpringBootTest(classes = ShopTheLookApp.class)
public @interface IntegrationTest {
}
