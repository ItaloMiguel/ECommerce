package br.com.api.mercado.controller;

import br.com.api.mercado.payload.request.UserRegisterRequest;
import br.com.api.mercado.payload.response.MessageResponse;
import br.com.api.mercado.payload.response.UserInfoResponse;
import br.com.api.mercado.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<UserInfoResponse>> findByUserForId() {
        log.info("Starting find all users");
        List<UserInfoResponse> userInfoResponseList = userService.findAll();
        return ResponseEntity.ok().body(userInfoResponseList);
    }


    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegisterRequest request) {
        log.info("Starting as request checks");
        UserInfoResponse infoResponse = userService.saveUser(request);
        return ResponseEntity.status(CREATED).body(new MessageResponse("User registered successfully!"));
    }
}
