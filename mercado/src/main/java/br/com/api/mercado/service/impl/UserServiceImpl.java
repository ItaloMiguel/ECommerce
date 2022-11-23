package br.com.api.mercado.service.impl;

import br.com.api.mercado.exceptions.MyRoleNotFoundException;
import br.com.api.mercado.model.Role;
import br.com.api.mercado.model.User;
import br.com.api.mercado.payload.request.UserRegisterRequest;
import br.com.api.mercado.payload.response.UserInfoResponse;
import br.com.api.mercado.repository.RoleRepository;
import br.com.api.mercado.repository.UserRepository;
import br.com.api.mercado.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;

import static br.com.api.mercado.enums.RoleType.ROLE_USER;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserInfoResponse saveUser(UserRegisterRequest request) {
        log.info("new user request sent, Request information: " + request);
        Role role_user = findTheRolesInTheDatabase(ROLE_USER.name());

        // todo: Verify if E-mail already exist in the Database
        // todo: Verify if Username already exist in the Database
        // todo: Verify if it's fraud
        // todo: Send confirmation E-mail
        // todo: If users miss password or username for three times, send an E-mail

        User user = User.myBuilder(request, Set.of(role_user), passwordEncoder);
        log.info("Full user build. User{ 'username': " + user.getUsername() + ",'email': " + user.getEmail() + " }");
        User save = userRepository.save(user);
        return UserInfoResponse.myBuilder(save);
    }

    @Override
    public List<UserInfoResponse> findAll() {
        log.info("Find all users in the Database");
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserInfoResponse::myBuilder).toList();
    }

    protected Role findTheRolesInTheDatabase(String role) {
        log.info("Find role: " + role);
        return roleRepository.findByName(role)
                .orElseThrow(() -> new MyRoleNotFoundException( role + " not found. There's a problem in the 'system' or 'code', please check if '" + role + "' is present"));
    }
}
