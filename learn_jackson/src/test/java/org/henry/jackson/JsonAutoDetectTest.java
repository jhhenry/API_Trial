package org.henry.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonAutoDetectTest
{
	private static final String TEST_JASON_1 = "{\"age\":18, \"name\":\"John\", \"email\":\"john.smith@gmail.com\"}";


	@Test
	public void testDefaultDeserialization() throws JsonParseException, JsonMappingException, IOException
	{
		String json = TEST_JASON_1;
		ObjectMapper om = new ObjectMapper();
		User4 u = om.readValue(json, User4.class);
		
		//om.setVisibilityChecker(om.getVisibilityChecker().withFieldVisibility(Visibility.ANY));
		Assert.assertNotNull(u);
		Assert.assertEquals(u.name, "John");
		Assert.assertEquals(u.age, 18);
		// @JsonProperty can co-exist with and overrides the "CREATOR"
		Assert.assertEquals(u.email, "john.smith@gmail.com");
	}
	
	
	@Test
	public void testJsonAutoDetectWithMethodCreator() throws JsonParseException, JsonMappingException, IOException
	{
		String json = TEST_JASON_1;
		ObjectMapper om = new ObjectMapper();
		User u = om.readValue(json, User.class);
		Assert.assertNotNull(u);
		Assert.assertEquals(u.name, "John");
		Assert.assertEquals(u.age, 18);
		// @JsonProperty can co-exist with and overrides the "CREATOR"
		Assert.assertEquals(u.email, "john.smith@gmail.com");
	}
	
	@Test
	public void testJsonAutoDetectWithCreatorVisibility() throws JsonParseException, JsonMappingException, IOException
	{
		String json = TEST_JASON_1;
		ObjectMapper om = new ObjectMapper();
		User2 u = om.readValue(json, User2.class);
		Assert.assertNotNull(u);
		Assert.assertEquals(u.name, "John");
		Assert.assertEquals(u.age, 19);
		Assert.assertEquals(u.email, null);
	}
	
	@Test
	public void testJsonAutoDetectWithVisibilityChecker() throws JsonParseException, JsonMappingException, IOException
	{
		String json = TEST_JASON_1;
		ObjectMapper om = new ObjectMapper();
		//om.setVisibilityChecker(om.getVisibilityChecker().withCreatorVisibility(Visibility.ANY).withFieldVisibility(Visibility.ANY));
		om.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		User3 u = om.readValue(json, User3.class);
		Assert.assertNotNull(u);
		Assert.assertEquals(u.name, "John");
		Assert.assertEquals(u.age, 18);
		Assert.assertEquals(u.email, "john.smith@gmail.com");
		
		try {
			om = new ObjectMapper();
			om.setVisibilityChecker(om.getVisibilityChecker().withCreatorVisibility(Visibility.NONE));
			u = om.readValue(json, User3.class);
			Assert.fail("should have thrown exception due to No suitable constructor found for type " );
		} catch (Exception ex) {
			Assert.assertEquals(ex.getClass(), JsonMappingException.class);
			//should read here;
		}
		
	}
	
	@JsonAutoDetect(value=JsonMethod.CREATOR)
	// @JsonIgnoreProperties(ignoreUnknown=true)
	private static class User
	{
		private int age;
		private String name;
		@JsonProperty
		private String email;
		
		public User(@JsonProperty("age") int age, @JsonProperty("name") String name) {
			this.age = age;
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
		
	}
	
	@JsonAutoDetect(creatorVisibility=Visibility.NON_PRIVATE)
	@JsonIgnoreProperties(ignoreUnknown=true)
	private static class User2
	{
		private int age;
		private String name;
//		@JsonProperty
		private String email;
		
//		@JsonCreator
		private User2(@JsonProperty("age") int age, @JsonProperty("name") String name) {
			this.age = age;
			this.name = name;
		}
		
		@JsonCreator
		public static User2 create(@JsonProperty("age") int age, @JsonProperty("name") String name) {
			User2 u = new User2(age + 1, name);
			return u;
		}

	}
	
	private static class User3
	{
		private int age;
		private String name;
		private String email;
		
		private User3(@JsonProperty("age") int age, @JsonProperty("name") String name) {
			this.age = age;
			this.name = name;
		}
	}
	
	
//	@JsonAutoDetect
	private static class User4
	{
		@JsonProperty("age")
		private int age;
		@JsonProperty("name")
		private String name;
		@JsonProperty("email")
		private String email;
		
	}
	
}
