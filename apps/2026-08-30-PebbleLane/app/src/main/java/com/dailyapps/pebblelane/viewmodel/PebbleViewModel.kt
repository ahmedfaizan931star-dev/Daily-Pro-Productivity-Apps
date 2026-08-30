package com.dailyapps.pebblelane.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyapps.pebblelane.data.model.Closeout
import com.dailyapps.pebblelane.data.model.Meeting
import com.dailyapps.pebblelane.data.model.ProtectedBlock
import com.dailyapps.pebblelane.data.repository.PebbleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PebbleViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = PebbleRepository(app)

    val meetings: StateFlow<List<Meeting>> = repo.meetings.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()
    )
    val blocks: StateFlow<List<ProtectedBlock>> = repo.blocks.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()
    )
    val closeouts: StateFlow<List<Closeout>> = repo.closeouts.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()
    )

    fun addMeeting(title: String, minutes: Int, drain: Int) =
        viewModelScope.launch { repo.addMeeting(title, minutes, drain) }
    fun deleteMeeting(item: Meeting) = viewModelScope.launch { repo.deleteMeeting(item) }
    fun addBlock(label: String, minutes: Int) =
        viewModelScope.launch { repo.addBlock(label, minutes) }
    fun toggleBlock(item: ProtectedBlock) = viewModelScope.launch { repo.toggleBlockDone(item) }
    fun deleteBlock(item: ProtectedBlock) = viewModelScope.launch { repo.deleteBlock(item) }
    fun addCloseout(wins: String, leftover: String) =
        viewModelScope.launch { repo.addCloseout(wins, leftover) }
}
