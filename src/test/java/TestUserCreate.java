import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

public class TestUserCreate {

    private User user;
    private User userWithoutNecessaryField;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.getDefault();
        userWithoutNecessaryField = UserGenerator.getUserWithoutNecessaryField();
        userClient = new UserClient();
    }

    @Test
    public void testUserCanBeCreated() {
        ValidatableResponse response = userClient.create(user);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user)); // для логина, чтоб получить accessToken
        accessToken = loginResponse.extract().path("accessToken"); // Вытащить accessToken, что передать его в cleanUpUser, для удаления курьера после тестирования

        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK, statusCode); //  Переиспользовать код статуса из интерфейса HttpStatus
    }

    @Test
    public void testPreviouslyCreatedUserCantBeCreatedAgain() {
        ValidatableResponse responseFirstUser = userClient.create(user);
        ValidatableResponse responseSecondUser = userClient.create(user);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        accessToken = loginResponse.extract().path("accessToken");
        int statusCode = responseSecondUser.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCode);
    }

    @Test
    public void testUserCantBeCreatedWithoutNecessaryField() {
        ValidatableResponse response = userClient.create(userWithoutNecessaryField);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCode);
    }

    @After
    public void cleanUpUser() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

}
