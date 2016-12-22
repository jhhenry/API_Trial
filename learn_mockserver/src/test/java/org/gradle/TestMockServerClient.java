package org.gradle;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import org.mockserver.client.server.MockServerClient;
import org.testng.annotations.Test;

public class TestMockServerClient {

	@Test
	public void f()
	{
		new MockServerClient("localhost", 1080)
        .when(
        		request()
                        .withMethod("POST")
                        .withPath("/login")
                        .withBody("{username: 'foo', password: 'bar'}")
        )
        .respond(
                response()
                        .withStatusCode(302)
                        .withCookie(
                                "sessionId", "2By8LOhBmaW5nZXJwcmludCIlMDAzMW"
                        )
                        .withHeader(
                                "Location", "https://www.mock-server.com"
                        )
        );
	}
}
