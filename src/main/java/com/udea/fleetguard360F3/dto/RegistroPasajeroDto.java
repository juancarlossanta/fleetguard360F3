package com.udea.fleetguard360F3.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroPasajeroDto {

    @NotBlank
    private String username;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    private String identificacion;

    @NotBlank
    private String telefono;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    private String passwordConfirm;

}

