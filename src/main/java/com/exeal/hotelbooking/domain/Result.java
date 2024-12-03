package com.exeal.hotelbooking.domain;

public class Result<T> {

  private final T value;
  private final String error;

  private Result(T value, String error) {
    this.value = value;
    this.error = error;
  }

  public static <T> Result<T> ok(T value) {
    return new Result<>(value, null);
  }

  public static <T> Result<T> failure(String error) {
    return new Result<>(null, error);
  }

  public T get() {
    if (error != null) {
      throw new IllegalStateException("Result is in error state: " + error);
    }
    return value;
  }

  public boolean isValid() {
    return error == null;
  }
}
