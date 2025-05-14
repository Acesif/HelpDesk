package com.grs.helpdeskmodule.controller;

import com.grs.helpdeskmodule.dto.LoginRequest;
import com.grs.helpdeskmodule.dto.Response;
import com.grs.helpdeskmodule.dto.UserDTO;
import com.grs.helpdeskmodule.dto.UserInformation;
import com.grs.helpdeskmodule.entity.Office;
import com.grs.helpdeskmodule.entity.User;
import com.grs.helpdeskmodule.repository.OfficeRepository;
import com.grs.helpdeskmodule.service.UserService;
import com.grs.helpdeskmodule.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    private final UserUtils userUtils;
    private final OfficeRepository officeRepository;

    /**
     * Creates a new user with the details provided in the UserDTO. Checks if a user with the same email
     * or phone number already exists, returning a conflict response if so.
     * Handles HTTP POST requests to the "/create" endpoint.
     *
     * @param userDto The data transfer object containing the new user's details.
     * @return A response containing the created user's details or a message if the user already exists.
     */
    @PostMapping("/create")
    public Response<?> createUser(@RequestBody UserDTO userDto){

        boolean existingUser = userService.findUserByUsername(userDto.getUsername()) != null || userService.findUserByPhoneNumber(userDto.getPhoneNumber()) != null;
        if (existingUser){
            return Response.<UserDTO>builder()
                    .status(HttpStatus.CONFLICT)
                    .message("User already exists")
                    .data(null)
                    .build();
        }

        User user = User.builder()
                .name(userDto.getName())
                .username(userDto.getUsername())
                .office(officeRepository.findById(userDto.getOfficeId()).orElse(null))
                .designation(userDto.getDesignation())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();

        User createdUser = userService.save(user);
        LoginRequest loginRequest = LoginRequest.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .build();
        String token = userService.verify(loginRequest);

        Map<String,Object> response = new LinkedHashMap<>();
        response.put("name",createdUser.getName());
        response.put("email",createdUser.getName());
        response.put("phoneNumber",createdUser.getPhoneNumber());
        response.put("officeId",createdUser.getOffice() != null ? createdUser.getOffice().getId() : null);
        response.put("designation",createdUser.getDesignation());
        response.put("token", token);

        return Response.builder()
                .status(HttpStatus.CREATED)
                .message("User successfully created")
                .data(response)
                .build();
    }

    @PostMapping("/referrer-grs")
    public Response<?> loginFromGRS(@RequestBody UserDTO userDto){

        boolean existingUser = userService.findUserByUsername(userDto.getUsername()) != null;
        if (existingUser){
            LoginRequest loginRequest = LoginRequest.builder()
                                            .username(userDto.getUsername())
                                            .password(userDto.getPassword())
                                            .build();

            String token = userService.verify(loginRequest);
            User user = userService.findUserByUsername(loginRequest.getUsername());

            Map<String,Object> response = new LinkedHashMap<>();
            response.put("name",user.getName());
            response.put("username",user.getUsername());
            response.put("officeId",user.getOffice() != null ? user.getOffice().getId() : null);
            response.put("role",userDto.getRole());
            response.put("officeMinistryId",userDto.getOfficeMinistryId());
            response.put("officeOriginId",userDto.getOfficeOriginId());
            response.put("designation",userDto.getDesignation());
            response.put("employeeRecordId",userDto.getEmployeeRecordId());
            response.put("officeUnitOrganogramId",userDto.getOfficeUnitOrganogramId());
            response.put("layerLevel",userDto.getLayerLevel());
            response.put("officeNameBangla",userDto.getOfficeNameBangla());
            response.put("token", token);

            return Response.builder()
                    .status(HttpStatus.OK)
                    .message("Logged in successfully")
                    .data(response)
                    .build();
        } else {

            Office office = officeRepository.findById(userDto.getOfficeId()).orElse(
                    officeRepository.save(Office.builder()
                            .id(userDto.getOfficeId())
                            .office_layer_id(userDto.getOfficeMinistryId())
                            .office_origin_id(userDto.getOfficeOriginId())
                            .office_ministry_id(userDto.getOfficeMinistryId())
                            .office_name_eng(userDto.getOfficeNameBangla())
                            .office_unit_organogram_id(userDto.getOfficeUnitOrganogramId())
                            .build())
            );

            User user = User.builder()
                    .name(userDto.getName())
                    .username(userDto.getUsername())
                    .office(office)
                    .designation(userDto.getDesignation())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .build();

            User createdUser = userService.save(user);
            LoginRequest loginRequest = LoginRequest.builder()
                    .username(userDto.getUsername())
                    .password(userDto.getPassword())
                    .build();
            String token = userService.verify(loginRequest);

            Map<String,Object> response = new LinkedHashMap<>();
            response.put("name",createdUser.getName());
            response.put("username",createdUser.getUsername());
            response.put("officeId",createdUser.getOffice() != null ? user.getOffice().getId() : null);
            response.put("role",createdUser.getRole());
            response.put("officeMinistryId",createdUser.getOffice().getOffice_ministry_id());
            response.put("officeOriginId",createdUser.getOffice().getOffice_origin_id());
            response.put("designation",createdUser.getDesignation());
            response.put("employeeRecordId",createdUser.getEmployeeRecordId());
            response.put("officeUnitOrganogramId",createdUser.getOffice().getOffice_unit_organogram_id());
            response.put("layerLevel",createdUser.getOffice().getOffice_layer_id());
            response.put("officeNameBangla",createdUser.getOffice().getOffice_name_eng());
            response.put("token", token);

            return Response.builder()
                    .status(HttpStatus.OK)
                    .message("Logged in successfully")
                    .data(response)
                    .build();
        }

    }

    /**
     * Verifies user login credentials and returns a token or verification result.
     * Handles HTTP POST requests to the "/login" endpoint.
     *
     * @param loginRequest The login request object containing the user's login credentials.
     * @return A verification response, typically a token, if login is successful.
     */

    @PostMapping("/login")
    public Response<?> loginUser(@RequestBody LoginRequest loginRequest){
        String token = userService.verify(loginRequest);
        User user = userService.findUserByUsername(loginRequest.getUsername());

        Map<String,Object> response = new LinkedHashMap<>();
        response.put("name",user.getName());
        response.put("email",user.getName());
        response.put("phoneNumber",user.getPhoneNumber());
        response.put("officeId",user.getOffice() != null ? user.getOffice().getId() : null);
        response.put("designation",user.getDesignation());
        response.put("token", token);

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Successfully logged in")
                .data(response)
                .build();
    }

    @GetMapping("/refresh/{oldToken}")
    public Response<?> refreshToken(@PathVariable String oldToken) {
        String newAccessToken = userService.refreshAccessToken(oldToken);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("refreshToken", newAccessToken);

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Token refreshed successfully")
                .data(response)
                .build();
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
        List<UserDTO> userDTOList = userList.stream().map(userUtils::maptoDTO).toList();
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
        existingUser.setUsername(userDto.getUsername() == null ? existingUser.getUsername() : userDto.getUsername());
        existingUser.setPhoneNumber(userDto.getPhoneNumber() == null ? existingUser.getPhoneNumber() : userDto.getPhoneNumber());
        existingUser.setOffice(existingUser.getOffice());
        existingUser.setDesignation(existingUser.getDesignation());
        existingUser.setPassword(userDto.getPassword() == null ? existingUser.getPassword() : passwordEncoder.encode(userDto.getPassword()));

        User updatedUser = userService.update(existingUser);

        UserDTO updatedDTO = UserDTO.builder()
                .name(updatedUser.getName())
                .username(updatedUser.getUsername())
                .phoneNumber(updatedUser.getPhoneNumber())
                .officeId(updatedUser.getOffice() != null ? updatedUser.getOffice().getId() : null)
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
    @GetMapping("/{id}")
    public Response<UserInformation> findById(
            @PathVariable("id") Long id
    ){
        User user = userService.findById(id);
        UserInformation userInformation = UserInformation.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .designation(user.getDesignation())
                .officeId(user.getOffice() != null ? user.getOffice().getId() : null)
                .build();

        if (userInformation == null){
            return Response.<UserInformation>builder()
                    .status(HttpStatus.CONFLICT)
                    .message("User doesn't exists")
                    .data(null)
                    .build();
        }

        return Response.<UserInformation>builder()
                .status(HttpStatus.OK)
                .message("User data shown successfully")
                .data(userInformation)
                .build();
    }

    @GetMapping("/auth/extract")
    public Response<UserInformation> extractUser(Authentication authentication){
        UserInformation userInformation = userUtils.extractUserInformation(authentication);

        return Response.<UserInformation>builder()
                .status(HttpStatus.OK)
                .message("User information retrieved")
                .data(userInformation)
                .build();
    }
}
