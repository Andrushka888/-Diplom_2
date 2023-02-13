import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class TestUserLogin {
    private User userSpecific;
    private User userSpecificWithoutRightAuthorisationData;
    private UserClient userClient;
    private String accessToken;
    private String refreshToken;


    @Before
    public void setUp() {
        userSpecific = UserGenerator.getSpecificUser();
        userSpecificWithoutRightAuthorisationData = UserGenerator.getSpecificUserWithNotRightLogin();
        userClient = new UserClient();
    }

    @Test
    public void TestLoginWithRightAuthorisationDataSuccessful() {
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(userSpecific));
        accessToken = loginResponse.extract().path("accessToken");
        refreshToken = loginResponse.extract().path("refreshToken");
        int statusCode = loginResponse.extract().statusCode();
        assertEquals(SC_OK, statusCode);
        userClient.logout(refreshToken);
    }

    @Test
    public void TestLoginWithoutRightAuthorisationDataNotSuccessful() {
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(userSpecificWithoutRightAuthorisationData));
        int statusCode = loginResponse.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED, statusCode);
    }
}