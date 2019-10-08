package gs.hexagonaldemo.springhexagonaldemo.controllers;

import gs.hexagonaldemo.springhexagonaldemo.models.ErrorMessage;
import gs.hexagonaldemo.springhexagonaldemo.models.User;
import gs.hexagonaldemo.springhexagonaldemo.ports.AddUserService;
import gs.hexagonaldemo.springhexagonaldemo.ports.GetUserService;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersRestController {

  private GetUserService getUserService;

  private AddUserService addUserService;

  @Autowired
  public UsersRestController(GetUserService getUserService, AddUserService addUserService) {
    this.getUserService = getUserService;
    this.addUserService = addUserService;
  }

  @RequestMapping(value = "/users", method = RequestMethod.GET)
  void getUsers() {
    getUserService.getAllUsers();
  }

  @RequestMapping(value = "/users/{userid}", method = RequestMethod.GET)
  ResponseEntity getUser(@PathVariable int userid) {
    Optional<User> possibleUser = getUserService.getUser(userid);
    return possibleUser
        .map(user -> new ResponseEntity(user, HttpStatus.OK))
        .orElseGet(
            () ->
                new ResponseEntity<>(
                    ErrorMessage.builder()
                        .message("User with id " + userid + " was not found")
                        .build(),
                    HttpStatus.NOT_FOUND));
  }

  @RequestMapping(value = "/users", method = RequestMethod.POST)
  ResponseEntity<String> addUser(@Valid @RequestBody User newUser) {
    int userId = addUserService.addUser(newUser);
    if (userId > 0) {
      return new ResponseEntity<>("{\"id\": " + userId + "}", HttpStatus.CREATED);
    }

    return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
  }
}
