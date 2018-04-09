package com.example.janus.confinder;

import android.location.Geocoder;

import com.example.janus.confinder.data.Convention;
import com.example.janus.confinder.data.ConventionsDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class ConventionFinderPresenterUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Convention mockConvention;

    @Mock
    private ConventionFinderContract.View mockConventionFinderView;

    @Mock
    private ConventionsDataSource mockConventionsDataSource;

    @Mock
    private Geocoder mockGeocoder;


    // This is the Class under test
    private ConventionFinderContract.Presenter conventionFinder;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        conventionFinder = new ConventionFinderPresenter(mockConventionFinderView, mockConventionsDataSource, mockGeocoder);

    }

    @Test
    public void testGetConventionsNoneFound() throws Exception {

        conventionFinder.getConventions();

        ArgumentCaptor<ConventionsDataSource.ConventionsDataSourceCallback> mGetConventionsCallbackCaptor =
                ArgumentCaptor.forClass(ConventionsDataSource.ConventionsDataSourceCallback.class);

        verify(mockConventionsDataSource).getConventions(mGetConventionsCallbackCaptor.capture());
        mGetConventionsCallbackCaptor.getValue().onConventionsComplete(new ArrayList<Convention>());

        verify(mockConventionFinderView).completeMap();

    }

    @Test
    public void testGetConventionsNetworkError() throws Exception {

        conventionFinder.getConventions();

        ArgumentCaptor<ConventionsDataSource.ConventionsDataSourceCallback> mGetConventionsCallbackCaptor =
                ArgumentCaptor.forClass(ConventionsDataSource.ConventionsDataSourceCallback.class);

        verify(mockConventionsDataSource).getConventions(mGetConventionsCallbackCaptor.capture());
        mGetConventionsCallbackCaptor.getValue().onNetworkError();

        verify(mockConventionFinderView).displayNetworkError();

    }


    @Test
    public void testGetConventionsOneFound() throws Exception {

        conventionFinder.getConventions();

        ArgumentCaptor<ConventionsDataSource.ConventionsDataSourceCallback> mGetConventionsCallbackCaptor =
                ArgumentCaptor.forClass(ConventionsDataSource.ConventionsDataSourceCallback.class);

        verify(mockConventionsDataSource).getConventions(mGetConventionsCallbackCaptor.capture());
        mGetConventionsCallbackCaptor.getValue().onConventionsComplete(new ArrayList<Convention>(1));

        verify(mockConventionFinderView).mapConvention(any(Convention.class));

    }


    @After
    public void tearDown() throws Exception {
    }

}