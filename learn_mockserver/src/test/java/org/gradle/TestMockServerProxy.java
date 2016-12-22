package org.gradle;

import static org.mockserver.integration.ClientAndProxy.startClientAndProxy;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.verify.VerificationTimes.exactly;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.mockserver.integration.ClientAndProxy;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestMockServerProxy {
	
	private ClientAndProxy proxy;
	
	@BeforeClass
	public void startProxy() {
	    proxy = startClientAndProxy(1090);
	    
	}
	
	@AfterClass
	public void stopProxy() {
	    proxy.stop();
	}
	
	@Test
	public void f() throws ClientProtocolException, IOException
	{
		System.out.println(System.getProperty("http.proxyPort"));
		HttpHost httpHost = new HttpHost("localhost", 1090);
		DefaultProxyRoutePlanner defaultProxyRoutePlanner = new DefaultProxyRoutePlanner(httpHost);
		CloseableHttpClient httpclient = HttpClients.custom().setRoutePlanner(defaultProxyRoutePlanner).build();
		
		HttpGet httpGet = new HttpGet("http://localhost:8080/manager/html");
		httpGet.setHeader("Authorization", "Basic dG9tY2F0OndlbGNvbWUx");
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		
		try {
			
		    System.out.println(response1.getStatusLine());
		    response1.getAllHeaders();
		    HttpEntity entity1 = response1.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
		    EntityUtils.consume(entity1);
		    
		    proxy.dumpToLogAsJava();
		    
		    proxy.verify(
	                request()
	                        .withPath("/manager/html"),
	                exactly(1)
	        );
		} finally {
		    response1.close();
		}
	}
	

}
