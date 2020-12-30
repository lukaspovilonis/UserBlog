package lt.userblog.rest;

import lt.userblog.entities.ResponseElement;
import lt.userblog.entities.User;
import lt.userblog.rest.contracts.BlogDto;
import lt.userblog.services.BlogService;
import lt.userblog.services.PasswordService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/blogs")
public class BlogsController {

    @Inject
    private BlogService blogService;

    @Inject
    private PasswordService passwordService;


    @GET
    @Path("/userBlogs/{userToken}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserBlogs(@PathParam("userToken") String  userToken){
        User user =  passwordService.validateToken(userToken);
        return Response.ok().entity(blogService.getUserBlogs(user)).build();
    }

    @GET
    @Path("/getThemAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBlogs(){
        return Response.ok().entity(blogService.getAllBLogs()).build();
    }

    @POST
    @Path("/saveBlog/{userToken}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveUserBlog(@PathParam("userToken") String  userToken, BlogDto blogDto){
        User user =  passwordService.validateToken(userToken);
        ResponseElement responseElement = new ResponseElement();
        if (user != null){
            return Response.ok(blogService.saveBlog(user, blogDto)).build();
        }
        responseElement.setError("No user is logged on ");
        return Response.status(409).entity(responseElement).build();
    }

    @PUT
    @Path("/updateBlog/{userToken}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserBlog(@PathParam("userToken") String  userToken, BlogDto blogDto){
        User user =  passwordService.validateToken(userToken);
        ResponseElement responseElement = new ResponseElement();
        if (user != null){
            return Response.ok(blogService.updateBlog(user, blogDto)).build();
        }
        responseElement.setError("No user is logged on ");
        return Response.status(409).entity(responseElement).build();
    }

    @DELETE
    @Path("/deleteBlog/{blogId}/{userToken}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUserBlog(@PathParam("userToken") String  userToken,
                                   @PathParam("blogId") Integer blogId){
        User user =  passwordService.validateToken(userToken);
        ResponseElement responseElement = new ResponseElement();
        if (user != null){
            if(blogService.removeUserBlog(blogId)){
                responseElement.setInfo("Blog deleted successfully");
                return Response.ok(responseElement).build();
            }else{
                responseElement.setError("Something gone terible wrong");
                return Response.status(409).entity(responseElement).build();
            }
        }
        responseElement.setError("No user is logged on ");
        return Response.status(409).entity(responseElement).build();
    }
}
