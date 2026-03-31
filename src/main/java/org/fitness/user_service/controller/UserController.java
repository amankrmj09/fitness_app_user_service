package org.fitness.user_service.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.fitness.user_service.models.dto.DeleteResponseDTO;
import org.fitness.user_service.models.dto.UserDTO;
import org.fitness.user_service.models.entity.User;
import org.fitness.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody User user) {
        UserDTO registeredUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteResponseDTO> deleteUser(@RequestParam UUID uuid) {
        DeleteResponseDTO deleteResponseDTO = userService.deleteUser(uuid);
        return ResponseEntity.status(HttpStatus.OK).body(deleteResponseDTO);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID uuid) {
        UserDTO userDTO = userService.getUserById(uuid);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO userDTO = userService.getUserByEmail(email);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/{uuid}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable UUID uuid) {
        boolean isValid = userService.validateUser(uuid);
        return ResponseEntity.status(HttpStatus.OK).body(isValid);
    }

}
