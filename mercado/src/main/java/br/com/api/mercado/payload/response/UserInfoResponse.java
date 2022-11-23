package br.com.api.mercado.payload.response;

import br.com.api.mercado.model.Role;
import br.com.api.mercado.model.User;
import lombok.*;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserInfoResponse {
    private Long id;
    private String username;
    private String email;
    private Collection<Role> roles;

    public static UserInfoResponse myBuilder(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }
}
