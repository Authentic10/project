package bdma.bigdata.project.rest.api;

import bdma.bigdata.project.rest.dao.GradesDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;


@Path("/GradesService")
public class GradesService {

    @GET
    @Path("/rates/{s}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRates(@PathParam("s") String s) throws URISyntaxException {

        HashMap<String, String> gda = GradesDAO.getPercents(s);
        if (!gda.isEmpty()) {
            return Response.ok(gda, MediaType.APPLICATION_JSON).build();
        } else {
            //java.net.URI location = new java.net.URI("/GradesServi");
            URI uri = new URI("http://localhost:8080/project_war_exploded/error404.html");
            return Response.temporaryRedirect(uri).build();
            //return Response.status(Response.Status.NOT_FOUND).entity("Semester not found: " + s).build();
        }
    }
}
