package com.mapbox.mapboxsdk.location;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.animation.Interpolator;

import com.mapbox.mapboxsdk.maps.MapboxMap;

import static com.mapbox.mapboxsdk.location.LocationComponentConstants.PULSING_CIRCLE_LAYER;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;

/**
 * Manages the logic of the interpolated animation which is applied to the LocationComponent's pulsing circle
 */
class PulsingLocationCircleAnimator {

  private static final String TAG = "Mbgl-PulsingLocationCircleAnimator";
  private int count = 0;
  private Handler handler;
  private Runnable runnable;

  public PulsingLocationCircleAnimator() {
  }

  /**
   * Start the LocationComponent circle pulse animation
   *
   * @param interpolatorToUse   the type of Android-system interpolator to use
   * @param singlePulseDuration how many milliseconds a single pulse should last
   * @param mapboxMap           the MapboxMap object which pulsing circle should be shown on
   */
  public void animatePulsingCircleRadius(@NonNull final Interpolator interpolatorToUse, @NonNull final float singlePulseDuration,
                                         @NonNull final MapboxMap mapboxMap, final float targetPulsingCircleRadius,
                                         @NonNull final long targetPulsingCircleFrequency) {


    handler = new Handler();
    runnable = new Runnable() {
      @Override
      public void run() {
        // Check if we are at the end of the points list, if so we want to stop using
        // the handler.
        if ((100 > count)) {
          ValueAnimator animator = ValueAnimator.ofFloat(0f, 200);
          animator.setDuration(Math.round(singlePulseDuration));
          animator.setInterpolator(interpolatorToUse);
          animator.setStartDelay(1000);
          animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
              Log.d(TAG, "onAnimationUpdate: valueAnimator = " + valueAnimator.toString());
              if (mapboxMap.getStyle().getLayer(PULSING_CIRCLE_LAYER) != null) {
                Log.d(TAG, "onAnimationUpdate: (Float) valueAnimator.getAnimatedValue()) = " + valueAnimator.getAnimatedValue());
                mapboxMap.getStyle().getLayer(PULSING_CIRCLE_LAYER).setProperties(
                  circleRadius((Float) valueAnimator.getAnimatedValue()));
              }
            }
          });
          animator.start();

          // Keeping the current point count we are on.
          count++;

          // Once we finish we need to repeat the entire process by executing the
          // handler again once the ValueAnimator is finished.
          handler.postDelayed(this, targetPulsingCircleFrequency);
        }
      }
    };
    handler.post(runnable);
  }
}
