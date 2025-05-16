package edu.ucne.registrotecnico.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrotecnico.data.local.dao.PrioridadDao
import edu.ucne.registrotecnico.data.local.dao.TecnicoDao
import edu.ucne.registrotecnico.data.local.dao.TicketDao
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.local.entities.TicketEntity

@Database(
    entities = [
        TecnicoEntity::class,
        PrioridadEntity::class,
        TicketEntity::class
    ],
    version = 2,
    exportSchema = false
)

abstract  class TecnicoDb : RoomDatabase(){
    abstract fun TecnicoDao(): TecnicoDao
    abstract  fun PrioridadDao(): PrioridadDao
    abstract fun TicketDao(): TicketDao
}