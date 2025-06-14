package edu.ucne.registrotecnico.data.remote

import edu.ucne.registrotecnico.data.remote.dto.VehiculoDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val vehiculoinApi: VehiculoinApi
){
    suspend fun getVehiculos() = vehiculoinApi.getVehiculos()

    suspend fun getVehiculo(id: Int) = vehiculoinApi.getVehiculos(id)

    suspend fun updateVehiculo(vehiculoDto: VehiculoDto) = vehiculoinApi.updateVehiculo(vehiculoDto)

    suspend fun saveVehiculo(vehiculoDto: VehiculoDto) = vehiculoinApi.saveVehiculo(vehiculoDto)

    suspend fun deleteVehiculo(idVehiculo: Int) = vehiculoinApi.deleteVehiculo(idVehiculo)
}