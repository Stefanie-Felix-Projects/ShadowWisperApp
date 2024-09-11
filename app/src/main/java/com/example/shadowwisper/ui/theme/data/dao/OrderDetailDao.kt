/**
 * DAO (Data Access Object) für den Zugriff auf die Tabelle `order_details` in der Datenbank.
 * Diese Schnittstelle enthält Methoden zum Einfügen, Aktualisieren und Abrufen von Bestelldetails.
 */
package com.example.shadowwisper.ui.theme.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shadowwisper.ui.theme.data.model.OrderDetail

/**
 * Das OrderDetailDao-Interface bietet Methoden zur Interaktion mit der Datenbank, um OrderDetail-Daten
 * zu speichern, zu aktualisieren und abzurufen. Die DAO arbeitet mit Room, um SQL-Operationen zu kapseln.
 */
@Dao
interface OrderDetailDao {

    /**
     * Fügt ein neues OrderDetail in die Datenbank ein.
     * Diese Methode wird asynchron (suspend) ausgeführt.
     * @param orderDetail Das OrderDetail-Objekt, das eingefügt werden soll.
     */
    @Insert
    suspend fun insert(orderDetail: OrderDetail)

    /**
     * Aktualisiert ein bestehendes OrderDetail in der Datenbank.
     * Diese Methode wird asynchron (suspend) ausgeführt.
     * @param orderDetail Das OrderDetail-Objekt, das aktualisiert werden soll.
     */
    @Update
    suspend fun update(orderDetail: OrderDetail)

    /**
     * Ruft ein OrderDetail aus der Datenbank ab, basierend auf der ID.
     * Die Methode gibt ein LiveData-Objekt zurück, das beobachtet werden kann, um Änderungen in Echtzeit zu verfolgen.
     * @param id Die eindeutige ID des OrderDetail-Objekts, das abgerufen werden soll.
     * @return Ein LiveData-Objekt, das das OrderDetail enthält.
     */
    @Query("SELECT * FROM order_details WHERE id = :id")
    fun getOrderById(id: Int): LiveData<OrderDetail>

    /**
     * Ruft alle OrderDetail-Einträge aus der Datenbank ab.
     * Die Methode gibt ein LiveData-Objekt zurück, das eine Liste aller OrderDetail-Objekte enthält.
     * @return Ein LiveData-Objekt, das eine Liste von OrderDetail-Objekten enthält.
     */
    @Query("SELECT * FROM order_details")
    fun getAllOrders(): LiveData<List<OrderDetail>>
}