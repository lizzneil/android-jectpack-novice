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
//1。  Creating a GlideModule  ///   public class MyGlideModule implements GlideModule
//2。  Keeping a GlideModule ////add your implementation class to your proguard.cfg/proguard.pro file to allow your module to be instantiated via reflection.
//3。Registering a GlideModule /////Finally add a meta-data tag to your AndroidManifest.xml file so Glide can find your module:
//other： Conflicting GlideModules   排除错误的///<meta-data android:name="com.mypackage.MyGlideModule" tools:node="remove" />
@GlideModule
class GlideConfig : AppGlideModule() {


     override fun applyOptions(context: Context, builder: GlideBuilder) {
        // Default empty impl.
        builder.setDiskCache(ExternalCacheDiskCacheFactory(context))
            .setLogLevel(Log.DEBUG);

        builder.setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_launcher_background)
                .fitCenter()
                .transform(CircleCrop())
        )
    }


    //运行阶段，实例化glide时，报这个错，暂时这样解，但感觉这个方法不好。看源码看出来的。不是正规路线。
    //Fatal Exception: java.lang.RuntimeException: Expected instanceof GlideModule, but found:
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
//    override fun applyOptions(context: Context, builder: GlideBuilder) {
//        // Default empty impl.
//    }
//    override fun registerComponents(
//        context: Context, glide: Glide, registry: Registry
//    ) {
//        // Default empty impl.
//    }

}
