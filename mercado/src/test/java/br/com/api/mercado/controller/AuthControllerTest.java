package br.com.api.mercado.controller;

import br.com.api.mercado.model.Role;
import br.com.api.mercado.model.User;
import br.com.api.mercado.payload.request.UserRegisterRequest;
import br.com.api.mercado.payload.response.MessageResponse;
import br.com.api.mercado.payload.response.UserInfoResponse;
import br.com.api.mercado.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthControllerTest {

    /*  |--> INFO OF USER INFO RESPONSE
    *   |----> This class is call when return user in the body
    * */
    private static final Long RESPONSE_ID = 1L;
    private static final String RESPONSE_FIRST_NAME = "response_firstName";
    private static final String RESPONSE_LAST_NAME = "response_lastName";
    private static final String RESPONSE_USERNAME = "response";
    private static final String RESPONSE_EMAIL = "response@test.com";
    private static final Integer RESPONSE_YOUR_AGE = 22;
    private static final String RESPONSE_ROLE = "ROLE_USER";

    /*  |--> INFO OF USER REGISTER REQUEST
    *   |----> This class is call when user sand register and update
    * */
    private static final String REQUEST_FIRST_NAME = "request_firstName";
    private static final String REQUEST_LAST_NAME = "request_lastName";
    private static final String REQUEST_USERNAME = "request";
    private static final String REQUEST_EMAIL = "request@test.com";
    private static final String REQUEST_PASSWORD = "password";
    private static final Integer REQUEST_YOUR_AGE = 20;

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

     /*  |--> CREATE ROLES
     * */
    private final List<Role> ROLES_USER = getRoleUserList();
    private final List<Role> ROLES_ADMIN = getRoleAdminList();
    private final Role ROLE_USER = getRoleUser();
    private final Role ROLE_ADMIN = getRoleAdmin();
    private final Optional<Role> ROLES_OPTIONAL_USER = getRoleUserOptional();
    private final Optional<Role> ROLES_OPTIONAL_ADMIN = getRoleAdminOptional();


     /*  |--> CREATE ALL TYPE OF USERS
     * */
    private final User USER_ADMIN = createUserAdmin();
    private final User USER_NORMAL = createUserNormal();
    private final UserRegisterRequest REQUEST = createUserRegisterRequest();
    private final UserInfoResponse RESPONSE_USER = createUserInfoResponseUser();
    private final UserInfoResponse RESPONSE_ADMIN = createUserInfoResponseAdmin();
    private final List<UserInfoResponse> USER_INFO_RESPONSE_LIST = getUsersList();

    private AuthController controller;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.controller = new AuthController(userService);
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
    void registerUserThenReturnSuccessfully() {
        when(userService.saveUser(any())).thenReturn(RESPONSE_USER);

        ResponseEntity<?> response = controller.registerUser(REQUEST);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getStatusCode());
        assertEquals(ResponseEntity.class , response.getClass());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(201 , response.getStatusCodeValue());
        assertEquals(MessageResponse.class , response.getBody().getClass());
    }

    @Test
    void findAllUserThenReturnSuccessfully() {
        when(userService.findAll()).thenReturn(USER_INFO_RESPONSE_LIST);

        ResponseEntity<List<UserInfoResponse>> response = controller.findByUserForId();

        assertNotNull(response);
        assertNotNull(response.getBody().get(0).getClass());
        assertNotNull(response.getBody().get(1).getClass());
        assertNotNull(response.getBody());

        assertEquals(ResponseEntity.class , response.getClass());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200 , response.getStatusCodeValue());
        assertEquals(UserInfoResponse.class , response.getBody().get(0).getClass());
        assertEquals(UserInfoResponse.class , response.getBody().get(1).getClass());


        assertEquals(RESPONSE_USER.getId(), response.getBody().get(0).getId());
        assertEquals(RESPONSE_USER.getFistName(), response.getBody().get(0).getFistName());
        assertEquals(RESPONSE_USER.getLastName(), response.getBody().get(0).getLastName());
        assertEquals(RESPONSE_USER.getUsername(), response.getBody().get(0).getUsername());
        assertEquals(RESPONSE_USER.getEmail(), response.getBody().get(0).getEmail());
        assertEquals(RESPONSE_USER.getYourAge(), response.getBody().get(0).getYourAge());
        assertEquals(RESPONSE_USER.getRoles(), response.getBody().get(0).getRoles());


        assertEquals(RESPONSE_ADMIN.getId(), response.getBody().get(1).getId());
        assertEquals(RESPONSE_ADMIN.getFistName(), response.getBody().get(1).getFistName());
        assertEquals(RESPONSE_ADMIN.getLastName(), response.getBody().get(1).getLastName());
        assertEquals(RESPONSE_ADMIN.getUsername(), response.getBody().get(1).getUsername());
        assertEquals(RESPONSE_ADMIN.getEmail(), response.getBody().get(1).getEmail());
        assertEquals(RESPONSE_ADMIN.getYourAge(), response.getBody().get(1).getYourAge());
        assertEquals(RESPONSE_ADMIN.getRoles(), response.getBody().get(1).getRoles());
    }

     /*  |--> CREATING USERS FOR TEST
     *   |----> Just create new users for testing
     * */
    private User createUserAdmin() {
        return new User(ADMIN_ID, ADMIN_FIST_NAME, ADMIN_LAST_NAME, ADMIN_USERNAME, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_AGE_YEAR, ROLES_ADMIN);
    }
    private User createUserNormal() {
        return new User(USER_ID, USER_FIST_NAME, USER_LAST_NAME, USER_USERNAME, USER_EMAIL, USER_PASSWORD, USER_AGE_YEAR, ROLES_USER);
    }

    private List<UserInfoResponse> getUsersList() {
        return List.of(RESPONSE_USER, RESPONSE_ADMIN);
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