package org.henry.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonUnwrapped;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UnWrapperAnnotationTest
{
	private static final String JSON_STR = "{\"age\":44,\"first\":\"John\",\"last\":\"Smith\"}";
	@Test
	public void testWrite() throws JsonGenerationException, JsonMappingException, IOException
	{
		Parent p = new Parent();
		Name n = new Name();
		n.first = "John";
		n.last = "Smith";
		p.age = 44;
		p.name = n;
		ObjectMapper m = new ObjectMapper();
		String json = m.writeValueAsString(p);
		Assert.assertNotNull(json);
		Assert.assertEquals(json, JSON_STR);
	}
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void testRead() throws JsonGenerationException, JsonMappingException, IOException
	{
		
		ObjectMapper m = new ObjectMapper();
		Parent p = m.readValue(JSON_STR, Parent.class);
				
		Name n = p.name;
//		n.first = "John";
//		n.last = "Smith";
//		p.age = 44;
//		p.name = n;
	}

	public class Parent {
		public int age;
		@JsonUnwrapped
		public Name name;
	}
	public class Name {
		public String first, last;
	}

}
