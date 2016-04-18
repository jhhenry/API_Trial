package org.henry.jackson;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;


public class AlertErrorMessage extends SavedSearchResult
{
//	[
//	 {"meId":"entityId","entityType":"host","entityName":"slc03rpu.us.oracle.com",
//	"metricGroupName":"Network","collectionTimeUTC":"2015-09-22T23:52:08.793Z",
//	"errorMessage":"<Some Error>","collectionName":"NetworkLinux", "clearTimeUTC": null, metricState":"CLEAR|ERROR"}
//	]
	
	private String errorMessage;
	
	private String collectionName;
	
	@JsonSerialize(include=Inclusion.ALWAYS)
	private String clearTimeUTC = null;
	
	@JsonSerialize(include=Inclusion.ALWAYS)
	private String metricState = "ERROR";
	
	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getCollectionName()
	{
		return collectionName;
	}

	public void setCollectionName(String collectionName)
	{
		this.collectionName = collectionName;
	}

	public String getClearTimeUTC()
	{
		return clearTimeUTC;
	}

	public String getMetricState()
	{
		return metricState;
	}

}
