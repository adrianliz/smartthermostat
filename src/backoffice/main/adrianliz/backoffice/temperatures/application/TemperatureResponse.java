package adrianliz.backoffice.temperatures.application;

import adrianliz.backoffice.temperatures.domain.Temperature;
import adrianliz.shared.domain.bus.query.Response;
import java.util.Objects;

public final class TemperatureResponse implements Response {

  private final String id;
  private final String sensorId;
  private final Double celsiusRegistered;
  private final long timestamp;

  public TemperatureResponse(
      final String id,
      final String sensorId,
      final Double celsiusRegistered,
      final long timestamp) {
    this.id = id;
    this.sensorId = sensorId;
    this.celsiusRegistered = celsiusRegistered;
    this.timestamp = timestamp;
  }

  public static TemperatureResponse fromAggregate(final Temperature temperature) {
    return new TemperatureResponse(
        temperature.id().value(),
        temperature.sensorId().value(),
        temperature.celsiusRegistered().value(),
        temperature.timestamp().value());
  }

  public String id() {
    return id;
  }

  public String sensorId() {
    return sensorId;
  }

  public Double celsiusRegistered() {
    return celsiusRegistered;
  }

  public long timestamp() {
    return timestamp;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final TemperatureResponse response = (TemperatureResponse) o;
    return (timestamp == response.timestamp
        && Objects.equals(id, response.id)
        && Objects.equals(sensorId, response.sensorId)
        && Objects.equals(celsiusRegistered, response.celsiusRegistered));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sensorId, celsiusRegistered, timestamp);
  }
}
