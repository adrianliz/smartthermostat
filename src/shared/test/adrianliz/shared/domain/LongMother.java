package adrianliz.shared.domain;

public final class LongMother {

  public static Long random() {
    return MotherCreator.random().random().nextLong();
  }
}
