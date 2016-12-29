package org.henry.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PolymophicTypeTest {

	/*
	 * Senario 1£º The raw class name without the package name is used.
	 */
	@Test
	public void testDefaultTypingName() throws JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper om = new ObjectMapper();
		
		class Tiger2 extends Animal {}
		
		Animal t = new Tiger2();
		t.name = "Robert";
		String tigerStr = om.writeValueAsString(t);
		Assert.assertNotNull(tigerStr);
		// by default, JsonSerialize$Type.Dynamic is used so the subclass is used; otherwise the Animal will be used
		String expected = "{\"@class\":\"PolymophicTypeTest$1Tiger2\",\"name\":\"Robert\"}"; 
		Assert.assertEquals(tigerStr, expected);
		
		// Error, cannot be deseri-ed whether Tiger or Animal is used in readValue.
//		Animal t2 = om.readValue(expected, Tiger.class); 
//		Assert.assertEquals(t2.getClass(), Tiger.class);
//		Assert.assertEquals(t2, t);
		
		/*
		 * FAQ
		 * Question: If I want to deserialize simple JSON values (Strings, integer / decimal numbers) into types other than supported by default, do I need to write a custom deserializer?
		 * Not necessarily. If the class to deserialize into has one of:
		 * Single-argument constructor with matching type (String, int/double), or
		 * Single-argument static method with name "valueOf()", and matching argument type 
		 * Jackson will use such method, passing in matching JSON value as argument.
		 */
		
	}
	
	/*
	 * Senario 2: To deseri the string that was serialized, @JsonSubTypes and @Type should be used to indicate a type name
	 */
	@Test
	public void testDefaultTypingNameWithPackage() throws JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper om = new ObjectMapper();
		
		Animal t = new Tiger();
		t.name = "Robert";
		String tigerStr = om.writeValueAsString(t);
		Assert.assertNotNull(tigerStr);
		String expected = "{\"@class\":\"Tiger_tiger\",\"name\":\"Robert\"}";
		Assert.assertEquals(tigerStr, expected);
		
		Animal t2 = om.readValue(expected, Tiger.class); 
		Assert.assertEquals(t2.getClass(), Tiger.class);
		Assert.assertEquals(t2, t);
	}
	
	@Test
	public void testGlobalDefaultTyping() throws JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper om = new ObjectMapper();
		//om.enableDefaultTyping(DefaultTyping.NON_FINAL); // when it is enabled, if no other type info ann specified
		// ["org.henry.jackson.PolymophicTypeTest$Dog",{"name":"jiejie","barkVolume":70.0}] will be generated
		
		Dog dog = new Dog();
		dog.name = "jiejie";
		dog.barkVolume = 70.0;
		
		String dogStr = om.writeValueAsString(dog);
		Assert.assertNotNull(dogStr);
		String expected = "{\"@class\":\"puppy\",\"name\":\"jiejie\",\"barkVolume\":70.0}";
		Assert.assertEquals(dogStr, expected);
		
		Animal t2 = om.readValue(expected, Dog.class); 
		Assert.assertEquals(t2.getClass(), Dog.class);
		Assert.assertEquals(t2, dog);
	}
	
	@Test
	public void testDeseri() throws JsonParseException, JsonMappingException, IOException
	{
		String json = "{\"@class\":\"cat_cat\",\"name\":\"luca\",\"likesCream\":true, \"lives\": 9}";
		ObjectMapper om = new ObjectMapper();
		Animal c = om.readValue(json, Animal.class);
		Assert.assertEquals(c.getClass(), Cat.class);
		Assert.assertEquals(c.name, "luca");
		Assert.assertEquals(((Cat) c).likesCream, true);
		Assert.assertEquals(((Cat) c).lives, 9);
	}

	public static class Zoo {
		public Animal animal;
	}

	@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="@class")
	@JsonSubTypes(value = { @Type(value = Cat.class, name ="cat_cat"), @Type(value = Tiger.class, name="Tiger_tiger") })
	static class Animal { // All animals have names, for our demo purposes...
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

	@JsonTypeName("puppy")
	static final class Dog extends Animal {
		public double barkVolume; // in decibels
		public Dog() { }
	}

	static class Cat extends Animal {
		public boolean likesCream;
		public int lives;
		public Cat() { }
	}
	
	static class Tiger extends Animal {
	}
}
