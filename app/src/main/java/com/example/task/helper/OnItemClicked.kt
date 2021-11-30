package com.example.task.helper

import com.example.task.HistoryItem

interface OnItemClicked {
    fun downloadReceipt(historyItem: HistoryItem)
}
