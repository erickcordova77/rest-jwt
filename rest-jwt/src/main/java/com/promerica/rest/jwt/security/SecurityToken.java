/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.promerica.rest.jwt.security;

/**
 *
 * @author megarcia
 */
public class SecurityToken {
    private String appUser;
    private String encryptedPassword;

    /**
     * @return the appUser
     */
    public String getAppUser() {
        return appUser;
    }

    /**
     * @param appUser the appUser to set
     */
    public void setAppUser(String appUser) {
        this.appUser = appUser;
    }

    /**
     * @return the encryptedPassword
     */
    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    /**
     * @param encryptedPassword the encryptedPassword to set
     */
    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    
    
}
