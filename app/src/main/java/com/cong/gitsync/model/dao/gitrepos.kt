package com.cong.gitsync.model.dao

import android.content.Context
import androidx.room.*

data class Meta(
    var CreateTime: String,
    var UpdateTime: String,
)

data class Config(
    var Auto: Boolean,
    var AutoTimer: Int,
)

// 代表需要存在数据库中的要同步的repo的配置
data class GitRepoSpec(
    var Url: String,
    var Branch: String,
    var Dest: String,
    @Embedded var config: Config
)

@Entity(tableName = "gitrepos")
class GitRepo(spec: GitRepoSpec) {
    @PrimaryKey(autoGenerate = true)
    var Id: Long=0
    @Embedded
    var meta: Meta= Meta("","")
    @Embedded
    var spec: GitRepoSpec = spec
}

typealias  GitRepoList = List<GitRepo>

typealias GitRepoId = Long
@Dao
interface GitRepoDao {
    @Insert
    fun Create(repo: GitRepo):Long

    @Query("select * from gitrepos")
    fun List(): GitRepoList

    @Query("select * from gitrepos where id=:id")
    fun Get(id:GitRepoId): GitRepo

    @Update
    fun Update(repo: GitRepo)

    @Query("delete from gitrepos where id=:id")
    fun Delete(id: GitRepoId)
}

@Database(entities = [GitRepo::class], version = 1, exportSchema = false)
abstract class DB : RoomDatabase() {
    abstract fun GitRepos(): GitRepoDao

    companion object {
        private var INSTANCE: DB? = null

        fun Get(context: Context): DB? {
            if (INSTANCE == null) {
                synchronized(DB::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DB::class.java, "gitsync"
                    ).allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}