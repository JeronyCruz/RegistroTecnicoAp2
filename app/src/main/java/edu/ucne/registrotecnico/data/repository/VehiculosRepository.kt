package edu.ucne.registrotecnico.data.repository

import edu.ucne.registrotecnico.data.remote.RemoteDataSource
import edu.ucne.registrotecnico.data.remote.Resource
import edu.ucne.registrotecnico.data.remote.dto.VehiculoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class VehiculosRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
     fun getVehiculo(id: Int): Flow<Resource<VehiculoDto>> {
        return flow {
            try {
                emit(Resource.Loading())
                val vehiculos = remoteDataSource.getVehiculo(id)
                if (vehiculos.isNotEmpty()) {
                    emit(Resource.Success(vehiculos.first()))
                } else {
                    emit(Resource.Error("No se encontró el vehículo"))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Error: ${e.localizedMessage ?: "Error desconocido"}"))
            }
        }
    }

    fun getVehiculos(): Flow<Resource<List<VehiculoDto>>> = flow {
        try {
            emit(Resource.Loading())
            val vehiculos = remoteDataSource.getVehiculos()
            emit(Resource.Success(vehiculos))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexion $errorMessage"))
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
    }

    suspend fun saveVehiculo(vehiculoDto: VehiculoDto) = remoteDataSource.saveVehiculo(vehiculoDto)

    suspend fun editVehiculo(vehiculoDto: VehiculoDto) = remoteDataSource.updateVehiculo(vehiculoDto)

    suspend fun deleteVehiculo(idVehiculo: Int) = remoteDataSource.deleteVehiculo(idVehiculo)
}