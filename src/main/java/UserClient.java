import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {

    public ValidatableResponse create(User user) {

        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post("/api/auth/register")
                .then().log().all();

    }

    public ValidatableResponse login(UserCredentials userCredentials) {

        return given()
                .spec(getSpec())
                .body(userCredentials)
                .when()
                .post("/api/auth/login")
                .then().log().all();
    }

    public ValidatableResponse logout(String accessToken) {

        return given()
                .spec(getSpec())
                .body(accessToken)
                .when()
                .post("/api/auth/login")
                .then().log().all();
    }

    public ValidatableResponse change(String accessToken, UserCredentialsToChange userCredentialsToChange) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .body(userCredentialsToChange)
                .when()
                .patch("/api/auth/user")
                .then().log().all();
    }

    public ValidatableResponse delete(String accessToken) {

        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then().log().all();
    }
}


