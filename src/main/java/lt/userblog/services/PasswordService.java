package lt.userblog.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lt.userblog.entities.ResponseElement;
import lt.userblog.entities.User;
import lt.userblog.rest.contracts.UserDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.Date;

@ApplicationScoped
public class PasswordService {

    @Inject
    private UserService userService;

    public static String  getSHA512SecurePassword(String passwordToHash){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(byte aByte:bytes){
                sb.append(Integer.toString((aByte & 0xff)+ 0x10, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }catch (NoSuchAlgorithmException e){
        }
        return generatedPassword;
    }

    public User validateToken(String token) {
        try {
            if(token != null) {
                Algorithm algorithm = Algorithm.HMAC256(Constants.KEY);
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("jwtauth")
                        .build();
                DecodedJWT jwt = verifier.verify(token);
                Claim userId = jwt.getClaim("userId");
                return userService.findOne(userId.asInt());            }
        } catch (UnsupportedEncodingException | JWTVerificationException e){
        }
        return null;
    }

    public Response validateUserProp(UserDto user){
        ResponseElement responseElement = new ResponseElement();
        if(user.getUsername() == null || user.getUsername().isEmpty()){
            responseElement.setError("User name is empty!");
            return Response.status(422).entity(responseElement).build();
        }
        if(user.getPassword() == null || user.getPassword().isEmpty()){
            responseElement.setError("Password is empty!");
            return Response.status(422).entity(responseElement).build();
        }
        return null;
    }

    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(Constants.KEY);
            Date expirationDate = Date.from(ZonedDateTime.now().plusHours(24).toInstant());
            Date issuedAt = Date.from(ZonedDateTime.now().toInstant());
            return JWT.create()
                    .withIssuedAt(issuedAt)
                    .withExpiresAt(expirationDate)
                    .withClaim("userId", user.getId())
                    .withIssuer("jwtauth")
                    .sign(algorithm);
        } catch (UnsupportedEncodingException | JWTCreationException e){
        }
        return null;
    }

}
