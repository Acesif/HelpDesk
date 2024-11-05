package com.grs.helpdeskmodule.controller;

import com.grs.helpdeskmodule.dto.LoginRequest;
import com.grs.helpdeskmodule.dto.Response;
import com.grs.helpdeskmodule.dto.UserDTO;
import com.grs.helpdeskmodule.entity.User;
import com.grs.helpdeskmodule.service.UserService;
import com.grs.helpdeskmodule.utils.UserMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    /**
     * Creates a new user with the details provided in the UserDTO. Checks if a user with the same email
     * or phone number already exists, returning a conflict response if so.
     * Handles HTTP POST requests to the "/create" endpoint.
     *
     * @param userDto The data transfer object containing the new user's details.
     * @return A response containing the created user's details or a message if the user already exists.
     */
    @PostMapping("/create")
    public Response<UserDTO> createUser(@RequestBody UserDTO userDto){

        boolean existingUser = userService.findUserByEmail(userDto.getEmail()) != null || userService.findUserByPhoneNumber(userDto.getPhoneNumber()) != null;
        if (existingUser){
            return Response.<UserDTO>builder()
                    .status(HttpStatus.CONFLICT)
                    .message("User already exists")
                    .data(null)
                    .build();
        }

        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .officeId(userDto.getOfficeId())
                .designation(userDto.getDesignation())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();

        User createdUser = userService.save(user);
        UserDTO returnedDto = UserDTO.builder()
                .name(createdUser.getName())
                .email(createdUser.getEmail())
                .phoneNumber(createdUser.getPhoneNumber())
                .officeId(createdUser.getOfficeId())
                .designation(createdUser.getDesignation())
                .password("***")
                .createdOn(createdUser.getCreateDate())
                .build();
        return Response.<UserDTO>builder()
                .status(HttpStatus.CREATED)
                .message("User successfully created")
                .data(returnedDto)
                .build();
    }

    /**
     * Verifies user login credentials and returns a token or verification result.
     * Handles HTTP POST requests to the "/login" endpoint.
     *
     * @param loginRequest The login request object containing the user's login credentials.
     * @return A verification response, typically a token, if login is successful.
     */

    @PostMapping("/login")
    public String loginUser(@RequestBody LoginRequest loginRequest){
        return userService.verify(loginRequest);
    }

    /**
     * Retrieves a list of all users in the system.
     * Handles HTTP GET requests to the "/auth/all" endpoint.
     *
     * @return A response containing a list of all users or a message if no users are found.
     */

    @GetMapping("/auth/all")
    public Response<List<UserDTO>> findAllUsers(){
        List<User> userList = userService.findAll();
        List<UserDTO> userDTOList = userList.stream().map(UserMapper::maptoDTO).toList();
        return Response.<List<UserDTO>>builder()
                .status(HttpStatus.OK)
                .message("All users retrieved")
                .data(userDTOList)
                .build();
    }

    /**
     * Updates the details of an existing user based on the provided UserDTO and user ID.
     * If a field in the UserDTO is null, the existing value is retained.
     * Handles HTTP PUT requests to the "/auth/{id}" endpoint.
     *
     * @param userDto The data transfer object containing the updated user details.
     * @param id      The ID of the user to update.
     * @return A response containing the updated user details or a message if the user does not exist.
     */

    @PutMapping("/auth/{id}")
    public Response<UserDTO> updateUser(@RequestBody UserDTO userDto, @PathVariable("id") Long id){

        User existingUser = userService.findById(id);
        if (existingUser == null){
            return Response.<UserDTO>builder()
                    .status(HttpStatus.CONFLICT)
                    .message("User doesn't exists")
                    .data(null)
                    .build();
        }

        existingUser.setFlag(true);
        existingUser.setName(userDto.getName() == null ? existingUser.getName() : userDto.getName());
        existingUser.setEmail(userDto.getEmail() == null ? existingUser.getEmail() : userDto.getEmail());
        existingUser.setPhoneNumber(userDto.getPhoneNumber() == null ? existingUser.getPhoneNumber() : userDto.getPhoneNumber());
        existingUser.setOfficeId(userDto.getOfficeId() == null ? existingUser.getOfficeId() : userDto.getOfficeId());
        existingUser.setDesignation(existingUser.getDesignation());
        existingUser.setPassword(userDto.getPassword() == null ? existingUser.getPassword() : passwordEncoder.encode(userDto.getPassword()));

        User updatedUser = userService.update(existingUser);

        UserDTO updatedDTO = UserDTO.builder()
                .name(updatedUser.getName())
                .email(updatedUser.getEmail())
                .phoneNumber(updatedUser.getPhoneNumber())
                .officeId(updatedUser.getOfficeId())
                .designation(updatedUser.getDesignation())
                .password("***")
                .createdOn(updatedUser.getCreateDate())
                .build();

        return Response.<UserDTO>builder()
                .status(HttpStatus.CREATED)
                .message("User successfully updated")
                .data(updatedDTO)
                .build();
    }
}
