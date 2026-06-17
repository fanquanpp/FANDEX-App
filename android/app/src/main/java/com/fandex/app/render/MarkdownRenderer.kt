package com.fandex.app.render

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan

/**
 * 极简 Markdown/LaTeX 渲染器
 * 逐行解析 Markdown 文本，输出 SpannableStringBuilder
 * 零第三方依赖，纯 Android SDK Span 实现
 */
object MarkdownRenderer {

    /* 代码块背景色（浅灰） */
    private const val CODE_BG_COLOR = 0xFFF5F5F5.toInt()
    /* 公式前景色（深蓝灰） */
    private const val FORMULA_FG_COLOR = 0xFF4A5568.toInt()
    /* 标题颜色（主蓝） */
    private const val HEADING_COLOR = 0xFF3366CC.toInt()

    /**
     * 渲染 Markdown 文本为 SpannableStringBuilder
     * 输入：原始 Markdown 文本
     * 输出：带 Span 样式的可显示文本
     */
    fun render(content: String): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        val lines = content.split("\n")
        var inCodeBlock = false

        for (line in lines) {
            when {
                /* 围栏代码块开始/结束 */
                line.trimStart().startsWith("```") -> {
                    inCodeBlock = !inCodeBlock
                    if (inCodeBlock) {
                        /* 代码块开始：输出语言标签（如果有） */
                        val lang = line.trimStart().removePrefix("```").trim()
                        if (lang.isNotBlank()) {
                            val start = builder.length
                            builder.append("$lang\n")
                            builder.setSpan(
                                StyleSpan(Typeface.BOLD),
                                start, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                    } else {
                        builder.append("\n")
                    }
                    continue
                }

                /* 代码块内容 */
                inCodeBlock -> {
                    val start = builder.length
                    builder.append(line)
                    builder.setSpan(
                        StyleSpan(Typeface.MONOSPACE.style),
                        start, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    builder.append("\n")
                }

                /* 标题（1-3 级） */
                line.trimStart().matches(Regex("^#{1,3}\\s.+")) -> {
                    val trimmed = line.trimStart()
                    val level = trimmed.takeWhile { it == '#' }.length
                    val text = trimmed.drop(level).trim()
                    val start = builder.length
                    builder.append(text)
                    val sizeScale = when (level) {
                        1 -> 1.6f
                        2 -> 1.35f
                        else -> 1.15f
                    }
                    builder.setSpan(
                        RelativeSizeSpan(sizeScale),
                        start, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    builder.setSpan(
                        StyleSpan(Typeface.BOLD),
                        start, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    builder.setSpan(
                        ForegroundColorSpan(HEADING_COLOR),
                        start, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    builder.append("\n")
                }

                /* 块级公式 $$...$$ */
                line.trim().startsWith("$$") && line.trim().endsWith("$$") && line.trim().length > 4 -> {
                    val formula = line.trim().drop(2).dropLast(2).trim()
                    val start = builder.length
                    builder.append("  $formula  ")
                    builder.setSpan(
                        StyleSpan(Typeface.MONOSPACE.style or Typeface.ITALIC),
                        start, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    builder.setSpan(
                        ForegroundColorSpan(FORMULA_FG_COLOR),
                        start, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    builder.append("\n")
                }

                /* 普通段落（含行内公式 $...$） */
                else -> {
                    val processed = processInlineElements(line)
                    builder.append(processed)
                    builder.append("\n")
                }
            }
        }
        return builder
    }

    /**
     * 处理行内元素：粗体、斜体、行内代码、行内公式
     */
    private fun processInlineElements(line: String): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        /* 简化处理：直接输出原始文本，保留 $ 标记便于识别公式 */
        /* 行内代码 `...` */
        val codePattern = Regex("`([^`]+)`")
        var lastEnd = 0
        codePattern.findAll(line).forEach { match ->
            if (match.range.first > lastEnd) {
                builder.append(line.substring(lastEnd, match.range.first))
            }
            val start = builder.length
            builder.append(match.groupValues[1])
            builder.setSpan(
                StyleSpan(Typeface.MONOSPACE.style),
                start, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            builder.setSpan(
                BackgroundColorSpan(CODE_BG_COLOR),
                start, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            lastEnd = match.range.last + 1
        }
        if (lastEnd < line.length) {
            builder.append(line.substring(lastEnd))
        }
        return builder
    }
}
