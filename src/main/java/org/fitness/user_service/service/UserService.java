package org.fitness.user_service.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fitness.user_service.exception.DuplicateValueException;
import org.fitness.user_service.models.dto.DeleteResponseDTO;
import org.fitness.user_service.models.dto.JwtTokenResponse;
import org.fitness.user_service.models.dto.LoginRequestDTO;
import org.fitness.user_service.models.dto.UserDTO;
import org.fitness.user_service.models.entity.RefreshToken;
import org.fitness.user_service.models.entity.User;
import org.fitness.user_service.repository.UserRepository;
import org.fitness.user_service.util.JwtService;
import org.fitness.user_service.util.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public UserDTO createUser(User user){
        var existingUser = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (existingUser.isPresent()) {
            throw new DuplicateValueException("Username or email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    public JwtTokenResponse authenticateUser(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        // Cast the principal — no extra DB call needed
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.user();

        String token = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        JwtTokenResponse response = new JwtTokenResponse();
        response.setUsername(user.getUsername());
        response.setToken(token);
        response.setRefreshToken(refreshToken.getToken());
        response.setTokenType("Bearer");
        response.setValidUntil(jwtService.getExpirationDateFromToken(token));
        return response;
    }

    public DeleteResponseDTO deleteUser(UUID uuid) {
        User user = userRepository.findById(uuid).orElseThrow(
                () -> new EntityNotFoundException("User not found with id: " + uuid)
        );

        userRepository.delete(user);

        return new DeleteResponseDTO("User with %s deleted successfully".formatted(uuid));
    }

    public UserDTO getUserById(UUID uuid) {
        User user = userRepository.findById(uuid).orElseThrow(
                () -> new EntityNotFoundException("User not found with id: " + uuid)
        );

        return modelMapper.map(user, UserDTO.class);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User not found with email: " + email)
        );

        return modelMapper.map(user, UserDTO.class);
    }
}
