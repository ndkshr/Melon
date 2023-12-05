package me.ndkshr.melon.worker

interface SongDownloader {
    fun downloadFile(prompt: String, url: String): Long
}