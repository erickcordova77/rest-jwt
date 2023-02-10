/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.promerica.rest.jwt.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.promerica.rest.jwt.security.JwtAuthorized;
import com.promerica.rest.jwt.security.PromApplicationStore;
import com.promerica.rest.jwt.security.SecurityToken;
import com.promerica.rest.jwt.security.SecurityUtil;
import com.promerica.rest.jwt.users.User;
import com.promerica.rest.jwt.users.UserService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Gabriel Bran <gabriel.bran@promerica.com.sv>
 */
@RequestScoped
@JwtAuthorized
@Path("prestamo")
public class LoanPaymentFacade extends AbstractServiceFacade {


    
    @Inject
    private PromApplicationStore promApplicationStore;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JwtAuthorized
    @Path("/consulta")
    public Response consultaPrestamo(String json) {

        Map<String, String> requestJson = Collections.EMPTY_MAP;

        ObjectMapper mapper = new ObjectMapper();

        try {
            
            SecurityToken securityToken = promApplicationStore.getTokenStorage().get("spiTokenRequest");
            


            return Response.status(Response.Status.OK)
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        }
        //TODO SET BAD REQUEST CODE
         catch (Exception ex) {
            Logger.getLogger(LoanPaymentFacade.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("ERROR EN EL PROCESO DE PAGO DE PRESTAMOS")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        
    }
}
