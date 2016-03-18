package org.henry.jackson;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonDeserialize(using = SavedSearchMessageJsonDeserializer.class)
public class SavedSearchMessage 
{
	private String id;
	private String queryStr;
	
	private TargetFilter[] filters;
	
	public String getQueryStr()
	{
		return queryStr;
	}
	public void setQueryStr(String queryStr)
	{
		this.queryStr = queryStr;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	
	public TargetFilter[] getFilters() {
		return filters;
	}
	public void setFilters(TargetFilter[] filters) {
		this.filters = filters;
	}

	public static class TargetFilter {
		private String filterType;
		private String filterId;
		private String filterName;
		
		public String getFilterType() {
			return filterType;
		}

		public void setType(String type) {
			this.filterType = type;
		}

		public String getFilterName() {
			return filterName;
		}

		public void setFilterName(String filterName) {
			this.filterName = filterName;
		}

		public String getFilterId() {
			return filterId;
		}

		public void setFilterId(String value) {
			this.filterId = value;
		}

	}
}
