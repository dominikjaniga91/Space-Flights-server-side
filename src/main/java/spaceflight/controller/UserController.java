package spaceflight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spaceflight.model.User;
import spaceflight.service.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<User> saveNewUser(@RequestBody User user){
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUserById(@PathVariable String username){
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }

    @DeleteMapping("/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserById(@PathVariable String username){
        userService.deleteUserByUsername(username);
    }

    @PutMapping("/user")
    public ResponseEntity<User> updateUser(@RequestBody User user){

        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
    }

}
