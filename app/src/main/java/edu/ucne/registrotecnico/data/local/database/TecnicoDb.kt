package edu.ucne.registrotecnico.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import edu.ucne.registrotecnico.data.local.dao.PrioridadDao
import edu.ucne.registrotecnico.data.local.dao.TecnicoDao
import edu.ucne.registrotecnico.data.local.dao.TicketDao
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

@Database(
    entities = [
        TecnicoEntity::class,
        PrioridadEntity::class,
        TicketEntity::class
    ],
    version = 2,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract  class TecnicoDb : RoomDatabase(){
    abstract fun TecnicoDao(): TecnicoDao
    abstract  fun PrioridadDao(): PrioridadDao
    abstract fun TicketDao(): TicketDao
}