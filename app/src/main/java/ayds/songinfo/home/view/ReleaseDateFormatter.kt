package ayds.songinfo.home.view

import android.icu.text.SimpleDateFormat
import ayds.songinfo.home.model.entities.Song
import java.time.Year

interface ReleaseDateFormatterFactory {
    fun getReleaseDateFormatter(song: Song.SpotifySong): ReleaseDateFormatter
}
class ReleaseDateFormatterFactoryImpl: ReleaseDateFormatterFactory {
    override fun getReleaseDateFormatter(song: Song.SpotifySong): ReleaseDateFormatter =
        when (song.releaseDatePrecision) {
            "day" -> ReleaseDateDayFormatter(song)
            "month" -> ReleaseDateMonthFormatter(song)
            "year" -> ReleaseDateYearFormatter(song)
            else -> ReleaseDateDefaultFormatter(song)
        }
}

interface ReleaseDateFormatter {
    val song: Song.SpotifySong
    fun getReleaseDate(): String
}

internal class ReleaseDateDayFormatter(override val song: Song.SpotifySong) :
    ReleaseDateFormatter {
    override fun getReleaseDate(): String = SimpleDateFormat("dd/MM/yyyy").format(SimpleDateFormat("yyyy-MM-dd").parse(song.releaseDate))
}

internal class ReleaseDateMonthFormatter(override val song: Song.SpotifySong) :
    ReleaseDateFormatter {
    override fun getReleaseDate(): String = SimpleDateFormat("MMMM, yyyy").format(SimpleDateFormat("yyyy-MM").parse(song.releaseDate))

}

internal class ReleaseDateYearFormatter(override val song: Song.SpotifySong) :
    ReleaseDateFormatter {
    override fun getReleaseDate(): String {
        return toYearLeapOrNotFormat(song.releaseDate.split("-").first().toLong())
        /*val isLeapYear = isLeapYear(song.releaseDate.toInt())
        val suffix = if (isLeapYear) "(leap year)" else "(not a leap year)"
        return "${song.releaseDate} $suffix"*/
    }
   // private fun isLeapYear(n: Int) = (n % 4 == 0) && (n % 100 != 0 || n % 400 == 0)
    private fun toYearLeapOrNotFormat(year: Long): String = "$year (${if (Year.isLeap(year)) "" else "Not a "}leap year)"
}

internal class ReleaseDateDefaultFormatter(override val song: Song.SpotifySong) :
    ReleaseDateFormatter {
    override fun getReleaseDate() = song.releaseDate
}

