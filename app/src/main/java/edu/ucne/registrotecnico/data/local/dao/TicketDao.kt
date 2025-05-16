package edu.ucne.registrotecnico.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {
    @Upsert()
    suspend fun save(Ticket: TicketEntity)
    @Query("""
            SELECT *
                FROM Tickets
                WHERE ticketId =:id
                limit 1
    """)
    suspend fun find(id: Int): TicketEntity?
    @Delete
    suspend fun delete(Ticket: TicketEntity)
    @Query("SELECT * FROM Tickets")
    fun getAll(): Flow<List<TicketEntity>>
}