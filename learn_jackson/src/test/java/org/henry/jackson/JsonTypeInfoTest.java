package org.henry.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectMapper.DefaultTyping;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonTypeInfoTest {
	
	private Tiger an;
	/*
	 * Scenario 1: Use the class name along with the package name
	 */
	@Test
	public void testDefaultTypingName() throws JsonGenerationException, JsonMappingException, IOException
	{
		an = new Tiger();
		ObjectMapper om = new ObjectMapper();
		om.enableDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE);
		String str = om.writeValueAsString(an);
		String expected = "{\"@class\":\"org.henry.jackson.JsonTypeInfoTest$Tiger\",\"name\":null}";
		Assert.assertEquals(str, expected);
		
		Animal t2 = om.readValue(expected, Tiger.class); 
		Assert.assertEquals(t2.getClass(), Tiger.class);
		Assert.assertEquals(t2, an);
		
	}
	@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
	public static class Animal {
		public String name;
		protected Animal() { }
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Animal other = (Animal) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
	}

	private static class Tiger extends Animal{}
	
}
