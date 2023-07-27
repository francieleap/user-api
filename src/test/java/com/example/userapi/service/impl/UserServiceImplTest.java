package com.example.userapi.service.impl;

import com.example.userapi.api.dto.UserDTO;
import com.example.userapi.exception.BusinessException;
import com.example.userapi.model.entity.User;
import com.example.userapi.model.repository.UserRepository;
import com.example.userapi.service.UserService;
import com.example.userapi.util.UserUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.Validator;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.userapi.util.UserUtil.createNewUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class UserServiceImplTest {

    public UserService service;

    @MockBean
    public UserRepository repository;

    @MockBean
    public ModelMapper modelMapper;

    @MockBean
    public Validator validator;


    @BeforeEach
    public void setUp() {
        this.service = new UserServiceImpl(repository, modelMapper, validator);
    }

    @Test
    @DisplayName("Deve salvar um usuário com sucesso")
    public void shouldSaveUserWhenSuccessful() {
        User userToSaved = createNewUser();

        when(repository.existsByCpf(Mockito.anyString()) ).thenReturn(false);
        when(repository.save(userToSaved)).thenReturn(
                User.builder().id(1L)
                        .name("Franciele Ferreira")
                        .cpf("897.408.970-02")
                        .email("email@example.com")
                        .age(25)
                        .build()
        );

        User savedUser = service.save(userToSaved);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo(userToSaved.getName());
        assertThat(savedUser.getCpf()).isEqualTo(userToSaved.getCpf());
        assertThat(savedUser.getEmail()).isEqualTo(userToSaved.getEmail());
        assertThat(savedUser.getAge()).isEqualTo(userToSaved.getAge());
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um usuário com cpf duplicado")
    public void shouldNotSaveUserWithDuplicatedCpf(){

        User user = createNewUser();

        when(repository.existsByCpf(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.save(user));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Já existe um usuário com o cpf informado!");

        Mockito.verify(repository, Mockito.never()).save(user);

    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um usuário com email duplicado")
    public void shouldNotSaveUserWithDuplicatedEmail(){

        User user = createNewUser();

        when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.save(user));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Já existe um usuário com o email informado!");

        Mockito.verify(repository, Mockito.never()).save(user);

    }

    @Test
    @DisplayName("Deve obter um usuário por Id")
    public void shouldReturnUserById(){
        Long id = 1L;
        User user = createNewUser();
        user.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> foundUser = service.findById(id);

        assertThat(foundUser.isPresent() ).isTrue();
        assertThat(foundUser.get().getId()).isEqualTo(id);
        assertThat(foundUser.get().getName()).isEqualTo(user.getName());
        assertThat(foundUser.get().getCpf()).isEqualTo(user.getCpf());
        assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());
        assertThat(foundUser.get().getAge()).isEqualTo(user.getAge());
    }

    @Test
    @DisplayName("Deve retornar vazio ao tentar obter um usuário por Id inexistente na base")
    public void shouldNotReturnUserWithNonExistentId(){

        Long id = 1L;
        when( repository.findById(id) ).thenReturn(Optional.empty());

        Optional<User> user = service.findById(id);

        assertThat(user.isPresent() ).isFalse();

    }

    @Test
    @DisplayName("Deve deletar o usuário com o id informado.")
    public void shouldDelteUserByIdWhenSuccessful(){

        User user = new User();
        user.setId(1L);

        assertDoesNotThrow( () -> service.delete(user) );

        Mockito.verify(repository, Mockito.times(1)).delete(user);
    }


    @Test
    @DisplayName("Deve atualizar a idade de um usuario.")
    public void shouldUpdateUserAge(){

        Integer newAge = 60;

        UserDTO userDTO = UserDTO.builder().age(newAge).build();

        Long id = 1L;
        User updatingUser = User.builder().id(1L).build();

        User updatedUser = createNewUser();
        updatedUser.setId(id);

        when(repository.save(updatingUser)).thenReturn(updatedUser);

        doAnswer(invocation -> {
            UserDTO userDTOO = invocation.getArgument(0);
            updatedUser.setName(userDTOO.getName() != null ? userDTOO.getName(): updatedUser.getName());
            updatedUser.setCpf(userDTOO.getCpf() != null ? userDTOO.getCpf(): updatedUser.getCpf());
            updatedUser.setEmail(userDTOO.getEmail() != null ? userDTOO.getEmail(): updatedUser.getEmail());
            updatedUser.setAge(userDTOO.getAge() != null ? userDTOO.getAge(): updatedUser.getAge());
            return null;
        }).when(modelMapper).map(userDTO, updatingUser);

        User user = service.update(userDTO, updatingUser);

        assertThat(user.getId()).isEqualTo(updatedUser.getId());
        assertThat(user.getName()).isEqualTo(updatedUser.getName());
        assertThat(user.getCpf()).isEqualTo(updatedUser.getCpf());
        assertThat(user.getEmail()).isEqualTo(updatedUser.getEmail());
        assertThat(user.getAge()).isEqualTo(newAge);

    }

    @Test
    @DisplayName("Deve retornar exceção quando atualizar cpf já cadastrado.")
    public void shouldThrowExceptionWhenUpdatingUserCpfWithCpfExists(){

        User user = createNewUser();
        UserDTO userDTO = UserDTO.builder().cpf("148.031.650-41").build();

        when(repository.existsByCpf(Mockito.anyString()) ).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.update(userDTO, user));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Já existe um usuário com o cpf informado!");

        Mockito.verify(repository, Mockito.never()).save(user);

    }

    @Test
    @DisplayName("Deve retornar exceção quando atualizar cpf já cadastrado.")
    public void shouldThrowExceptionWhenUpdatingUserEmailWithEmailExists(){

        User user = createNewUser();
        UserDTO userDTO = UserDTO.builder().email("newemail@exampli.com.br").build();

        when(repository.existsByEmail(Mockito.anyString()) ).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.update(userDTO, user));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Já existe um usuário com o email informado!");

        Mockito.verify(repository, Mockito.never()).save(user);

    }

    @Test
    @DisplayName("Deve filtrar usuários pelo filtro informado")
    public void shouldFilterUserByFilter(){

        User user = createNewUser();

        List<User> lista = Collections.singletonList(user);

        when(repository.findAll(UserUtil.createFilterUser(user))).thenReturn(lista);

        List<User> filterdUsers = service.find(user);

        assertThat(filterdUsers).isNotEmpty();
        assertThat(filterdUsers).contains(user);
    }
}