package org.wordpress.android.ui.reader.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.wordpress.android.models.ReaderTagList
import org.wordpress.android.modules.UI_THREAD
import org.wordpress.android.ui.reader.usecases.LoadReaderTabsUseCase
import org.wordpress.android.viewmodel.ScopedViewModel
import javax.inject.Inject
import javax.inject.Named

class ReaderViewModel @Inject constructor(
    @Named(UI_THREAD) private val mainDispatcher: CoroutineDispatcher,
    private val loadReaderTabsUseCase: LoadReaderTabsUseCase
) : ScopedViewModel(mainDispatcher) {
    private var started: Boolean = false
    private val _uiState = MutableLiveData<ReaderUiState>()
    val uiState: LiveData<ReaderUiState> = _uiState

    fun start() {
        if (started) return
        started = true
        loadTabs()
    }

    private fun loadTabs() {
        launch {
            val tagList = loadReaderTabsUseCase.loadTabs()
            _uiState.value = ReaderUiState(
                    tagList.map { it.tagDisplayName }, // TODO should we use displayname or title?
                    tagList
            )
        }
    }

    data class ReaderUiState(val tabTitles: List<String>, val readerTagList: ReaderTagList)
}
