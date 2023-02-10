/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.promerica.rest.jwt.security;

import java.security.MessageDigest;
import java.util.Base64;

/**
 *
 * @author megarcia
 */
public class SecurityUtil {
    public static String encodeText(String text){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes("UTF-8"));
            byte[] passwordDigest = md.digest();
            return new String(Base64.getEncoder().encode(passwordDigest));
        }catch(Exception e){
            throw new RuntimeException("Excepcion cifrando password");
        }
    }
}
