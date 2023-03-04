package com.cong.gitsync.model.dao

import android.content.Context
import androidx.room.*
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class Meta(
    var CreateTime: OffsetDateTime,
    var UpdateTime: OffsetDateTime,
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
class GitRepo(@Embedded var spec: GitRepoSpec) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @Embedded
    var meta: Meta

    init {
        val now = OffsetDateTime.now()
        meta = Meta(now, now)
    }
}

class MyTypeConverters {

    @TypeConverter
    fun toOffsetDateTime(value: String): OffsetDateTime {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        return formatter.parse(value, OffsetDateTime::from)
    }

    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime): String {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        return date.format(formatter)
    }
}


typealias  GitRepoList = List<GitRepo>

typealias GitRepoId = Long

@Dao
interface GitRepoDao {
    @Insert
    suspend fun create(repo: GitRepo): Long

    @Query("select * from gitrepos")
    suspend fun list(): GitRepoList

    @Query("select * from gitrepos where id=:id")
    suspend fun get(id: GitRepoId): GitRepo?

    @Update
    suspend fun update(repo: GitRepo)

    @Query("delete from gitrepos where id=:id")
    suspend fun delete(id: GitRepoId)
}

@Database(entities = [GitRepo::class], version = 1, exportSchema = false)
@TypeConverters(MyTypeConverters::class)
abstract class DB : RoomDatabase() {
    abstract fun gitRepos(): GitRepoDao

    companion object {
        private var INSTANCE: DB? = null

        fun get(context: Context): DB? {
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

    }
}