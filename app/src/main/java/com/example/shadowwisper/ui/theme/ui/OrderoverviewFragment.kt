/**
 * Fragment-Klasse `OrderoverviewFragment`, die für die Anzeige einer Übersicht aller Aufträge zuständig ist.
 * Das Fragment zeigt eine Liste der vorhandenen Aufträge an und ermöglicht es dem Benutzer, einen Auftrag auszuwählen oder einen neuen hinzuzufügen.
 */
package com.example.shadowwisper.ui.theme.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shadowwisper.databinding.FragmentOrderoverviewBinding
import com.example.shadowwisper.ui.theme.data.adapter.OrderOverviewAdapter
import com.example.shadowwisper.ui.theme.data.model.OrderOverview
import com.example.shadowwisper.ui.theme.data.view.OrderViewModel

/**
 * Fragment zur Verwaltung der Übersicht über die bestehenden Aufträge.
 * Es zeigt alle Aufträge in einer Liste an und bietet dem Benutzer die Möglichkeit, neue Aufträge hinzuzufügen oder vorhandene zu bearbeiten.
 */
class OrderoverviewFragment : Fragment() {

    // Binding-Objekt zur einfachen Referenzierung der UI-Elemente.
    private lateinit var binding: FragmentOrderoverviewBinding
    // ViewModel für die Verwaltung der Auftragsdaten.
    private val orderViewModel: OrderViewModel by activityViewModels()

    /**
     * Erstellt die View für das Fragment und bindet das Layout an das Fragment.
     * @param inflater Das LayoutInflater-Objekt zum Aufblasen des Layouts für dieses Fragment.
     * @param container Das übergeordnete View-Element, in das die Fragment-UI eingebunden wird.
     * @param savedInstanceState Wenn nicht null, wird dieses Fragment mit einem früher gespeicherten Zustand wiederhergestellt.
     * @return Die View für das Fragment-UI, oder null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Bindet das Layout für die Auftragsübersicht.
        binding = FragmentOrderoverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Wird nach der Erstellung der View aufgerufen und initialisiert die Benutzeroberfläche.
     * Hier wird die Liste der Aufträge angezeigt und der Button zum Hinzufügen eines neuen Auftrags konfiguriert.
     * @param view Die erstellte View für dieses Fragment.
     * @param savedInstanceState Wenn nicht null, wird dieses Fragment mit einem früher gespeicherten Zustand wiederhergestellt.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setzt das Layout für die RecyclerView auf eine vertikale Liste (LinearLayout).
        binding.rvOrder.layoutManager = LinearLayoutManager(context)

        // Beobachtet die Liste aller Aufträge im ViewModel und zeigt sie in der RecyclerView an.
        orderViewModel.allOrders.observe(viewLifecycleOwner, Observer { orders ->

            // Wandelt die OrderDetail-Daten in eine Liste von OrderOverview-Objekten um.
            val orderList = orders.map { orderDetail ->
                OrderOverview(
                    id = orderDetail.id,
                    profileImage = orderDetail.image,
                    orderTitle = orderDetail.orderName,
                    subTitle = orderDetail.subText,
                    mapImage = orderDetail.mapImage
                )
            }

            // Initialisiert den Adapter und setzt die Liste der Aufträge in die RecyclerView ein.
            val adapter = OrderOverviewAdapter(orderList) { orderItem ->
                // Klicklistener für die Auswahl eines Auftrags, der zur Detailansicht navigiert.
                val action = OrderoverviewFragmentDirections.actionOrderoverviewFragmentToOrderdetailFragment(
                    orderId = orderItem.id.toString(),
                    orderName = orderItem.orderTitle,
                    subText = orderItem.subTitle,
                    image = orderItem.profileImage,
                    mapImage = orderItem.mapImage,
                    storyTitle = "Story Title",
                    storyText = "Story Text"
                )
                findNavController().navigate(action)
            }

            binding.rvOrder.adapter = adapter
        })

        // Klicklistener für den Button zum Hinzufügen eines neuen Auftrags.
        binding.btnAddOrder.setOnClickListener {
            // Navigiert zur Auftragsdetailansicht, um einen neuen Auftrag zu erstellen.
            val action = OrderoverviewFragmentDirections.actionOrderoverviewFragmentToOrderdetailFragment(
                orderId = null,
                orderName = "",
                subText = "",
                image = 0,
                mapImage = 0,
                storyTitle = "",
                storyText = "",
                karma = 0,
                money = 0
            )
            findNavController().navigate(action)
        }
    }
}