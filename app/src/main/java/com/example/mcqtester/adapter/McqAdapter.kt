package com.example.mcqtestlab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mcqtester.databinding.ItemMcqBinding
import com.example.mcqtestlab.model.MCQ

class McqAdapter(
    private val mcqList: List<MCQ>,
    private val onOptionSelected: (questionIndex: Int, selectedOptionIndex: Int) -> Unit
) : RecyclerView.Adapter<McqAdapter.McqViewHolder>() {


    private val selectedOptions = mutableMapOf<Int, Int>()

    inner class McqViewHolder(val binding: ItemMcqBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): McqViewHolder {
        val binding = ItemMcqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return McqViewHolder(binding)
    }

    override fun getItemCount(): Int = mcqList.size

    override fun onBindViewHolder(holder: McqViewHolder, position: Int) {
        val mcq = mcqList[position]
        val b = holder.binding

        b.questionText.text = mcq.question
        b.optionA.text = mcq.options.getOrNull(0) ?: ""
        b.optionB.text = mcq.options.getOrNull(1) ?: ""
        b.optionC.text = mcq.options.getOrNull(2) ?: ""
        b.optionD.text = mcq.options.getOrNull(3) ?: ""



        // Restore previous selection if any
        when (selectedOptions[position]) {
            0 -> b.optionA.isChecked = true
            1 -> b.optionB.isChecked = true
            2 -> b.optionC.isChecked = true
            3 -> b.optionC.isChecked = true

        }


    }
}
