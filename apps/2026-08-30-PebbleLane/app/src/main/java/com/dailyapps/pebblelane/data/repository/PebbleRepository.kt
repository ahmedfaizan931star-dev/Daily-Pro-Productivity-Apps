package com.dailyapps.pebblelane.data.repository

import android.app.Application
import com.dailyapps.pebblelane.data.local.AppDatabase
import com.dailyapps.pebblelane.data.model.Closeout
import com.dailyapps.pebblelane.data.model.Meeting
import com.dailyapps.pebblelane.data.model.ProtectedBlock

class PebbleRepository(app: Application) {
    private val db = AppDatabase.get(app)
    val meetings = db.meetingDao().observe()
    val blocks = db.blockDao().observe()
    val closeouts = db.closeoutDao().observe()

    suspend fun addMeeting(title: String, minutes: Int, drain: Int) {
        db.meetingDao().insert(Meeting(title = title, minutes = minutes, drain = drain))
    }
    suspend fun deleteMeeting(item: Meeting) = db.meetingDao().delete(item)

    suspend fun addBlock(label: String, minutes: Int) {
        db.blockDao().insert(ProtectedBlock(label = label, minutes = minutes))
    }
    suspend fun toggleBlockDone(item: ProtectedBlock) {
        db.blockDao().update(item.copy(done = !item.done))
    }
    suspend fun deleteBlock(item: ProtectedBlock) = db.blockDao().delete(item)

    suspend fun addCloseout(wins: String, leftover: String) {
        db.closeoutDao().insert(Closeout(wins = wins, leftover = leftover))
    }
}
