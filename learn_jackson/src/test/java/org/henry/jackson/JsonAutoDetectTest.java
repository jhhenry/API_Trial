package org.henry.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonCreator;
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
		String json = "{\"age\":18, \"name\":\"John\"}";
		ObjectMapper om = new ObjectMapper();
		User u = om.readValue(json, User.class);
		Assert.assertNotNull(u);
		Assert.assertNotNull(u.name);
	}
	
	@JsonAutoDetect(value=JsonMethod.CREATOR)
	private static class User
	{
		private int age;
		private String name;
		
		@JsonCreator
		public User(@JsonProperty("age") int age, @JsonProperty("name") String name) {
			this.age = age;
			this.name = name;
		}
		
	}
}
