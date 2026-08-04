/**
 * 轻量 Markdown → HTML，供 <rich-text> 渲染。
 *
 * 小程序 rich-text 支持的标签有限（没有 pre、不支持 class），因此：
 * - 代码块用 <div> 包裹 <code> 模拟
 * - 所有样式用内联 style（rich-text 不解析 class）
 * - 块级元素直接拼 HTML，块与块之间不加换行（避免 pre-wrap 产生多余空行）
 *
 * 支持：标题 / 无序有序列表 / 引用块 / 代码块 / 行内代码 / 粗体斜体删除线 / 链接 / 分割线 / 表格
 */

function escapeHtml(s) {
  return String(s == null ? '' : s)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
}

const CODE_STYLE = 'font-size:24rpx;color:#d6336c;background:#f2f3f5;padding:2rpx 6rpx;border-radius:6rpx;'
const LINK_STYLE = 'color:#4a67ff;'
const P_STYLE = 'margin:0 0 14rpx;line-height:1.7;word-break:break-word;'

/** 行内元素：行内代码 / 粗体 / 斜体 / 删除线 / 链接（入参需已转义） */
function inline(text) {
  if (!text) return ''
  return text
    .replace(/`([^`]+)`/g, (m, code) => `<code style="${CODE_STYLE}">${code}</code>`)
    .replace(/\*\*\*([^*]+)\*\*\*/g, '<strong><em>$1</em></strong>')
    .replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>')
    .replace(/~~([^~]+)~~/g, '<del>$1</del>')
    .replace(/\*([^*]+)\*/g, '<em>$1</em>')
    .replace(/\[([^\]]+)\]\(([^)]+)\)/g, `<a href="$2" style="${LINK_STYLE}">$1</a>`)
}

/** Markdown 文本 → rich-text 可渲染的 HTML */
function mdToHtml(md) {
  if (!md) return ''
  const text = String(md).replace(/\r\n/g, '\n').replace(/^﻿/, '')

  // 1. 先提取代码块（内容单独转义，脱离后续的 markdown 处理）
  const codeBlocks = []
  const noCode = text.replace(/```[^\n]*\n?([\s\S]*?)```/g, (block, code) => {
    codeBlocks.push(
      '<div style="background:#f5f6fa;border-radius:10rpx;padding:16rpx 20rpx;margin:0 0 14rpx;">' +
      '<code style="color:#333;font-size:26rpx;line-height:1.6;word-break:break-all;white-space:pre-wrap;">' +
      escapeHtml(code.replace(/^\n/, '').replace(/\n$/, '')) +
      '</code></div>'
    )
    return '@@KANO_CODE_' + (codeBlocks.length - 1) + '@@'
  })

  // 2. 其余内容整体转义，防止注入
  const safe = escapeHtml(noCode)
  const lines = safe.split('\n')

  const out = []
  let pendingText = [] // 段落文本行
  let listType = ''    // '' | 'ul' | 'ol'
  let listItems = []
  let tableRows = []

  const flushText = () => {
    if (!pendingText.length) return
    out.push(`<p style="${P_STYLE}">` + pendingText.map(inline).join('<br>') + '</p>')
    pendingText = []
  }
  const flushList = () => {
    if (!listType) return
    out.push(`<${listType} style="margin:0 0 14rpx;padding-left:44rpx;">` + listItems.join('') + `</${listType}>`)
    listType = ''
    listItems = []
  }
  const flushTable = () => {
    if (!tableRows.length) return
    let html = '<table style="border-collapse:collapse;margin:0 0 14rpx;width:100%;">'
    let sawSep = false
    for (const row of tableRows) {
      const cells = row.replace(/^\||\|$/g, '').split('|').map((c) => c.trim())
      // 分隔行（| --- | --- |）之前的第一行视为表头
      if (!sawSep && cells.length && cells.every((c) => /^:?-{2,}:?$/.test(c))) {
        sawSep = true
        continue
      }
      const tag = sawSep ? 'td' : 'th'
      html += '<tr>' + cells
        .map((c) => `<${tag} style="border:1rpx solid #e5e6eb;padding:8rpx 12rpx;font-size:26rpx;text-align:left;">${inline(c)}</${tag}>`)
        .join('') + '</tr>'
    }
    html += '</table>'
    out.push(html)
    tableRows = []
  }
  const flushAll = () => { flushText(); flushList(); flushTable() }

  let i = 0
  while (i < lines.length) {
    const raw = lines[i]
    const trimmed = raw.trim()

    // 空行：段落 / 列表断行
    if (!trimmed) { flushAll(); i++; continue }

    // 代码块占位符（独占一行）
    const pb = trimmed.match(/^@@KANO_CODE_(\d+)@@$/)
    if (pb) {
      flushAll()
      out.push(codeBlocks[Number(pb[1])])
      i++
      continue
    }

    // 表格行（连续收集）
    if (/^\|.+\|\s*$/.test(raw)) {
      flushText(); flushList()
      tableRows.push(raw)
      i++
      continue
    }

    // 引用块（连续 > 合并为一个；注意 > 已被转义成 &gt;）
    if (/^&gt;/.test(trimmed)) {
      flushAll()
      const quotes = []
      while (i < lines.length && /^&gt;/.test(lines[i].trim())) {
        quotes.push(lines[i].trim().replace(/^&gt;\s?/, ''))
        i++
      }
      out.push('<blockquote style="margin:0 0 14rpx;border-left:6rpx solid #c9d4ff;background:#f7f8ff;color:#5b6472;padding:12rpx 20rpx;border-radius:8rpx;line-height:1.6;">' + quotes.map(inline).join('<br>') + '</blockquote>')
      continue
    }

    // 标题
    const hm = trimmed.match(/^(#{1,6})\s+(.*)$/)
    if (hm) {
      flushAll()
      const lv = hm[1].length
      const size = lv <= 2 ? 34 : lv === 3 ? 30 : 28
      out.push(`<h${lv} style="margin:16rpx 0 10rpx;font-size:${size}rpx;font-weight:600;color:#1a1a1a;line-height:1.5;">` + inline(hm[2]) + `</h${lv}>`)
      i++
      continue
    }

    // 分割线
    if (/^(\*{3,}|-{3,}|_{3,})$/.test(trimmed)) {
      flushAll()
      out.push('<hr style="border:none;border-top:2rpx solid #e5e6eb;margin:20rpx 0;">')
      i++
      continue
    }

    // 列表项（连续同类合并为一个列表）
    const ulm = trimmed.match(/^[-*]\s+(.*)$/)
    const olm = trimmed.match(/^\d+[.)、]\s+(.*)$/)
    if (ulm || olm) {
      flushText(); flushTable()
      const type = olm ? 'ol' : 'ul'
      if (listType && listType !== type) flushList()
      listType = type
      while (i < lines.length) {
        const l = lines[i].trim()
        const u2 = l.match(/^[-*]\s+(.*)$/)
        const o2 = l.match(/^\d+[.)、]\s+(.*)$/)
        if ((type === 'ul' && u2) || (type === 'ol' && o2)) {
          listItems.push('<li style="margin:4rpx 0;line-height:1.6;">' + inline(u2 ? u2[1] : o2[1]) + '</li>')
          i++
        } else break
      }
      continue
    }

    // 普通段落文本：先收尾挂起的列表/表格，保证输出顺序
    flushList()
    flushTable()
    pendingText.push(raw)
    i++
  }

  flushAll()
  // 兜底：还原可能嵌在段落里的代码块占位符（如「语法：```...```」同行场景）
  return out.join('').replace(/@@KANO_CODE_(\d+)@@/g, (m, n) => codeBlocks[Number(n)])
}

module.exports = { mdToHtml, inline, escapeHtml }
