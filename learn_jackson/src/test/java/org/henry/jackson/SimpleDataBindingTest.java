package org.henry.jackson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SimpleDataBindingTest {
	@Test
	public void test() throws JsonGenerationException, JsonMappingException, IOException
	{
		Map<String,Object> userData = new HashMap<String,Object>();
		Map<String,String> nameStruct = new HashMap<String,String>();
		nameStruct.put("first", "Joe");
		nameStruct.put("last", "Sixpack");
		userData.put("name", nameStruct);
		userData.put("gender", "MALE");
		userData.put("verified", Boolean.FALSE);
		userData.put("userImage", "Rm9vYmFyIQ==");
		
		ObjectMapper om = new ObjectMapper();
		String str = om.writeValueAsString(userData);
		
		String expected = "{\"userImage\":\"Rm9vYmFyIQ==\",\"gender\":\"MALE\",\"name\":{\"last\":\"Sixpack\",\"first\":\"Joe\"},\"verified\":false}";
		Assert.assertEquals(str, expected);
		
		Map<String,User> result = om.readValue("{\"joe sixpack\":" + expected + "}", new TypeReference<Map<String,User>>() { });
		Assert.assertTrue(result.size() == 1);
		String expectedKey = "joe sixpack";
		Assert.assertEquals(result.keySet().iterator().next(), expectedKey);
		User user = result.get(expectedKey);
		Assert.assertEquals(user.getName().getFirst(), "Joe");
	}
	
	public static class User {
	    public enum Gender { MALE, FEMALE };

	    public static class Name {
	      private String _first, _last;

	      public String getFirst() { return _first; }
	      public String getLast() { return _last; }

	      public void setFirst(String s) { _first = s; }
	      public void setLast(String s) { _last = s; }
	    }

	    private Gender _gender;
	    private Name _name;
	    private boolean _isVerified;
	    private byte[] _userImage;

	    public Name getName() { return _name; }
	    public boolean isVerified() { return _isVerified; }
	    public Gender getGender() { return _gender; }
	    public byte[] getUserImage() { return _userImage; }

	    public void setName(Name n) { _name = n; }
	    public void setVerified(boolean b) { _isVerified = b; }
	    public void setGender(Gender g) { _gender = g; }
	    public void setUserImage(byte[] b) { _userImage = b; }
	}
}
