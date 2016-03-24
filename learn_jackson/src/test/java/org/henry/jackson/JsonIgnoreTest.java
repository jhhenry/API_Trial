package org.henry.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonIgnoreTest {
	
	@Test
	public void testSeri() throws JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper om = new ObjectMapper();
		User u = new User();
		u.age = 18;
		u.setName("John");
		u.setEmail("aa@gmail.com");
		String json = om.writeValueAsString(u);
		Assert.assertEquals(!json.contains("aa@gmail.com"), true);
		Assert.assertEquals(!json.contains("John"), true);
	}
	
	@Test void testDeseri() throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper om = new ObjectMapper();
		String json = "{\"age\":18, \"name\":\"John\", \"email\":\"john.smith@gmail.com\"}";
		User u = om.readValue(json, User.class);
		
		Assert.assertEquals(u.age, 18);
		Assert.assertEquals(u.name, null);
		Assert.assertEquals(u.email, null);
	}
	
	@JsonAutoDetect(getterVisibility=Visibility.NONE)
	private static class User
	{
		@JsonProperty("age")
		private int age;
		private String name;
		@JsonIgnore
		private String email;
		
		public String getEmail() {
			return email;
		}
		
		public void setEmail(String email) {
			this.email = email;
		}
		
		public String getName() {
			return name;
		}
		
		@JsonIgnore
		public void setName(String name) {
			this.name = name;
		}
		
	}
}
