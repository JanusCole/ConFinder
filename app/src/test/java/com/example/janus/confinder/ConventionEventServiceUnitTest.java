package com.example.janus.confinder;

import com.example.janus.confinder.data.ConventionEvent;
import com.example.janus.confinder.data.ConventionsEventService;
import com.example.janus.confinder.data.RemoteConventionsEventService;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConventionEventServiceUnitTest {

    String retroFiteResponse = "[{\"@context\":\"http://schema.org/\",\"@type\":\"Event\",\"name\":\"Ikkicon\",\"startDate\":\"2018-12-29\",\"endDate\":\"2018-12-31\",\"url\":\"http://fancons.com/events/info.shtml/9192/Ikkicon_2018/\",\"location\":{\"@type\":\"Place\",\"name\":\"Renaissance Austin Hotel\",\"address\":{\"@type\":\"PostalAddress\",\"addressLocality\":\"Austin\",\"addressRegion\":\"TX\",\"postalCode\":\"78759\",\"addressCountry\":\"USA\"}}}]";

    MockWebServer mockWebServer;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
    }

    @Test
    public void testRetrofitWithBadResponse() throws Exception {

        mockWebServer.enqueue(new MockResponse().setBody(""));

        RemoteConventionsEventService retrofitConsFromWeb = new RemoteConventionsEventService();
        retrofitConsFromWeb.setBaseURL(mockWebServer.url("").toString());

        retrofitConsFromWeb.getConventionEvents(new ConventionsEventService.ConventionEventsCallback() {
            @Override
            public void onConventionsComplete(List<ConventionEvent> conventions) {
                assertTrue(false);
            }

            @Override
            public void onNetworkError() {
                assertTrue(true);
            }
        });

    }

    @Test
    public void testRetrofitWithGoodResponse() throws Exception {

        mockWebServer.enqueue(new MockResponse().setBody(retroFiteResponse));

        RemoteConventionsEventService retrofitConsFromWeb = new RemoteConventionsEventService();
        retrofitConsFromWeb.setBaseURL(mockWebServer.url("").toString());
        retrofitConsFromWeb.getConventionEvents(new ConventionsEventService.ConventionEventsCallback() {
            @Override
            public void onConventionsComplete(List<ConventionEvent> conventions) {
                ConventionEvent oneConvention = conventions.get(0);

                assertFalse(oneConvention == null);
                assertEquals("Ikkicon", oneConvention.getName());
                assertEquals("2018-12-29", oneConvention.getStartDate());
                assertEquals("Austin, USA", oneConvention.getAddress());
                assertEquals("http://fancons.com/events/info.shtml/9192/Ikkicon_2018/", oneConvention.getWebsite());

            }

            @Override
            public void onNetworkError() {
                assertTrue(false);

            }
        });



    }

    @After
    public void tearDown() throws Exception {
    }


}