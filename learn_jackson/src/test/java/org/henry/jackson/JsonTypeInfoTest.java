package org.henry.jackson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
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
		String str = om.writeValueAsString(an);
		String expected = "{\"@class\":\"org.henry.jackson.JsonTypeInfoTest$Tiger\",\"name\":null}";
		Assert.assertEquals(str, expected);
		
		Animal t2 = om.readValue(expected, Tiger.class); 
		Assert.assertEquals(t2.getClass(), Tiger.class);
		Assert.assertEquals(t2, an);
	}
	
	/*
	 * Scenario 2: Want to use a different kind of type info in an collection, like List, Map, etc.
	 * This annotation is added either to a Class (which MUST be the base class of subtypes that are used); or to a property. 
	 * Interpretation differs in that when used for a property, information will apply to contents of container types (Lists, Maps, arrays).
	 */
	@Test
	public void testTypeInfoOnProperty() throws JsonGenerationException, JsonMappingException, IOException
	{
		Bird b = new Bird();
		b.name = "Linda";
		Tiger t = new Tiger();
		t.name = "Jim";
		b.getFriends().add(t);
		ObjectMapper om = new ObjectMapper();
		String str = om.writeValueAsString(b);
		String expected = "{\"@class\":\"org.henry.jackson.JsonTypeInfoTest$Bird\",\"name\":\"Linda\",\"friends\":[[\"JsonTypeInfoTest$Tiger\",{\"name\":\"Jim\"}]]}";
		Assert.assertEquals(str, expected);
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
	
	public static class Bird  extends Animal{
		@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.WRAPPER_ARRAY, property="type")
		private List<Animal> friends = new ArrayList<>();

		public List<Animal> getFriends() {
			return friends;
		}

		public void setFriends(List<Animal> friends) {
			this.friends = friends;
		} 
	}
	
}
