package br.com.api.mercado.service.impl;

import br.com.api.mercado.model.Role;
import br.com.api.mercado.model.User;
import br.com.api.mercado.payload.request.UserRegisterRequest;
import br.com.api.mercado.payload.response.UserInfoResponse;
import br.com.api.mercado.repository.RoleRepository;
import br.com.api.mercado.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    // USER ADMIN
    private static final Long ID = 1L;
    private static final String USERNAME = "admin";
    private static final String EMAIL = "admin@test.com";
    private static final String PASSWORD = "password";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    // USER NORMAL
    private static final Long USER_ID = 2L;
    private static final String USER_USERNAME = "user";
    private static final String USER_EMAIL = "user@test.com";
    private static final String USER_PASSWORD = "password";
    private static final String ROLE_USER = "ROLE_USER";

    // USER REGISTER REQUEST
    private static final String USERNAME_REQUEST = "request";
    private static final String EMAIL_REQUEST = "request@test.com";
    private static final String PASSWORD_REQUEST = "password";


    private UserServiceImpl service;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.service = new UserServiceImpl(userRepository, roleRepository, passwordEncoder);

    }

    @Test
    @SneakyThrows
    void saveUser_With_Successfully() {
        User user = createUserAdmin();
        Optional<Role> roles = getRoleAdminOptional();
        List<Role> ROLES = getRoleAdminList();
        UserRegisterRequest request = createUserRegisterRequest();
        when(userRepository.save(any())).thenReturn(user);
        when(roleRepository.findByName(any())).thenReturn(roles);

        UserInfoResponse response = service.saveUser(request);
        assertNotNull(response);
        assertEquals(ID, response.getId());
        assertEquals(USERNAME, response.getUsername());
        assertEquals(EMAIL ,response.getEmail());
        assertEquals(ROLES, response.getRoles());
    }

    @Test
    void listAllUsers_With_UserInfoResponse() {
        List<Role> ROLES = getRoleUserList();
        List<User> userList = new ArrayList<>();
        userList.add(createUser());
        userList.add(createUserAdmin());
        UserInfoResponse infoResponse = createUserInfoResponseUser();

        when(userRepository.findAll()).thenReturn(userList);

        List<UserInfoResponse> responses = service.findAll();
        assertNotNull(responses);
        assertEquals(USER_ID, responses.get(0).getId());
        assertEquals(USER_USERNAME, responses.get(0).getUsername());
        assertEquals(USER_EMAIL, responses.get(0).getEmail());
        assertEquals(ROLES, responses.get(0).getRoles());
    }


    /*
    * PART OF CREATING USERS
    * */
    private User createUserAdmin() {
        List<Role> ROLES = getRoleAdminList();
        return new User(ID, USERNAME, EMAIL, passwordEncoder.encode(PASSWORD), ROLES);
    }
    private User createUser() {
        List<Role> ROLES = getRoleUserList();
        return new User(USER_ID, USER_USERNAME, USER_EMAIL, passwordEncoder.encode(USER_PASSWORD), ROLES);
    }
    private UserRegisterRequest createUserRegisterRequest() {
        return new UserRegisterRequest(USERNAME_REQUEST, EMAIL_REQUEST, passwordEncoder.encode(PASSWORD_REQUEST));
    }

    private UserInfoResponse createUserInfoResponseUser() {
        List<Role> ROLES = getRoleUserList();
        return new UserInfoResponse(USER_ID, USER_USERNAME, USER_EMAIL, ROLES);
    }

    /*
    * PART OF CREATING A ROLES
    * */
    private static List<Role> getRoleAdminList() {
        return List.of(new Role(2L, ROLE_ADMIN));
    }

    private static Optional<Role> getRoleAdminOptional() {
        return Optional.of(new Role(2L, ROLE_ADMIN));
    }

    private static List<Role> getRoleUserList() {
        return List.of(new Role(3L, ROLE_USER));
    }

    private static Role getRoleAdmin() {
        return new Role(2L, ROLE_ADMIN);
    }

    private Role getRoleUser() {
        return new Role(3L, ROLE_USER);
    }
}