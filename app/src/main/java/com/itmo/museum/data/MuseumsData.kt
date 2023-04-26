package com.itmo.museum.data

import android.content.Context
import com.itmo.museum.R
import com.itmo.museum.models.Museum

interface MuseumDataProvider {
    val museums: List<Museum>

    companion object {
        fun defaultProvider(context: Context): MuseumDataProvider =
            MuseumDataRegularProvider(context)
    }
}

/**
 * This provider returns a list of museums defined in the source code.
 */
private class MuseumDataRegularProvider(val context: Context) : MuseumDataProvider {
    override val museums: List<Museum>
        get() = with(context) {
            listOf(
                hermitage,
                russianMuseum,
                etnographicMuseum,
                fabergeMuseum,
                shadowMuseum,
                musicMuseum,
                petersAquatoria,
                kunstcamera,
                leningradDefenceMuseum,
                cosmonautics_museum
            )
        }

    private val Context.hermitage
        get() = Museum(
            name = "Государственный Эрмитаж",
            address = "Дворцовая пл., 2, Санкт-Петербург, 190000",
            isVisited = false,
            info = getString(R.string.hermitage_description),
            imageId = R.drawable.hermitage,
            latitude = 59.93986870405074,
            longitude = 30.314555425796726
        )

    private val Context.russianMuseum
        get() = Museum(
            name = "Русский музей, Михайловский дворец",
            address = "Инженерная ул., 4, Санкт-Петербург, 191186",
            isVisited = false,
            info = getString(R.string.rus_museum_description),
            imageId = R.drawable.rus_museum,
            latitude = 59.938657829289525,
            longitude = 30.3322752180001
        )

    private val Context.etnographicMuseum
        get() = Museum(
            name = "Российский этнографический музей",
            address = "Инженерная ул., 4/1, Санкт-Петербург, 191011",
            isVisited = false,
            info = getString(R.string.etnographic_museum_description),
            imageId = R.drawable.etnographic,
            latitude = 59.9378716410758,
            longitude = 30.334385512002164
        )

    private val Context.fabergeMuseum
        get() = Museum(
            name = "Музей Фаберже",
            address = "набережная реки Фонтанки, 21, Санкт-Петербург, 191023",
            isVisited = false,
            info = getString(R.string.faberge_museum_description),
            imageId = R.drawable.faberge,
            latitude = 59.9347518533602,
            longitude = 30.34285464240878
        )

    private val shadowMuseum
        get() = Museum(
            name = "Музей теней",
            address = "Большая Конюшенная ул., 5А, Санкт-Петербург, 191186",
            isVisited = false,
            info = NO_INFORMATION,
            imageId = R.drawable.shadow,
            latitude = 59.940512944386754,
            longitude = 30.323519373760718
        )

    private val musicMuseum
        get() = Museum(
            name = "Шереметевский Дворец - Музей Музыки",
            address = "набережная реки Фонтанки, 34, Санкт-Петербург, 191014",
            isVisited = false,
            info = NO_INFORMATION,
            imageId = R.drawable.music,
            latitude = 59.93652633045967,
            longitude = 30.345319397453377
        )

    private val Context.petersAquatoria
        get() = Museum(
            name = "Петровская Акватория",
            address = "Санкт-Петербург Малая Морская 4/1 ТРК Адмирал, Санкт-Петербург, 191186",
            isVisited = false,
            info = getString(R.string.peters_aquatoria_description),
            imageId = R.drawable.aquatoria,
            latitude = 59.936028494806614,
            longitude = 30.314979903088112
        )

    private val Context.kunstcamera
        get() = Museum(
            name = "Кунсткамера",
            address = "",
            isVisited = false,
            info = getString(R.string.kunstcamera_description),
            imageId = R.drawable.kunstcamera,
            latitude = 59.941502068497286,
            longitude = 30.304575593724714
        )

    private val Context.leningradDefenceMuseum
        get() = Museum(
            name = "Государственный мемориальный музей обороны и блокады Ленинграда",
            address = "Соляной пер., 9, Санкт-Петербург, 191187",
            isVisited = false,
            info = getString(R.string.leningrad_defence_museum_description),
            imageId = R.drawable.leningrad_defence_museum,
            latitude = 59.944429839983115,
            longitude = 30.34084143335516
        )

    private val cosmonautics_museum
        get() = Museum(
            name = "Музей космонавтики и ракетной техники имени В. П. Глушко",
            address = "территория Петропавловская Крепость, Санкт-Петербург, 197046",
            isVisited = false,
            info = NO_INFORMATION,
            imageId = R.drawable.cosmonautics_museum,
            latitude = 59.951829842478084,
            longitude = 30.32142908729199
        )

    private companion object {
        private const val NO_INFORMATION = "Нет информации"
    }
}
