package com.example.userapi.model.repository;

import com.example.userapi.model.entity.User;
import com.example.userapi.util.UserUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static com.example.userapi.util.UserUtil.createNewUser;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    @Transactional
    @DisplayName("Deve salvar um usuário com sucesso")
    void saveUserWhenSuccessful(){

        User userToSaved = createNewUser();

        User userSaved = this.repository.save(userToSaved);

        Assertions.assertThat(userSaved).isNotNull();

        Assertions.assertThat(userSaved.getId()).isNotNull();

        Assertions.assertThat(userSaved.getName()).isEqualTo(userToSaved.getName());
    }

    @Test
    @Transactional
    @DisplayName("Deve atualizar um usuário com sucesso")
    void updateUserWhenSuccessful(){

        User userToSaved = createNewUser();

        User userSaved = this.repository.save(userToSaved);

        userSaved.setName("Juliana Silva");

        User userUpdated = this.repository.save(userSaved);

        Assertions.assertThat(userUpdated).isNotNull();

        Assertions.assertThat(userUpdated.getId()).isNotNull();

        Assertions.assertThat(userUpdated.getName()).isEqualTo(userSaved.getName());
    }

    @Test
    @Transactional
    @DisplayName("Deve deletar um usuário com sucesso")
    public void deleteUserWhenSuccessful(){

        User userToSaved = createNewUser();

        User userSaved = this.repository.save(userToSaved);

        this.repository.delete(userSaved);

        Optional<User> userOptional = this.repository.findById(userSaved.getId());

        Assertions.assertThat(userOptional).isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("Deve obter um usuario por id.")
    public void findUserByIdWhenSuccessful(){

        User userToSaved = createNewUser();

        User userSaved = this.repository.save(userToSaved);

        Long userId = userSaved.getId();

        Optional<User> userFound = this.repository.findById(userId);

        Assertions.assertThat(userFound.isPresent()).isTrue();

    }

    @Test
    @Transactional
    @DisplayName("Deve obter uma lista de usuário informando um filtro vazio")
    public void listUsersWithEmptyFilter(){

        User userToSaved = createNewUser();

        User userSaved = this.repository.save(userToSaved);

        User user = new User();

        Example<User> filter =  UserUtil.createFilterUser(user);

        List<User> filterdUsers = this.repository.findAll(filter);

        Assertions.assertThat(filterdUsers)
                .isNotEmpty()
                .contains(userSaved);
    }

    @Test
    @Transactional
    @DisplayName("Deve obter uma lista de usuário informando um filtro preenchido")
    public void listUsersWithFilledFilter(){

        User userToSaved = createNewUser();
        userToSaved.setAge(30);

        User userSaved = this.repository.save(userToSaved);

        User user = User.builder().age(30).build();

        Example<User> filter =  UserUtil.createFilterUser(user);

        List<User> filterdUsers = this.repository.findAll(filter);

        Assertions.assertThat(filterdUsers)
                .isNotEmpty()
                .contains(userSaved);
    }

    @Test
    @Transactional
    @DisplayName("Deve retonar uma lista vazia ao informar um filtro que não correponde a nenhum usuário")
    public void listEmptyListWhenNotFoundUsersByFilledFilter(){

        User userToSaved = createNewUser();

        User userSaved = this.repository.save(userToSaved);

        User user = User.builder().age(30).build();

        Example<User> filter =  UserUtil.createFilterUser(user);

        List<User> filterdUsers = this.repository.findAll(filter);

        Assertions.assertThat(filterdUsers)
                .doesNotContain(userSaved)
                .isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("Deve retornar verdadeiro quando existir um usuario com o cpf informado")
    public void returnTrueWhenCpfExists(){

        String cpf = "748.531.580-30";
        User userToSaved = createNewUser();
        userToSaved.setCpf(cpf);

        this.repository.save(userToSaved);

        boolean exists = this.repository.existsByCpf(cpf);

        Assertions.assertThat(exists).isTrue();

    }

    @Test
    @Transactional
    @DisplayName("Deve retornar verdadeiro quando existir um usuario com o email informado")
    public void returnTrueWhenEmailExists(){

        String email = "email1@email.com.br";
        User userToSaved = createNewUser();
        userToSaved.setEmail(email);

        this.repository.save(userToSaved);

        boolean exists = this.repository.existsByEmail(email);

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    @Transactional
    @DisplayName("Deve retornar falso quando não existir um usuario com o cpf informado")
    public void returnFalseWhenCpfDoesntExists(){

        String cpf = "243.774.040-67";

        boolean exists = this.repository.existsByCpf(cpf);

        Assertions.assertThat(exists).isFalse();
    }

    @Test
    @Transactional
    @DisplayName("Deve retornar falso quando não existir um usuario com o email informado")
    public void returnFalseWhenEmailDoesntExists(){

        String email = "email2@email.com.br";

        boolean exists = this.repository.existsByEmail(email);

        Assertions.assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    void throwsConstraintViolationExceptionWhenSaveUserWithNameIsEmpty(){

        User userToSaved = createNewUser();
        userToSaved.setName(null);
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.repository.save(userToSaved));
    }
}