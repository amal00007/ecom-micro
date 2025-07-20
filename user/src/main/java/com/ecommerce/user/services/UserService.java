package com.ecommerce.user.services;


import com.ecommerce.user.dto.AddressDTO;
import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.models.Address;
import com.ecommerce.user.models.User;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final List<User> userList = new ArrayList<User>();
    Long id = 0L;
    private final UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().
                map(this::mapToUserResponse).
                collect(Collectors.toList());
    }

    public boolean createUser(@RequestBody UserRequest userRequest) {
        //user.setId(id++);
        // userList.add(user);
        User user = new User();
        updateUserFromRequest(user, userRequest);
        User savedUser = userRepository.save(user);
        if(savedUser.getId()!=null)
            return true;
        else return false;
    }

    public List<UserResponse> getUser(String id) {
        // return userList.stream().filter(user -> user.getId().equals(id)).findFirst();
        return userRepository.findById(id).stream().map(this::mapToUserResponse).collect(Collectors.toList());
    }

    public boolean updateUser(String id, UserRequest userRequest) {
        /*return userList.stream().filter(u -> u.getId().equals(user.getId()))
                .findFirst().map(u -> {
                    u.setId(user.getId());
                    u.setFirstName(user.getFirstName());
                    u.setLastName(user.getLastName());
                    return true;
                }).orElse(false);*/
        return userRepository.findById(id).map(u -> {
            updateUserFromRequest(u, userRequest);
            userRepository.save(u);
            return true;
        }).orElse(false);

    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setUserRole(user.getUserRole());

        if (user.getAddress() != null) {
            AddressDTO addressDto = new AddressDTO();

            addressDto.setCity(user.getAddress().getCity());
            addressDto.setCountry(user.getAddress().getCountry());
            addressDto.setState(user.getAddress().getState());
            addressDto.setStreet(user.getAddress().getStreet());
            addressDto.setZip(user.getAddress().getZip());
            userResponse.setAddress(addressDto);


        }
        return userResponse;
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if (userRequest.getAddress() != null) {
            Address address = new Address();
            address.setCity(userRequest.getAddress().getCity());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setState(userRequest.getAddress().getState());
            address.setStreet(userRequest.getAddress().getStreet());
            address.setZip(userRequest.getAddress().getZip());
            user.setAddress(address);

        }

    }
}