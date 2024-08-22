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
import com.syntax_institut.whatssyntax.databinding.FragmentOrderdetailBinding


class OrderdetailFragment : Fragment() {

    private lateinit var binding: FragmentOrderdetailBinding
    private val args: OrderdetailFragmentArgs by navArgs()
    private val orderViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderdetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.orderName.isNotEmpty()) {
            binding.etTitle.setText(args.orderName)
            binding.etSubhead.setText(args.subText)
            binding.imageView.setImageResource(args.image)
            binding.imageView2.setImageResource(args.mapImage)
            binding.etStoryTitle.setText(args.storyTitle)
            binding.etStoryText.setText(args.storyText)
            binding.inputKarma.setText(args.karma.toString())
            binding.inputMoney.setText(args.money.toString())
        }

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
                orderName = binding.etTitle.text.toString(),
                subText = binding.etSubhead.text.toString(),
                image = args.image,
                mapImage = args.mapImage,
                storyTitle = binding.etStoryTitle.text.toString(),
                storyText = binding.etStoryText.text.toString(),
                karma = binding.inputKarma.text.toString().toIntOrNull() ?: 0,
                money = binding.inputMoney.text.toString().toIntOrNull() ?: 0
            )

            orderViewModel.insert(updatedOrderDetail)
            findNavController().navigateUp()
        }
    }
}