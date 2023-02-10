/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.promerica.rest.jwt.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Singleton;

//TODO: CAMBIAR METODO DE ALMACENAMIENTO ¿HAZELCAST?

@Startup
@RequestScoped
public class PromApplicationStore implements Serializable{

   private Map<String,SecurityToken> tokenStorage;
   
   @PostConstruct
   private void init(){
        tokenStorage = new HashMap<>();
   }
    
    /**
     * @return the tokenStorage
     */
    public Map<String,SecurityToken> getTokenStorage() {
        return tokenStorage;
    }

    

   
}
