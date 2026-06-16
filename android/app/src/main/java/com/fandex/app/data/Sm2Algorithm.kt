package com.fandex.app.data

import kotlin.math.max
import kotlin.math.min

/**
 * SM-2 间隔重复算法实现
 *
 * 功能：根据用户答题质量计算下次复习时间和难度因子
 * 输入：当前复习进度、答题质量（0-5）
 * 输出：更新后的复习进度
 *
 * 答题质量等级：
 *   0 - 完全不记得
 *   1 - 有印象但答错
 *   2 - 答对但很困难
 *   3 - 答对但有犹豫
 *   4 - 答对且较轻松
 *   5 - 答对且非常轻松
 */
object Sm2Algorithm {

    /** 最小难度因子 */
    private const val MIN_EASE_FACTOR = 1.3

    /**
     * 计算下次复习进度
     *
     * 输入：当前复习进度、答题质量（0-5）
     * 输出：更新后的 ReviewProgress
     * 流程：
     *   1. 质量 >= 3 时根据间隔公式计算下次间隔
     *   2. 质量 < 3 时重置间隔为1天
     *   3. 更新难度因子
     *   4. 计算下次复习时间戳
     */
    fun calculateNextReview(progress: ReviewProgress, quality: Int): ReviewProgress {
        val normalizedQuality = max(0, min(5, quality))

        val newEaseFactor = if (normalizedQuality >= 3) {
            max(MIN_EASE_FACTOR, progress.easeFactor + (0.1 - (5 - normalizedQuality) * (0.08 + (5 - normalizedQuality) * 0.02)))
        } else {
            progress.easeFactor
        }

        val newInterval = when {
            normalizedQuality < 3 -> 1
            progress.reviewCount == 0 -> 1
            progress.reviewCount == 1 -> 6
            else -> max(1, (progress.intervalDays * newEaseFactor).toInt())
        }

        val now = System.currentTimeMillis()
        val dayMillis = 24 * 60 * 60 * 1000L

        return progress.copy(
            nextReviewAt = now + newInterval * dayMillis,
            lastReviewAt = now,
            reviewCount = progress.reviewCount + 1,
            easeFactor = newEaseFactor,
            intervalDays = newInterval
        )
    }

    /**
     * 创建初始复习进度
     *
     * 输入：模块 ID、文档 slug、标题、分类
     * 输出：初始 ReviewProgress（立即需要复习）
     */
    fun createInitialProgress(
        module: String,
        slug: String,
        title: String,
        category: String
    ): ReviewProgress {
        val now = System.currentTimeMillis()
        return ReviewProgress(
            id = ReviewProgress.createId(module, slug),
            module = module,
            slug = slug,
            title = title,
            category = category,
            nextReviewAt = now,
            lastReviewAt = 0L,
            reviewCount = 0,
            easeFactor = 2.5,
            intervalDays = 0
        )
    }
}
