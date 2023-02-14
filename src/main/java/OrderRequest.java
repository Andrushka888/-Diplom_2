import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderRequest extends Client {

    public ValidatableResponse createOrderWithToken(String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .body("{\n\"ingredients\": [\"61c0c5a71d1f82001bdaaa6f\", \"61c0c5a71d1f82001bdaaa7a\", \"61c0c5a71d1f82001bdaaa79\"]\n}").log().all()
                .when()
                .post("/api/orders")
                .then().log().all();
    }

    public ValidatableResponse createOrderWithoutToken() {
        return given()
                .spec(getSpec())
                .body("{\n\"ingredients\": [\"61c0c5a71d1f82001bdaaa6f\", \"61c0c5a71d1f82001bdaaa7a\", \"61c0c5a71d1f82001bdaaa79\"]\n}").log().all()
                .when()
                .post("/api/orders")
                .then().log().all();
    }

    public ValidatableResponse createOrderWithoutIngredients() {
        return given()
                .spec(getSpec())
                .body("{\n\"ingredients\": []\n}").log().all()
                .when()
                .post("/api/orders")
                .then().log().all();
    }

    public ValidatableResponse createOrderWithIncorrectIngredients() {
        return given()
                .spec(getSpec())
                .body("{\n\"ingredients\": [131]\n}").log().all()
                .when()
                .post("/api/orders")
                .then().log().all();
    }

    public static ValidatableResponse gettingUserOrdersList(String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .when()
                .get("/api/orders")
                .then().log().all();
    }

    public static ValidatableResponse gettingUserOrdersListWithoutLogin() {
        return given()
                .spec(getSpec())
                .when()
                .get("/api/orders")
                .then().log().all();
    }
}
