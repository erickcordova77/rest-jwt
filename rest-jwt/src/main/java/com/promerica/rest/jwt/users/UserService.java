/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.promerica.rest.jwt.users;


import com.promerica.rest.jwt.security.SecurityUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Startup
public class UserService {

    
    List<User> users;
    
    @PostConstruct
    public void init(){
        
       users = new ArrayList<User>(); 
       User user = new User();
       user.setUsuario("pex");
       user.setPassword("secret");
       
       users.add(user);
    }
    
    public String encryptedKey(String subject){
        for(User u:users){
            if(u.getUsuario().equals(subject)){
                return SecurityUtil.encodeText(u.getPassword());
            }
        }
        throw new SecurityException("Subject de token invalido" );
    }
    
    public boolean login(User user){
         for(User u:users){
            if(u.getUsuario().equals(u.getUsuario()) && u.getPassword().equals(user.getPassword())){
                
                return true;
            }
        }
        throw new SecurityException("Usuario o password incorrecto" );
    } 
}
