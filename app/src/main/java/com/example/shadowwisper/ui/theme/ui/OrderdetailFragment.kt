package com.example.shadowwisper.ui.theme.ui

import android.content.Intent
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

class OrderdetailFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentOrderdetailBinding
    private val args: OrderdetailFragmentArgs by navArgs()
    private val orderViewModel: OrderViewModel by activityViewModels()
    private lateinit var googleMap: GoogleMap
    private val PICK_IMAGE_REQUEST = 1
    private var profileImageBytes: ByteArray? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderdetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.orderName.isNotEmpty()) {
            binding.etTitle.setText(args.orderName)
            binding.etSubhead.setText(args.subText)
            binding.imageView.setImageResource(args.image)
            binding.etStoryTitle.setText(args.storyTitle)
            binding.etStoryText.setText(args.storyText)
            binding.inputKarma.setText(args.karma.toString())
            binding.inputMoney.setText(args.money.toString())
        }

        binding.imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnComplete.setOnClickListener {
            val action = OrderdetailFragmentDirections
                .actionOrderdetailFragmentToOrdercompletionFragment()
            findNavController().navigate(action)
        }

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

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
            findNavController().navigateUp()
        }

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
                } else {
                    // ToDo: Optionale Benachrichtigung, wenn der Ort nicht gefunden wird
                    // ToDo: Ort soll beim Speichern bestehen bleiben, wenn das geht
                }
            }
            true
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                binding.imageView.setImageURI(selectedImageUri)

                profileImageBytes = getBytesFromUri(selectedImageUri)
            }
        }
    }

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