package com.example.shadowwisper.ui.theme.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shadowwisper.ui.theme.data.model.OrderDetail
import com.example.shadowwisper.ui.theme.data.view.OrderViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.syntax_institut.whatssyntax.R
import com.syntax_institut.whatssyntax.databinding.FragmentOrderdetailBinding

class OrderdetailFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentOrderdetailBinding
    private val args: OrderdetailFragmentArgs by navArgs()
    private val orderViewModel: OrderViewModel by activityViewModels()
    private lateinit var googleMap: GoogleMap

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
                money = binding.inputMoney.text.toString().toIntOrNull() ?: 0
            )

            if (args.orderId != null) {
                orderViewModel.update(updatedOrderDetail)
            } else {
                orderViewModel.insert(updatedOrderDetail)
            }
            findNavController().navigateUp()
        }

        binding.btnOpenMap.setOnClickListener {
            val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
            mapFragment.getMapAsync(this)
            mapFragment.view?.visibility = View.VISIBLE

            val location = LatLng(50.9375, 6.9603)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val cologne = LatLng(50.9375, 6.9603)
        googleMap.addMarker(MarkerOptions().position(cologne).title("Marker in Köln"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cologne, 12f))
    }
}