import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class TestGettingOrdersList {

    private User user;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.getDefault();
        userClient = new UserClient();
    }

    @Test
    public void testOrderListCanBeGettingIfUserLogin() {
        ValidatableResponse response = userClient.create(user);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        accessToken = loginResponse.extract().path("accessToken");

        ValidatableResponse gettingOrdersListResponse = OrderRequest.gettingUserOrdersList(accessToken);

        int statusCode = gettingOrdersListResponse.extract().statusCode();
        assertEquals(SC_OK, statusCode);
    }

    @Test
    public void testOrderListCantBeGettingIfUserNotLogin() {

        ValidatableResponse gettingOrdersListResponse = OrderRequest.gettingUserOrdersListWithoutLogin();

        int statusCode = gettingOrdersListResponse.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED, statusCode);
    }

    @After
    public void cleanUpUser() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

}
