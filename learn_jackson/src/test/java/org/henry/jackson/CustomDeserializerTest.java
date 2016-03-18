package org.henry.jackson;

import java.io.IOException;

import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.henry.jackson.SavedSearchMessage.TargetFilter;
import org.stringtemplate.v4.ST;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CustomDeserializerTest
{
	@Test
	public void test() throws JsonParseException, JsonMappingException, IOException
	{
		ST st = new ST("{\r\n  \"id\": 2001,\r\n  "
				+ "\"parameters\": [\r\n\t{\r\n      \"name\": \"TARGET_FILTER\",\r\n      "
				+ "\"value\": \"[{\\\"filterType\\\":\\\"target\\\",\\\"filterId\\\":\\\"B7906C6DCB0403EBFB17BA272E164089\\\",\\\"filterName\\\":\\\"slc08twq.us.oracle.com\\\"}, "
				+ "{\\\"filterType\\\":\\\"target\\\",\\\"filterId\\\":\\\"B7A4F5A9BEB8D8E317DA447DB2B1821E\\\",\\\"filterName\\\":\\\"slc08twq.us.oracle.com\\\"}]\",\r\n"
				+ "\"type\": \"CLOB\"\r\n    }\r\n  ],\r\n  \"queryStr\": \"*\"\r\n}");
		String json = st.render();

		ObjectMapper om = new ObjectMapper();
		SavedSearchMessage ssm = om.readValue(json, SavedSearchMessage.class);
		TargetFilter[] filters = ssm.getFilters();
		Assert.assertNotNull(filters);
		Assert.assertEquals(filters.length, 2);
		Assert.assertEquals(ssm.getQueryStr(), "*");
		Assert.assertEquals(ssm.getId(), "2001");
	}

	@Test void testWithOtherParameters() throws JsonParseException, JsonMappingException, IOException
	{
		ST st = new ST("{\r\n  \"id\": 2001,\r\n  "
				+ "\"parameters\": [\r\n\t"
				+ "{\r\n      \"name\": \"TARGET_FILTER\",\r\n      "
				+ "\"value\": \"[{\\\"filterType\\\":\\\"target\\\",\\\"filterId\\\":\\\"B7906C6DCB0403EBFB17BA272E164089\\\",\\\"filterName\\\":\\\"slc08twq.us.oracle.com\\\"}, "
				+ "{\\\"filterType\\\":\\\"target\\\",\\\"filterId\\\":\\\"B7A4F5A9BEB8D8E317DA447DB2B1821E\\\",\\\"filterName\\\":\\\"slc08twq.us.oracle.com\\\"}]\",\r\n"
				+ "\"type\": \"CLOB\"\r\n    }\r\n  "
				+ ", {\r\n      \"name\": \"time\",\r\n      \"value\": \"{\\\"type\\\":\\\"relative\\\", \\\"duration\\\":\\\"60\\\", \\\"timeUnit\\\":\\\"MINUTE\\\"}\",\r\n      \"type\": \"STRING\"\r\n    }],\r\n  "
				+ "\"queryStr\": \"*\"\r\n}");
		String json = st.render();

		ObjectMapper om = new ObjectMapper();
		SavedSearchMessage ssm = om.readValue(json, SavedSearchMessage.class);
		TargetFilter[] filters = ssm.getFilters();
		Assert.assertNotNull(filters);
		Assert.assertEquals(filters.length, 2);
		Assert.assertEquals(ssm.getQueryStr(), "*");
		Assert.assertEquals(ssm.getId(), "2001");
	}

	@Test
	public void testArray() throws JsonParseException, JsonMappingException, IOException
	{
		String json = "[{\\\"filterType\\\":\\\"target\\\",\\\"filterId\\\":\\\"B7906C6DCB0403EBFB17BA272E164089\\\",\\\"filterName\\\":\\\"slc08twq.us.oracle.com\\\"}, "
				+ "{\\\"filterType\\\":\\\"target\\\",\\\"filterId\\\":\\\"B7A4F5A9BEB8D8E317DA447DB2B1821E\\\",\\\"filterName\\\":\\\"slc08twq.us.oracle.com\\\"}]";
		ObjectMapper om = new ObjectMapper();
		String un = StringEscapeUtils.unescapeJava(json);
		TargetFilter[] ssm = om.readValue(un, TargetFilter[].class);
		Assert.assertNotNull(ssm);
	}

}
