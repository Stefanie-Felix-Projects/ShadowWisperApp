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
import com.example.shadowwisper.ui.theme.data.adapter.OrderOverviewAdapter
import com.example.shadowwisper.ui.theme.data.model.OrderOverview
import com.example.shadowwisper.ui.theme.data.view.OrderViewModel
import com.syntax_institut.whatssyntax.databinding.FragmentOrderoverviewBinding

class OrderoverviewFragment : Fragment() {

    private lateinit var binding: FragmentOrderoverviewBinding
    private val orderViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderoverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvOrder.layoutManager = LinearLayoutManager(context)

        orderViewModel.allOrders.observe(viewLifecycleOwner, Observer { orders ->

            val orderList = orders.map { orderDetail ->
                OrderOverview(
                    id = orderDetail.id,  // Die ID wird hier hinzugefügt
                    profileImage = orderDetail.image,
                    orderTitle = orderDetail.orderName,
                    subTitle = orderDetail.subText,
                    mapImage = orderDetail.mapImage
                )
            }

            val adapter = OrderOverviewAdapter(orderList) { orderItem ->
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

        binding.btnAddOrder.setOnClickListener {
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