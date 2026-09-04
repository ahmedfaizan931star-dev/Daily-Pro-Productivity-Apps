package com.dailyapps.sablequorum

import android.app.Application
import com.dailyapps.sablequorum.data.AppDatabase
import com.dailyapps.sablequorum.data.DecisionRepository

class SableQuorumApp : Application() {
    val repository: DecisionRepository by lazy {
        DecisionRepository(AppDatabase.get(this).decisionDao())
    }
}
