package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.firstTab.PhoneBookFragment
import com.example.myapplication.secondTab.GalleryFragment
import com.example.myapplication.thirdTab.ScheduleFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity() {
    //    첫번째탭 관련 권한처리
    val READ_CONTACTS = Manifest.permission.READ_CONTACTS
    val CALL_PHONE = Manifest.permission.CALL_PHONE
    //    두번째탭 관련 권한처리
    val CAMERA_PERMISSION = Manifest.permission.CAMERA
    val READ_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    val WRITE_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE

    private val permissions = arrayOf(READ_CONTACTS, CALL_PHONE, CAMERA_PERMISSION, READ_STORAGE_PERMISSION, WRITE_STORAGE_PERMISSION)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        checkAndStart()
    }
    private fun startProcess() {
        // 권한처리 후 일반 프로세스(화면그리기, 데이터 가져오기) 시작
        setContentView(R.layout.activity_main)
        setAdapter()
        val tabTitles = listOf<String>("연락처", "갤러리", "시간표")
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
    private fun setAdapter() {
        val adapter = FragmentAdapter(this)
        val fragments = listOf<Fragment>(PhoneBookFragment(), GalleryFragment(), ScheduleFragment())
        adapter.fragments.addAll(fragments)
        viewPager.adapter = adapter
    }
    // 권한처리 코드
    private fun checkAndStart() {
        if( isLower23() || isPermitted(permissions)) {
            startProcess()
        }
        else {
            ActivityCompat.requestPermissions(this, permissions, 99)
        }
    }
    private fun isLower23() : Boolean{
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun isPermitted(permissions: Array<String>) : Boolean {
        for(perm in permissions) {
            val result = ContextCompat.checkSelfPermission(this, perm)
            if(result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 99) {
            var checked = true
            for(grant in grantResults) {
                if(grant != PackageManager.PERMISSION_GRANTED) {
                    checked = false
                    break
                }
            }
            if(checked) {
                startProcess()
            } else {
                Toast.makeText(this, "권한 승인을 하셔야지만 앱을 사용할 수 있습니다.", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    var lastTimeBackPressed : Long = -1;
    override fun onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 2000){
            finish()
            return
        }
        Snackbar.make(viewPager, "뒤로가기 버튼을 한번 더 눌러 종료", Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
        lastTimeBackPressed = System.currentTimeMillis();
    }

}
class FragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    val fragments = mutableListOf<Fragment>()
    override fun getItemCount(): Int {
        return fragments.size
    }
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}