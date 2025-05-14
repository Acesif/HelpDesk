package com.grs.helpdeskmodule.controller;

import com.grs.helpdeskmodule.dto.Response;
import com.grs.helpdeskmodule.dto.UserDTO;
import com.grs.helpdeskmodule.entity.Permissions;
import com.grs.helpdeskmodule.entity.Role;
import com.grs.helpdeskmodule.entity.User;
import com.grs.helpdeskmodule.repository.PermissionRepository;
import com.grs.helpdeskmodule.repository.RoleRepository;
import com.grs.helpdeskmodule.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/su")
@RequiredArgsConstructor
public class SuperadminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    /**
     * Updates the details of an existing user based on the provided UserDTO.
     * If a field is null in the UserDTO, the existing value is retained.
     * Handles HTTP PUT requests to the "/update/user" endpoint.
     *
     * @param userDTO The data transfer object containing the updated user details.
     * @return A response containing the updated user details or a message if the user is not found.
     */
    @PutMapping("/update/user")
    public Response<?> updateUser(@RequestBody UserDTO userDTO){

        User existingUser = userService.findUserByUsername(userDTO.getUsername());

        if (existingUser == null){
            return Response.<UserDTO>builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("User not found")
                    .data(null)
                    .build();
        }

        existingUser.setFlag(true);
        existingUser.setName(userDTO.getName() == null ? existingUser.getName() : userDTO.getName());
        existingUser.setUsername(userDTO.getUsername() == null ? existingUser.getUsername() : userDTO.getUsername());
        existingUser.setPhoneNumber(userDTO.getPhoneNumber() == null ? existingUser.getPhoneNumber() : userDTO.getPhoneNumber());
        existingUser.setOffice(existingUser.getOffice());
        existingUser.setDesignation(userDTO.getDesignation() == null ? existingUser.getDesignation() : userDTO.getDesignation());
        existingUser.setPassword(userDTO.getPassword() == null ? existingUser.getPassword() : passwordEncoder.encode(userDTO.getPassword()));

        User updatedUser = userService.update(existingUser);

        UserDTO updatedDTO = UserDTO.builder()
                .name(updatedUser.getName())
                .username(updatedUser.getUsername())
                .phoneNumber(updatedUser.getPhoneNumber())
                .officeId(updatedUser.getOffice() != null ? updatedUser.getOffice().getId() : null)
                .designation(updatedUser.getDesignation())
                .password(updatedUser.getPassword())
                .createdOn(updatedUser.getCreateDate())
                .build();

        return Response.<UserDTO>builder()
                .status(HttpStatus.CREATED)
                .message("User successfully updated")
                .data(updatedDTO)
                .build();
    }

    /**
     * Retrieves a list of all available permissions in the system.
     * Handles HTTP GET requests to the "/permissions" endpoint.
     *
     * @return A response containing a list of permissions or a message if no permissions are found.
     */

    @GetMapping("/permissions")
    public Response<?> getPermissions(){
        List<Permissions> permissions = permissionRepository.findAll();

        if (permissions.isEmpty()){
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("No permissions found")
                    .data(permissions)
                    .build();
        }
        return Response.builder()
                .status(HttpStatus.OK)
                .message("List of permissions found")
                .data(permissions)
                .build();
    }

    /**
     * Edits the permissions associated with a specific role based on the provided role ID.
     * If the role is not found, returns a message indicating so.
     * Handles HTTP PUT requests to the "/role/edit/{role_id}" endpoint.
     *
     * @param id          The ID of the role to update.
     * @param requestBody A map containing the list of permission IDs to associate with the role.
     * @return A response indicating the success of the operation and the updated role details.
     */

    @PutMapping("/role/edit/{role_id}")
    public Response<?> editPermissions(
            @PathVariable("role_id") Long id,
            @RequestBody Map<String, List<Long>> requestBody
            ){
        Role role = roleRepository.findById(id).orElse(null);

        if (role == null){
            return Response.<UserDTO>builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Role not found")
                    .data(null)
                    .build();
        }

        List<Long> permissionList = requestBody.get("permissionList");
        List<Permissions> permissions = new ArrayList<>();

        for (Long i : permissionList){
            permissions.add(permissionRepository.findById(i).orElse(null));
        }

        role.setPermissions(permissions);
        Role savedRole = roleRepository.save(role);

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Permissions updated")
                .data(savedRole)
                .build();
    }

    /**
     * Retrieves details of a specific role based on its ID.
     * Handles HTTP GET requests to the "/role/{role_id}" endpoint.
     *
     * @param id The ID of the role whose details are to be retrieved.
     * @return A response containing the role details or a message if the role is not found.
     */

    @GetMapping("/role/{role_id}")
    public Response<?> getRoleDetails(@PathVariable("role_id") Long id){
        Role role = roleRepository.findById(id).orElse(null);

        if (role == null){
            return Response.<UserDTO>builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Role not found")
                    .data(null)
                    .build();
        }

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Details of role "+role.getRole()+" have been fetched")
                .data(role)
                .build();
    }
}
