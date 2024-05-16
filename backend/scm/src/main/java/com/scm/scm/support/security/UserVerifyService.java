package com.scm.scm.support.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserVerifyService {
    private static final Logger log = Logger.getLogger(UserAccessService.class.toString());

    public FirebaseToken verifyUserToken(String token) {
        try {
            log.log(Level.INFO, "Verifying user token");
            return FirebaseAuth.getInstance().verifyIdToken(token);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Invalid token: {0}", token);
            throw new CustomHttpException("Invalid token", HttpStatus.UNAUTHORIZED.value(), ExceptionCause.USER_ERROR);
        }
    }
}
