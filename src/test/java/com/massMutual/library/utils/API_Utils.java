package com.massMutual.library.utils;

import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.decorators.request.RequestSpecificationDecorated;
import net.serenitybdd.rest.utils.RequestSpecificationDecoratedFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static net.serenitybdd.rest.SerenityRest.*;

import java.time.LocalDateTime;

@SerenityTest
public class API_Utils {

  private static String librarianToken;
  private static String studentToken;
  private static LocalDateTime librarianTokenTime;
  private static LocalDateTime studentTokenTime;
  private static String librarianEmail;
  private static String librarianPassword;
  private static String studentEmail;
  private static String studentPassword;

  static {
    librarianEmail = ConfigReader.getProperty("librarian.username");
    librarianPassword = ConfigReader.getProperty("librarian.password");
    studentEmail = ConfigReader.getProperty("student133");
    studentPassword = ConfigReader.getProperty("student133Password");
  }


  @BeforeAll
  public static void initAPI() {
    RestAssured.baseURI = ConfigReader.getProperty("base.url");
    RestAssured.basePath = ConfigReader.getProperty("base.path");
  }

  @AfterAll
  public static void closeAPI() {
    reset();
  }

  public static String getLibrarianToken() {
    if (
            librarianToken == null ||
            librarianTokenTime.isAfter(librarianTokenTime.plusMinutes(6L))
    ) {
      librarianTokenTime = LocalDateTime.now();
      librarianToken = login(librarianEmail, librarianPassword);
    }
    return librarianToken;
  }

  public static String getStudentToken() {
    if(
            studentToken == null ||
            studentTokenTime.isAfter(studentTokenTime.plusMinutes(6L))
    ){
      studentTokenTime = LocalDateTime.now();
      studentToken = login(studentEmail, studentPassword);
    }
    return studentToken;
  }

  protected static String login(String email, String password) {
    return given()
            .contentType(ContentType.URLENC)
            .formParam("email", email)
            .formParam("password", password)
            .when()
            .post("/login")
            .jsonPath().getString("token");
  }

  public static Object[][] getTokensForDecode() {
    return new Object[][]{
            {"librarian", getLibrarianToken(), 200},
            {"student", getStudentToken(), 200},
            {"invalid", "invalid token", 500}
    };
  }

  public static Object[][] getTokensForDashboardStats() {
    return new Object[][]{
            {"librarian", getLibrarianToken(), 200},
            {"student", getStudentToken(), 200},
            {"invalid", "invalid token", 401}
    };
  }
}
