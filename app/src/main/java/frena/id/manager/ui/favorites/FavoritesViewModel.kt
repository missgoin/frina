package frena.id.manager.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import frena.id.data.model.FavoriteLocation
import frena.id.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesRepository = PreferencesRepository(application)

    private val _favorites = MutableStateFlow<List<FavoriteLocation>>(emptyList())
    val favorites: StateFlow<List<FavoriteLocation>> get() = _favorites

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        _favorites.value = preferencesRepository.getFavorites()
    }

    fun removeFavorite(favorite: FavoriteLocation) {
        preferencesRepository.removeFavorite(favorite)
        loadFavorites()
    }
}