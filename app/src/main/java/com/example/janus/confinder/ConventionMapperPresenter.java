package com.example.janus.confinder;

import com.example.janus.confinder.data.ConventionEvent;
import com.example.janus.confinder.data.ConventionLocation;
import com.example.janus.confinder.data.ConventionLocatorService;
import com.example.janus.confinder.data.ConventionsEventService;

import java.util.List;

/*
 This is the presenter for the mapping view. It gets a list of upcoming convention events and then obtains their
 respective locations, passing this info back to the mapping view.
*/

public class ConventionMapperPresenter implements ConventionMapperContract.Presenter {

        private ConventionMapperContract.View conventionMapperView;
        private ConventionLocatorService conventionsLocator;
        private ConventionsEventService conventionsEventService;

        public ConventionMapperPresenter(ConventionMapperContract.View conventionMapperView, ConventionsEventService conventionsEventService, ConventionLocatorService conventionsLocator) {
            this.conventionMapperView = conventionMapperView;
            this.conventionsEventService = conventionsEventService;
            this.conventionsLocator = conventionsLocator;
        }

        public void mapConventions() {

            // Call the Conventions Event service first to get the actual events. Then pass them on to the locator service for mapping objects
            conventionsEventService.getConventionEvents(new ConventionsEventService.ConventionEventsCallback() {
                @Override
                public void onConventionsComplete(List<ConventionEvent> conventions) {

                    conventionsLocator.getConventionLocations(conventions, new ConventionLocatorService.ConventionLocatorServiceCallback() {
                        // Pass the located convention event to the mapper
                        @Override
                        public void onConventionLocated(ConventionLocation conventionLocation) {
                            conventionMapperView.mapConvention(conventionLocation);
                        }

                        // Tell the map to perform the zoom animation
                        @Override
                        public void onConventionLocationsComplete() {
                            conventionMapperView.completeMap();
                        }

                        @Override
                        public void onNetworkError() {
                            conventionMapperView.displayNetworkError();
                        }
                    });
                }

                @Override
                public void onNetworkError() {
                    conventionMapperView.displayNetworkError();
                }
            });

        }

}
