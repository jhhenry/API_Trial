package org.gradle;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class PersonTest {
    @Test
    public void canConstructAPersonWithAName() {
        Person person = new Person("Larry");
        assertEquals("Larry", person.getName());
    }
    
    @Test
    public void jsonTest() throws JsonParseException, JsonMappingException, IOException
    {
    	ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
    	Person user = mapper.readValue("{\"name\":\"Larry\"}", Person.class);
    	assertEquals("Larry", user.getName());
    	
    	Map<String,Object> userData = new HashMap<String,Object>();
    	Map<String,String> nameStruct = new HashMap<String,String>();
    	nameStruct.put("first", "Joe");
    	nameStruct.put("last", "Sixpack");
    	userData.put("name", nameStruct);
    	userData.put("gender", "MALE");
    	userData.put("verified", Boolean.FALSE);
    	userData.put("userImage", "Rm9vYmFyIQ==");
    	
    	mapper.writeValue(new File("user-modified.json"), userData);
    }
}
