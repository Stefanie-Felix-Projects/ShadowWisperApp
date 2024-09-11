/**
 * Repository-Klasse `OrderRepository`, die als Vermittler zwischen der Datenquelle (Room-Datenbank) und der Anwendung fungiert.
 * Diese Klasse verwendet das `OrderDetailDao`, um auf die Order-Details in der Datenbank zuzugreifen.
 */
package com.example.shadowwisper.ui.theme.data.repository

import androidx.lifecycle.LiveData
import com.example.shadowwisper.ui.theme.data.dao.OrderDetailDao
import com.example.shadowwisper.ui.theme.data.model.OrderDetail

/**
 * Das `OrderRepository` stellt Methoden zum Abrufen, Einfügen und Aktualisieren von `OrderDetail`-Einträgen bereit.
 * Es verwendet das `OrderDetailDao`, um SQL-Operationen in der Room-Datenbank durchzuführen.
 * @param orderDetailDao Das DAO-Objekt, das für den Datenbankzugriff verwendet wird.
 */
class OrderRepository(private val orderDetailDao: OrderDetailDao) {

    /**
     * Eine LiveData-Liste aller Bestellungen (`OrderDetail`-Objekte), die in der Room-Datenbank gespeichert sind.
     * Diese LiveData-Liste wird automatisch aktualisiert, wenn sich die Daten in der Datenbank ändern.
     */
    val allOrders: LiveData<List<OrderDetail>> = orderDetailDao.getAllOrders()

    /**
     * Fügt ein neues `OrderDetail` in die Datenbank ein.
     * Diese Methode wird asynchron ausgeführt, um den Hauptthread nicht zu blockieren.
     * @param orderDetail Das `OrderDetail`-Objekt, das in die Datenbank eingefügt werden soll.
     */
    suspend fun insert(orderDetail: OrderDetail) {
        orderDetailDao.insert(orderDetail)
    }

    /**
     * Aktualisiert ein bestehendes `OrderDetail` in der Datenbank.
     * Diese Methode wird ebenfalls asynchron ausgeführt, um den Hauptthread nicht zu blockieren.
     * @param orderDetail Das `OrderDetail`-Objekt, das aktualisiert werden soll.
     */
    suspend fun update(orderDetail: OrderDetail) {
        orderDetailDao.update(orderDetail)
    }

    /**
     * Ruft ein `OrderDetail` aus der Datenbank ab, basierend auf der ID.
     * Diese Methode gibt ein `LiveData`-Objekt zurück, das das `OrderDetail` enthält.
     * @param id Die eindeutige ID des `OrderDetail`, das abgerufen werden soll.
     * @return Ein `LiveData`-Objekt, das das `OrderDetail` enthält.
     */
    fun getOrderById(id: Int): LiveData<OrderDetail> {
        return orderDetailDao.getOrderById(id)
    }
}