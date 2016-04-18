package org.henry.jackson;

import java.util.Date;
import java.util.List;

public class SavedSearchResult
{
	private String meId; // saved search entity id
	private String entityType; // saved search entity type>
	private String metricGroupName; // saved search result metric type
	private Date collectionTimeUTC; // collectionTimeUTC
	private List<String> metricColumnNames;
	private List<List<String>> metricColumnValues;

	public String getMeId()
	{
		return meId;
	}

	public void setMeId(String meId)
	{
		this.meId = meId;
	}

	public String getEntityType()
	{
		return entityType;
	}

	public void setEntityType(String entityType)
	{
		this.entityType = entityType;
	}

	public String getMetricGroupName()
	{
		return metricGroupName;
	}

	public void setMetricGroupName(String metricGroupName)
	{
		this.metricGroupName = metricGroupName;
	}

	public Date getCollectionTimeUTC()
	{
		return collectionTimeUTC;
	}

	public void setCollectionTimeUTC(Date collectionTimeUTC)
	{
		this.collectionTimeUTC = collectionTimeUTC;
	}

	public List<String> getMetricColumnNames()
	{
		return metricColumnNames;
	}

	public void setMetricColumnNames(List<String> metricColumnNames)
	{
		this.metricColumnNames = metricColumnNames;
	}

	public List<List<String>> getMetricColumnValues()
	{
		return metricColumnValues;
	}

	public void setMetricColumnValues(List<List<String>> metricColumnValues)
	{
		this.metricColumnValues = metricColumnValues;
	}

}
