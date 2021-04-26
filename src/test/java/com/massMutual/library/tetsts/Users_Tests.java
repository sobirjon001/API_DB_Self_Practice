package com.massMutual.library.tetsts;

import com.massMutual.library.utils.API_Utils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;

@SerenityTest
public class Users_Tests extends API_Utils {

//  @Disabled
  @DisplayName("POST /login test")
  @ParameterizedTest(name = "{index} ==> login with {0} credentials expected {3} code")
  @CsvSource({
          "librarian, librarian69@library, KNPXrm3S, 200",
          "student, student133@library, jrlJEWT0, 200",
          "invalid, invalid@mailrator, peace, 404"
  })
  public void login_test(
          String role,
          String email,
          String password,
          String code
  ) {
    login(email, password);
    Ensure.that(
            "expected login code is " + code,
            r -> r.statusCode(Integer.parseInt(code))
    );
  }

  @DisplayName("POST /decode")
  @ParameterizedTest(name = "{index} ==> testing with {0} credentials expected code {2}")
  @MethodSource("getTokensForDecode")
  public void testDecodeParameterized(
          String user,
          String token,
          int code
  ) {
    SerenityRest.given()
              .contentType(ContentType.URLENC)
              .formParam("token", token).
     when()
              .post("/decode").prettyPeek();
    Ensure.that("checking status code to be " + code, res -> res.statusCode(code));
  }

  @DisplayName("DET /dashboard_stats")
  @ParameterizedTest(name = "{index} ==> testing with {0} credentials expected code {2}")
  @MethodSource("getTokensForDashboardStats")
  public void testDashboardStatsParameterized(
          String user,
          String token,
          int code
  ) {
    SerenityRest.given()
            .header("x-library-token", token).
            when()
            .get("/dashboard_stats").prettyPeek();
    Ensure.that("checking status code to be " + code, res -> res.statusCode(code));

  }

}
