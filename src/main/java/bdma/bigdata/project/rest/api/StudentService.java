package bdma.bigdata.project.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import bdma.bigdata.project.rest.core.Student;
import bdma.bigdata.project.rest.dao.StudentDAO;

import java.util.HashMap;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/StudentService")

public class StudentService {

    private StudentDAO sdao = StudentDAO.create();

    @GET
    @Path("/students/{id}/transcripts/{program}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("id") String ID, @PathParam("program") String program) throws URISyntaxException {
        Student stu = sdao.getStudent(ID, program);
        if (stu != null) {
            return Response.ok(stu, MediaType.APPLICATION_JSON).build();
        } else {
            URI uri = new URI("http://localhost:8080/project_war_exploded/error404.html");
            return Response.temporaryRedirect(uri).build();        }
    }

    @GET
    @Path("/ranks/{p}/years/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRatesforProgram(@PathParam("p") String p, @PathParam("year") String year) {
        HashMap<String, Float> sdao = StudentDAO.getStudentsRanks(p, year);
        if (sdao!=null) {
            return Response.ok(sdao, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Program or year not found: ").build();
        }
    }
}