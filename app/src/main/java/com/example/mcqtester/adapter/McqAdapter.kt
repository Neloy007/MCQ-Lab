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

        // Use explicit option fields, not a list
        b.optionA.text = mcq.optionA
        b.optionB.text = mcq.optionB
        b.optionC.text = mcq.optionC
        b.optionD.text = mcq.optionD

        // Clear previous listener & selection to avoid recycle issues
        b.optionsGroup.setOnCheckedChangeListener(null)
        b.optionsGroup.clearCheck()

        // Restore selected option if any
        selectedOptions[position]?.let { selectedIndex ->
            val radioButtonId = when (selectedIndex) {
                0 -> b.optionA.id
                1 -> b.optionB.id
                2 -> b.optionC.id
                3 -> b.optionD.id
                else -> -1
            }
            if (radioButtonId != -1) {
                b.optionsGroup.check(radioButtonId)
            }
        }

        // Listen for selection changes
        b.optionsGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedIndex = when (checkedId) {
                b.optionA.id -> 0
                b.optionB.id -> 1
                b.optionC.id -> 2
                b.optionD.id -> 3
                else -> -1
            }
            if (selectedIndex != -1) {
                selectedOptions[position] = selectedIndex
                onOptionSelected(position, selectedIndex)
            }
        }
    }
}
