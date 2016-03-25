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
import org.codehaus.jackson.map.ObjectMapper.DefaultTyping;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PolymophicTypeTest {

	@Test
	public void testGlobalDefaultTyping() throws JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper om = new ObjectMapper();
		om.enableDefaultTyping(DefaultTyping.NON_FINAL); // when it is enabled, if no other type info ann specified
		// ["org.henry.jackson.PolymophicTypeTest$Dog",{"name":"jiejie","barkVolume":70.0}] will be generated
		
		Dog dog = new Dog();
		dog.name = "jiejie";
		dog.barkVolume = 70.0;
		
		String dogStr = om.writeValueAsString(dog);
		Assert.assertNotNull(dogStr);
		Assert.assertEquals(dogStr, "{\"@class\":\"puppy\",\"name\":\"jiejie\",\"barkVolume\":70.0}");
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
	@JsonSubTypes(value = { @Type(value = Cat.class, name ="cat_cat") })
	static class Animal { // All animals have names, for our demo purposes...
		public String name;
		protected Animal() { }
	}

	@JsonTypeName("puppy")
	static class Dog extends Animal {
		public double barkVolume; // in decibels
		public Dog() { }
	}

	static class Cat extends Animal {
		public boolean likesCream;
		public int lives;
		public Cat() { }
	}
}
