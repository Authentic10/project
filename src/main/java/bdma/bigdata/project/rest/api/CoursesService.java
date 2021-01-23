package bdma.bigdata.project.rest.api;

import bdma.bigdata.project.rest.dao.CoursesDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

@Path("/CoursesService")
public class CoursesService {

    @GET
    @Path("/courses/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRates(@PathParam("id") String id) throws URISyntaxException {
        HashMap<String, String> cdao = CoursesDAO.getPercents(id);
        if (!cdao.isEmpty()) {
            return Response.ok(cdao, MediaType.APPLICATION_JSON).build();
        } else {
            URI uri = new URI("http://localhost:8080/project_war_exploded/error404.html");
            return Response.temporaryRedirect(uri).build();
        }
    }
}
