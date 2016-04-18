package org.henry.jackson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonSerializeTest 
{
	@Test
	public void testAlertErrorMessage() throws JsonGenerationException, JsonMappingException, IOException, ParseException
	{
		AlertErrorMessage msg = new AlertErrorMessage();
		msg.setMeId("2001");
		msg.setEntityType("etype");
		msg.setMetricGroupName("metric_group");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ROOT);
	    df.setTimeZone(TimeZone.getTimeZone("UTC"));
	    Date d = df.parse("2016-09-01T03:29:42.378Z");
		msg.setCollectionTimeUTC(d);
		msg.setCollectionName("collectionName");
		msg.setErrorMessage("error happened");
		
		ObjectMapper mapper =  new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_DEFAULT);
        //Internally Jackson uses df.clone to work around thread safety issues with dateformat. See StdDeserializationContext.getDateFormat and StdSerializer.defaultSerializeDateValue
        mapper.setDateFormat(df);
        mapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        
		String expected = "{\"meId\":\"2001\",\"entityType\":\"etype\",\"metricGroupName\":\"metric_group\",\"collectionTimeUTC\":\"2016-09-01T03:29:42.378Z\","
				+ "\"errorMessage\":\"error happened\",\"collectionName\":\"collectionName\",\"clearTimeUTC\":null,\"metricState\":\"ERROR\"}";
		String actual = mapper.writeValueAsString(msg);
		Assert.assertFalse(actual.contains("metricColumnNames"));
		Assert.assertTrue(actual.contains("clearTimeUTC"));
		Assert.assertTrue(actual.contains("metricState"));
		Assert.assertEquals(expected, actual);
	}
}
