package com.shidqi.phinconminiproject.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.shidqi.phinconminiproject.R
import com.shidqi.phinconminiproject.models.remote.pokemonDetail.Type
import java.util.*


fun changeContainerColorWithGradient(context: Context, imageUrl: String, imageView: ImageView, imageViewContainer:View) {
    Log.d("bindData2", imageUrl)
    Glide.with(context)
        .asBitmap()
        .load(imageUrl)
        .apply(RequestOptions.centerCropTransform())
        .into(object : BitmapImageViewTarget(imageView) {
            override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                val dominantColor = getDominantColor(bitmap)
                imageView.setImageBitmap(bitmap)
                setContainerGradientBackground(imageViewContainer, dominantColor)
            }
        })
}

private fun getDominantColor(bitmap: Bitmap): Int {
    val palette = androidx.palette.graphics.Palette.from(bitmap).generate()
    return palette.getDominantColor(Color.WHITE)
}

private fun setContainerGradientBackground(container: View, dominantColor: Int) {
    val strokeValue = 6
    val gradientDrawable = GradientDrawable(
        GradientDrawable.Orientation.TOP_BOTTOM,
        intArrayOf(dominantColor,Color.WHITE)
    )
    gradientDrawable.cornerRadius = 15f

    val shadow = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(dominantColor,Color.WHITE))
    shadow.cornerRadius = 15f
    val layers = arrayOfNulls<Drawable>(2)
    layers[0] = gradientDrawable
    layers[1] = shadow
    val layerList = LayerDrawable(layers)
    layerList.setLayerInset(0, 0, 0, 0, 0)
    layerList.setLayerInset(1, strokeValue, strokeValue, strokeValue, strokeValue)
    container.background = layerList
}

fun parseTypeToColor(type: Type): Int {
    return when(type.type.name.toLowerCase(Locale.ROOT)) {
        "normal" -> R.color.Green
        "fire" -> R.color.Red
        "water" -> R.color.Aqua
        "electric" -> R.color.Green
        "grass" -> R.color.Green
        "ice" -> R.color.Green
        "fighting" -> R.color.CornflowerBlue
        "poison" -> R.color.LimeGreen
        "ground" -> R.color.Green
        "flying" -> R.color.Green
        "psychic" -> R.color.Green
        "bug" -> R.color.MediumSeaGreen
        "rock" -> R.color.DimGray
        "ghost" -> R.color.DarkBlue
        "dragon" -> R.color.Aquamarine
        "dark" -> R.color.DeepSkyBlue
        "steel" -> R.color.SpringGreen
        "fairy" -> R.color.Lime
        else -> R.color.Black
    }
}