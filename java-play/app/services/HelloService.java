package services;

import com.google.inject.Singleton;

/**
 *
 */
@Singleton
public class HelloService {
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
