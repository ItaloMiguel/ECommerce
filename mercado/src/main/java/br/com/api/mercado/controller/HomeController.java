package br.com.api.mercado.controller;

import br.com.api.mercado.payload.request.UserRegisterRequest;
import br.com.api.mercado.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.server.util.matcher.IpAddressServerWebExchangeMatcher;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/api/home")
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final UserService userService;

}
