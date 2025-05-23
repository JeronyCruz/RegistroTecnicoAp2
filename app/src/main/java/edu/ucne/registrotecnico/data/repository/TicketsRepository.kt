package edu.ucne.registrotecnico.data.repository

import edu.ucne.registrotecnico.data.local.dao.TicketDao
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TicketsRepository @Inject constructor(
    private val dao: TicketDao
) {
    suspend fun save(prioridad: TicketEntity) = dao.save(prioridad)

    suspend fun find(id: Int): TicketEntity? = dao.find(id)

    suspend fun delete(prioridad: TicketEntity) = dao.delete(prioridad)

    fun getAll(): Flow<List<TicketEntity>> = dao.getAll()
}