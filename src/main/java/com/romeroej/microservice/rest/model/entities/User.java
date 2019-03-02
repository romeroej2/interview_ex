package com.romeroej.microservice.rest.model.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Base entity that describes a User in the Database.
 * Getters,Setters and constructors auto generated via Lombok
 *
 * @author RomeroEJ
 * @version 1.0
 * @since 2019-02-28
 */
@Data
@AllArgsConstructor
@Entity
@ApiModel(value = "User")
public class User implements Serializable {


    @ApiModelProperty(value = "username")
    @Id
    private String username;

    public User() {
    }


    @ApiModelProperty(value = "Full Name")
    private String fullName;
    @ApiModelProperty(value = "Address")
    private String address;
    @ApiModelProperty(value = "Email")
    private String email;
    @ApiModelProperty(value = "Password")
    private String password;


    public User(String username, String fullName, String email, String password) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;

    }
}

