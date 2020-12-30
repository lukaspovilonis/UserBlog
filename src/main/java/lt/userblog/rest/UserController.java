package lt.userblog.rest;

import lt.userblog.entities.ResponseElement;
import lt.userblog.entities.User;
import lt.userblog.persistence.UserDAO;

import lt.userblog.rest.contracts.UserDto;
import lt.userblog.services.PasswordService;
import lt.userblog.services.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static lt.userblog.services.PasswordService.getSHA512SecurePassword;

@ApplicationScoped
@Path("/user")
public class UserController {

    @Inject
    private UserService userService;

    @Inject
    private PasswordService passwordService;

    @Path("/registration")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response registerUser(UserDto userDto) {
        //need to put in service
        User user = new User();
        user.setUser_name(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        ResponseElement responseElement = new ResponseElement();
        if (userService.ifExistName(userDto.getUsername())){
            responseElement.setError("User name is already exists!");
            return Response.status(409).entity(responseElement).build();
        }
        Response response = passwordService.validateUserProp(userDto);
        if (response != null)
            return response;
        user.setPassword(getSHA512SecurePassword(user.getPassword()));
        User createdUser =  userService.update(user);
        responseElement.setInfo(passwordService.generateToken(createdUser));
        return Response.ok(responseElement).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(UserDto userDto){
        ResponseElement responseElement = new ResponseElement();
        Response response = passwordService.validateUserProp(userDto);
        if (response != null)
            return response;
        if (!userService.ifExistName(userDto.getUsername())){
            responseElement.setError("Wrong user name or  password!");
            return Response.status(401).entity(responseElement).build();
        }
        User existingUser = userService.findOne(userDto.getUsername());
        if (existingUser == null) {
            responseElement.setError("User doesn't exists!");
            return Response.ok().entity(responseElement).build();
        }
        if (existingUser.getPassword().equals(getSHA512SecurePassword(userDto.getPassword()))){
            responseElement.setInfo(passwordService.generateToken(existingUser));
            return Response.ok().entity(responseElement).build();
        }else{
            responseElement.setError("Wrong user name or  password!");
            return Response.status(401).entity(responseElement).build();
        }
    }
}
