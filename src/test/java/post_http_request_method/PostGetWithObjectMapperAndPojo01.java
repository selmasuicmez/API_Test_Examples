package post_http_request_method;

import Utils.JsonUtil;
import base_urls.HerOkuAppBaseUrl;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;
import pojos.BookingDatesPojo;
import pojos.BookingPojo;
import pojos.BookingPostResponseBodyPojo;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class PostGetWithObjectMapperAndPojo01 extends HerOkuAppBaseUrl {

    /*
        Given
	        https://restful-booker.herokuapp.com/booking

	        {
                "firstname": "Selim",
                "lastname": "Ak",
                "totalprice": 11111,
                "depositpaid": true,
                "bookingdates": {
                    "checkin": "2021-09-09",
                    "checkout": "2021-09-21"
                 },
                 "additinalneeds": "Breakfast"
              }
        When
	 		I send POST Request to the Url
	    And
	        I send GET Request to the Url
	 	Then
	 		Status code is 200
	 	And
	 	    GET response body should be like {
                                                "firstname": "Selim",
                                                "lastname": "Ak",
                                                "totalprice": 11111,
                                                "depositpaid": true,
                                                "bookingdates": {
                                                    "checkin": "2020-09-09",
                                                    "checkout": "2020-09-21"
                                                },
                                                "additinalneeds": "Breakfast"
                                             }
     */
    @Test
    public void postGetWithObjectMapperAndPojo01(){
                    //TO SEND POST REQUEST WE DID 1.Step, 2.Step, 3.Step
        //1.Step: Set the URL
        spec.pathParam("first", "booking");

        //2.Step: Set the request body
        BookingDatesPojo bookingDates = new BookingDatesPojo("2020-09-09", "2020-09-21");
        BookingPojo requestBody = new BookingPojo("Selim", "Ak", 11111, true, bookingDates, "Breakfast");
        System.out.println(requestBody);

        //3.Step: Send the request and get the response
        Response response = given().spec(spec).contentType(ContentType.JSON).body(requestBody).when().post("/{first}");
        response.prettyPrint();

                    //CONVERT POST RESPONSE BODY TO JAVA OBJECT BY USING OBJECT MAPPER
        BookingPostResponseBodyPojo postResponseBodyInPojo = JsonUtil.convertJsonToJava(response.asString(), BookingPostResponseBodyPojo.class);
        System.out.println(postResponseBodyInPojo);

                    //FROM postResponseBodyInPojo WE GOT THE VALUE OF bookingId by using GETTER of bookingId
        Integer bookingId = postResponseBodyInPojo.getBookingid();

                    //BY USING bookingId I WILL SEND GET REQUEST IN 1.Step, 2.Step, and 3.Step
        //1.Step: Set the URL for GET Request
        spec.pathParams("first", "booking", "second", bookingId);

        //2.Step: Set the expected data
        //No need to create expected data because the data which is sent in the POST Request will be expected data

        //3.Step: Send the GET request and get the response
        Response response2 = given().spec(spec).when().get("/{first}/{second}");
        response2.prettyPrint();

                    //I CONVERT GET RESPONSE BODY TO JAVA OBJECT BY USING OBJECT MAPPER
        BookingPojo getResponseBodyInPojo = JsonUtil.convertJsonToJava(response2.asString(), BookingPojo.class);

                    //BY USING requestBody and getResponseBodyInPojo, I DID ASSERTIONS
        //4.Step: Do assertions
        assertEquals(200, response2.getStatusCode());

        assertEquals(requestBody.getFirstname(), getResponseBodyInPojo.getFirstname());
        assertEquals(requestBody.getLastname(), getResponseBodyInPojo.getLastname());
        assertEquals(requestBody.getTotalprice(), getResponseBodyInPojo.getTotalprice());
        assertEquals(requestBody.getDepositpaid(), getResponseBodyInPojo.getDepositpaid());
        assertEquals(requestBody.getBookingdates().getCheckin(), getResponseBodyInPojo.getBookingdates().getCheckin());
        assertEquals(requestBody.getBookingdates().getCheckout(), getResponseBodyInPojo.getBookingdates().getCheckout());
        assertEquals(requestBody.getAdditionalneeds(), getResponseBodyInPojo.getAdditionalneeds());

    }

}
