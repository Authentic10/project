package bdma.bigdata.project.rest.api;

import bdma.bigdata.project.rest.dao.GradesDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;


@Path("/GradesService")
public class GradesService {

    @GET
    @Path("/rates/{s}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRates(@PathParam("s") String s) {

        HashMap<String, String> gda = GradesDAO.getPercents(s);
        if (!gda.isEmpty()) {
            return Response.ok(gda, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Semester not found: " + s).build();
        }
    }
}
