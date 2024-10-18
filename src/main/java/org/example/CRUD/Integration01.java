package org.example.CRUD;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Integration01 {

        RequestSpecification requestSpecification;
        ValidatableResponse validatableResponse;
        Response response;
        String token="";
        String bookingId ="";

        public String getToken()
        {
            String payload  = "{\n" +
                    "                    \"username\" : \"admin\",\n" +
                    "                    \"password\" : \"password123\"\n" +
                    "                }";

            RequestSpecification r= RestAssured.given();
            r.baseUri("https://restful-booker.herokuapp.com");
            r.basePath("/auth");
            r.contentType(ContentType.JSON).log().all();
            r.body(payload);

            Response response= r.when().post();

            ValidatableResponse validatableResponse= response.then();
            validatableResponse.statusCode(200);

            token= response.jsonPath().getString("token");
            System.out.println(token);
            return token;



        }
        public String getBookingID()
        {
            String payload_POST = "{\n" +
                "    \"firstname\" : \"Raja\",\n" +
                "    \"lastname\" : \"Pattanayak\",\n" +
                "    \"totalprice\" : 121,\n" +
                "    \"depositpaid\" : false,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2024-01-03\",\n" +
                "        \"checkout\" : \"2024-01-03\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Lunch\"\n" +
                "}";


            requestSpecification = RestAssured.given();
            requestSpecification.baseUri("https://restful-booker.herokuapp.com/");
            requestSpecification.basePath("/booking");
            requestSpecification.contentType(ContentType.JSON);
            requestSpecification.body(payload_POST).log().all();

            Response response = requestSpecification.when().post();

            // Get Validatable response to perform validation
            validatableResponse = response.then().log().all();
            validatableResponse.statusCode(200);

            bookingId = response.jsonPath().getString("bookingid");
            System.out.println(bookingId);
            return bookingId;

        }
        @Test(priority = 1)
        public void Test_Post_Detail()
        {
            token= getToken();
            String payloadPOST = "{\n" +
                    "    \"firstname\" : \"Raja\",\n" +
                    "    \"lastname\" : \"Pattanayak\",\n" +
                    "    \"totalprice\" : 121,\n" +
                    "    \"depositpaid\" : false,\n" +
                    "    \"bookingdates\" : {\n" +
                    "        \"checkin\" : \"2024-01-03\",\n" +
                    "        \"checkout\" : \"2024-01-03\"\n" +
                    "    },\n" +
                    "    \"additionalneeds\" : \"Lunch\"\n" +
                    "}";


            requestSpecification = RestAssured.given();
            requestSpecification.baseUri("https://restful-booker.herokuapp.com/");
            requestSpecification.basePath("/booking/" + bookingId);
            requestSpecification.contentType(ContentType.JSON);
            requestSpecification.cookie("token", token);
            requestSpecification.body(payloadPOST).log().all();

            Response response = requestSpecification.when().post();


            // Get Validatable response to perform validation
            validatableResponse = response.then().log().all();
            validatableResponse.statusCode(200);
        }
        @Test(priority = 2)
        public void test_Put_Details()
        {
            token = getToken();
            bookingId= getBookingID();

            String payloadPUT= "{\n" +
                    "    \"firstname\" : \"BAISHALI\",\n" +
                    "    \"lastname\" : \"PATTANAYAK\",\n" +
                    "    \"totalprice\" : 111,\n" +
                    "    \"depositpaid\" : false,\n" +
                    "    \"bookingdates\" : {\n" +
                    "        \"checkin\" : \"2024-01-01\",\n" +
                    "        \"checkout\" : \"2024-01-01\"\n" +
                    "    },\n" +
                    "    \"additionalneeds\" : \"Lunch\"\n" +
                    "}";


            requestSpecification = RestAssured.given();
            requestSpecification.baseUri("https://restful-booker.herokuapp.com/");
            requestSpecification.basePath("/booking/"+bookingId);
            requestSpecification.contentType(ContentType.JSON);
            requestSpecification.cookie("token",token);
            requestSpecification.body(payloadPUT);

            response = requestSpecification.when().put();


            // Get Validatable response to perform validation
            validatableResponse = response.then().log().all();
            validatableResponse.statusCode(200);
        }
        @Test(priority = 3)
        public void Test_Update_Details_Get()
        {
            //bookingId= getBookingID();
            requestSpecification = RestAssured.given();
            requestSpecification.baseUri("https://restful-booker.herokuapp.com");
            requestSpecification.basePath("/booking/" + bookingId).log().all();

            response = requestSpecification.when().get();

            validatableResponse = response.then().log().all();
            validatableResponse.statusCode(200);

            Assert.assertEquals(response.then().extract().path("firstname"), "BAISHALI");
            Assert.assertEquals(response.then().extract().path("lastname"), "PATTANAYAK");
            System.out.println(bookingId+" is updated");
        }

        @Test(priority =4)
        public void Test_Delete_Details()
        {

            requestSpecification = RestAssured.given();
            requestSpecification.baseUri("https://restful-booker.herokuapp.com/booking");
            requestSpecification.basePath("/" + bookingId);
            requestSpecification.contentType(ContentType.JSON);
            requestSpecification.cookie("token", token).log().all();

            response = requestSpecification.when().delete();

            validatableResponse = response.then().log().all();
            validatableResponse.statusCode(201);
            System.out.println(bookingId+" the details are deleted in here");

        }
    @Test(priority = 5)
    public void Test_verify_delete()
    {
        //bookingId= getBookingID();
        requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://restful-booker.herokuapp.com");
        requestSpecification.basePath("/booking/" + bookingId).log().all();

        response = requestSpecification.when().get();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(404);
        System.out.println(bookingId+"The details are deleted");

    }


}
