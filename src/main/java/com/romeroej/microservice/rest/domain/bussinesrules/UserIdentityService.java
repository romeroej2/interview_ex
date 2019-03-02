package com.romeroej.microservice.rest.domain.bussinesrules;


import com.romeroej.microservice.rest.model.entities.Event;
import com.romeroej.microservice.rest.model.entities.User;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


/**
 * @author RomeroEJ
 * @version 1.0
 * @since 2019-02-28
 */
@Stateless
public class UserIdentityService {


    private static final Logger LOG = Logger.getLogger(UserIdentityService.class);


    @PersistenceContext
    private EntityManager entityManager;


    @PostConstruct
    public void UserIdentityServicePostConstruct() {

    }


    private String fullName;
    private String address;
    private String email;
    private String password;

    public User createUser(User user) throws Exception {


        User userExists = entityManager.find(User.class, user.getUsername());

        if (userExists != null)
            throw new Exception("User Already Exists");
        else {
            try {
                entityManager.persist(user);
                return user;
            } catch (Exception exCreation) {
                throw new Exception(String.format("Problem Persisting New User %s %s", user.getUsername(), user.getEmail()));
            }
        }


    }


    public User isValidLogin(String username, String validpassword) throws Exception {

        try {
            User user = entityManager.find(User.class, username);

            if (user != null && user.getPassword().equals(validpassword)) {
                return user;
            } else {
                throw new Exception("Invalid Credentials");
            }

        } catch (Exception ex) {
            throw new Exception("User is not found on system");
        }

    }


}
