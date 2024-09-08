package com.hipradeep.user.services.controllers;

import com.hipradeep.user.services.entities.Hotel;
import com.hipradeep.user.services.entities.Rating;
import com.hipradeep.user.services.entities.User;
import com.hipradeep.user.services.exceptions.ResourceNotFoundException;
import com.hipradeep.user.services.external.services.HotelService;
import com.hipradeep.user.services.repositories.UserRepository;
import com.hipradeep.user.services.services.UserService;
import com.hipradeep.user.services.utils.AppConfig;
import com.hipradeep.user.services.utils.HeaderUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    private Logger logger = LoggerFactory.getLogger(UserController.class);

    //SIMPLEWAY123
    @Autowired
    private RestTemplate restTemplate;

    //SIMPLEWAY124
    @Autowired
    private HotelService hotelService;

    @Autowired
    private HeaderUtil headerUtil;

    public UserController() {
    }

    //CREATE
    //@RequestBody to get all data through JSON (in JSON format)
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User user1 = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    //GET All Users
    @GetMapping
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
            String ratingUrl2="http://RATING-SERVICE/ratings/users/";
            Rating[] ratingArray = restTemplate.getForObject(ratingUrl2 + user.getUserId(), Rating[].class  );

            assert ratingArray != null;
            List<Rating> ratings = Arrays.stream(ratingArray).toList();

            logger.info("{} ", ratings);

            String hotelUrl= "http://localhost:8082/hotels/";
            String hotelUrl2= "http://HOTEL-SERVICE/hotels/";
            List<Rating> newRList = ratings.stream().map(rt -> {
                //SIMLEWAY123
               // Hotel hotelDetails = restTemplate.getForEntity(hotelUrl2 + rt.getHotelId(), Hotel.class).getBody();
                //SIMPLEWAY124
                Hotel hotelDetails = hotelService.getHotel(rt.getHotelId());
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
   // @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
   // @Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallback")
   // @RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId){

        logger.info("---retry--- {}", reTry);
        reTry++;

        User user = userService.getUser(userId);


        //SIMPLEWAY123
        // fetch user rating from RATING_SERVICE
        //http://localhost:8083/ratings/users/13f7594c-9b9f-4e77-95c7-5e1270deed20

        String ratingUrl="http://localhost:8083/ratings/users/";
        String ratingUrl2="http://RATING-SERVICE/ratings/users/";
        Rating[] ratingArray = restTemplate.getForObject(ratingUrl2 + user.getUserId(), Rating[].class  );


        assert ratingArray != null;
        List<Rating> ratings = Arrays.stream(ratingArray).toList();

        logger.info("{} ", ratings);
        String hotelUrl= "http://localhost:8082/hotels/";
        String hotelUrl2= "http://HOTEL-SERVICE/hotels/";
        List<Rating> newList=  ratings.stream().peek(rt -> {
            Hotel hotelDetails = restTemplate.getForEntity(hotelUrl2+rt.getHotelId(), Hotel.class).getBody();

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


    //GET Single User
    @GetMapping("u1/{userId}")
    public ResponseEntity<User> getSingleUserU1(@PathVariable String userId, @RequestHeader("loggedInUser") String userName) {
        logger.info("---username--- {}", userName);


        // Fetch the user details using the user service
        User user = userService.getUser(userId);

        // Return the user details in the response
        return ResponseEntity.ok(user);
    }

    //GET Single User
    @GetMapping("u2/{userId}")
    public ResponseEntity<User> getSingleUserU2(@PathVariable String userId,  HttpServletRequest request) {
        // Fetch the "loggedInUser" header using the utility method
        String userName = headerUtil.getHeaderValue(request, AppConfig.keyHeaderUsername);
        logger.info("---u21 username--- {}", userName);

        // Use request object
        String userNameK = request.getHeader(AppConfig.keyHeaderUsername);
        String emailK = request.getHeader(AppConfig.keyHeaderEmail);
        String passwordK = request.getHeader(AppConfig.keyHeaderPassword);
        String nameK = request.getHeader(AppConfig.keyHeaderName);
        logger.info("---u22 request username--- {}", userNameK);
        logger.info("---u22 request email--- {}", emailK);
        logger.info("---u22 request password--- {}", passwordK);
        logger.info("---u22 request name--- {}", nameK);

        // no need to request
        String userName3 = headerUtil.getHeaderValue(AppConfig.keyHeaderUsername);

        logger.info("---u23 username--- {}", userName3);


        // Fetch the user details using the user service
        User user = userService.getUser(userId);

        // Return the user details in the response
        return ResponseEntity.ok(user);
    }




}
