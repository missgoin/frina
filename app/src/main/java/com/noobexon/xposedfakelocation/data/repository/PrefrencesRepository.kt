// PreferencesRepository.kt
package com.noobexon.xposedfakelocation.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noobexon.xposedfakelocation.data.*
import com.noobexon.xposedfakelocation.data.model.FavoriteLocation
import com.noobexon.xposedfakelocation.data.model.LastClickedLocation

class PreferencesRepository(context: Context) {
    private val tag = "PreferencesRepository"

    @SuppressLint("WorldReadableFiles")
    private val sharedPrefs = try {
        context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_WORLD_READABLE)
    } catch (e: SecurityException) {
        context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE) // Fallback to MODE_PRIVATE
    }

    private val gson = Gson()

    // Is Playing
    fun saveIsPlaying(isPlaying: Boolean) {
        sharedPrefs.edit()
            .putBoolean(KEY_IS_PLAYING, isPlaying)
            .apply()
        Log.d(tag, "Saved IsPlaying: $isPlaying")
    }

    // Last Clicked Location
    fun saveLastClickedLocation(latitude: Double, longitude: Double) {
        val location = LastClickedLocation(latitude, longitude)
        val json = gson.toJson(location)
        sharedPrefs.edit()
            .putString(KEY_LAST_CLICKED_LOCATION, json)
            .apply()
        Log.d(tag, "Saved LastClickedLocation: $json")
    }

    fun clearNonPersistentSettings() {
        sharedPrefs.edit()
            .remove(KEY_LAST_CLICKED_LOCATION)
            .apply()
        saveIsPlaying(false)
        Log.d(tag, "Cleared 'LastClickedLocation' from shared preferences and set 'IsPlaying' to false")
    }

    // Settings
    fun saveUseAccuracy(useAccuracy: Boolean) {
        sharedPrefs.edit()
            .putBoolean(KEY_USE_ACCURACY, useAccuracy)
            .apply()
        Log.d(tag, "Saved UseAccuracy: $useAccuracy")
    }

    fun getUseAccuracy(): Boolean {
        return sharedPrefs.getBoolean(KEY_USE_ACCURACY, DEFAULT_USE_ACCURACY)
    }

    fun saveAccuracy(accuracy: Double) {
        val bits = java.lang.Double.doubleToRawLongBits(accuracy)
        sharedPrefs.edit()
            .putLong(KEY_ACCURACY, bits)
            .apply()
        Log.d(tag, "Saved Accuracy: $accuracy")
    }

    fun getAccuracy(): Double {
        val bits = sharedPrefs.getLong(KEY_ACCURACY, java.lang.Double.doubleToRawLongBits(DEFAULT_ACCURACY))
        return java.lang.Double.longBitsToDouble(bits)
    }

    fun saveUseAltitude(useAltitude: Boolean) {
        sharedPrefs.edit()
            .putBoolean(KEY_USE_ALTITUDE, useAltitude)
            .apply()
        Log.d(tag, "Saved UseAltitude: $useAltitude")
    }

    fun getUseAltitude(): Boolean {
        return sharedPrefs.getBoolean(KEY_USE_ALTITUDE, DEFAULT_USE_ALTITUDE)
    }

    fun saveAltitude(altitude: Double) {
        val bits = java.lang.Double.doubleToRawLongBits(altitude)
        sharedPrefs.edit()
            .putLong(KEY_ALTITUDE, bits)
            .apply()
        Log.d(tag, "Saved Altitude: $altitude")
    }

    fun getAltitude(): Double {
        val bits = sharedPrefs.getLong(KEY_ALTITUDE, java.lang.Double.doubleToRawLongBits(DEFAULT_ALTITUDE))
        return java.lang.Double.longBitsToDouble(bits)
    }

    fun saveUseRandomize(randomize: Boolean) {
        sharedPrefs.edit()
            .putBoolean(KEY_USE_RANDOMIZE, randomize)
            .apply()
        Log.d(tag, "Saved UseRandomize: $randomize")
    }

    fun getUseRandomize(): Boolean {
        return sharedPrefs.getBoolean(KEY_USE_RANDOMIZE, DEFAULT_USE_RANDOMIZE)
    }

    fun saveRandomizeRadius(radius: Double) {
        val bits = java.lang.Double.doubleToRawLongBits(radius)
        sharedPrefs.edit()
            .putLong(KEY_RANDOMIZE_RADIUS, bits)
            .apply()
        Log.d(tag, "Saved RandomizeRadius: $radius")
    }

    fun getRandomizeRadius(): Double {
        val bits = sharedPrefs.getLong(
            KEY_RANDOMIZE_RADIUS,
            java.lang.Double.doubleToRawLongBits(DEFAULT_RANDOMIZE_RADIUS)
        )
        return java.lang.Double.longBitsToDouble(bits)
    }

    // Favorites
    fun addFavorite(favorite: FavoriteLocation) {
        val favorites = getFavorites().toMutableList()
        favorites.add(favorite)
        saveFavorites(favorites)
        Log.d(tag, "Added Favorite: $favorite")
    }

    fun getFavorites(): List<FavoriteLocation> {
        val json = sharedPrefs.getString("favorites", null)
        return if (json != null) {
            val type = object : TypeToken<List<FavoriteLocation>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    private fun saveFavorites(favorites: List<FavoriteLocation>) {
        val json = gson.toJson(favorites)
        sharedPrefs.edit()
            .putString("favorites", json)
            .apply()
        Log.d(tag, "Saved Favorites: $json")
    }

    fun removeFavorite(favorite: FavoriteLocation) {
        val favorites = getFavorites().toMutableList()
        favorites.remove(favorite)
        saveFavorites(favorites)
        Log.d(tag, "Removed Favorite: $favorite from shared preferences")
    }

    // Vertical Accuracy
    fun saveUseVerticalAccuracy(useVerticalAccuracy: Boolean) {
        sharedPrefs.edit()
            .putBoolean(KEY_USE_VERTICAL_ACCURACY, useVerticalAccuracy)
            .apply()
        Log.d(tag, "Saved UseVerticalAccuracy: $useVerticalAccuracy")
    }

    fun getUseVerticalAccuracy(): Boolean {
        return sharedPrefs.getBoolean(KEY_USE_VERTICAL_ACCURACY, DEFAULT_USE_VERTICAL_ACCURACY)
    }

    fun saveVerticalAccuracy(verticalAccuracy: Float) {
        sharedPrefs.edit()
            .putFloat(KEY_VERTICAL_ACCURACY, verticalAccuracy)
            .apply()
        Log.d(tag, "Saved VerticalAccuracy: $verticalAccuracy")
    }

    fun getVerticalAccuracy(): Float {
        return sharedPrefs.getFloat(KEY_VERTICAL_ACCURACY, DEFAULT_VERTICAL_ACCURACY)
    }

    fun saveUseMeanSeaLevel(useMeanSeaLevel: Boolean) {
        sharedPrefs.edit()
            .putBoolean(KEY_USE_MEAN_SEA_LEVEL, useMeanSeaLevel)
            .apply()
        Log.d(tag, "Saved UseMeanSeaLevel: $useMeanSeaLevel")
    }

    fun getUseMeanSeaLevel(): Boolean {
        return sharedPrefs.getBoolean(KEY_USE_MEAN_SEA_LEVEL, DEFAULT_USE_MEAN_SEA_LEVEL)
    }

    fun saveMeanSeaLevel(meanSeaLevel: Double) {
        val bits = java.lang.Double.doubleToRawLongBits(meanSeaLevel)
        sharedPrefs.edit()
            .putLong(KEY_MEAN_SEA_LEVEL, bits)
            .apply()
        Log.d(tag, "Saved MeanSeaLevel: $meanSeaLevel")
    }

    fun getMeanSeaLevel(): Double {
        val bits = sharedPrefs.getLong(KEY_MEAN_SEA_LEVEL, java.lang.Double.doubleToRawLongBits(DEFAULT_MEAN_SEA_LEVEL))
        return java.lang.Double.longBitsToDouble(bits)
    }

    fun saveUseMeanSeaLevelAccuracy(useMeanSeaLevelAccuracy: Boolean) {
        sharedPrefs.edit()
            .putBoolean(KEY_USE_MEAN_SEA_LEVEL_ACCURACY, useMeanSeaLevelAccuracy)
            .apply()
        Log.d(tag, "Saved UseMeanSeaLevelAccuracy: $useMeanSeaLevelAccuracy")
    }

    fun getUseMeanSeaLevelAccuracy(): Boolean {
        return sharedPrefs.getBoolean(KEY_USE_MEAN_SEA_LEVEL_ACCURACY, DEFAULT_USE_MEAN_SEA_LEVEL_ACCURACY)
    }

    fun saveMeanSeaLevelAccuracy(meanSeaLevelAccuracy: Float) {
        sharedPrefs.edit()
            .putFloat(KEY_MEAN_SEA_LEVEL_ACCURACY, meanSeaLevelAccuracy)
            .apply()
        Log.d(tag, "Saved MeanSeaLevelAccuracy: $meanSeaLevelAccuracy")
    }

    fun getMeanSeaLevelAccuracy(): Float {
        return sharedPrefs.getFloat(KEY_MEAN_SEA_LEVEL_ACCURACY, DEFAULT_MEAN_SEA_LEVEL_ACCURACY)
    }

    fun saveUseSpeed(useSpeed: Boolean) {
        sharedPrefs.edit()
            .putBoolean(KEY_USE_SPEED, useSpeed)
            .apply()
        Log.d(tag, "Saved UseSpeed: $useSpeed")
    }

    fun getUseSpeed(): Boolean {
        return sharedPrefs.getBoolean(KEY_USE_SPEED, DEFAULT_USE_SPEED)
    }

    fun saveSpeed(speed: Float) {
        sharedPrefs.edit()
            .putFloat(KEY_SPEED, speed)
            .apply()
        Log.d(tag, "Saved Speed: $speed")
    }

    fun getSpeed(): Float {
        return sharedPrefs.getFloat(KEY_SPEED, DEFAULT_SPEED)
    }

    fun saveUseSpeedAccuracy(useSpeedAccuracy: Boolean) {
        sharedPrefs.edit()
            .putBoolean(KEY_USE_SPEED_ACCURACY, useSpeedAccuracy)
            .apply()
        Log.d(tag, "Saved UseSpeedAccuracy: $useSpeedAccuracy")
    }

    fun getUseSpeedAccuracy(): Boolean {
        return sharedPrefs.getBoolean(KEY_USE_SPEED_ACCURACY, DEFAULT_USE_SPEED_ACCURACY)
    }

    fun saveSpeedAccuracy(speedAccuracy: Float) {
        sharedPrefs.edit()
            .putFloat(KEY_SPEED_ACCURACY, speedAccuracy)
            .apply()
        Log.d(tag, "Saved SpeedAccuracy: $speedAccuracy")
    }

    fun getSpeedAccuracy(): Float {
        return sharedPrefs.getFloat(KEY_SPEED_ACCURACY, DEFAULT_SPEED_ACCURACY)
    }
}