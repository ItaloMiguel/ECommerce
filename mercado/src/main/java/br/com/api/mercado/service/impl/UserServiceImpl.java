package br.com.api.mercado.service.impl;

import br.com.api.mercado.model.Role;
import br.com.api.mercado.model.User;
import br.com.api.mercado.payload.request.UserRegisterRequest;
import br.com.api.mercado.payload.response.UserInfoResponse;
import br.com.api.mercado.repository.RoleRepository;
import br.com.api.mercado.repository.UserRepository;
import br.com.api.mercado.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Override
    public UserInfoResponse saveUser(UserRegisterRequest request) throws RoleNotFoundException {
        log.info("new user request sent, Request information: " + request);
        Role role_user = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("ROLE_USER not found. There's a problem in the 'system' or 'code', please check if 'ROLE_USER' is present"));

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
}
