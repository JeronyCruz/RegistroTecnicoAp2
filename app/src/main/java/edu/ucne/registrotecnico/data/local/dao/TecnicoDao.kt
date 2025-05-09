package edu.ucne.registrotecnico.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TecnicoDao {
    @Upsert()
    suspend fun save(Tecnico: TecnicoEntity)
    @Query("""
            SELECT *
                FROM Tecnicos
                WHERE tecnicoId =:id
                limit 1
    """)
    suspend fun find(id: Int): TecnicoEntity?
    @Delete
    suspend fun delete(Tecnico: TecnicoEntity)
    @Query("SELECT * FROM Tecnicos")
    fun getAll(): Flow<List<TecnicoEntity>>

}