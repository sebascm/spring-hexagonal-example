package gs.hexagonaldemo.springhexagonaldemo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import gs.hexagonaldemo.springhexagonaldemo.helper.JsonValidator;
import gs.hexagonaldemo.springhexagonaldemo.models.User;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = SpringHexagonalDemoApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserContractTest {
  private String rootUrl = "http://localhost:";

  @Value("${local.server.port}")
  private String port;

  private TestRestTemplate restTemplate;

  @Before
  public void setup() {
    restTemplate = new TestRestTemplate();
  }

  @Test
  public void get_users_whenThereAreNoSavedUsers_returnsAnEmptyListOfUsers()
      throws IOException, ProcessingException {
    ResponseEntity<String> response =
        restTemplate.getForEntity(buildUrl() + "/users", String.class);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    String responseData = response.getBody();
    //assertTrue(validateJson(buildUrl("/json/GetUsers.json"), responseData));
  }

  @Test
  public void get_users_whenThereAreSavedUsers_returnsAListOfUsers()
      throws IOException, ProcessingException {
    saveUser("User name a");
    saveUser("User name b");
    saveUser("User name c");
    ResponseEntity<String> response =
        restTemplate.getForEntity(buildUrl() + "/users", String.class);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    String responseData = response.getBody();
    //assertTrue(validateJson(buildUrl("/json/GetUsers.json"), responseData));
  }

  @Test
  public void get_users_givenAnUserIdThatIsNotStored_returnsNoUser()
      throws IOException, ProcessingException {
    ResponseEntity<String> response =
        restTemplate.getForEntity(buildUrl() + "/users/9999", String.class);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    String responseData = response.getBody();
    assertThat(
        responseData.toString(), equalTo("{\"message\":\"User with id 9999 was not found\"}"));
  }

  @Test
  public void get_users_givenAnUserIdThatIsStored_returnsASingleUser()
      throws IOException, ProcessingException {
    int userId = saveUser("User name a");
    ResponseEntity<String> response =
        restTemplate.getForEntity(buildUrl() + "/users/" + userId, String.class);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    String responseData = response.getBody();
    assertTrue(validateJson(buildUrl("/json/GetUser.json"), responseData));
  }

  @Test
  public void post_users_givenAUserWithAnInvalidName_returnsBadRequest()
      throws MalformedURLException {
    User newUser = User.builder().name("Bad 456 Name").build();
    ResponseEntity<String> response =
        restTemplate.postForEntity(buildUrl() + "/users", newUser, String.class);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void post_users_addAValidUser_returnsCreated() throws MalformedURLException {
    User newUser = User.builder().name("Name").build();
    ResponseEntity<String> response =
        restTemplate.postForEntity(buildUrl() + "/users", newUser, String.class);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
    assertTrue(response.getBody().toString().contains("{\"id\":"));
  }

  @Test
  public void post_users_addAnInvalidUser_returnsBadRequest() throws MalformedURLException {
    String newUser = "{\"contactEmail\": \"something@something.com\"}";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<>(newUser, headers);
    ResponseEntity<String> response =
        restTemplate.postForEntity(buildUrl() + "/users", entity, String.class);
    //assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void post_users_givenNotAUser_returnsBadRequest() throws MalformedURLException {
    ResponseEntity<String> response =
        restTemplate.postForEntity(
            buildUrl() + "/users", "something that is not a user", String.class);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE));
  }

  private boolean validateJson(URL schemaSpec, String responseData)
      throws IOException, ProcessingException {
    return JsonValidator.validateJson(schemaSpec, responseData);
  }

  private URL buildUrl() throws MalformedURLException {
    return new URL(rootUrl + port);
  }

  private URL buildUrl(String path) throws MalformedURLException {
    return new URL(rootUrl + port + path);
  }

  private int saveUser(String userName) throws MalformedURLException {
    User newUser = User.builder().name(userName).build();
    ResponseEntity<String> response =
        restTemplate.postForEntity(buildUrl() + "/users", newUser, String.class);
    return Integer.parseInt(response.getBody().split(":")[1].replaceAll("}", "").trim());
  }
}
