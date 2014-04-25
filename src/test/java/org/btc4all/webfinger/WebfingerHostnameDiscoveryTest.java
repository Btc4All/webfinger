package org.btc4all.webfinger;

import org.apache.http.message.BasicStatusLine;
import org.btc4all.webfinger.helpers.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;

import static org.btc4all.webfinger.matchers.Matchers.hasHostnameMatching;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

/**
 * @author Kosta Korenkov <7r0ggy@gmail.com>
 */
@RunWith(Parameterized.class)
public class WebfingerHostnameDiscoveryTest extends AbstractWebfingerClientTest {

    private String resource;

    private String expectedHost;

    public WebfingerHostnameDiscoveryTest(String resource, String expectedHost) {
        this.resource = resource;
        this.expectedHost = expectedHost;
    }

    @Parameterized.Parameters(name = "{0}")
    public static java.util.Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                { "bob@example.com", "example.com" },
                { "bob@192.168.0.11", "192.168.0.11" },
                { "bob@[2001:db8:85a3:0:0:8a2e:370:7334]", "[2001:db8:85a3:0:0:8a2e:370:7334]" },
                { "bob@%D1%82%D0%B5%D1%81%D1%82.%D1%80%D1%84", "%D1%82%D0%B5%D1%81%D1%82.%D1%80%D1%84" },
                { "acct:bob@example.com", "example.com" },
                { "mailto:bob@example.com", "example.com" },
                { "bob.joe@example.com", "example.com" },
                { "http://example.com/bob", "example.com" },
                { "http://example.com/~bob", "example.com" },
                { "https://example.com/bob", "example.com" },
                { "http://discover.example.com/bob", "discover.example.com" },
                { "http://192.168.0.11/bob", "192.168.0.11" },
                { "http://[2001:db8:85a3:0:0:8a2e:370:7334]/bob", "[2001:db8:85a3:0:0:8a2e:370:7334]" },
                { "http://%D1%82%D0%B5%D1%81%D1%82.%D1%80%D1%84/bob", "%D1%82%D0%B5%D1%81%D1%82.%D1%80%D1%84" },
        });
    }

    /**  RFC 7033 4, 8.2, 3.2.2 */
    @Test
    public void shouldDiscoverTargetHostFromURI() throws IOException {
        setUpToRespondWith(Response.NOT_FOUND);
        client.webFinger(resource);
        verify(mockHttpClient).execute(argThat(hasHostnameMatching(expectedHost)));
    }




}
