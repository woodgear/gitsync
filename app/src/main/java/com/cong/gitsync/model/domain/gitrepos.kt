package com.cong.gitsync.model.domain

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.cong.gitsync.model.dao.Config
import com.cong.gitsync.model.dao.DB
import com.cong.gitsync.model.dao.GitRepo
import com.cong.gitsync.model.dao.GitRepoList
import com.cong.gitsync.model.dao.GitRepoSpec
import java.time.Instant
import java.time.format.DateTimeFormatter

// some dto here
typealias GitRepoDto = GitRepo
typealias Config = Config
typealias GitRepoSpec = GitRepoSpec
typealias GitRepoListDto = GitRepoList
typealias GitRepoId = Long

@RequiresApi(Build.VERSION_CODES.O)
fun now(): String {
   return DateTimeFormatter.ISO_INSTANT.format(Instant.now())

}
class GitRepoBasicR {
    val db: DB
    constructor(db: DB) {
        this.db=db
    }
    constructor(ctx: Context) {
        db = DB.Get(ctx) ?: throw Exception("invalid db")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun add(r: GitRepoDto): GitRepoDto {
        r.meta.CreateTime= now()
        r.meta.UpdateTime= r.meta.CreateTime
        val id = db.GitRepos().Create(r)
        return db.GitRepos().Get(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun update(r: GitRepoDto): GitRepoDto {
        r.meta.UpdateTime= now()
        println("update it ${r.spec.Branch}")
        db.GitRepos().Update(r)
        return db.GitRepos().Get(r.Id)
    }

    // find 有可能找不到这个对象
    fun find(id: GitRepoId): GitRepoDto? {
        return db.GitRepos().Get(id)
    }

    fun list(): GitRepoListDto {
        return db.GitRepos().List()
    }

    // 删除有可能没有找到这个id
    fun delete(id: GitRepoId): GitRepoDto? {
        val repo = find(id)
        if (repo != null) {
           return null
        }
        db.GitRepos().Delete(id)
        return repo
    }
}