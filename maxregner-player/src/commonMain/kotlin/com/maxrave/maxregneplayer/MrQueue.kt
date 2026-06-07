package com.maxrave.maxregneplayer

class MrQueue {
    private val original = mutableListOf<MrTrack>()
    private var shuffled: List<MrTrack>? = null
    var cursor: Int = 0
        private set

    val tracks: List<MrTrack> get() = shuffled ?: original
    val current: MrTrack?    get() = tracks.getOrNull(cursor)
    val size: Int             get() = tracks.size

    fun set(tracks: List<MrTrack>, startIndex: Int = 0) {
        original.clear(); original.addAll(tracks)
        shuffled = null
        cursor = startIndex.coerceIn(0, (original.size - 1).coerceAtLeast(0))
    }

    fun addNext(track: MrTrack) {
        original.add((cursor + 1).coerceAtMost(original.size), track); shuffled = null
    }

    fun addLast(track: MrTrack) { original.add(track); shuffled = null }

    fun remove(index: Int) {
        if (index !in original.indices) return
        original.removeAt(index); shuffled = null
        if (cursor >= original.size) cursor = (original.size - 1).coerceAtLeast(0)
    }

    fun move(index: Int): MrTrack? {
        cursor = index.coerceIn(0, (tracks.size - 1).coerceAtLeast(0))
        return current
    }

    fun advance(repeat: MrRepeatMode): MrTrack? = when (repeat) {
        MrRepeatMode.One  -> current
        MrRepeatMode.All  -> { cursor = (cursor + 1) % tracks.size.coerceAtLeast(1); current }
        MrRepeatMode.Off  -> if (cursor < tracks.lastIndex) { cursor++; current } else null
    }

    fun back(): MrTrack? { if (cursor > 0) cursor--; return current }

    fun shuffle(on: Boolean) {
        if (on) {
            val cur = current
            val rest = original.toMutableList().also {
                val idx = it.indexOf(cur); if (idx >= 0) it.removeAt(idx)
            }.shuffled()
            shuffled = if (cur != null) listOf(cur) + rest else rest
            cursor = 0
        } else {
            val cur = current; shuffled = null
            cursor = if (cur != null) original.indexOf(cur).coerceAtLeast(0) else 0
        }
    }

    fun clear() { original.clear(); shuffled = null; cursor = 0 }
}
