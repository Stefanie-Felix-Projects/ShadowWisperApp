package com.example.shadowwisper.ui.theme.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shadowwisper.ui.theme.data.adapter.OrderOverviewAdapter
import com.example.shadowwisper.ui.theme.data.view.OrderViewModel
import com.syntax_institut.whatssyntax.databinding.FragmentOrderoverviewBinding

class OrderoverviewFragment : Fragment() {

    private lateinit var binding: FragmentOrderoverviewBinding
    private val viewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderoverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderList = viewModel.getOrderList()

        val adapter = OrderOverviewAdapter(orderList)
        binding.rvOrder.layoutManager = LinearLayoutManager(context)
        binding.rvOrder.adapter = adapter
    }
}