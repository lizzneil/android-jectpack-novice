package com.gabe.navigateapplication.glide

import android.content.Context
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.gabe.navigateapplication.R

//配置 定制公用glide的方法。会生成 类GlideApp  替代Glide 少重复配置
//https://github.com/bumptech/glide/wiki/Configuration
//Starting in Glide 3.5,
// you can use the GlideModule interface to lazily configure Glide
// and register components like ModelLoaders automatically
// when the first Glide request is made.
// 具体方法如下：
//1。  Creating a GlideModule  ///   public class MyGlideModule implements GlideModule
//2。  Keeping a GlideModule ////add your implementation class to your proguard.cfg/proguard.pro file to allow your module to be instantiated via reflection.
//3。Registering a GlideModule /////Finally add a meta-data tag to your AndroidManifest.xml file so Glide can find your module:
//other： Conflicting GlideModules   排除错误的///<meta-data android:name="com.mypackage.MyGlideModule" tools:node="remove" />

//append：
//
// Glide 4.0 need not have declare "GlideModule" in AndroidManifest.xml. You just need to following steps:
//
//YourGlideModule extends AppGlideModule, you can override function applyOptions in the YourGlideModule class.
//You should make project in "android studio -> build -> make project", it will generate the GlideApp class.
//Use the GlideApp such as GlideApp.with(this).load(imgUrl).into(glide_test_iv1)

//https://muyangmin.github.io/glide-docs-cn/doc/configuration.html  文档不错,详细，层次结构清晰

@GlideModule
class GlideConfig : AppGlideModule() {


     override fun applyOptions(context: Context, builder: GlideBuilder) {
        // Default empty impl.
        builder.setDiskCache(ExternalCacheDiskCacheFactory(context))
            .setLogLevel(Log.DEBUG);

        builder.setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_baseline_account_circle)
                .fitCenter()
                .transform(CircleCrop())
        )
    }


    //1，运行阶段，实例化glide时，报这个错，暂时这样解，但感觉这个方法不好。看源码看出来的。不是正规路线。
    //Fatal Exception: java.lang.RuntimeException: Expected instanceof GlideModule, but found:
//2，APPEND；   高版本不需要在manifest里声明。所以不需要分析。 换高版本就没事了。
//    override fun isManifestParsingEnabled(): Boolean {
//        return false
//    }


//    override fun applyOptions(context: Context, builder: GlideBuilder) {
//        // Default empty impl.
//    }
//    override fun registerComponents(
//        context: Context, glide: Glide, registry: Registry
//    ) {
//        // Default empty impl.
//    }

}
