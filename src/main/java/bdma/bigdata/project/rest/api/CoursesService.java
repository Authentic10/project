package bdma.bigdata.project.rest.api;

import bdma.bigdata.project.rest.dao.CoursesDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

@Path("/CoursesService")
public class CoursesService {

    @GET
    @Path("/courses/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRates(@PathParam("id") String id) {
        HashMap<String, String> cdao = CoursesDAO.getPercents(id);
        if (!cdao.isEmpty()) {
            return Response.ok(cdao, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("UE not found: " + id).build();
        }
    }
}
