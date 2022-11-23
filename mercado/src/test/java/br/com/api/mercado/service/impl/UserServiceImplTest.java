package br.com.api.mercado.service.impl;

import br.com.api.mercado.model.Role;
import br.com.api.mercado.model.User;
import br.com.api.mercado.payload.request.UserRegisterRequest;
import br.com.api.mercado.payload.response.UserInfoResponse;
import br.com.api.mercado.repository.RoleRepository;
import br.com.api.mercado.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    /*  |--> INFO OF USER ADMIN
    *   |----> it's was use for create user_admin
    * */
    private static final Long ADMIN_ID = 1L;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@test.com";
    private static final String ADMIN_PASSWORD = "password";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

     /*  |--> INFO OF USER NORMAL
     *   |----> it's was use for create user_normal
     * */
    private static final Long USER_ID = 2L;
    private static final String USER_USERNAME = "user";
    private static final String USER_EMAIL = "user@test.com";
    private static final String USER_PASSWORD = "password";
    private static final String ROLE_USER = "ROLE_USER";


     /*  |--> INFO OF USER REGISTER REQUEST
     *   |----> it's was use for create user_register_request
     * */
    private static final String USERNAME_REQUEST = "request";
    private static final String EMAIL_REQUEST = "request@test.com";
    private static final String PASSWORD_REQUEST = "password";


    /*  |--> CREATE ROLES
    * */
    private final List<Role> ROLES_USER = getRoleUserList();
    private final List<Role> ROLES_ADMIN = getRoleAdminList();
    private final Optional<Role> ROLES_OPTIONAL_USER = getRoleUserOptional();
    private final Optional<Role> ROLES_OPTIONAL_ADMIN = getRoleAdminOptional();

    /*  |--> CREATE USERS
    * */
    private final User USER_ADMIN = createUserAdmin();
    private final User USER_NORMAL = createUserNormal();
    private final List<User> USER_LIST = getUsersList();




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
        startAllMethods();
    }

    private void startAllMethods() {
        getRoleUserList();
        getRoleAdminList();
        getRoleUserOptional();
        getRoleAdminOptional();
        createUserAdmin();
        createUserNormal();
        getUsersList();
    }

    @Test
    @SneakyThrows
    void saveUser_With_Successfully() {
        UserRegisterRequest request = createUserRegisterRequest();

        when(userRepository.save(any())).thenReturn(USER_ADMIN);
        when(roleRepository.findByName(any())).thenReturn(ROLES_OPTIONAL_ADMIN);

        UserInfoResponse response = service.saveUser(request);

        assertNotNull(response);
        assertEquals(ADMIN_ID, response.getId());
        assertEquals(ADMIN_USERNAME, response.getUsername());
        assertEquals(ADMIN_EMAIL,response.getEmail());
        assertEquals(ROLES_ADMIN, response.getRoles());
    }

    @Test
    void listAllUsers_With_UserInfoResponse() {
        when(userRepository.findAll()).thenReturn(USER_LIST);

        List<UserInfoResponse> responses = service.findAll();

        assertNotNull(responses.get(0));
        assertEquals(USER_ID, responses.get(0).getId());
        assertEquals(USER_USERNAME, responses.get(0).getUsername());
        assertEquals(USER_EMAIL, responses.get(0).getEmail());
        assertEquals(ROLES_USER, responses.get(0).getRoles());

        assertNotNull(responses.get(1));
        assertEquals(ADMIN_ID, responses.get(1).getId());
        assertEquals(ADMIN_USERNAME, responses.get(1).getUsername());
        assertEquals(ADMIN_EMAIL, responses.get(1).getEmail());
        assertEquals(ROLES_ADMIN, responses.get(1).getRoles());
    }


    /*
    *   |--> CREATING USERS FOR TEST
    *   |----> Just create new users for testing
    * */
    private User createUserAdmin() {
        return new User(ADMIN_ID, ADMIN_USERNAME, ADMIN_EMAIL, ADMIN_PASSWORD, ROLES_ADMIN);
    }
    private User createUserNormal() {
        return new User(USER_ID, USER_USERNAME, USER_EMAIL, USER_PASSWORD, ROLES_USER);
    }

    private List<User> getUsersList() {
        List<User> userList = new ArrayList<>();
        userList.add(USER_NORMAL);
        userList.add(USER_ADMIN);
        return userList;
    }

    /*  |--> USER REGISTER RESPONSE
    *   |----> This class is call when exist a new register of user
    *   |----> Example: saveUser.
    * */
    private UserRegisterRequest createUserRegisterRequest() {
        return new UserRegisterRequest(USERNAME_REQUEST, EMAIL_REQUEST, PASSWORD_REQUEST);
    }

    /*  |--> USER INFO RESPONSE =-=-=
    *   |----> This class is call when return information of users
    *   |----> Example: listAll, findById, etc...
    * */
    private UserInfoResponse createUserInfoResponseUser() {
        return new UserInfoResponse(USER_ID, USER_USERNAME, USER_EMAIL, ROLES_USER);
    }
    private UserInfoResponse createUserInfoResponseAdmin() {
        return new UserInfoResponse(ADMIN_ID, ADMIN_USERNAME, ADMIN_EMAIL, ROLES_ADMIN);
    }

    /*  |--> CREATING ROLES
    *   |----> Just create new roles for testing
    * */
    private List<Role> getRoleAdminList() {
        return List.of(new Role(2L, ROLE_ADMIN));
    }

    private Optional<Role> getRoleUserOptional() {
        return Optional.of(new Role(2L, ROLE_USER));
    }
    private Optional<Role> getRoleAdminOptional() {
        return Optional.of(new Role(1L, ROLE_ADMIN));
    }

    private List<Role> getRoleUserList() {
        return List.of(new Role(3L, ROLE_USER));
    }

    private Role getRoleAdmin() {
        return new Role(2L, ROLE_ADMIN);
    }

    private Role getRoleUser() {
        return new Role(3L, ROLE_USER);
    }
}