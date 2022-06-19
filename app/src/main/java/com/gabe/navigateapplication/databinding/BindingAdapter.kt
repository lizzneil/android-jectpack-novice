package com.gabe.navigateapplication.databinding


import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
import com.gabe.navigateapplication.glide.GlideApp
import kotlin.math.roundToInt

/**
 * Binding adapter that loads an image into an ImageView using Glide
 *
 * Also, check the {@link com.gabe.navigateapplication.glide.GlideConfig#applyOptions() applyOptions} method for  config details.
 *  @see com.gabe.navigateapplication.glide.GlideConfig#applyOptions( Context context, GlideBuilder builder )
 */
@BindingAdapter("image_url")
fun loadImage(imageView: ImageView, url: String) {
    GlideApp.with(imageView.context)
        .load(url)
        /***
         * 改到
         *   @see com.gabe.navigateapplication.glide.GlideConfig#applyOptions( Context context, GlideBuilder builder )
         *   配置。 统一配置，减少重复代码
         */
//        .placeholder(R.drawable.ic_launcher_background)
//        .fitCenter()
//        .transform(CircleCrop())
        .into(imageView)
}

/**
 * Binding adapter that sets the view height based on two mandatory attributes:
 * @param matchParent Boolean, if set to true, will set the view's height to 'match_parent'
 * @param layoutHeight Dimen resource that. If [matchParent] is false, the value from this resource will be used
 *          to set the height of the view.
 */
@BindingAdapter("match_parent", "layout_height", requireAll = true)
fun setLayoutHeight(view: View, matchParent: Boolean, layoutHeight: Float) {
    val params = view.layoutParams
    if (matchParent) {
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
    } else {
        params.height = layoutHeight.roundToInt()
    }
    view.layoutParams = params
}

@BindingAdapter("asyncText")
fun setTextAsync(textView: AppCompatTextView, text: String) {
    val params = TextViewCompat.getTextMetricsParams(textView)
    textView.setTextFuture(PrecomputedTextCompat.getTextFuture(text, params, null))
}
