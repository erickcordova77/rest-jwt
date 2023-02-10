/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.promerica.rest.jwt.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import static javax.ws.rs.Priorities.AUTHENTICATION;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Gabriel Bran <gabriel.bran@promerica.com.sv>
 */
@Provider
@JwtAuthorized
@ApplicationScoped
@Priority(AUTHENTICATION)
public class JwtAuthorizerVerifier implements ContainerRequestFilter, ContainerResponseFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_TYPE = "Bearer";

    private final ThreadLocal<DecodedJWT> decodedJWT = new ThreadLocal<>();

    //@Context
    //private HttpServletRequest servletRequest;
    @Context
    private UriInfo uriInfo;
    
    
    @Inject
    PromApplicationStore promApplicationStore;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        try {
            //if (servletRequest.getRequestURI().contains("application.wadl")) {
            //    return;
            //}
            
            //if (!uriInfo.getPath().contains("login")) {
                String header = getAuthorizationHeader(requestContext);
                decodeBearerToken(header);
            //}

            
        } catch (SecurityException e) {
            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(e.getMessage())
                    .build());
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        decodedJWT.remove();
    }

    @Produces
    @RequestScoped
    public DecodedJWT inject() {
        return decodedJWT.get();
    }

    private void decodeBearerToken(String authorization) {
        String token = extractJwtToken(authorization);
        Verification verification = JWT.require(getSecret(token)).acceptLeeway(1L);
        DecodedJWT jwt = verify(token, verification);
        
        Claim claimToken = jwt.getClaims().get("token");
        Claim claimUser = jwt.getClaims().get("user");
        
        SecurityToken tokenInfo = new SecurityToken();
        tokenInfo.setAppUser(claimUser.asString());
        tokenInfo.setEncryptedPassword(claimToken.asString());        
        
        promApplicationStore.getTokenStorage().put("spiTokenRequest", tokenInfo);
        
        
        //String debug = Jwts.parser().setSigningKey("c2VjcmV0").parseClaimsJws(token).getBody().toString();
        
        decodedJWT.set(jwt);
    }

    private DecodedJWT verify(String token, Verification verification) throws SecurityException {
        try {
            JWTVerifier verifier = verification.build();
            return verifier.verify(token);
        } catch (JWTVerificationException var4) {
            throw new SecurityException("Token JWT invalido", var4);
        }
    }

    private Algorithm getSecret(String token) {
        try {
            
            /*if(promApplicationStore.getTokenStorage().containsKey(token)){
                SecurityToken securityToken = promApplicationStore.getTokenStorage().get(token);
                return Algorithm.HMAC256(securityToken.getEncryptedPassword());
            }else{
                throw new SecurityException("Token Invalido");
            }*/
            return Algorithm.HMAC256("promeric@P3x");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String getAuthorizationHeader(ContainerRequestContext requestContext) {
        return requestContext.getHeaderString(AUTHORIZATION_HEADER);
    }

    private String extractJwtToken(String authorization) {

        if (authorization == null || "".equals(authorization)) {
            throw new SecurityException("Autorizacion requerida");
        }

        String[] parts = authorization.split(" ");

        if (!BEARER_TYPE.equals(parts[0])) {
            throw new SecurityException("Autorizacion no soportada " + parts[0]);
        }
        
        if(parts.length != 2){
            throw new SecurityException("Autorizacion no soportada token requerido" );
        }

        return parts[1];
    }
}
