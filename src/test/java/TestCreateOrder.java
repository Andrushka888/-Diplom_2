import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

public class TestCreateOrder {

    private User userSpecific;
    private UserClient userClient;
    private String accessToken;
    private String refreshToken;
    private OrderRequest orderRequest;

    @Before
    public void setUp() {
        userSpecific = UserGenerator.getSpecificUser();
        userClient = new UserClient();
        orderRequest = new OrderRequest();
    }

    @Test
    public void testOrderCanByCreatedIfUserAuthorized() {
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(userSpecific));
        accessToken = loginResponse.extract().path("accessToken");
        refreshToken = loginResponse.extract().path("refreshToken");

        ValidatableResponse orderCreateResponse = orderRequest.createOrderWithToken(accessToken);

        int statusCode = orderCreateResponse.extract().statusCode();
        assertEquals(SC_OK, statusCode);
        userClient.logout(refreshToken);
    }

    @Test
    public void testOrderCanByCreatedIfUserNotAuthorized() {
        ValidatableResponse orderCreateResponse = orderRequest.createOrderWithoutToken();

        int statusCode = orderCreateResponse.extract().statusCode();
        assertEquals(SC_OK, statusCode);
    }

    @Test
    public void testOrderCantByCreatedWithoutIngredients() {
        ValidatableResponse orderCreateResponse = orderRequest.createOrderWithoutIngredients();

        int statusCode = orderCreateResponse.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);
    }

    @Test
    public void testOrderCantByCreatedWithIncorrectIngredients() {
        ValidatableResponse orderCreateResponse = orderRequest.createOrderWithIncorrectIngredients();

        int statusCode = orderCreateResponse.extract().statusCode();
        assertEquals(SC_INTERNAL_SERVER_ERROR, statusCode);
    }

}
