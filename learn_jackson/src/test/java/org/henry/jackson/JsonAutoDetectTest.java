package org.henry.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
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
		Assert.assertNotNull(u.name);
		Assert.assertEquals(u.age, 18);
		// @JsonProperty can co-exist with and overrides the "CREATOR"
		Assert.assertNotNull(u.email);
	}
	
	@JsonAutoDetect(value=JsonMethod.CREATOR)
	// @JsonIgnoreProperties(ignoreUnknown=true)
	private static class User
	{
		private int age;
		private String name;
		@JsonProperty
		private String email;
		
		@JsonCreator
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
	
}
