/**
 * ViewModel-Klasse `OrderViewModel`, die für die Verwaltung von Bestellungsdaten in der Benutzeroberfläche zuständig ist.
 * Diese Klasse verwendet das `OrderRepository`, um auf die Bestelldaten zuzugreifen und Operationen wie Einfügen und Aktualisieren durchzuführen.
 * Sie ermöglicht der UI, die Daten über den Lebenszyklus hinweg konsistent zu halten.
 */
package com.example.shadowwisper.ui.theme.data.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.shadowwisper.ui.theme.data.database.OrderDatabase
import com.example.shadowwisper.ui.theme.data.model.OrderDetail
import com.example.shadowwisper.ui.theme.data.repository.OrderRepository
import kotlinx.coroutines.launch

/**
 * ViewModel zur Verwaltung von Bestellungsdetails.
 * Diese Klasse verwendet das `OrderRepository`, um auf die Bestelldaten zuzugreifen und Methoden wie Einfügen und Aktualisieren anzubieten.
 * @param application Der Anwendungskontext, der für die Lebenszyklusverwaltung des ViewModels verwendet wird.
 */
class OrderViewModel(application: Application) : AndroidViewModel(application) {

    // Das Repository, das für den Zugriff auf die Bestelldaten verwendet wird.
    private val repository: OrderRepository

    /**
     * Ein `LiveData`-Objekt, das eine Liste aller Bestellungen enthält.
     * Diese Daten werden automatisch aktualisiert, wenn sich die Bestellungen in der Datenbank ändern.
     */
    val allOrders: LiveData<List<OrderDetail>>

    /**
     * Initialisiert das `OrderViewModel` und richtet das Repository sowie das LiveData für alle Bestellungen ein.
     * Es wird das `OrderDetailDao` von der `OrderDatabase` verwendet, um Datenbankoperationen durchzuführen.
     */
    init {
        // Zugriff auf das OrderDetailDao der Datenbank.
        val orderDetailDao = OrderDatabase.getDatabase(application).orderDetailDao()
        // Initialisierung des Repositories mit dem DAO.
        repository = OrderRepository(orderDetailDao)
        // LiveData für alle Bestellungen wird vom Repository bereitgestellt.
        allOrders = repository.allOrders
    }

    /**
     * Fügt eine neue Bestellung in die Datenbank ein.
     * Die Operation wird innerhalb des `viewModelScope` ausgeführt, um asynchron zu arbeiten und den Hauptthread nicht zu blockieren.
     * @param orderDetail Das `OrderDetail`-Objekt, das in die Datenbank eingefügt werden soll.
     */
    fun insert(orderDetail: OrderDetail) = viewModelScope.launch {
        repository.insert(orderDetail)
    }

    /**
     * Aktualisiert eine bestehende Bestellung in der Datenbank.
     * Diese Methode wird ebenfalls im `viewModelScope` ausgeführt, um asynchron zu arbeiten.
     * @param orderDetail Das `OrderDetail`-Objekt, das in der Datenbank aktualisiert werden soll.
     */
    fun update(orderDetail: OrderDetail) = viewModelScope.launch {
        repository.update(orderDetail)
    }

    /**
     * Ruft eine Bestellung basierend auf ihrer ID aus der Datenbank ab.
     * Diese Methode gibt ein `LiveData`-Objekt zurück, das die Bestellung enthält.
     * @param id Die eindeutige ID der Bestellung, die abgerufen werden soll.
     * @return Ein `LiveData`-Objekt, das das `OrderDetail` enthält.
     */
    fun getOrderById(id: Int): LiveData<OrderDetail> {
        return repository.getOrderById(id)
    }
}