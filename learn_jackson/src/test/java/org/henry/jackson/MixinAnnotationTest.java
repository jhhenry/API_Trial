package org.henry.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MixinAnnotationTest
{

	@Test 
	public void testSeria() throws JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.getSerializationConfig().addMixInAnnotations(Rectangle.class, MixIn.class);
		objectMapper.getDeserializationConfig().addMixInAnnotations(Rectangle.class, MixIn.class);
		String value = objectMapper.writeValueAsString(new Rectangle(22,33));
		Assert.assertNotNull(value);
		Assert.assertEquals(value, "{\"width\":22,\"height\":33}");
	}
	
	@Test 
	public void testDeseria() throws JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		//objectMapper.getSerializationConfig().addMixInAnnotations(Rectangle.class, MixIn.class);
		objectMapper.getDeserializationConfig().addMixInAnnotations(Rectangle.class, MixIn.class);
		Rectangle value = objectMapper.readValue("{\"width\":22,\"height\":33}", Rectangle.class);
		Assert.assertEquals(value.h, 33);
		Assert.assertEquals(value.w, 22);
	}
	
	@Test 
	public void testDeseriaUsingModule() throws JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new MyModule());
		//objectMapper.getSerializationConfig().addMixInAnnotations(Rectangle.class, MixIn.class);
		Rectangle value = objectMapper.readValue("{\"width\":22,\"height\":33}", Rectangle.class);
		Assert.assertEquals(value.h, 33);
		Assert.assertEquals(value.w, 22);
	}
	
	/* Inner class must be static.*/
	static abstract class MixIn {
		public MixIn(@JsonProperty("width") int w, @JsonProperty("height") int h) { }

		// note: could alternatively annotate fields "w" and "h" as well -- if so, would need to @JsonIgnore getters
		@JsonProperty("width") abstract int getW(); // rename property
		@JsonProperty("height") abstract int getH(); // rename property
		@JsonIgnore abstract int getSize(); // we don't need it!

	}

	/* It represents a class in some third-party lib, which cannot be modified in any manner. */
	public static final class Rectangle {
		final private int w, h;
		public Rectangle(int w, int h) {
			this.w = w;
			this.h = h;
		}
		public int getW() { return w; }
		public int getH() { return h; }
		public int getSize() { return w * h; }
	} 
	
	
	public class MyModule extends SimpleModule
	{
		
	  public MyModule() {
	    super("ModuleName", new Version(0,0,1,null));
	  }
	  
	  @Override
	  public void setupModule(SetupContext context)
	  {
	    context.setMixInAnnotations(Rectangle.class, MixIn.class);
	    // and other set up, if any
	  }
	}
}
