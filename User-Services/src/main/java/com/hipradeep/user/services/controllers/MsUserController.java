package com.hipradeep.user.services.controllers;

import com.hipradeep.user.services.entities.Hotel;
import com.hipradeep.user.services.entities.Rating;
import com.hipradeep.user.services.entities.User;
import com.hipradeep.user.services.entities.UserProfile;
import com.hipradeep.user.services.exceptions.ResourceNotFoundException;
import com.hipradeep.user.services.repositories.UserRepository;
import com.hipradeep.user.services.services.FileService;
import com.hipradeep.user.services.services.UserProfileService;
import com.hipradeep.user.services.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ms-users")
public class MsUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    private Logger logger = LoggerFactory.getLogger(MsUserController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private UserProfileService userProfileService;

    @Value("${project.image}")
    private String path;

    //SIMPLEWAY123
    @Autowired
    private RestTemplate restTemplate;


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

        //SIMPLEWAY123
        List<User> newList = allUsers.stream().map(user -> {
            // Specify the type for the ratings list
//            ResponseEntity<List<Ratings>> responseEntity = restTemplate.exchange(
//                    "http://localhost:8083/ratings/users/" + user.getUserId(),
//                    HttpMethod.GET,
//                    null,
//                    new ParameterizedTypeReference<>() {
//                    }
//            );
            String ratingUrl="http://localhost:8083/ratings/users/";
            Rating[] ratingArray = restTemplate.getForObject(ratingUrl + user.getUserId(), Rating[].class  );

            assert ratingArray != null;
            List<Rating> ratings = Arrays.stream(ratingArray).toList();

            logger.info("{} ", ratings);

            String hotelUrl= "http://localhost:8082/hotels/";
            List<Rating> newRList = ratings.stream().map(rt -> {
                //SIMLEWAY123
               Hotel hotelDetails = restTemplate.getForEntity(hotelUrl + rt.getHotelId(), Hotel.class).getBody();

                logger.info("{} ", hotelDetails);
                rt.setHotel(hotelDetails);

                return rt;
            }).collect(Collectors.toList());

            user.setRatings(newRList);
            return user;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(newList);
    }

    int reTry=0;
    //GET Single User
    @GetMapping("/{userId}")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId){
        User user = userService.getUser(userId);
        logger.info("retry ", reTry++);

        //SIMPLEWAY123
        // fetch user rating from RATING_SERVICE
        //http://localhost:8083/ratings/users/13f7594c-9b9f-4e77-95c7-5e1270deed20

        String ratingUrl="http://localhost:8083/ratings/users/";
        Rating[] ratingArray = restTemplate.getForObject(ratingUrl + user.getUserId(), Rating[].class  );


        assert ratingArray != null;
        List<Rating> ratings = Arrays.stream(ratingArray).toList();

        logger.info("{} ", ratings);
        String hotelUrl= "http://localhost:8082/hotels/";
        List<Rating> newList=  ratings.stream().peek(rt -> {
            Hotel hotelDetails = restTemplate.getForEntity(hotelUrl+rt.getHotelId(), Hotel.class).getBody();

            logger.info("{} ",hotelDetails);
            rt.setHotel(hotelDetails);

        }).toList();


        user.setRatings(newList);


        return ResponseEntity.ok(user);
    }
    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex) {
//        logger.info("Fallback is executed because service is down : ", ex.getMessage());

        ex.printStackTrace();

        User user = User.builder().email("dummy@gmail.com").name("Dummy").about("This user is created dummy because some service is down").userId("141234").build();
        return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
    }
    //UPDATE User
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User user){
        User existingUser = this.userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("User not found with user id: "+userId));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setAbout(user.getAbout());

        User savedUser = this.userRepository.save(existingUser);

        return ResponseEntity.ok(savedUser);
    }

    //Delete User
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId){

        User user = this.userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("User not found with id: "+userId));

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

        UserProfile updatedUser = this.userProfileService.uploadProfile(userProfile);

        //CommonDto commonDto=CommonDto.builder().fieldName1(fileName).fieldName2(userId).build();

        return new ResponseEntity<UserProfile>(updatedUser, HttpStatus.OK);
    }

}
