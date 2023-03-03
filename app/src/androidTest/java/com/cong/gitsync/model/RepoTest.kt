package com.cong.gitsync.model

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cong.gitsync.model.dao.DB
import com.cong.gitsync.model.domain.*
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var db: DB

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, DB::class.java
        ).build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun BasicCrud() {
        val url = "https://github.com/woodgear/mc"
        val cli = GitRepoBasicR(db)
        var a = GitRepoDto(GitRepoSpec(url, "master", "/repos", config = Config(false, 0)))
        a = cli.add(a)
        println("reta ${a.spec.Url}")
        assertThat(a.spec.Url, equalTo("https://github.com/woodgear/mc"))
        cli.show()
        a.spec.Branch = "main"
        a = cli.update(a)
        cli.show()
        assertThat(a.spec.Branch, equalTo("main"))
        assertNotEquals(a.meta.CreateTime, a.meta.UpdateTime)
    }
}
