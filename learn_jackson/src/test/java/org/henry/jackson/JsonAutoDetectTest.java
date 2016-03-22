package org.henry.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonAutoDetectTest
{
	@Test
	public void testJsonAutoDetectWithMethodCreator() throws JsonParseException, JsonMappingException, IOException
	{
		String json = "{\"age\":18, \"name\":\"John\", \"email\":\"john.smith@gmail.com\"}";
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
		String json = "{\"age\":18, \"name\":\"John\", \"email\":\"john.smith@gmail.com\"}";
		ObjectMapper om = new ObjectMapper();
		User2 u = om.readValue(json, User2.class);
		Assert.assertNotNull(u);
		Assert.assertEquals(u.name, "John");
		Assert.assertEquals(u.age, 19);
		Assert.assertEquals(u.email, null);
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

//		public void setEmail(String email) {
//			this.email = email;
//		}
		
	}
	
}
