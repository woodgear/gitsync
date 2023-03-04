package com.cong.gitsync.model.domain

import android.content.Context
import com.cong.gitsync.model.dao.*
import com.cong.gitsync.model.dao.Config
import com.cong.gitsync.model.dao.GitRepoSpec
import java.time.OffsetDateTime

// some dto here
typealias GitRepoDto = GitRepo
typealias Config = Config
typealias GitRepoSpec = GitRepoSpec
typealias GitRepoListDto = GitRepoList
typealias GitRepoId = Long


class GitRepoBasicR {
    val db: DB

    // for mock
    constructor(db: DB) {
        this.db = db
    }

    constructor(ctx: Context) {
        db = DB.get(ctx) ?: throw Exception("invalid db")
    }

    suspend fun add(r: GitRepoDto): GitRepoDto {
        r.meta.CreateTime = OffsetDateTime.now()
        r.meta.UpdateTime = r.meta.CreateTime
        val id = db.gitRepos().create(r)
        return db.gitRepos().get(id)!!
    }

    suspend fun update(r: GitRepoDto): GitRepoDto {
        r.meta.UpdateTime = OffsetDateTime.now()
        println("update it ${r.spec.Branch}")
        db.gitRepos().update(r)
        return db.gitRepos().get(r.id)!!
    }

    // find 有可能找不到这个对象
    suspend fun find(id: GitRepoId): GitRepoDto? {
        return db.gitRepos().get(id)
    }

    suspend fun list(): GitRepoListDto {
        return db.gitRepos().list()
    }

    // 删除有可能没有找到这个id
    suspend fun delete(id: GitRepoId): GitRepoDto? {
        val repo = find(id)
        if (repo != null) {
            return null
        }
        db.gitRepos().delete(id)
        return repo
    }
}