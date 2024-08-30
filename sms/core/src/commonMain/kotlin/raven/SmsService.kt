package raven

interface SmsService {
    val account: AccountService
    val sender: SmsSender
}