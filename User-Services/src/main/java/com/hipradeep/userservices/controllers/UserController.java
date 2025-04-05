package com.hipradeep.userservices.controllers;

import com.hipradeep.userservices.entities.User;
import com.hipradeep.userservices.entities.UserProfile;
import com.hipradeep.userservices.exceptions.ResourceNotFoundException;
import com.hipradeep.userservices.repositories.UserRepository;
import com.hipradeep.userservices.services.FileService;
import com.hipradeep.userservices.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private FileService fileService;



    @Value("${project.image}")
    private String path;


    //CREATE
    //@RequestBody to get all data through JSON (in JSON format)
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User user1 = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    //GET All Users
    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    //GET Single User
    @GetMapping("/{userId}")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId){
        User user = userService.getUser(userId);

        return ResponseEntity.ok(user);
    }

    //UPDATE User
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userName, @RequestBody User user){
        User existingUser = this.userRepository.findByUserName(userName).orElseThrow(()->
                new ResourceNotFoundException("User not found with user id: "+userName));

        existingUser.setUserName(user.getUserName());
        existingUser.setEmail(user.getEmail());
        existingUser.setAbout(user.getAbout());

        User savedUser = this.userRepository.save(existingUser);

        return ResponseEntity.ok(savedUser);
    }

    //Delete User
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userName){

        User user = this.userRepository.findByUserName(userName).orElseThrow(()->
                new ResourceNotFoundException("User not found with id: "+userName));

        this.userRepository.delete(user);
        System.out.println("User deleted");
    }



    @PostMapping("/image/{userId}")
    public ResponseEntity<UserProfile> uploadUserProfile(@RequestParam("image") MultipartFile image,
                                                         @PathVariable String userId) throws IOException {

        String fileName = this.fileService.uploadImage(path, image);

        UserProfile userProfile=new UserProfile();
        userProfile.setProfileUrl(fileName);
        userProfile.setUserId(userId);

       // UserProfile updatedUser = this.userProfileService.uploadProfile(userProfile);
        UserProfile updatedUser = new UserProfile();
        //CommonDto commonDto=CommonDto.builder().fieldName1(fileName).fieldName2(userId).build();

        return new ResponseEntity<UserProfile>(updatedUser, HttpStatus.OK);
    }

}
