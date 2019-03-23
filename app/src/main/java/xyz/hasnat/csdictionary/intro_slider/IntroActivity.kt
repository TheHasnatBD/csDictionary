package xyz.hasnat.csdictionary.intro_slider

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

import xyz.hasnat.csdictionary.constant.Constant
import xyz.hasnat.csdictionary.R
import android.os.Build
import android.text.Spanned
import xyz.hasnat.csdictionary.MainActivity


class IntroActivity : Activity() {

    private var viewPager: ViewPager? = null
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private var dotsLayout: LinearLayout? = null
    private var layouts: IntArray? = null
    private var btnSkip: Button? = null
    private var btnNext: Button? = null
    private var dots: Array<TextView>? = null


    /**
     * Add PageChangeLister for PageAdapter
     */
    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            addBottomDots(position)
            /**
             * changing the next button text 'NEXT' / 'GOT IT'
             */
            if (position == layouts!!.size - 1) {
                // last page. make button text to GOT IT
                btnNext!!.text = getString(R.string.start)
                btnSkip!!.visibility = View.GONE
            } else {
                // still pages are left
                btnNext!!.text = getString(R.string.next)
                btnSkip!!.visibility = View.VISIBLE
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}

        override fun onPageScrollStateChanged(arg0: Int) {}

    } //Add PageChangeLister for PageAdapter End

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //For Full Screen
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.intro_activity)

        // saving in local cache through Shared Preferences
        val sharedPreferences = getSharedPreferences(Constant.SHARED_PREF, Context.MODE_PRIVATE)
        if (sharedPreferences.getInt(Constant.INTRO, 0) == 1) {
            startActivity(Intent(this@IntroActivity, MainActivity::class.java))
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


        // initializations
        viewPager = findViewById(R.id.view_pager)
        dotsLayout = findViewById(R.id.layoutDots)
        btnSkip = findViewById(R.id.btn_skip)
        btnNext = findViewById(R.id.btn_next)

        layouts = intArrayOf(R.layout.slide1, R.layout.slide2)

        // adding bottom dots
        addBottomDots(0)

        viewPagerAdapter = ViewPagerAdapter()
        viewPager!!.adapter = viewPagerAdapter
        viewPager!!.addOnPageChangeListener(viewPagerPageChangeListener)
    }

    //Click Listener for both buttons
    fun btnSkipClick(v: View) {
        launchHomeScreen()
    }

    fun btnNextClick(v: View) {
        // checking for last page
        /**
         * if last page home_menu screen will be launched
         */
        val current = getItem(1)
        if (current < layouts!!.size) {
            /** move to next screen    */
            viewPager!!.currentItem = current
        } else {
            launchHomeScreen()
        }
    }


    private fun addBottomDots(currentPage: Int) {
        //dots = arrayOfNulls(layouts!!.size)
        //dots= arrayOf(TextView(layouts!!.size))

        var arraySize = layouts!!.size
        dots = Array<TextView>(arraySize) { textViewInit() }

        dotsLayout!!.removeAllViews()
        for (i in dots!!.indices) {
            dots!![i] = TextView(this)
            dots!![i].text = fromHtml("&#8226;")
            dots!![i].textSize = 35f
            dots!![i].setTextColor(Color.WHITE) // inactive color
            dotsLayout!!.addView(dots!![i])
        }

        if (dots!!.isNotEmpty())
            dots!![currentPage].setTextColor(Color.GRAY) // active color
    }

    //because of deprecated
    private fun fromHtml(html: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
    }

    private fun textViewInit(): TextView {
        return TextView(applicationContext)
    }


    private fun getItem(i: Int): Int {
        return viewPager!!.currentItem + i
    }

    private fun launchHomeScreen() {
        /**
         * Save Intro Screen for First Time with shared
         */
        val sharedPreferences = getSharedPreferences(Constant.SHARED_PREF, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor
        editor = sharedPreferences.edit()
        editor.putInt(Constant.INTRO, 1)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }


    /**
     * Create class extending PagerAdapter
     */
    inner class ViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater!!.inflate(layouts!![position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return layouts!!.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    } //Create class extending PagerAdapter End
}
