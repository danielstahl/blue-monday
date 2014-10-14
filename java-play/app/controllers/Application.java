package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import play.*;
import play.mvc.*;

import services.HelloService;
import views.html.*;

@Singleton
public class Application extends Controller {

    private final HelloService helloService;

    @Inject
    public Application(HelloService helloService) {
        this.helloService = helloService;
    }

    public Result index() {
        return ok(index.render("The hello service says: " + helloService.sayHello("Spotify")));
    }

}
