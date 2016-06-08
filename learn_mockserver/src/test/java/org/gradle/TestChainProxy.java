package org.gradle;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.notFoundResponse;
import static org.mockserver.model.HttpResponse.response;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.mockserver.integration.ClientAndProxy;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.mock.action.ExpectationCallback;
import org.mockserver.model.HttpCallback;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.Parameter;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestChainProxy {
	private ClientAndServer mockServer;
//	private ClientAndProxy proxy;
	private CloseableHttpClient httpclient;
	
	@BeforeClass
	public void setUp() {
		startMockServer();
		int proxyPort = 1090;
		startProxy(proxyPort);
		initClientWithProxy(proxyPort);
	}

	private void initClientWithProxy(int proxyPort) {
		HttpHost httpHost = new HttpHost("localhost", proxyPort);
		DefaultProxyRoutePlanner defaultProxyRoutePlanner = new DefaultProxyRoutePlanner(httpHost);
		httpclient = HttpClients.custom().setRoutePlanner(defaultProxyRoutePlanner).build();
	}

	private void startProxy(int portt) {
		new ClientAndProxy(portt, "localhost", 1080);
	}
	
	private void startMockServer()
	{
		mockServer = startClientAndServer(1080);
		mockServer.when(request().withMethod("GET").withPath("/manager/html")).callback(HttpCallback.callback().withCallbackClass("org.gradle.TestChainProxy$HttpExpectationCallback"));
	}

	@Test
	public void testWithoutQueryString() throws Exception
	{

		HttpGet httpGet = new HttpGet("http://localhost:8080/manager/html");
		httpGet.setHeader("Authorization", "Basic dG9tY2F0OndlbGNvbWUx");
		try(CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
			Assert.assertEquals(response1.getStatusLine().getStatusCode(), 200);
			Header[] headers = response1.getHeaders("Test");
			Assert.assertNotNull(headers);
			Assert.assertEquals(headers.length, 1);
			Assert.assertEquals(headers[0].getValue(), "noQueryString");
		}

	}
	
	@Test
	public void testQueryString() throws Exception
	{
		HttpGet httpGet = new HttpGet("http://localhost:8080/manager/html?qs1=value1");
		httpGet.setHeader("Authorization", "Basic dG9tY2F0OndlbGNvbWUx");
		
		try(CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
			Assert.assertEquals(response1.getStatusLine().getStatusCode(), 200);
			Header[] headers = response1.getHeaders("Test");
			Assert.assertNotNull(headers);
			Assert.assertEquals(headers.length, 1);
			Assert.assertEquals(headers[0].getValue(), "testQueryString");
		} 
	}
	
	@Test
	public void testNonDefinedPath() throws Exception
	{
		HttpGet httpGet = new HttpGet("http://localhost:8080/index.html");
		httpGet.setHeader("Authorization", "Basic dG9tY2F0OndlbGNvbWUx");
		
		try(CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
			Assert.assertEquals(response1.getStatusLine().getStatusCode(), 404);
		} 
	}
	
	public static class HttpExpectationCallback implements ExpectationCallback {

	    public static HttpResponse httpResponse = response().withStatusCode(200)
				.withCookie("sessionId",
						"2By8LOhBmaW5nZXJwcmludCIlMDAzMW")
				.withHeader("Location", "https://www.mock-server.com")
				.withHeader("Test", "testQueryString")
				.withBody("Hacked!");
	    
	    public static HttpResponse httpResponse2 = response().withStatusCode(200)
				.withCookie("sessionId",
						"2By8LOhBmaW5nZXJwcmludCIlMDAzMW")
				.withHeader("Location", "https://www.mock-server.com")
				.withHeader("Test", "noQueryString")
				.withBody("Hacked!");
				
	    @Override
	    public HttpResponse handle(HttpRequest httpRequest) {
	    	List<Parameter> qs = httpRequest.getQueryStringParameters();
	    	if (qs == null || qs.size() <= 0) {
	    		return httpResponse2;
	    	}
	    	for (Parameter q : qs) {
	    		if (q.getName().equals("qs1")) {
	    			return httpResponse;
	    		}
	    	}
	            return notFoundResponse();
	    }
	}
	
}
