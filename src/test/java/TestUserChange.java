import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

public class TestUserChange {
    private User user;
    private User userWithAnotherData;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.getDefault();
        userWithAnotherData = UserGenerator.getSpecificUserForChangeData();
        userClient = new UserClient();
    }

    @Test
    public void testUserCanChangeDataIfTheyRegistered() {
        userClient.create(user);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        accessToken = loginResponse.extract().path("accessToken");

        ValidatableResponse changeResponse = userClient.change(accessToken, UserCredentialsToChange.from(userWithAnotherData));
        int statusCode = changeResponse.extract().statusCode();

        assertEquals(SC_OK, statusCode);
    }

    @Test
    public void testUserCantChangeDataIfTheyNotRegistered() {

        accessToken = "123"; // Задан несуществующий токен, для имитации неавторизованного пользователя

        ValidatableResponse changeResponse = userClient.change(accessToken, UserCredentialsToChange.from(userWithAnotherData));
        int statusCode = changeResponse.extract().statusCode();

        assertEquals(SC_UNAUTHORIZED, statusCode);
    }

    @After
    public void cleanUpUser() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

}
