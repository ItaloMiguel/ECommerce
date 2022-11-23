package br.com.api.mercado.service;

import br.com.api.mercado.payload.request.UserRegisterRequest;
import br.com.api.mercado.payload.response.UserInfoResponse;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

public interface UserService {

    UserInfoResponse saveUser(UserRegisterRequest request) throws RoleNotFoundException;

    List<UserInfoResponse> findAll();
}
