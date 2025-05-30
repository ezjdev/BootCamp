package com.colvir.bootcamp.homework5.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizedUser {
    private String username;
    private String[] roles;
}
