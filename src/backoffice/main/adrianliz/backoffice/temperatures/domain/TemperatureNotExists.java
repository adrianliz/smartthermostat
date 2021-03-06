package adrianliz.backoffice.temperatures.domain;

import adrianliz.shared.domain.DomainError;

public final class TemperatureNotExists extends DomainError {

  public TemperatureNotExists() {
    super("temperature_not_exists", "The temperature doesn't exists");
  }

  public TemperatureNotExists(final TemperatureId id) {
    super("temperature_not_exists", String.format("The temperature <%s> doesn't exists", id));
  }
}
