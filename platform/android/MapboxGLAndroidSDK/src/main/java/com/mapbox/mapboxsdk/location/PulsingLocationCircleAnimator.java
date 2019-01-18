package com.mapbox.mapboxsdk.location;

import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.Interpolator;

import com.mapbox.mapboxsdk.maps.MapboxMap;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;

/**
 * Manages the logic of the interpolated animation which is applied to the LocationComponent's pulsing circle
 */
class PulsingLocationCircleAnimator {

  public PulsingLocationCircleAnimator() {
  }

  /**
   * Start the LocationComponent circle pulse animation
   *
   * @param interpolatorToUse   the type of Android-system interpolator to use
   * @param singlePulseDuration how many milliseconds a single pulse should last
   * @param mapboxMap           the MapboxMap object which pulsing circle should be shown on
   */
  public void animatePulsingCircleRadius(@NonNull Interpolator interpolatorToUse, @NonNull float singlePulseDuration,
                                         @NonNull final MapboxMap mapboxMap, float targetPulsingCircleRadius) {

    ValueAnimator animator = ValueAnimator.ofFloat(0f, targetPulsingCircleRadius);
    animator.setDuration(Math.round(singlePulseDuration));
    animator.setInterpolator(interpolatorToUse);
    animator.setStartDelay(1000);
    animator.start();
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        if (mapboxMap.getStyle().getLayer("") != null) {
          mapboxMap.getStyle().getLayer("").setProperties(
            circleRadius((Float) valueAnimator.getAnimatedValue()));
        }
      }
    });
  }
}
