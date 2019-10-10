package gs.hexagonaldemo.springhexagonaldemo.repositoryadapters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import gs.hexagonaldemo.springhexagonaldemo.models.User;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;

public class UserRepositoryAdapterTest {

  private UserRepositoryAdapter userRepositoryAdapter;

  @Before
  public void setup() {
    userRepositoryAdapter = new UserRepositoryAdapter();
  }

  @Test
  public void userRepository_whenThereAreNoStoredUsers_gettingAllUsersGivesEmptyList() {
    List<User> users = userRepositoryAdapter.getUsers();

    assertEquals(0, users.size());
  }

  @Test
  public void userRepository_givenAUserToAdd_itStoresTheUser() {
    User newUser = User.builder().name("A name").build();

    assertEquals(0, userRepositoryAdapter.getUsers().size());

    userRepositoryAdapter.addUser(newUser);

    assertEquals(1, userRepositoryAdapter.getUsers().size());
  }

  @Test
  public void userRepository_givenAUserIdToDelete_itDeletesTheUser() {
    User user = User.builder().name("A name").build();
    int idOfUserToDelete = userRepositoryAdapter.addUser(user);

    userRepositoryAdapter.deleteUser(idOfUserToDelete);

    assertEquals(0, userRepositoryAdapter.getUsers().size());
  }

  @Test
  public void
      userRepository_givenAUserIdToDelete_itDeletesTheCorrectUserWhenThereAreMultipleUsersStored() {
    User user1 = User.builder().name("A name 1").build();
    User user2 = User.builder().name("A name 2").build();
    User user3 = User.builder().name("A name 3").build();
    int idOfUserToDelete = userRepositoryAdapter.addUser(user1);
    userRepositoryAdapter.addUser(user2);
    userRepositoryAdapter.addUser(user3);

    userRepositoryAdapter.deleteUser(idOfUserToDelete);

    List<User> users = userRepositoryAdapter.getUsers();

    assertEquals(2, users.size());

    assertTrue(users.get(0).getId() != idOfUserToDelete);
    assertTrue(users.get(1).getId() != idOfUserToDelete);
  }

  @Test
  public void userRepository_givenAUserToAdd_theAddedUserIsGivenAnId() {
    User newUser = User.builder().name("A name").build();

    userRepositoryAdapter.addUser(newUser);

    List<User> users = userRepositoryAdapter.getUsers();

    assertTrue(users.get(0).getId() != 0);
  }

  @Test
  public void userRepository_allUsersInRepository_haveAnUnqiueId() {
    User user1 = User.builder().name("A name 1").build();
    User user2 = User.builder().name("A name 2").build();
    User user3 = User.builder().name("A name 3").build();
    int idOfUserToDelete = userRepositoryAdapter.addUser(user1);
    userRepositoryAdapter.addUser(user2);
    userRepositoryAdapter.addUser(user3);

    userRepositoryAdapter.deleteUser(idOfUserToDelete);

    User user4 = User.builder().name("A name 4").build();
    userRepositoryAdapter.addUser(user4);

    List<User> users = userRepositoryAdapter.getUsers();

    List<Integer> ids = users.stream().map(User::getId).distinct().collect(Collectors.toList());

    assertEquals(ids.size(), users.size());
  }

  @Test
  public void userRepository_givenAnIdThatIsNotStored_gettingAUserReturnsNoUser() {
    Optional<User> returnedUser = userRepositoryAdapter.getUser(1);

    assertFalse(returnedUser.isPresent());
  }

  @Test
  public void userRepository_givenAnIdThatIsStored_gettingAUserReturnsTheUserWithThatId() {
    User user1 = User.builder().name("A name 1").build();
    User user2 = User.builder().name("A name 2").build();
    User user3 = User.builder().name("A name 3").build();
    userRepositoryAdapter.addUser(user1);
    int idOfUserToGet = userRepositoryAdapter.addUser(user2);
    userRepositoryAdapter.addUser(user3);

    Optional<User> returnedUser = userRepositoryAdapter.getUser(idOfUserToGet);

    assertTrue(returnedUser.isPresent());
    assertEquals(returnedUser.get().getId(), idOfUserToGet);
    assertEquals("A name 2", returnedUser.get().getName());
  }
}
