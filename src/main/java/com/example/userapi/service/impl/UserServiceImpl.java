package com.example.userapi.service.impl;

import com.example.userapi.api.dto.UserDTO;
import com.example.userapi.exception.BusinessException;
import com.example.userapi.model.entity.User;
import com.example.userapi.model.repository.UserRepository;
import com.example.userapi.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;

    public UserServiceImpl(UserRepository repository, ModelMapper modelMapper, Validator validator) {
        this.userRepository = repository;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @Override
    public User save(User user) {

        if( userRepository.existsByCpf(user.getCpf()) ){

            log.error("Já existe um usuário com o cpf {} informado!", user.getCpf());

            throw new BusinessException("Já existe um usuário com o cpf informado!");
        }

        if( userRepository.existsByEmail(user.getCpf()) ){

            log.error("Já existe um usuário com o email {} informado!", user.getEmail());

            throw new BusinessException("Já existe um usuário com o email informado!");
        }

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long userId) {

        return userRepository.findById(userId);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public User update(UserDTO userDTO, User user) {

        if (userDTO.getCpf() != null && !userDTO.getCpf().equals(user.getCpf())) {
            if (userRepository.existsByCpf(userDTO.getCpf())) {

                log.error("Já existe um usuário com o cpf {} informado!", userDTO.getCpf());

                throw new BusinessException("Já existe um usuário com o cpf informado!");
            }
        }

        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {

            if (userRepository.existsByEmail(userDTO.getEmail())) {

                log.error("Já existe um usuário com o email {} informado!", userDTO.getEmail());

                throw new BusinessException("Já existe um usuário com o email informado!");
            }
        }

        modelMapper.map(userDTO, user);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            throw new BusinessException(violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", ")));
        }

        return userRepository.save(user);
    }

    @Override
    public User replace(UserDTO userDTO, User user) {

        if (userDTO.getCpf() != null && !userDTO.getCpf().equals(user.getCpf())) {

            if (userRepository.existsByCpf(userDTO.getCpf())) {

                log.error("Já existe um usuário com o cpf {} informado!", userDTO.getCpf());

                throw new BusinessException("Já existe um usuário com o cpf informado!");
            }
        }

        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {

            if (userRepository.existsByEmail(userDTO.getEmail())) {

                log.error("Já existe um usuário com o email {} informado!", userDTO.getEmail());

                throw new BusinessException("Já existe um usuário com o email informado!");
            }
        }

        modelMapper.map(userDTO, user);

        return userRepository.save(user);
    }


    @Override
    public List<User> find(User filter) {

        Example<User> example = Example.of(filter,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
        );

        return userRepository.findAll(example);
    }

}
