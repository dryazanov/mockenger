package com.socialstartup.mockenger.frontend.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by dryazanov
 */
@RunWith(MockitoJUnitRunner.class)
public class WebServiceProxyTest {

    @Test
    public void destinationsPaginationTest() {}

    /*@InjectMocks
    WebServiceProxy proxy;

    @Mock
    private RestTemplate restTemplateMock;

    private DestinationFinder destinationFinder;

    private AirportDetails airportDetails;

    private String code;

    private String pos;

    private double minBudget;

    private double maxBudget;

    private String priceField;

    private int limit;


    @Before
    public void init() {
        code = "AMS";
        pos = "NL";
        minBudget = 50;
        maxBudget = 200;
        limit = 2;
        priceField = "price";
        proxy.setRestTemplate(restTemplateMock);
        proxy.setNumOfThreads(2);

        List<DestinationView> destinationViews = new ArrayList<DestinationView>();
        destinationViews.add(createDestinationView("EDI", createFare(97.0)));
        destinationViews.add(createDestinationView("BSL", createFare(98.0)));
        destinationViews.add(createDestinationView("MAD", createFare(99.0)));

        destinationFinder = new DestinationFinder();
        destinationFinder.setDestinations(destinationViews);
        when(restTemplateMock.getForObject(any(URI.class), eq(DestinationFinder.class))).thenReturn(destinationFinder);

        ContinentDetails continent = new ContinentDetails();
        continent.setName("Europe");
        CountryDetails country = new CountryDetails();
        country.setParent(continent);
        CityDetails city = new CityDetails();
        city.setParent(country);
        airportDetails = new AirportDetails();
        airportDetails.setParent(city);
        airportDetails.setDescription("Amsterdam - Schiphol (AMS)");
        when(restTemplateMock.getForObject(any(String.class), eq(AirportDetails.class), any(String.class))).thenReturn(airportDetails);
    }

    @Test
    public void airportDetailsTest() {
        AirportDetails result = proxy.getAirportDetails(code);

        Assert.assertNotNull(result);
        Assert.assertEquals(result, airportDetails);
        verify(restTemplateMock).getForObject(any(String.class), eq(AirportDetails.class), eq(code));
    }

    @Test
    public void destinationsTest() {
        IDestinationFinder df = runRequest("price", 1);
        Assert.assertNotNull(df);

        List<DestinationView> dw = df.getDestinations();

        Assert.assertEquals(2, dw.size());
        Assert.assertEquals(2, df.getNumberOfPages());

        Assert.assertEquals("EDI", dw.get(0).getDestination().getCode());
        Assert.assertEquals(new Double(97.0), dw.get(0).getLowestFare().getValue());

        Assert.assertEquals("BSL", dw.get(1).getDestination().getCode());
        Assert.assertEquals(new Double(98.0), dw.get(1).getLowestFare().getValue());

        verify(restTemplateMock).getForObject(any(URI.class), eq(DestinationFinder.class));
        verify(restTemplateMock, times(2)).getForObject(any(String.class), eq(AirportDetails.class), any(String.class));
    }


    @Test
    public void destinationsSortedByNameTest() {
        IDestinationFinder df = runRequest("name", 1);
        List<DestinationView> dw = df.getDestinations();

        Assert.assertEquals(2, dw.size());

        Assert.assertEquals("BSL", dw.get(0).getDestination().getCode());
        Assert.assertEquals(new Double(98.0), dw.get(0).getLowestFare().getValue());

        Assert.assertEquals("EDI", dw.get(1).getDestination().getCode());
        Assert.assertEquals(new Double(97.0), dw.get(1).getLowestFare().getValue());

        verify(restTemplateMock).getForObject(any(URI.class), eq(DestinationFinder.class));
        verify(restTemplateMock, times(2)).getForObject(any(String.class), eq(AirportDetails.class), any(String.class));
    }


    @Test
    public void destinationsPaginationTest() {
        IDestinationFinder df = runRequest("name", 2);
        List<DestinationView> dw = df.getDestinations();

        Assert.assertEquals(1, dw.size());
        Assert.assertEquals("MAD", dw.get(0).getDestination().getCode());
        Assert.assertEquals(new Double(99.0), dw.get(0).getLowestFare().getValue());

        verify(restTemplateMock).getForObject(any(URI.class), eq(DestinationFinder.class));
        verify(restTemplateMock, times(1)).getForObject(any(String.class), eq(AirportDetails.class), any(String.class));
    }


    private IDestinationFinder runRequest(String orderBy, int page) {
        RequestOptions options = new RequestOptions()
                .getBuilder()
                .setOrigin(code)
                .setPos(pos)
                .setMinBudget(minBudget)
                .setMaxBudget(maxBudget)
                .setPriceField(priceField)
                .setLimit(limit)
                .setOrderBy(orderBy)
                .setPage(page)
                .build();

        return proxy.getDestinations(options);
    }

    private Destination createDestination(String code) {
        Destination destination = new Destination();
        destination.setCode(code);
        return destination;
    }

    private Fare createFare(double value) {
        Fare fare = new Fare();
        fare.setValue(value);
        fare.setCurrency("EUR");
        return fare;
    }

    private DestinationView createDestinationView(String destinationCode, Fare fare) {
        DestinationView view = new DestinationView();
        view.setOrigin(code);
        view.setLowestFare(fare);
        view.setDestination(createDestination(destinationCode));
        return view;
    }*/
}
