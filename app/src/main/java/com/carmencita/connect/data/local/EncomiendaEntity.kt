package com.carmencita.connect.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.carmencita.connect.model.Encomienda
import com.carmencita.connect.model.Tarifa

@Entity(
    tableName = "encomiendas",
    indices = [Index(value = ["numeroGuia"], unique = true)]
)
data class EncomiendaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val numeroGuia: String,
    val largo: Double,
    val ancho: Double,
    val alto: Double,
    val peso: Double,
    val origen: String,
    val destino: String,
    val tarifaDestino: String,
    val tarifaCosto: Double,
    val estado: String,
    val fechaRegistro: String?,
    val fechaTransito: String?,
    val fechaAgencia: String?,
    val fechaEntrega: String?
) {
    fun toModel(): Encomienda {
        return Encomienda(
            id = id.toInt(),
            numeroGuia = numeroGuia,
            largo = largo,
            ancho = ancho,
            alto = alto,
            peso = peso,
            origen = origen,
            destino = destino,
            tarifa = Tarifa(destino = tarifaDestino, costo = tarifaCosto),
            estado = estado,
            fechaRegistro = fechaRegistro,
            fechaTransito = fechaTransito,
            fechaAgencia = fechaAgencia,
            fechaEntrega = fechaEntrega
        )
    }
}
