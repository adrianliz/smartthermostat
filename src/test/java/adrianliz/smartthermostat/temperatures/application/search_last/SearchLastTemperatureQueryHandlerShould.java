package adrianliz.smartthermostat.temperatures.application.search_last;

import adrianliz.smartthermostat.temperatures.TemperaturesModuleUnitTestCase;
import adrianliz.smartthermostat.temperatures.application.TemperatureResponse;
import adrianliz.smartthermostat.temperatures.application.TemperatureResponseMother;
import adrianliz.smartthermostat.temperatures.domain.Temperature;
import adrianliz.smartthermostat.temperatures.domain.TemperatureMother;
import adrianliz.smartthermostat.temperatures.domain.TemperatureNotExists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SearchLastTemperatureQueryHandlerShould extends TemperaturesModuleUnitTestCase {
	private SearchLastTemperatureQueryHandler handler;

	@BeforeEach
	protected void setUp() {
		super.setUp();

		handler = new SearchLastTemperatureQueryHandler(new LastTemperatureSearcher(repository));
	}

	@Test
	void should_search_last_temperature() {
		Temperature lastTemperature = TemperatureMother.random();
		SearchLastTemperatureQuery query = SearchLastTemperatureQueryMother.random();
		TemperatureResponse response =
				TemperatureResponseMother.create(lastTemperature.id(), lastTemperature.sensorId(),
						lastTemperature.celsiusRegistered(), lastTemperature.timestamp());

		shouldSearchLastTemperature(lastTemperature);

		assertEquals(response, handler.handle(query));
	}

	@Test
	void should_throw_temperature_not_exists_when_there_arent_temperatures() {
		SearchLastTemperatureQuery query = SearchLastTemperatureQueryMother.random();

		assertThrows(TemperatureNotExists.class, () -> handler.handle(query));
	}
}
