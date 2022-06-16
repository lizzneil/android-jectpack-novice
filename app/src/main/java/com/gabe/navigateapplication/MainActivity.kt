package com.gabe.navigateapplication

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.gabe.navigateapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // 底部导航栏的LOTTIE 动画 。注意菜单 最多能放5个！！(源码规定)
    private val pairAnimationList = listOf(
        "tissue.json" to "首页",
        "chemical.json" to "通讯录",
//        "astronaout.json" to "我的"
        "cow.json" to "setting"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.menu.apply {
            for (i in pairAnimationList.indices) {
                // 添加title
//                add(Menu.NONE, i, Menu.NONE, pairAnimationList[i].second)
                // 设置lottie
                setLottieDrawable(pairAnimationList, navView, i)
            }
        }

        val mainDestinationChangedListener =
            NavController.OnDestinationChangedListener { controller, destination, arguments ->
                val currentItemId = destination.id;
                // 处理 tab 切换，icon 对应调整
                var menu: MenuItem? = null
                for (i in 0 until navView.menu.size()) {
                    menu = navView.menu.getItem(i)
                    if (menu.itemId != currentItemId) {
                        menu.icon =
                            replaceLottieDrawable(
                                pairAnimationList[i].first,
                                navView
                            )
                    } else {
                        val currentIcon = menu.icon as? LottieDrawable
                        currentIcon?.apply {
                            // 开启动画
                            playAnimation()
                            // 无限播放
                            // loop(true)
                        }
                    }
                }
            }
        navController.addOnDestinationChangedListener(mainDestinationChangedListener)
        true
    }


    /*
    *  将lottie转变为drawable 设置给menu
    */
    private fun setLottieDrawable(
        lottieAnimationList: List<Pair<String, String>>,
        bottomNav: BottomNavigationView,
        position: Int
    ) {
        // 转变为lottieDrawable 父类指向子类没有问题。
        bottomNav.menu.getItem(position).icon =
            replaceLottieDrawable(lottieAnimationList[position].first, bottomNav)
    }

    /**
     * 替换成 LottieDrawable
     */
    private fun replaceLottieDrawable(
        jsonKey: String,
        bottomNavigationView: BottomNavigationView
    ): LottieDrawable {
        return LottieDrawable().apply {
            val result = LottieCompositionFactory.fromAssetSync(
                // 加载lottie 数据
                bottomNavigationView.context.applicationContext, jsonKey
            )
            //这个是用来刷新的。
            callback = bottomNavigationView

            // 设置动画
            composition = result.value
        }
    }
}







