package br.com.api.mercado.security;

import br.com.api.mercado.model.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDetailsImpl implements UserDetails {

    @EqualsAndHashCode.Include
    private Long id;
    private String fistName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private Integer yourAge;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(
                user.getId(), user.getFistName(), user.getLastName(),
                user.getUsername(), user.getEmail(), user.getPassword(),
                user.getYourAge(), authorities);
    }

    public UserDetailsImpl(Long id, String fistName, String lastName, String username, String email, String password, Integer yourAge, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.fistName = fistName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.yourAge = yourAge;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
