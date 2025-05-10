package it.epicode.Progettosettimanale_back_end_S7_L5.auth;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Set<Role> roles;
}
