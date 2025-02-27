/**
 * Fragment-Klasse `OrderdetailFragment`, die für die Anzeige und Bearbeitung von Auftragsdetails zuständig ist.
 * Diese Klasse ermöglicht das Anzeigen, Bearbeiten und Speichern von Auftragsdaten sowie das Hochladen von Bildern.
 * Sie enthält auch eine Kartenansicht zur Visualisierung von Standorten, die über den Geocoder abgerufen werden.
 */
package com.example.shadowwisper.ui.theme.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shadowwisper.R
import com.example.shadowwisper.databinding.FragmentOrderdetailBinding
import com.example.shadowwisper.ui.theme.data.model.OrderDetail
import com.example.shadowwisper.ui.theme.data.view.OrderViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.ByteArrayOutputStream
import java.util.Locale

/**
 * Fragment zur Verwaltung der Detailansicht eines Auftrags.
 * Hier können Auftragsdetails wie Titel, Beschreibung, Bilder und Standort bearbeitet werden.
 * Außerdem wird eine Karte zur Visualisierung des Standorts bereitgestellt.
 */
class OrderdetailFragment : Fragment(), OnMapReadyCallback {

    // Binding-Objekt zur einfachen Referenzierung der UI-Elemente.
    private lateinit var binding: FragmentOrderdetailBinding
    // Argumente, die von einem anderen Fragment übergeben wurden (z.B. Auftrags-ID).
    private val args: OrderdetailFragmentArgs by navArgs()
    // ViewModel zur Verwaltung der Auftragsdetails.
    private val orderViewModel: OrderViewModel by activityViewModels()
    // GoogleMap-Objekt zur Darstellung des Standorts.
    private lateinit var googleMap: GoogleMap
    // Konstanten und Variablen zur Bildauswahl.
    private val PICK_IMAGE_REQUEST = 1
    private var profileImageBytes: ByteArray? = null
    private var selectedImageViewId: Int? = null

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
        binding = FragmentOrderdetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Wird nach der Erstellung der View aufgerufen und initialisiert die Benutzeroberfläche.
     * Hier werden die Auftragsdetails geladen und es werden Funktionen wie Bildauswahl und Karteninitialisierung bereitgestellt.
     * @param view Die erstellte View für dieses Fragment.
     * @param savedInstanceState Wenn nicht null, wird dieses Fragment mit einem früher gespeicherten Zustand wiederhergestellt.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lädt die Auftragsdetails, wenn eine Auftrags-ID übergeben wurde.
        if (args.orderId != null) {
            orderViewModel.getOrderById(args.orderId!!.toInt()).observe(viewLifecycleOwner) { orderDetail ->
                orderDetail?.let {
                    // Setzt die Daten in die entsprechenden Felder des Formulars.
                    binding.etTitle.setText(it.orderName)
                    binding.etSubhead.setText(it.subText)
                    binding.imageView.setImageResource(it.image)
                    binding.etStoryTitle.setText(it.storyTitle)
                    binding.etStoryText.setText(it.storyText)
                    binding.inputKarma.setText(it.karma.toString())
                    binding.inputMoney.setText(it.money.toString())

                    // Zeigt das gespeicherte Bild an, wenn vorhanden.
                    if (it.profileImage != null) {
                        binding.imageView.setImageBitmap(
                            BitmapFactory.decodeByteArray(it.profileImage, 0, it.profileImage.size)
                        )
                    }
                }
            }
        }

        // Öffnet die Galerie zur Auswahl eines Bildes, wenn auf die ImageView geklickt wird.
        binding.imageView.setOnClickListener {
            selectedImageViewId = binding.imageView.id
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Öffnet die Galerie zur Auswahl eines Bildes für die zweite ImageView.
        binding.imageView2.setOnClickListener {
            selectedImageViewId = binding.imageView2.id
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Initialisiert die Karte.
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Navigiert zur Auftragsabschlussseite, wenn der Benutzer den Auftrag abschließt.
        binding.btnComplete.setOnClickListener {
            val action = OrderdetailFragmentDirections
                .actionOrderdetailFragmentToOrdercompletionFragment()
            findNavController().navigate(action)
        }

        // Bricht den Vorgang ab und kehrt zum vorherigen Bildschirm zurück.
        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        // Speichert den Auftrag, wenn der Benutzer auf Speichern klickt.
        binding.btnSaveNew.setOnClickListener {
            val updatedOrderDetail = OrderDetail(
                id = args.orderId?.toIntOrNull() ?: 0,
                orderName = binding.etTitle.text.toString(),
                subText = binding.etSubhead.text.toString(),
                image = args.image,
                mapImage = args.mapImage,
                storyTitle = binding.etStoryTitle.text.toString(),
                storyText = binding.etStoryText.text.toString(),
                karma = binding.inputKarma.text.toString().toIntOrNull() ?: 0,
                money = binding.inputMoney.text.toString().toIntOrNull() ?: 0,
                profileImage = profileImageBytes
            )

            if (args.orderId != null) {
                orderViewModel.update(updatedOrderDetail)
            } else {
                orderViewModel.insert(updatedOrderDetail)
            }

            // Kehrt nach dem Speichern zum vorherigen Bildschirm zurück.
            findNavController().navigateUp()
        }

        // Konvertiert den eingegebenen Standort in eine Karte und zeigt ihn an.
        binding.etLocation.setOnEditorActionListener { _, _, _ ->
            val locationName = binding.etLocation.text.toString()
            if (locationName.isNotEmpty()) {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocationName(locationName, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val address = addresses[0]
                    val location = LatLng(address.latitude, address.longitude)

                    googleMap.clear()
                    googleMap.addMarker(MarkerOptions().position(location).title(locationName))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
                    mapFragment.view?.visibility = View.VISIBLE
                }
            }
            true
        }
    }

    /**
     * Wird aufgerufen, wenn die Karte bereit ist. Initialisiert das `GoogleMap`-Objekt.
     * @param map Die GoogleMap, die verwendet wird.
     */
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }

    /**
     * Wird aufgerufen, wenn der Benutzer ein Bild aus der Galerie ausgewählt hat.
     * Das Bild wird in die entsprechenden ImageViews geladen.
     * @param requestCode Der Code für die angeforderte Aktion.
     * @param resultCode Das Ergebnis der Aktion.
     * @param data Die Daten, die das ausgewählte Bild enthalten.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                when (selectedImageViewId) {
                    binding.imageView.id -> {
                        binding.imageView.setImageURI(selectedImageUri)
                    }
                    binding.imageView2.id -> {
                        binding.imageView2.setImageURI(selectedImageUri)
                    }
                }

                // Konvertiert die Bild-URI in ein Byte-Array zur Speicherung.
                profileImageBytes = getBytesFromUri(selectedImageUri)
            }
        }
    }

    /**
     * Konvertiert eine Bild-URI in ein Byte-Array zur Speicherung in der Datenbank.
     * @param uri Die URI des Bildes, das konvertiert werden soll.
     * @return Das Byte-Array, das das Bild darstellt.
     */
    private fun getBytesFromUri(uri: Uri): ByteArray? {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val byteBuffer = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len: Int
        while (inputStream?.read(buffer).also { len = it!! } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }
}