package fr.free.nrw.commons.media

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.webkit.WebView
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.soloader.SoLoader
import fr.free.nrw.commons.Media
import fr.free.nrw.commons.R
import fr.free.nrw.commons.TestAppAdapter
import fr.free.nrw.commons.TestCommonsApplication
import fr.free.nrw.commons.category.CategoryEditSearchRecyclerViewAdapter
import fr.free.nrw.commons.explore.SearchActivity
import fr.free.nrw.commons.kvstore.JsonKvStore
import fr.free.nrw.commons.location.LatLng
import fr.free.nrw.commons.ui.widget.HtmlTextView
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.powermock.reflect.Whitebox
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import org.wikipedia.AppAdapter
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.*
import kotlin.collections.HashMap

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21], application = TestCommonsApplication::class)
@LooperMode(LooperMode.Mode.PAUSED)
class MediaDetailFragmentUnitTests {

    private lateinit var fragment: MediaDetailFragment
    private lateinit var fragmentManager: FragmentManager
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var view: View
    private lateinit var context: Context

    @Mock
    private lateinit var categoryEditSearchRecyclerViewAdapter: CategoryEditSearchRecyclerViewAdapter

    @Mock
    private lateinit var savedInstanceState: Bundle

    @Mock
    private lateinit var scrollView: ScrollView

    @Mock
    private lateinit var media: Media

    @Mock
    private lateinit var categoryRecyclerView: RecyclerView

    @Mock
    private lateinit var simpleDraweeView: SimpleDraweeView

    @Mock
    private lateinit var textView: TextView

    @Mock
    private lateinit var htmlTextView: HtmlTextView

    @Mock
    private lateinit var linearLayout: LinearLayout

    @Mock
    private lateinit var genericDraweeHierarchy: GenericDraweeHierarchy

    @Mock
    private lateinit var button: Button

    @Mock
    private lateinit var detailProvider: MediaDetailPagerFragment.MediaDetailProvider

    @Mock
    private lateinit var applicationKvStore: JsonKvStore

    @Mock
    private lateinit var webView: WebView

    @Mock
    private lateinit var progressBar: ProgressBar

    @Mock
    private lateinit var listView: ListView

    @Mock
    private lateinit var searchView: SearchView

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        context = RuntimeEnvironment.application.applicationContext

        AppAdapter.set(TestAppAdapter())

        SoLoader.setInTestMode()

        Fresco.initialize(RuntimeEnvironment.application.applicationContext)

        val activity = Robolectric.buildActivity(SearchActivity::class.java).create().get()

        fragment = MediaDetailFragment()
        fragmentManager = activity.supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(fragment, null)
        fragmentTransaction.commitNowAllowingStateLoss()

        layoutInflater = LayoutInflater.from(activity)

        view = LayoutInflater.from(activity)
            .inflate(R.layout.fragment_media_detail, null) as View

        scrollView = view.findViewById(R.id.mediaDetailScrollView)
        Whitebox.setInternalState(fragment, "scrollView", scrollView)

        categoryRecyclerView = view.findViewById(R.id.rv_categories)
        Whitebox.setInternalState(fragment, "categoryRecyclerView", categoryRecyclerView)

        Whitebox.setInternalState(fragment, "media", media)
        Whitebox.setInternalState(fragment, "progressBar", progressBar)
        Whitebox.setInternalState(fragment, "captionsListView", listView)
        Whitebox.setInternalState(fragment, "descriptionWebView", webView)
        Whitebox.setInternalState(fragment, "detailProvider", detailProvider)
        Whitebox.setInternalState(fragment, "image", simpleDraweeView)
        Whitebox.setInternalState(fragment, "title", textView)
        Whitebox.setInternalState(fragment, "toDoReason", textView)
        Whitebox.setInternalState(fragment, "desc", htmlTextView)
        Whitebox.setInternalState(fragment, "license", textView)
        Whitebox.setInternalState(fragment, "coordinates", textView)
        Whitebox.setInternalState(fragment, "seeMore", textView)
        Whitebox.setInternalState(fragment, "uploadedDate", textView)
        Whitebox.setInternalState(fragment, "mediaCaption", textView)
        Whitebox.setInternalState(fragment, "captionLayout", linearLayout)
        Whitebox.setInternalState(fragment, "depictsLayout", linearLayout)
        Whitebox.setInternalState(fragment, "depictionContainer", linearLayout)
        Whitebox.setInternalState(fragment, "toDoLayout", linearLayout)
        Whitebox.setInternalState(fragment, "dummyCategoryEditContainer", linearLayout)
        Whitebox.setInternalState(fragment, "showCaptionAndDescriptionContainer", linearLayout)
        Whitebox.setInternalState(fragment, "updateCategoriesButton", button)
        Whitebox.setInternalState(fragment, "categoryContainer", linearLayout)
        Whitebox.setInternalState(fragment, "categorySearchView", searchView)
        Whitebox.setInternalState(fragment, "mediaDiscussion", textView)
        Whitebox.setInternalState(
            fragment,
            "categoryEditSearchRecyclerViewAdapter",
            categoryEditSearchRecyclerViewAdapter
        )

        `when`(simpleDraweeView.hierarchy).thenReturn(genericDraweeHierarchy)
        val map = HashMap<String, String>()
        map[Locale.getDefault().language] = ""
        `when`(media.descriptions).thenReturn(map)
    }

    @Test
    @Throws(Exception::class)
    fun checkFragmentNotNull() {
        Assert.assertNotNull(fragment)
    }

    @Test
    @Throws(Exception::class)
    fun testOnCreateView() {
        Whitebox.setInternalState(fragment, "applicationKvStore", applicationKvStore)
        `when`(applicationKvStore.getBoolean("login_skipped")).thenReturn(true)
        fragment.onCreateView(layoutInflater, null, savedInstanceState)
    }

    @Test
    @Throws(Exception::class)
    fun testOnSaveInstanceState() {
        fragment.onSaveInstanceState(savedInstanceState)
    }

    @Test
    @Throws(Exception::class)
    fun testLaunchZoomActivity() {
        `when`(media.imageUrl).thenReturn("")
        fragment.launchZoomActivity(view)
    }

    @Test
    @Throws(Exception::class)
    fun testOnResume() {
        fragment.onResume()
    }

    @Test
    @Throws(Exception::class)
    fun testOnConfigurationChangedCaseTrue() {
        val newConfig = mock(Configuration::class.java)
        fragment.onConfigurationChanged(newConfig)
    }

    @Test
    @Throws(Exception::class)
    fun testOnConfigurationChangedCaseFalse() {
        val newConfig = mock(Configuration::class.java)
        Whitebox.setInternalState(fragment, "heightVerifyingBoolean", false)
        fragment.onConfigurationChanged(newConfig)
    }

    @Test
    @Throws(Exception::class)
    fun testOnDestroyView() {
        val layoutListener = mock(ViewTreeObserver.OnGlobalLayoutListener::class.java)
        Whitebox.setInternalState(fragment, "layoutListener", layoutListener)
        fragment.onDestroyView()
    }

    @Test
    @Throws(Exception::class)
    fun testExtractDescription() {
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "extractDescription",
            String::class.java
        )
        method.isAccessible = true
        method.invoke(fragment, "")
    }

    @Test
    @Throws(Exception::class)
    fun testGetDescription() {
        `when`(media.filename).thenReturn("")
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "getDescription"
        )
        method.isAccessible = true
        method.invoke(fragment)
    }

    @Test
    @Throws(Exception::class)
    fun testGetCaptions() {
        `when`(media.captions).thenReturn(mapOf(Pair("a", "b")))
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "getCaptions"
        )
        method.isAccessible = true
        method.invoke(fragment)
    }

    @Test
    @Throws(Exception::class)
    fun testGetCaptionsCaseEmpty() {
        `when`(media.captions).thenReturn(mapOf())
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "getCaptions"
        )
        method.isAccessible = true
        method.invoke(fragment)
    }

    @Test
    @Throws(Exception::class)
    fun testSetUpCaptionAndDescriptionLayout() {
        `when`(media.filename).thenReturn("")
        val field: Field =
            MediaDetailFragment::class.java.getDeclaredField("descriptionHtmlCode")
        field.isAccessible = true
        field.set(fragment, null)
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "setUpCaptionAndDescriptionLayout"
        )
        method.isAccessible = true
        method.invoke(fragment)
    }

    @Test
    @Throws(Exception::class)
    fun testUpdateCategoryDisplayCaseNull() {
        Assert.assertEquals(fragment.updateCategoryDisplay(null), false)
    }

    @Test
    @Throws(Exception::class)
    fun testUpdateCategoryDisplayCaseNonNull() {
        Assert.assertEquals(fragment.updateCategoryDisplay(listOf()), true)
    }

    @Test
    @Throws(Exception::class)
    fun testShowCaptionAndDescriptionCaseVisible() {
        fragment.showCaptionAndDescription()
    }

    @Test
    @Throws(Exception::class)
    fun testShowCaptionAndDescription() {
        `when`(linearLayout.visibility).thenReturn(View.GONE)
        `when`(media.filename).thenReturn("")
        fragment.showCaptionAndDescription()
    }

    @Test
    @Throws(Exception::class)
    fun testPrettyCoordinatesCaseNull() {
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "prettyCoordinates",
            Media::class.java
        )
        method.isAccessible = true
        method.invoke(fragment, media)
    }

    @Test
    @Throws(Exception::class)
    fun testPrettyCoordinates() {
        `when`(media.coordinates).thenReturn(LatLng(-0.000001, -0.999999, 0f))
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "prettyCoordinates",
            Media::class.java
        )
        method.isAccessible = true
        method.invoke(fragment, media)
    }

    @Test
    @Throws(Exception::class)
    fun testPrettyUploadedDateCaseNull() {
        `when`(media.dateUploaded).thenReturn(null)
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "prettyUploadedDate",
            Media::class.java
        )
        method.isAccessible = true
        method.invoke(fragment, media)
    }

    @Test
    @Throws(Exception::class)
    fun testPrettyUploadedDateCaseNonNull() {
        `when`(media.dateUploaded).thenReturn(Date(2000, 1, 1))
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "prettyUploadedDate",
            Media::class.java
        )
        method.isAccessible = true
        method.invoke(fragment, media)
    }

    @Test
    @Throws(Exception::class)
    fun testPrettyLicenseCaseNull() {
        `when`(media.license).thenReturn(null)
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "prettyLicense",
            Media::class.java
        )
        method.isAccessible = true
        method.invoke(fragment, media)
    }

    @Test
    @Throws(Exception::class)
    fun testPrettyLicenseCaseNonNull() {
        `when`(media.license).thenReturn("licence")
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "prettyLicense",
            Media::class.java
        )
        method.isAccessible = true
        method.invoke(fragment, media)
    }

    @Test
    @Throws(Exception::class)
    fun testPrettyDiscussion() {
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "prettyDiscussion",
            String::class.java
        )
        method.isAccessible = true
        method.invoke(fragment, "mock")
    }

    @Test
    @Throws(Exception::class)
    fun testPrettyCaptionCaseEmpty() {
        `when`(media.captions).thenReturn(mapOf(Pair("a", "")))
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "prettyCaption",
            Media::class.java
        )
        method.isAccessible = true
        method.invoke(fragment, media)
    }

    @Test
    @Throws(Exception::class)
    fun testPrettyCaptionCaseNonEmpty() {
        `when`(media.captions).thenReturn(mapOf(Pair("a", "b")))
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "prettyCaption",
            Media::class.java
        )
        method.isAccessible = true
        method.invoke(fragment, media)
    }

    @Test
    @Throws(Exception::class)
    fun testPrettyCaption() {
        `when`(media.captions).thenReturn(mapOf())
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "prettyCaption",
            Media::class.java
        )
        method.isAccessible = true
        method.invoke(fragment, media)
    }

    @Test
    @Throws(Exception::class)
    fun testSetupImageView() {
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "setupImageView"
        )
        method.isAccessible = true
        method.invoke(fragment)
    }

    @Test
    @Throws(Exception::class)
    fun testSetupToDo() {
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "setupToDo"
        )
        method.isAccessible = true
        method.invoke(fragment)
    }

    @Test
    @Throws(Exception::class)
    fun testOnDiscussionLoaded() {
        val method: Method = MediaDetailFragment::class.java.getDeclaredMethod(
            "onDiscussionLoaded",
            String::class.java
        )
        method.isAccessible = true
        method.invoke(fragment, "")
    }

    @Test
    @Throws(Exception::class)
    fun testForMedia() {
        MediaDetailFragment.forMedia(0, true, true, true)
    }

}