package com.example.myapplication.util

enum class TransactionType(val value: String) {
    DEPOSIT("deposit"),
    TRANSFER("transfer"),
    WITHDRAW("withdraw"),
    TOP_UP("topup"),
    BILLING("billing");

    override fun toString(): String = value
}