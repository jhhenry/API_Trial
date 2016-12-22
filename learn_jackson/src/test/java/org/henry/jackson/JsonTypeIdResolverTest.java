package org.henry.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonTypeIdResolver;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;
import org.codehaus.jackson.map.type.SimpleType;
import org.codehaus.jackson.type.JavaType;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonTypeIdResolverTest 
{
	@Test
	public void testDeseri() throws JsonParseException, JsonMappingException, IOException
	{
		String json = "{\"@class\":\"Abyssinian\", \"name\":\"luca\",\"likesCream\":true, \"lives\": 9}";
		ObjectMapper om = new ObjectMapper();
		Animal c = om.readValue(json, Animal.class);
		Assert.assertEquals(c.getClass(), Abyssinian.class);
		Assert.assertEquals(c.name, "luca");
		Assert.assertEquals(((Cat) c).likesCream, true);
		Assert.assertEquals(((Cat) c).lives, 9);
	}
	
	@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="@class")
//	@JsonSubTypes(value = { @Type(value = Cat.class, name ="cat_cat") })
	@JsonTypeIdResolver(value = MyTypeIdResolver.class)
	static class Animal { // All animals have names, for our demo purposes...
		public String name;
		protected Animal() { }
	}

	static class Dog extends Animal {
		public double barkVolume; // in decibels
		public Dog() { }
	}

	static class Cat extends Animal {
		public boolean likesCream;
		public int lives;
		public Cat() { }
	}
	
	static class Abyssinian extends Cat {
		
	}
	
	
	static class MyTypeIdResolver implements TypeIdResolver
	{

		@Override
		public void init(JavaType baseType) {
			// TODO Auto-generated method stub
			baseType.getTypeHandler();
		}

		@Override
		public String idFromValue(Object value) {
			if (value == null) return null;
			return null;
		}

		@Override
		public String idFromValueAndType(Object value, Class<?> suggestedType) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JavaType typeFromId(String id) {
			if ("Abyssinian".equalsIgnoreCase(id)) {
				return SimpleType.construct(Cat.class);
			} else if ("cat_cat".equalsIgnoreCase(id)){
				return SimpleType.construct(Cat.class);
			}
			return null;
		}

		@Override
		public Id getMechanism() {
			// TODO Auto-generated method stub
			return Id.CUSTOM;
		}
		
	}
}
