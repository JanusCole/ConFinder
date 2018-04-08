package com.example.janus.confinder;

import com.example.janus.confinder.data.Convention;
import com.example.janus.confinder.data.RemoteConventionsAPI;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


@RunWith(JUnit4.class)
public class RetrofitTest {

    String retroFiteResponse = "[{\"@context\":\"http://schema.org/\",\"@type\":\"Event\",\"name\":\"Ikkicon\",\"startDate\":\"2018-12-29\",\"endDate\":\"2018-12-31\",\"url\":\"http://fancons.com/events/info.shtml/9192/Ikkicon_2018/\",\"location\":{\"@type\":\"Place\",\"name\":\"Renaissance Austin Hotel\",\"address\":{\"@type\":\"PostalAddress\",\"addressLocality\":\"Austin\",\"addressRegion\":\"TX\",\"postalCode\":\"78759\",\"addressCountry\":\"USA\"}}}]";

//    @Rule
//    public ActivityTestRule<MapsActivity> mActivityRule = new ActivityTestRule<>(
//            MapsActivity.class);

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testRetrofitWithNullResponse() throws Exception {

        MockWebServer mockWebServer = new MockWebServer();

        mockWebServer.enqueue(new MockResponse().setBody(""));

        RemoteConventionsAPI retrofitConsFromWeb = new RemoteConventionsAPI();
        retrofitConsFromWeb.setBaseURL(mockWebServer.url("").toString());
        List<Convention> conventionsFromWeb = retrofitConsFromWeb.getConventions();

        assertEquals(null, conventionsFromWeb);

    }

    @Test
    public void testRetrofitWithGoodResponse() throws Exception {

        MockWebServer mockWebServer = new MockWebServer();

        mockWebServer.enqueue(new MockResponse().setBody(retroFiteResponse));

        RemoteConventionsAPI retrofitConsFromWeb = new RemoteConventionsAPI();
        retrofitConsFromWeb.setBaseURL(mockWebServer.url("").toString());
        List<Convention> conventionsFromWeb = retrofitConsFromWeb.getConventions();
        Convention oneConvention = conventionsFromWeb.get(0);

        assertFalse(oneConvention == null);
        assertEquals("Ikkicon", oneConvention.getName());
        assertEquals("2018-12-29", oneConvention.getStartDate());
        assertEquals("Austin, USA", oneConvention.getAddress());
        assertEquals("http://fancons.com/events/info.shtml/9192/Ikkicon_2018/", oneConvention.getWebsite());

    }

    @After
    public void tearDown() throws Exception {
    }

}
