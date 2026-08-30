package com.dailyapps.covedraft.data.repository

import android.content.Context
import com.dailyapps.covedraft.data.local.AppDatabase
import com.dailyapps.covedraft.data.model.Closeout
import com.dailyapps.covedraft.data.model.Decision
import com.dailyapps.covedraft.data.model.Draft

class CoveRepository(context: Context) {
    private val db = AppDatabase.get(context)
    val drafts = db.draftDao().observe()
    val decisions = db.decisionDao().observe()
    val closeouts = db.closeoutDao().observe()

    suspend fun addDraft(title: String, note: String) =
        db.draftDao().insert(Draft(title = title, note = note))
    suspend fun toggleLaunch(draft: Draft) =
        db.draftDao().update(draft.copy(launched = !draft.launched, parked = draft.launched))
    suspend fun deleteDraft(id: Long) = db.draftDao().delete(id)
    suspend fun addDecision(request: String, verdict: String) =
        db.decisionDao().insert(Decision(request = request, verdict = verdict))
    suspend fun deleteDecision(id: Long) = db.decisionDao().delete(id)
    suspend fun addCloseout(win: String, leftover: String) =
        db.closeoutDao().insert(Closeout(win = win, leftover = leftover))
}
