package com.rahul.rglauncher

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.rahul.applicationmodule.AppFactory
import com.rahul.applicationmodule.model.AppModel
import com.rahul.rglauncher.adapter.AppRecycleAdapter
import java.util.*
@SuppressLint("ClickableViewAccessibility")
class MainActivity : AppCompatActivity() {
    var DRAWER_PEEK_HEIGHT = 0
    private var adapter: AppRecycleAdapter? = null
    var installedAppList: List<AppModel> = ArrayList()
    var mDrawerGridView: RecyclerView? = null
    var mBottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    var appFactory: AppFactory? = null
    private var broadcastReceiver: BroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        permissions
        seachListeners()
        applistOpenLisneser()
        appFactory = AppFactory(applicationContext)
        installedAppList = appFactory!!.getInstalledAppList(AppFactory.Order.ASC)
        val mTopDrawerLayout = findViewById<LinearLayout>(R.id.topDrawerLayout)
        mTopDrawerLayout.post {
            initializeDrawer()
            //  registerForContextMenu(mDrawerGridView);
        }
        appInstallUninstallBroadcast()
    }

    private fun appInstallUninstallBroadcast() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == Intent.ACTION_PACKAGE_ADDED) {
                    Toast.makeText(context, "App Installed!!!!.", Toast.LENGTH_LONG).show()
                } else if (intent.action == Intent.ACTION_PACKAGE_REMOVED) {
                    Toast.makeText(context, "App Uninstalled!!!!.", Toast.LENGTH_LONG).show()
                }
                Log.e("broadcastReceiver", intent.toString())
                installedAppList = appFactory!!.getInstalledAppList(AppFactory.Order.ASC)
                adapter!!.updateList(installedAppList)
                adapter!!.notifyDataSetChanged()
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addDataScheme("package")
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    private fun applistOpenLisneser() {
        val homeScreenImage = findViewById<View>(R.id.homeScreenImage)
        val slideMenu = findViewById<View>(R.id.slideMenu)
        /*slideMenu.setOnClickListener(v -> {
            try {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/
        homeScreenImage.setOnTouchListener { view: View?, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Log.d("TouchTest", "Touch down")
            } else if (event.action == MotionEvent.ACTION_UP) {
                Log.d("TouchTest", "Touch up")
                mBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
            }
            true
        }
        slideMenu.setOnTouchListener { view: View?, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Log.d("TouchTest", "Touch down")
            } else if (event.action == MotionEvent.ACTION_UP) {
                Log.d("TouchTest", "Touch up")
                mBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
            }
            true
        }
    }

    private fun initializeDrawer() {
        val mBottomSheet = findViewById<LinearLayout>(R.id.bottomSheet)
        mDrawerGridView = findViewById(R.id.drawerGrid)
        mBottomSheetBehavior = BottomSheetBehavior.from<LinearLayout?>(mBottomSheet)
        mBottomSheetBehavior?.setHideable(false)
        mBottomSheetBehavior?.setPeekHeight(DRAWER_PEEK_HEIGHT)
        sampleBottomAppIcon(installedAppList)
        adapter = AppRecycleAdapter(this, installedAppList)
        mDrawerGridView?.setAdapter(adapter)

    }

    private fun sampleBottomAppIcon(installedAppList: List<AppModel>) {
        val msetting = findViewById<ImageView>(R.id.settings)
        val mphone = findViewById<ImageView>(R.id.phone)
        val mcontact = findViewById<ImageView>(R.id.contact)
        val mcamera = findViewById<ImageView>(R.id.camera)
        val mmessage = findViewById<ImageView>(R.id.message)
        for (app in installedAppList) {
            UpdateHomeIcon(mcamera, app, "camera")
            UpdateHomeIcon(mphone, app, "phone")
            UpdateHomeIcon(msetting, app, "setting")
            UpdateHomeIcon(mmessage, app, "messag")
            UpdateHomeIcon(mcontact, app, "contact")
        }
    }

    private fun UpdateHomeIcon(mcamera: ImageView, app: AppModel, camera: String) {
        if (app.appName.lowercase().contains(camera)) {
            mcamera.setImageDrawable(app.appIcon)
            mcamera.setOnClickListener {
                val launchAppIntent =
                    applicationContext.packageManager.getLaunchIntentForPackage(app.packageName)
                if (launchAppIntent != null) applicationContext.startActivity(launchAppIntent)
            }
        }
    }

    fun itemPress(app: AppModel) {
        collapseDrawer()
        val launchAppIntent =
            applicationContext.packageManager.getLaunchIntentForPackage(app.packageName)
        if (launchAppIntent != null) {
            applicationContext.startActivity(launchAppIntent)
        }
    }

    var mSelectedApp: AppModel? = null
    fun itemLongPress(app: AppModel?) {
        mSelectedApp = app
        //collapseDrawer();
    }

    private fun collapseDrawer() {
        // mDrawerGridView.setY(DRAWER_PEEK_HEIGHT);
        mBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    /*private int getDisplayContentHeight() {
        final WindowManager windowManager = getWindowManager();
        final Point size = new Point();
        int screenHeight = 0, actionBarHeight = 0, statusBarHeight = 0;
        if (getActionBar() != null) {
            actionBarHeight = getActionBar().getHeight();
        }

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        int contentTop = (findViewById(android.R.id.content)).getTop();
        windowManager.getDefaultDisplay().getSize(size);
        screenHeight = size.y;
        return screenHeight - contentTop - actionBarHeight - statusBarHeight;
    }*/
    private fun seachListeners() {
        val simpleSearchViewApp = findViewById<SearchView>(R.id.simpleSearchViewApp)
        // perform set on query text listener event
        simpleSearchViewApp.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter!!.modifySeach(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter!!.modifySeach(newText)
                return false
            }
        })
    }

    private val permissions: Unit
        private get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            }
        }

    override fun onResume() {
        super.onResume()
        //seachListeners();
    }

    override fun onBackPressed() {
        if (mBottomSheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED || mBottomSheetBehavior!!.state == BottomSheetBehavior.STATE_HALF_EXPANDED) {
            mBottomSheetBehavior!!.setState(BottomSheetBehavior.STATE_COLLAPSED)
        } else {
            //   super.onBackPressed();
        }
    }

    /* public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
     {
         super.onCreateContextMenu(menu, v, menuInfo);
         MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.menu_main, menu);
         menu.setHeaderTitle("Select The Action");
     }
    */
    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.appInfo) {
            try {
                //Open the specific App Info page:
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:" + mSelectedApp!!.packageName)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
              e.printStackTrace()
                val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                startActivity(intent)
            }
        } else if (item.itemId == R.id.appUnistall) {
            try {
                val intent = Intent(Intent.ACTION_DELETE)
                intent.data = Uri.parse("package:" + mSelectedApp!!.packageName)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            return false
        }
        return true
    }

    companion object {
        private const val TAG = "Main"
    }
}