package com.fandex.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * 复习进度数据访问对象
 *
 * 功能：提供复习进度记录的 CRUD 操作
 * 输入：Room 数据库操作请求
 * 输出：复习进度查询结果
 */
@Dao
interface ReviewDao {

    /**
     * 查询所有待复习的记录
     *
     * 输入：当前时间戳
     * 输出：nextReviewAt <= 当前时间的记录列表
     */
    @Query("SELECT * FROM review_progress WHERE nextReviewAt <= :currentTime ORDER BY nextReviewAt ASC")
    suspend fun getDueReviews(currentTime: Long): List<ReviewProgress>

    /**
     * 查询所有复习进度记录
     *
     * 输出：全部复习进度记录
     */
    @Query("SELECT * FROM review_progress ORDER BY nextReviewAt ASC")
    suspend fun getAllReviews(): List<ReviewProgress>

    /**
     * 根据标识查询复习进度
     *
     * 输入：记录 ID
     * 输出：对应的复习进度记录，不存在则返回 null
     */
    @Query("SELECT * FROM review_progress WHERE id = :id")
    suspend fun getReviewById(id: String): ReviewProgress?

    /**
     * 统计待复习数量
     *
     * 输入：当前时间戳
     * 输出：待复习记录数
     */
    @Query("SELECT COUNT(*) FROM review_progress WHERE nextReviewAt <= :currentTime")
    suspend fun getDueCount(currentTime: Long): Int

    /**
     * 统计已掌握数量（复习次数 >= 5）
     *
     * 输出：已掌握记录数
     */
    @Query("SELECT COUNT(*) FROM review_progress WHERE reviewCount >= 5")
    suspend fun getMasteredCount(): Int

    /**
     * 插入或更新复习进度
     *
     * 输入：ReviewProgress 对象
     * 输出：冲突时替换已有记录
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertReview(review: ReviewProgress)

    /**
     * 删除指定复习进度
     *
     * 输入：记录 ID
     */
    @Query("DELETE FROM review_progress WHERE id = :id")
    suspend fun deleteReview(id: String)
}
