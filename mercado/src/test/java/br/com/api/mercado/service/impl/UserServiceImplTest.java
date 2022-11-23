package br.com.api.mercado.service.impl;

import br.com.api.mercado.exceptions.EmailAlreadyExistException;
import br.com.api.mercado.exceptions.MyRoleNotFoundException;
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

import javax.management.relation.RoleNotFoundException;
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
    private static final String ROLE_ADMIN_NAME = "ROLE_ADMIN";

     /*  |--> INFO OF USER NORMAL
     *   |----> it's was use for create user_normal
     * */
    private static final Long USER_ID = 2L;
    private static final String USER_USERNAME = "user";
    private static final String USER_EMAIL = "user@test.com";
    private static final String USER_PASSWORD = "password";
    private static final String ROLE_USER_NAME = "ROLE_USER";


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
    private final UserRegisterRequest REQUEST = createUserRegisterRequest();
    private final UserInfoResponse RESPONSE_USER = createUserInfoResponseUser();
    private final UserInfoResponse RESPONSE_ADMIN = createUserInfoResponseAdmin();

    private final Role ROLE_USER = getRoleUser();
    private final Role ROLE_ADMIN = getRoleAdmin();




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
        createUserRegisterRequest();
        getRoleUser();
        getRoleAdmin();
    }

    @Test
    @SneakyThrows
    void saveUser_With_Successfully() {
        when(userRepository.save(any())).thenReturn(USER_NORMAL);
        when(roleRepository.findByName(any())).thenReturn(ROLES_OPTIONAL_USER);

        UserInfoResponse response = service.saveUser(REQUEST);

        assertNotNull(response);
        assertEquals(RESPONSE_USER.getId(), response.getId());
        assertEquals(RESPONSE_USER.getUsername(), response.getUsername());
        assertEquals(RESPONSE_USER.getEmail(),response.getEmail());
        assertEquals(RESPONSE_USER.getRoles(), response.getRoles());
    }

    @Test
    void saveUser_With_Unsuccessfully() {
        when(userRepository.save(any())).thenThrow(new EmailAlreadyExistException("There's account with email"));
        when(roleRepository.findByName(any())).thenReturn(ROLES_OPTIONAL_USER);

        try {
            UserInfoResponse infoResponse = service.saveUser(REQUEST);
        } catch (Exception exception) {
            assertEquals(EmailAlreadyExistException.class, exception.getClass());
            assertEquals("There's account with email", exception.getMessage());
        }
    }

    @Test
    void listAllUsers_With_UserInfoResponse() {
        when(userRepository.findAll()).thenReturn(USER_LIST);

        List<UserInfoResponse> responses = service.findAll();

        assertNotNull(responses.get(0));
        assertEquals(RESPONSE_USER.getId(), responses.get(0).getId());
        assertEquals(RESPONSE_USER.getUsername(), responses.get(0).getUsername());
        assertEquals(RESPONSE_USER.getEmail(), responses.get(0).getEmail());
        assertEquals(RESPONSE_USER.getRoles(), responses.get(0).getRoles());

        assertNotNull(responses.get(1));
        assertEquals(RESPONSE_ADMIN.getId(), responses.get(1).getId());
        assertEquals(RESPONSE_ADMIN.getUsername(), responses.get(1).getUsername());
        assertEquals(RESPONSE_ADMIN.getEmail(), responses.get(1).getEmail());
        assertEquals(RESPONSE_ADMIN.getRoles(), responses.get(1).getRoles());
    }

    @Test
    @SneakyThrows
    void findTheRolesUserInTheDatabase_With_Successfully() {
        when(roleRepository.findByName(any())).thenReturn(ROLES_OPTIONAL_USER);

        Role response = service.findTheRolesInTheDatabase(ROLE_USER.getName());

        assertNotNull(response);
        assertEquals(ROLE_USER.getClass() ,response.getClass());
        assertEquals(ROLE_USER.getId(),response.getId());
        assertEquals(ROLE_USER.getName(), response.getName());
    }

    @Test
    void findTheRolesInTheDatabase_With_Unsuccessfully() {
        when(roleRepository.findByName(any()))
                .thenThrow(new MyRoleNotFoundException("Role not found"));

        try {
            service.findTheRolesInTheDatabase(ROLE_USER_NAME);
        } catch (Exception exception) {
            assertEquals(MyRoleNotFoundException.class, exception.getClass());
            assertEquals("Role not found", exception.getMessage());
        }
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
        return List.of(new Role(2L, ROLE_ADMIN_NAME));
    }

    private Optional<Role> getRoleUserOptional() {
        return Optional.of(new Role(1L, ROLE_USER_NAME));
    }
    private Optional<Role> getRoleAdminOptional() {
        return Optional.of(new Role(2L, ROLE_ADMIN_NAME));
    }

    private List<Role> getRoleUserList() {
        return List.of(new Role(1L, ROLE_USER_NAME));
    }

    private Role getRoleAdmin() {
        return new Role(1L, ROLE_ADMIN_NAME);
    }

    private Role getRoleUser() {
        return new Role(1L, ROLE_USER_NAME);
    }
}