package com.example.janus.confinder;

import com.example.janus.confinder.data.ConventionEvent;
import com.example.janus.confinder.data.ConventionLocation;
import com.example.janus.confinder.data.ConventionLocatorService;
import com.example.janus.confinder.data.ConventionsEventService;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class ConventionFinderPresenterUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ConventionEvent mockConvention;

    @Mock
    private ConventionMapperContract.View mockConventionFinderView;

    @Mock
    private ConventionsEventService conventionsEventService;

    @Mock
    private ConventionLocatorService conventionLocatorService;


    // This is the Class under test
    private ConventionMapperContract.Presenter conventionMapperPresenter;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        conventionMapperPresenter = new ConventionMapperPresenter(mockConventionFinderView, conventionsEventService, conventionLocatorService);

    }

    @Test
    public void testGetConventionsNetworkError() throws Exception {

        conventionMapperPresenter.mapConventions();

        ArgumentCaptor<ConventionsEventService.ConventionEventsCallback> mGetConventionsCallbackCaptor =
                ArgumentCaptor.forClass(ConventionsEventService.ConventionEventsCallback.class);

        verify(conventionsEventService).getConventionEvents(mGetConventionsCallbackCaptor.capture());
        mGetConventionsCallbackCaptor.getValue().onNetworkError();

        verify(mockConventionFinderView).displayNetworkError();

    }


    @Test
    public void testGetConventionsOneFound() throws Exception {

        conventionMapperPresenter.mapConventions();

        List<ConventionEvent> mockConventionEvents = new ArrayList<>();
        mockConventionEvents.add(new ConventionEvent());

        ArgumentCaptor<ConventionsEventService.ConventionEventsCallback> conventionEventsCallbackArgumentCaptor =
                ArgumentCaptor.forClass(ConventionsEventService.ConventionEventsCallback.class);

        verify(conventionsEventService).getConventionEvents(conventionEventsCallbackArgumentCaptor.capture());
        conventionEventsCallbackArgumentCaptor.getValue().onConventionsComplete(mockConventionEvents);

        ConventionLocation mockConventionLocation = new ConventionLocation();

        ArgumentCaptor<ConventionLocatorService.ConventionLocatorServiceCallback> mGetConventionLocationCallbackCaptor =
                ArgumentCaptor.forClass(ConventionLocatorService.ConventionLocatorServiceCallback.class);

        ArgumentCaptor<List<ConventionEvent>> mConventionEventsCaptor =
                ArgumentCaptor.forClass(List.class);

        verify(conventionLocatorService).getConventionLocations(mConventionEventsCaptor.capture(), mGetConventionLocationCallbackCaptor.capture());
        assertEquals(mockConventionEvents, mConventionEventsCaptor.getValue());

        mGetConventionLocationCallbackCaptor.getValue().onConventionLocated(mockConventionLocation);

        verify(mockConventionFinderView).mapConvention(any(ConventionLocation.class));

    }

    @Test
    public void testGetConventionsMapComplete() throws Exception {

        conventionMapperPresenter.mapConventions();

        List<ConventionEvent> mockConventionEvents = new ArrayList<>();
        mockConventionEvents.add(new ConventionEvent());

        ArgumentCaptor<ConventionsEventService.ConventionEventsCallback> conventionEventsCallbackArgumentCaptor =
                ArgumentCaptor.forClass(ConventionsEventService.ConventionEventsCallback.class);

        verify(conventionsEventService).getConventionEvents(conventionEventsCallbackArgumentCaptor.capture());
        conventionEventsCallbackArgumentCaptor.getValue().onConventionsComplete(mockConventionEvents);

        ArgumentCaptor<ConventionLocatorService.ConventionLocatorServiceCallback> mGetConventionLocationCallbackCaptor =
                ArgumentCaptor.forClass(ConventionLocatorService.ConventionLocatorServiceCallback.class);

        ArgumentCaptor<List<ConventionEvent>> mConventionEventsCaptor =
                ArgumentCaptor.forClass(List.class);

        verify(conventionLocatorService).getConventionLocations(mConventionEventsCaptor.capture(), mGetConventionLocationCallbackCaptor.capture());
        assertEquals(mockConventionEvents, mConventionEventsCaptor.getValue());

        mGetConventionLocationCallbackCaptor.getValue().onConventionLocationsComplete();

        verify(mockConventionFinderView).completeMap();

    }

    @Test
    public void testGetConventionsEventError() throws Exception {

        conventionMapperPresenter.mapConventions();

        List<ConventionEvent> mockConventionEvents = new ArrayList<>();
        mockConventionEvents.add(new ConventionEvent());

        ArgumentCaptor<ConventionsEventService.ConventionEventsCallback> conventionEventsCallbackArgumentCaptor =
                ArgumentCaptor.forClass(ConventionsEventService.ConventionEventsCallback.class);

        verify(conventionsEventService).getConventionEvents(conventionEventsCallbackArgumentCaptor.capture());
        conventionEventsCallbackArgumentCaptor.getValue().onNetworkError();

        verify(mockConventionFinderView).displayNetworkError();

    }

    @Test
    public void testGetConventionLocationsError() throws Exception {

        conventionMapperPresenter.mapConventions();

        List<ConventionEvent> mockConventionEvents = new ArrayList<>();
        mockConventionEvents.add(new ConventionEvent());

        ArgumentCaptor<ConventionsEventService.ConventionEventsCallback> conventionEventsCallbackArgumentCaptor =
                ArgumentCaptor.forClass(ConventionsEventService.ConventionEventsCallback.class);

        verify(conventionsEventService).getConventionEvents(conventionEventsCallbackArgumentCaptor.capture());
        conventionEventsCallbackArgumentCaptor.getValue().onConventionsComplete(mockConventionEvents);

        ArgumentCaptor<ConventionLocatorService.ConventionLocatorServiceCallback> mGetConventionLocationCallbackCaptor =
                ArgumentCaptor.forClass(ConventionLocatorService.ConventionLocatorServiceCallback.class);

        ArgumentCaptor<List<ConventionEvent>> mConventionEventsCaptor =
                ArgumentCaptor.forClass(List.class);

        verify(conventionLocatorService).getConventionLocations(mConventionEventsCaptor.capture(), mGetConventionLocationCallbackCaptor.capture());
        assertEquals(mockConventionEvents, mConventionEventsCaptor.getValue());

        mGetConventionLocationCallbackCaptor.getValue().onNetworkError();

        verify(mockConventionFinderView).displayNetworkError();

    }



    @After
    public void tearDown() throws Exception {
    }

}