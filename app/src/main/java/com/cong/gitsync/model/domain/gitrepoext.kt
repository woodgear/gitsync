package com.cong.gitsync.model.domain

import java.time.Duration
import java.time.OffsetDateTime

fun getDurationString(start: OffsetDateTime, end: OffsetDateTime): String {
    val duration = Duration.between(start, end)
    val days = duration.toDays()
    val hours = duration.toHours() % 24
    val minutes = duration.toMinutes() % 60
    val seconds = duration.seconds % 60
    val ms = duration.toMillis() % 1000

    return when {
        days > 0 -> "${days}d ${hours}h ${minutes}m ${seconds}s"
        hours > 0 -> "${hours}h ${minutes}m ${seconds}s"
        minutes > 0 -> "${minutes}m ${seconds}s"
        seconds > 0 -> "${seconds}s"
        else -> "${ms}ms"
    }
}

suspend fun GitRepoBasicR.show() {
    for (repo in this.list()) {
        println(
            "repo is ${repo.spec.Url} ${repo.id} ${repo.spec.Branch} create ${repo.meta.CreateTime} ${repo.meta.UpdateTime} diff ${
                getDurationString(
                    repo.meta.CreateTime,
                    repo.meta.UpdateTime
                )
            }"
        )
    }
}