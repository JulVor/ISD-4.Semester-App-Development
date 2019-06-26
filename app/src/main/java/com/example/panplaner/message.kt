package com.example.panplaner

import java.security.Timestamp

class Message(val projectId: String, val sendFrom: String, val message: String, val timestamp: Long){
    constructor() : this("", "", "", -1)
}