package br.com.api.mercado.payload.response;

import br.com.api.mercado.model.Role;
import br.com.api.mercado.model.User;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserInfoResponse {
    private Long id;
    private String fistName;
    private String lastName;
    private String username;
    private String email;
    private Integer yourAge;
    private Collection<Role> roles;

    public static UserInfoResponse myBuilder(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .fistName(user.getFistName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .yourAge(user.getYourAge())
                .roles(user.getRoles())
                .build();
    }
}
