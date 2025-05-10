package it.epicode.Progettosettimanale_back_end_S7_L5.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
