package br.com.api.mercado.data;

import br.com.api.mercado.enums.RoleType;
import br.com.api.mercado.exceptions.MyRoleNotFoundException;
import br.com.api.mercado.model.Role;
import br.com.api.mercado.model.User;
import br.com.api.mercado.repository.RoleRepository;
import br.com.api.mercado.repository.UserRepository;
import br.com.api.mercado.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static br.com.api.mercado.enums.RoleType.ROLE_ADMIN;
import static br.com.api.mercado.enums.RoleType.ROLE_USER;

@Slf4j
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;

    public SetupDataLoader(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, UserService userService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
            createRoleIfNotExist(ROLE_ADMIN.name());
            createRoleIfNotExist(ROLE_USER.name());

            createNewUserTestIfNotExist();

    }

    @Transactional
    private void createNewUserTestIfNotExist() {
        log.info("Checking if 'user-test' is present in the Database");
        Role role = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new MyRoleNotFoundException("Not found ROLE_ADMIN. There's a problem in 'system' or 'code', please check in databases if 'ROLE_ADMIN' is present in database"));
        Optional<User> optionalEmail = userRepository.findByEmail("test@test.com");
        Optional<User> optionalUsername = userRepository.findByUsername("test");
        if(optionalEmail.isEmpty() || optionalUsername.isEmpty()) {
            log.info("Saving user test to the database");
            userRepository.save(new User(1L, "test_firstName", "test_lastName", "test",
                    "test@test.com", passwordEncoder.encode("password"), 22, Set.of(role)));
        }
    }

    @Transactional
    private void createRoleIfNotExist(String roles) {
        log.info("Checking if '" + roles +"' is present in the Database");
        Optional<Role> optional = roleRepository.findByName(roles);
        if (optional.isEmpty()) {
            log.info("Saving new role '" + roles + "' in the Database");
            roleRepository.save(new Role(roles));
        }
    }
}
