package com.mapbox.mapboxsdk.location;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PulsingLocationMode {

  private PulsingLocationMode() {
    // Class should not be initialized
  }

  /**
   * One of these constants should be used with {@link LocationComponentOptions#pulsingCircleInterpolator}.
   * Mode can be switched at anytime by calling the {@code pulsingCircleInterpolator()} method passing
   * in the new mode you'd like the pulsing circle to use.
   */
  @StringDef( {LINEAR, ACCELERATE, DECELERATE, BOUNCE})
  @Retention(RetentionPolicy.SOURCE)
  public @interface Mode {
  }

  /**
   * Basic linear interpolator
   */
  public static final String LINEAR = "linear";

  /**
   * Accelerated affect
   */
  public static final String ACCELERATE = "accelerate";

  /**
   * Deceleration affect
   */
  public static final String DECELERATE = "decelerate";

  /**
   * Bouncing affect
   */
  public static final String BOUNCE = "bounce";
}
