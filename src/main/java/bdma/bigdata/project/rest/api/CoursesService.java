package bdma.bigdata.project.rest.api;

import bdma.bigdata.project.rest.core.Courses;
import bdma.bigdata.project.rest.core.Instructors;
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

    @GET
    @Path("/courses/{id}/rates/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRatesforYear(@PathParam("id") String id, @PathParam("year") String year) {
        HashMap<String, String> cdao = CoursesDAO.getCoursePercent(id, year);
        if (cdao!=null) {
            return Response.ok(cdao, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("UE or year not found: ").build();
        }
    }

    @GET
    @Path("/courses/{p}/means/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRatesforProgram(@PathParam("p") String p, @PathParam("year") String year) {
        HashMap<String, Courses> cdao = CoursesDAO.getCourseProgram(p, year);
        if (cdao!=null) {
            return Response.ok(cdao, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Program or year not found: ").build();
        }
    }

    @GET
    @Path("/instructors/{n}/rates")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRateIns(@PathParam("n") String n) {
        HashMap<String, Instructors> cdao = CoursesDAO.getPercentsIns(n);
        if (!cdao.isEmpty()) {
            return Response.ok(cdao, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Instructor not found: " +n).build();
        }
    }
}
