package br.com.api.mercado.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    private String fistName;
    private String lastName;

    @Column(unique = true)
    @NotNull(message = "This field 'username' cannot be empty")
    private String username;

    @Email
    @Column(unique = true)
    @NotNull(message = "This field 'email' cannot be empty")
    private String email;

    @NotNull(message = "This field 'password' cannot be empty")
    private String password;

    private Integer yourAge;
}
