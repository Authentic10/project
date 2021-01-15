package bdma.bigdata.project.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import bdma.bigdata.project.rest.core.Student;
import bdma.bigdata.project.rest.dao.StudentDAO;

@Path("/StudentService")

public class StudentService {

    private StudentDAO sdao = StudentDAO.create();

    @GET
    @Path("/students")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudents() {
        return Response.ok(sdao.getStudents(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/students/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("id") String ID) {
        Student stu = sdao.getStudent(ID);
        if (stu != null) {
            return Response.ok(stu, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Student not found: " + ID).build();
        }
    }
}