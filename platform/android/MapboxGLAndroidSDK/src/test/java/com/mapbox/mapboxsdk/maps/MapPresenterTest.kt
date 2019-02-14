package com.mapbox.mapboxsdk.maps

import com.mapbox.mapboxsdk.maps.renderer.textureview.TextureViewMapRenderer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MapPresenterTest {
  private lateinit var mapPresenter: MapPresenter
  private lateinit var mapView: MapView
  private lateinit var mapboxMapOptions: MapboxMapOptions

  @Before
  fun setup() {
    mapView = mockk(relaxUnitFun = true)
    every { mapView.initializeTextureView(any(), any()) } returns mockk<TextureViewMapRenderer>()

    mapboxMapOptions = mockk()
    every { mapboxMapOptions.foregroundLoadColor } returns 0xFFF0E9E1.toInt()

    mapPresenter = MapPresenterImpl(mapView, mockk(), mapboxMapOptions)
  }

  @Test
  fun init_foregroundColorSet() {
    verify { mapView.setForegroundColor(0xFFF0E9E1.toInt()) }
  }

  @Test
  fun init_inflateInternalViews() {
    verify { mapView.inflateInternalViews() }
  }

  @Test
  fun init_setViewOptions() {
    verify { mapView.setViewOptions() }
  }

  @Test
  fun init_initializeTextureView() {
    every { mapboxMapOptions.textureMode } returns true
    every { mapboxMapOptions.translucentTextureSurface } returns false
    every { mapboxMapOptions.localIdeographFontFamily } returns null
    verify { mapView.initializeTextureView(null, false) }
  }

  @Test
  fun init_initializeTranslucentTextureView() {
    every { mapboxMapOptions.textureMode } returns true
    every { mapboxMapOptions.translucentTextureSurface } returns true
    every { mapboxMapOptions.localIdeographFontFamily } returns null
    verify { mapView.initializeTextureView(null, true) }
  }
}