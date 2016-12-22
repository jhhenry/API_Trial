package org.gradle;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestMockServer {
	
	private ClientAndServer mockServer;
	
	@BeforeClass
	public void startProxy() {
	    mockServer = startClientAndServer(1080);
	 //   proxy = startClientAndProxy(1090);
	}
	
	@AfterClass
	public void stopProxy() {
	   // proxy.stop();
	    mockServer.stop();
	}
	
	@Test
	public void f() throws ClientProtocolException, IOException
	{
		new MockServerClient("localhost", 1080)
        .when(
        		request()
                        .withMethod("POST")
                        .withPath("/login")
                        //.withBody("username:foo\r\npassword:bar")
        )
        .respond(
                response()
                        .withStatusCode(200)
                        .withCookie(
                                "sessionId", "2By8LOhBmaW5nZXJwcmludCIlMDAzMW"
                        )
                        .withHeader(
                                "Location", "https://www.mock-server.com"
                        )
        );
		
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://localhost:1080/login");
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("username", "foo"));
		nvps.add(new BasicNameValuePair("password", "bar"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse response1 = httpclient.execute(httpPost);
		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the network socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally clause.
		// Please note that if response content is not fully consumed the underlying
		// connection cannot be safely re-used and will be shut down and discarded
		// by the connection manager. 
		try {
		    System.out.println(response1.getStatusLine());
		    response1.getAllHeaders();
		    HttpEntity entity1 = response1.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
		    EntityUtils.consume(entity1);
		    entity1.toString();
		} finally {
		    response1.close();
		}
	}
	

}
