package dk.jyskebank.test;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/hello")
public class GreetingResource {

    @POST
    @Path("")
    @Consumes("application/vnd.mycompany.user.v1+json")
    @Produces("application/vnd.mycompany.greeting.v1+json")
    public Greeting helloV1(User user) {
        return new Greeting("Hello " + user + " from V1");
    }

    /** try removing this method and another exception is thrown */
    @POST
    @Path("")
    @Consumes("application/vnd.mycompany.user.v2+json")
    @Produces("application/vnd.mycompany.greeting.v2+json")
    public Greeting helloV2(User user) {
        return new Greeting("Hello " + user + " from V1");
    }

    public static final record Greeting (String text) {
    }

    public static final record User (String name, String title) {
    }
}
