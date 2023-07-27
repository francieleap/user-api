package com.example.userapi.api.controller;

import com.example.userapi.api.dto.UserDTO;
import com.example.userapi.model.entity.User;
import com.example.userapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Log4j2
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria um novo usuário", tags = {"Users"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro durante o cadastro do usuário")
            }
    )
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO dto ){

        log.info("Criando usuário com cpf: {}", dto.getCpf());

        User entity = modelMapper.map( dto, User.class );

        entity = userService.save(entity);

        log.info("Usuário com cpf: {} criado com sucesso ", dto.getCpf());

        return new ResponseEntity<>(modelMapper.map(entity, UserDTO.class), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtem um usuário por id", tags = {"Users"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retorna um usuário com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    public ResponseEntity<UserDTO> getById(@PathVariable(value = "id") Long userId) {

        log.info("Obtendo o  usuário com id: {}", userId);

        UserDTO userDTO = userService.findById(userId)
                                    .map(user -> modelMapper.map(user, UserDTO.class))
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deleta um usuário por id", tags = {"Users"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    public void deleteById(@PathVariable(value = "id") Long userId) {

        log.info("Deletando usuário com id: {} ", userId);

        User entity = userService.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        userService.delete(entity);
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualiza usuário com id", tags = {"Users"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    public ResponseEntity<UserDTO> replace(@PathVariable(value = "id") Long userId, @Valid @RequestBody UserDTO dto){

        log.info("Atualizando usuário com id: {} ", userId);

        User entity = userService.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        entity = userService.replace(dto, entity);

        return new ResponseEntity<>(modelMapper.map(entity, UserDTO.class), HttpStatus.OK);
    }

    @PatchMapping("{id}")
    @Operation(summary = "Atualiza parcialmente usuário com id", tags = {"Users"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    public ResponseEntity<UserDTO> update(@PathVariable(value = "id") Long userId, @RequestBody UserDTO dto){

        log.info("Atualizando usuário parcialmente com id: {} ", userId);

        User entity = userService.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        entity = userService.update(dto, entity);

        return new ResponseEntity<>(modelMapper.map(entity, UserDTO.class), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Busca usuários por parâmetros", tags = {"Users"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuários buscados com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro durante a busca de usuários")
            }
    )
    public ResponseEntity<List<User>> find(UserDTO dto){

        log.info("Buscando listagem de usuários cadastrados");

        User filter = modelMapper.map(dto, User.class);

        List<User> result = userService.find(filter);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
