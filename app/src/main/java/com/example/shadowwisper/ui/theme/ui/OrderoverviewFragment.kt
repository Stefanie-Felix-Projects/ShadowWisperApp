package com.example.shadowwisper.ui.theme.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shadowwisper.ui.theme.data.adapter.OrderOverviewAdapter
import com.example.shadowwisper.ui.theme.data.model.OrderOverview
import com.example.shadowwisper.ui.theme.data.view.OrderViewModel
import com.syntax_institut.whatssyntax.R
import com.syntax_institut.whatssyntax.databinding.FragmentOrderoverviewBinding

class OrderoverviewFragment : Fragment() {

    private lateinit var binding: FragmentOrderoverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderoverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderList = listOf(
            OrderOverview(R.drawable.order, "Order 1", "SubText 1", R.drawable.order),
            OrderOverview(R.drawable.order, "Order 2", "SubText 2", R.drawable.order),
            OrderOverview(R.drawable.order, "Order 3", "SubText 3", R.drawable.order)
        )

        val adapter = OrderOverviewAdapter(orderList) { orderItem ->
            val action = OrderoverviewFragmentDirections.actionOrderoverviewFragmentToOrderdetailFragment(
                orderName = orderItem.orderTitle,
                subText = orderItem.subTitle,
                image = orderItem.profileImage,
                mapImage = orderItem.mapImage,
                storyTitle = "Story Title",  // Placeholder
                storyText = "Story Text"     // Placeholder
            )
            findNavController().navigate(action)
        }

        binding.rvOrder.layoutManager = LinearLayoutManager(context)
        binding.rvOrder.adapter = adapter
    }
}