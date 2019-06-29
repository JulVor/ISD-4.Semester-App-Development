package com.example.panplaner

import java.security.Timestamp

class Message(val projectId: String, val sendFromId: String, val sendFromName: String, val message: String, val timestamp: Long){
    constructor() : this("", "","", "", -1)
}