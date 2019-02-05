package com.mapbox.mapboxsdk.testapp.telemetry;

import android.os.Bundle;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.idling.CountingIdlingResource;

import com.google.gson.Gson;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.testapp.activity.BaseTest;
import com.mapbox.mapboxsdk.testapp.activity.espresso.DeviceIndependentTestActivity;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.testapp.action.MapboxMapAction.invoke;


public class PerformanceEventTest extends BaseTest {


  private final CountingIdlingResource testIdlingResource =
          new CountingIdlingResource("performance_event_resource");

  @Override
  protected Class getActivityClass() {
    return DeviceIndependentTestActivity.class;
  }

  @Override
  public void beforeTest() {
    super.beforeTest();
    IdlingRegistry.getInstance().register(testIdlingResource);
  }

  @Test
  public void pushPerformanceEvent() {
    validateTestSetup();
    final long now = System.currentTimeMillis();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      long elapsed = System.currentTimeMillis() - now;
      triggerPerformanceEvent(mapboxMap.getStyle().getUrl(), elapsed);
      testIdlingResource.decrement();
    });

    Espresso.onIdle();
  }


  @Override
  public void afterTest() {
    super.afterTest();
    IdlingRegistry.getInstance().unregister(testIdlingResource);
  }


  private void triggerPerformanceEvent(String style, long elapsed) {

    List<Attribute<String>> attributes = new ArrayList<>();
    attributes.add(
            new Attribute<>("style_id", style));

    List<Attribute<? extends Number>> counters = new ArrayList();
    counters.add(new Attribute<>("elapsed", elapsed));


    Bundle bundle = new Bundle();
    bundle.putString("attributes", new Gson().toJson(attributes));
    bundle.putString("counters", new Gson().toJson(counters));

    Mapbox.getTelemetry().onPerformanceEvent(bundle);

  }


  class Attribute<T> {
    private String name;
    private T value;

    Attribute(String name, T value) {
      this.name = name;
      this.value = value;
    }
  }
}


