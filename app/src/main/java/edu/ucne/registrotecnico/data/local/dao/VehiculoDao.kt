package edu.ucne.registrotecnico.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.local.entities.VehiculoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehiculoDao {
    @Upsert()
    suspend fun save(Vehiculo: List<VehiculoEntity>)
    @Query("""
            SELECT *
                FROM Vehiculos
                WHERE vehiculoId =:id
                limit 1
    """)
    suspend fun find(id: Int): VehiculoEntity?
    @Delete
    suspend fun delete(Vehiculo: VehiculoEntity)
    @Query("SELECT * FROM Vehiculos")
    suspend fun getAll(): List<VehiculoEntity>
}