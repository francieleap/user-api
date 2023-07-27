package com.example.userapi.api.controller;

import com.example.userapi.api.dto.UserDTO;
import com.example.userapi.exception.BusinessException;
import com.example.userapi.model.entity.User;
import com.example.userapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static com.example.userapi.util.UserUtil.createNewUser;
import static com.example.userapi.util.UserUtil.createNewUserDTO;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    static String USER_API = "/users";

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService service;

    @Test
    @DisplayName("Deve criar um usuario com sucesso.")
    public void shouldCreateUserSucessfull() throws Exception {

        UserDTO dto = createNewUserDTO();
        User savedUser = createNewUser();
        Long id = 1L;
        savedUser.setId(id);

        BDDMockito.given(service.save(Mockito.any(User.class))).willReturn(savedUser);

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(USER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect( status().isCreated())
                .andExpect( jsonPath("name").value(dto.getName()))
                .andExpect( jsonPath("cpf").value(dto.getCpf()))
                .andExpect( jsonPath("email").value(dto.getEmail()))
                .andExpect( jsonPath("age").value(dto.getAge()));

    }

    @Test
    @DisplayName("Deve retornar erro de validação quando não houver dados suficiente para criação do usuario.")
    public void shouldReturnErrorsWhenInvalidUser() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new UserDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(USER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errors", hasSize(4)));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar cadastrar um usuario com cpf já utilizado por outro.")
    public void shouldReturnErrorsWithDuplicatedCpf() throws Exception {

        UserDTO dto = createNewUserDTO();

        String json = new ObjectMapper().writeValueAsString(dto);
        String mensagemErro = "Já existe um usuário com o cpf informado!";

        BDDMockito.given(service.save(Mockito.any(User.class)))
                    .willThrow(new BusinessException(mensagemErro));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(USER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform( request )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(mensagemErro));

    }

    @Test
    @DisplayName("Deve obter informacoes de um usuario.")
    public void shouldReturnUserDetails() throws Exception{

        Long id = 1L;

        User user = createNewUser();
        user.setId(id);

        BDDMockito.given( service.findById(id) ).willReturn(Optional.of(user));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(USER_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("name").value(createNewUser().getName()))
            .andExpect(jsonPath("cpf").value(createNewUser().getCpf()))
            .andExpect(jsonPath("email").value(createNewUser().getEmail()))
            .andExpect(jsonPath("age").value(createNewUser().getAge()));
    }
    @Test
    @DisplayName("Deve deletar um usuario")
    public void shouldDeleteUser() throws Exception {

        BDDMockito.given(service.findById(anyLong())).willReturn(Optional.of(User.builder().id(1L).build()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(USER_API.concat("/" + 1));

        mvc.perform( request )
            .andExpect( status().isNoContent() );
    }

}
