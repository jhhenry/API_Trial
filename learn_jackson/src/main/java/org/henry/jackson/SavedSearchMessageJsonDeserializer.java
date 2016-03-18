package org.henry.jackson;

import java.io.IOException;

import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.henry.jackson.SavedSearchMessage.TargetFilter;

public class SavedSearchMessageJsonDeserializer extends JsonDeserializer<SavedSearchMessage> {

	@Override
	public SavedSearchMessage deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException 
	{
		//			jp.getText();
		//			jp.getCurrentToken();
		//			SavedSearchMessage ss = jp.readValueAs(SavedSearchMessage.class);
		JsonToken token = jp.nextToken();
		while(token != null) {
			String name = jp.getCurrentName();
			String text = jp.getText();
			if (token == JsonToken.FIELD_NAME) {
				SavedSearchMessage msg = getMsg();
				JsonToken json;
				switch (name) {
				case "id" :
					json = jp.nextToken();
					msg.setId(jp.getText());
					break;
				case "queryStr" :
					json = jp.nextToken();
					msg.setQueryStr(jp.getText());
					break;
				case "parameters" :
					readParameters(jp);
					break;
				default:

				}
			}

			token = jp.nextToken();
		}

		return msg;
	}

	private void readParameters(JsonParser jp) throws JsonParseException, IOException
	{
		// array of {name, value, type}
		JsonToken token = jp.nextToken();
		if (token != JsonToken.START_ARRAY) {
			return; // unexpected: value of parameter should be an array
		}
		token = jp.nextToken();
		while (token != null && token != JsonToken.END_ARRAY)
		{
			if (token == JsonToken.FIELD_NAME) {
				String name = jp.getCurrentName();
				switch (name) {
				case "name":
					if ("TARGET_FILTER".equals(jp.nextTextValue())) {
						// read value
						readTargetFilters(jp);
					}
					break;
				default:
					break;
				}
			}
			token = jp.nextToken();
		}
	}

	private void readTargetFilters(JsonParser jp) throws JsonParseException, IOException
	{
		JsonToken token = jp.nextToken();
		while (token != JsonToken.END_OBJECT && token != null) {
			if (token == JsonToken.FIELD_NAME) {
				String name = jp.getCurrentName();
				switch (name) {
				case "value":
					token = jp.nextToken();
					String value = jp.getText();
					if (value != null) {
						value = StringEscapeUtils.unescapeJava(value);
						ObjectMapper om = new ObjectMapper();
						TargetFilter[] filters = om.readValue(value, TargetFilter[].class);
						getMsg().setFilters(filters);
					}
					break;
				default:
					break;
				}
			}


			token = jp.nextToken();
		}
	}

	private SavedSearchMessage msg;
	private SavedSearchMessage getMsg()
	{
		if (msg == null) {
			msg = new SavedSearchMessage();
		} 
		return msg;
	}

}