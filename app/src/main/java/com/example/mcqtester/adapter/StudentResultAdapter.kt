package com.example.mcqtester.adminside.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mcqtester.databinding.ItemStudentResultBinding
import com.example.mcqtestlab.model.StudentResult

class StudentResultAdapter(
    private val results: List<StudentResult>
) : RecyclerView.Adapter<StudentResultAdapter.ResultViewHolder>() {

    inner class ResultViewHolder(val binding: ItemStudentResultBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemStudentResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun getItemCount() = results.size

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = results[position]
        val b = holder.binding

        b.tvStudentName.text = result.studentId
        b.tvStudentScore.text = "Score: ${result.score} / ${result.total}"
    }
}
