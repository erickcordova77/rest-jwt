/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.promerica.rest.jwt.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.promerica.rest.jwt.security.PromApplicationStore;
import com.promerica.rest.jwt.security.SecurityToken;
import com.promerica.rest.jwt.security.SecurityUtil;
import com.promerica.rest.jwt.users.User;
import com.promerica.rest.jwt.users.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("usuarios/login")
public class UserFacade {
    
    @Inject
    PromApplicationStore promApplicationStore;
    
    @Inject
    UserService userService;
    
    @Context
    private UriInfo uriInfo;
    
    @GET
    public Response login() {
        return Response.status(Response.Status.BAD_REQUEST).entity("Recurso requiere envio de datos por peticion POST").build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String json) {
        Map<String, String> requestJson = Collections.EMPTY_MAP;
        
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            requestJson = mapper.readValue(json, Map.class);
        } catch (JsonProcessingException ex) {
            
        } catch (IOException e) {
            
        }
        
        User user = new User();
        user.setUsuario(requestJson.get("usuario"));
        user.setPassword(requestJson.get("password"));
        if (userService.login(user)) {
            String applicationUser = user.getUsuario();
            String encryptedPassword = SecurityUtil.encodeText(user.getPassword());
            
            
            String ENCODED_KEY = Base64.getEncoder().encodeToString(encryptedPassword.getBytes());
            
            
            
            long TIEMPO = System.currentTimeMillis();
            long MINUTOSMILISEGUNDOS = 10 * 60000;
            String jwt = Jwts.builder()
                    .signWith(SignatureAlgorithm.HS256, ENCODED_KEY)
                    .setSubject(user.getUsuario())
                    .setIssuer(uriInfo.getAbsolutePath().toString())
                    .setIssuedAt(new Date(TIEMPO))
                    .setExpiration(new Date(TIEMPO + MINUTOSMILISEGUNDOS))
                    .setAudience(uriInfo.getBaseUri().toString())
                    .compact();

            //JsonObject jsonresult = Json.createObjectBuilder().add("JWT", jwt).build();

            
            SecurityToken securityToken = new SecurityToken();
            securityToken.setAppUser(user.getUsuario());
            securityToken.setEncryptedPassword(encryptedPassword);
            promApplicationStore.getTokenStorage().put(jwt, securityToken);
            
            return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).build();
        
        } else {
            throw new SecurityException("No es posible autenticar el usuario");
        }
                
       
    }
}
