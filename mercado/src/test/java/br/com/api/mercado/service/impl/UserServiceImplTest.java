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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {

    /*  |--> INFO OF USER ADMIN
    *   |----> it's was use for create user_admin
    * */
    private static final Long ADMIN_ID = 1L;
    private static final String ADMIN_FIST_NAME = "admin_fistName";
    private static final String ADMIN_LAST_NAME = "admin_lastName";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@test.com";
    private static final String ADMIN_PASSWORD = "password";
    private static final Integer ADMIN_AGE_YEAR = 22;
    private static final String ROLE_ADMIN_NAME = "ROLE_ADMIN";

     /*  |--> INFO OF USER NORMAL
     *   |----> it's was use for create user_normal
     * */
    private static final Long USER_ID = 2L;
    private static final String USER_FIST_NAME = "user_fistName";
    private static final String USER_LAST_NAME = "user_lastName";
    private static final String USER_USERNAME = "user";
    private static final String USER_EMAIL = "user@test.com";
    private static final String USER_PASSWORD = "password";
    private static final Integer USER_AGE_YEAR = 21;
    private static final String ROLE_USER_NAME = "ROLE_USER";

    /*  |--> INFO OF USER REGISTER REQUEST
     *   |----> it's was use for create user_register_request
     * */
    private static final String REQUEST_FIRST_NAME = "request_firstName";
    private static final String REQUEST_LAST_NAME = "request_lastName";
    private static final String REQUEST_USERNAME = "request";
    private static final String REQUEST_EMAIL = "request@test.com";
    private static final String REQUEST_PASSWORD = "password";
    private static final Integer REQUEST_YOUR_AGE = 20;

    /*  |--> CREATE ROLES
    * */
    private final Set<Role> ROLES_USER = getRoleUserList();
    private final Set<Role> ROLES_ADMIN = getRoleAdminList();
    private final Optional<Role> ROLES_OPTIONAL_USER = getRoleUserOptional();
    private final Optional<Role> ROLES_OPTIONAL_ADMIN = getRoleAdminOptional();
    private final Role ROLE_USER = getRoleUser();
    private final Role ROLE_ADMIN = getRoleAdmin();

    /*  |--> CREATE USERS
    * */
    private final User USER_ADMIN = createUserAdmin();
    private final User USER_NORMAL = createUserNormal();
    private final List<User> USER_LIST = getUsersList();
    private final UserRegisterRequest REQUEST = createUserRegisterRequest();
    private final UserInfoResponse RESPONSE_USER = createUserInfoResponseUser();
    private final UserInfoResponse RESPONSE_ADMIN = createUserInfoResponseAdmin();

    /*  |--> CREATE DATA
    * */



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
    void saveUser_With_Successfully() {
        when(userRepository.save(any())).thenReturn(USER_NORMAL);
        when(roleRepository.findByName(any())).thenReturn(ROLES_OPTIONAL_USER);

        UserInfoResponse response = service.saveUser(REQUEST);

        assertNotNull(response);
        assertEquals(RESPONSE_USER.getId(), response.getId());
        assertEquals(RESPONSE_USER.getFistName(), response.getFistName());
        assertEquals(RESPONSE_USER.getLastName(), response.getLastName());
        assertEquals(RESPONSE_USER.getUsername(), response.getUsername());
        assertEquals(RESPONSE_USER.getEmail(), response.getEmail());
        assertEquals(RESPONSE_USER.getYourAge(), response.getYourAge());
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

        List<UserInfoResponse> response = service.findAll();

        assertNotNull(response.get(0));
        assertEquals(RESPONSE_USER.getId(), response.get(0).getId());
        assertEquals(RESPONSE_USER.getFistName(), response.get(0).getFistName());
        assertEquals(RESPONSE_USER.getLastName(), response.get(0).getLastName());
        assertEquals(RESPONSE_USER.getUsername(), response.get(0).getUsername());
        assertEquals(RESPONSE_USER.getEmail(), response.get(0).getEmail());
        assertEquals(RESPONSE_USER.getYourAge(), response.get(0).getYourAge());
        assertEquals(RESPONSE_USER.getRoles(), response.get(0).getRoles());

        assertNotNull(response.get(1));
        assertEquals(RESPONSE_ADMIN.getId(), response.get(1).getId());
        assertEquals(RESPONSE_ADMIN.getFistName(), response.get(1).getFistName());
        assertEquals(RESPONSE_ADMIN.getLastName(), response.get(1).getLastName());
        assertEquals(RESPONSE_ADMIN.getUsername(), response.get(1).getUsername());
        assertEquals(RESPONSE_ADMIN.getEmail(), response.get(1).getEmail());
        assertEquals(RESPONSE_ADMIN.getYourAge(), response.get(1).getYourAge());
        assertEquals(RESPONSE_ADMIN.getRoles(), response.get(1).getRoles());
    }

    @Test
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
        return new User(ADMIN_ID, ADMIN_FIST_NAME, ADMIN_LAST_NAME, ADMIN_USERNAME, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_AGE_YEAR, ROLES_ADMIN);
    }
    private User createUserNormal() {
        return new User(USER_ID, USER_FIST_NAME, USER_LAST_NAME, USER_USERNAME, USER_EMAIL, USER_PASSWORD, USER_AGE_YEAR, ROLES_USER);
    }

    private List<User> getUsersList() {
        return List.of(USER_NORMAL, USER_ADMIN);
    }

    /*  |--> USER REGISTER RESPONSE
    *   |----> This class is call when exist a new register of user
    *   |----> Example: saveUser.
    * */
    private UserRegisterRequest createUserRegisterRequest() {
        return new UserRegisterRequest(REQUEST_FIRST_NAME, REQUEST_LAST_NAME, REQUEST_USERNAME, REQUEST_EMAIL, REQUEST_PASSWORD, REQUEST_YOUR_AGE);
    }

    /*  |--> USER INFO RESPONSE =-=-=
    *   |----> This class is call when return information of users
    *   |----> Example: listAll, findById, etc...
    * */
    private UserInfoResponse createUserInfoResponseUser() {
        return new UserInfoResponse(USER_ID, USER_FIST_NAME, USER_LAST_NAME, USER_USERNAME, USER_EMAIL, USER_AGE_YEAR, ROLES_USER);
    }

    private UserInfoResponse createUserInfoResponseAdmin() {
        return new UserInfoResponse(ADMIN_ID, ADMIN_FIST_NAME, ADMIN_LAST_NAME, ADMIN_USERNAME, ADMIN_EMAIL, ADMIN_AGE_YEAR, ROLES_ADMIN);
    }

    /*  |--> CREATING ROLES
    *   |----> Just create new roles for testing
    * */


    private Optional<Role> getRoleUserOptional() {
        return Optional.of(new Role(1L, ROLE_USER_NAME));
    }
    private Optional<Role> getRoleAdminOptional() {
        return Optional.of(new Role(2L, ROLE_ADMIN_NAME));
    }

    private Set<Role> getRoleAdminList() {
        return Set.of(new Role(2L, ROLE_ADMIN_NAME));
    }

    private Set<Role> getRoleUserList() {
        return Set.of(new Role(1L, ROLE_USER_NAME));
    }

    private Role getRoleAdmin() {
        return new Role(1L, ROLE_ADMIN_NAME);
    }

    private Role getRoleUser() {
        return new Role(1L, ROLE_USER_NAME);
    }
}