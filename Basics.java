import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.nio.file.Paths;

import  org.testng.Assert;

import Files.ReUsableMethod;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.io.IOException;
import java.nio.file.Files;

public class Basics 
{

	public static void main(String[] args) throws IOException 
	{
		// to convert content to string - 1st convert it into bytes data type then - converts bytes into string
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(new String (Files.readAllBytes(Paths.get("E:\\postmanRequest\\addplace.json")))).
		when().post("maps/api/place/add/json")
		.then().assertThat().log().all().statusCode(200).body("scope",equalTo ("APP")).extract().response().asString();
		
		System.out.println(response);
		
		// Add place -> Update place with new address -> Get place to validate if new address is present in response
		
		JsonPath js = new JsonPath(response);   //parsing json
		String placeId = js.getString("place_id");
		System.out.println(placeId);
		
		//update place
		
		String newaddress = "Summer walk,Afrika";
		
	     given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
	     .body("{\r\n"
	     		+ "\"place_id\":\""+placeId+"\",\r\n"
	     		+ "\"address\":\""+newaddress+"\",\r\n"
	     		+ "\"key\":\"qaclick123\"\r\n"
	     		+ "}")
	     .when().put("maps/api/place/update/json")
	     .then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
	  
	     
		//Get updated address
	     
		String getplaceresponse = given().log().all().queryParam("key", "qaclick123")
		.queryParam("place_id", placeId)
		.when().get("maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
		
		JsonPath js1 = ReUsableMethod.rowToJson(getplaceresponse);
		String actualaddress = js1.getString("address");
	    System.out.println(actualaddress);
	    Assert.assertEquals(actualaddress, newaddress);
		
	}
	
}
