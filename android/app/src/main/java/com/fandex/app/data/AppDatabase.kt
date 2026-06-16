package com.fandex.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * FANDEX 应用数据库
 *
 * 功能：管理 Room 数据库实例，提供复习进度持久化
 * 输入：Context
 * 输出：AppDatabase 单例实例
 * 流程：首次调用 -> 创建数据库 -> 返回实例；后续调用 -> 直接返回实例
 */
@Database(entities = [ReviewProgress::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * 获取复习进度 DAO
     *
     * 输出：ReviewDao 实例
     */
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * 获取数据库单例
         *
         * 输入：Context
         * 输出：AppDatabase 实例
         * 流程：检查现有实例 -> 不存在则创建 -> 返回实例
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fandex-database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
