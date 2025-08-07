package com.example.mcqtester.adminside

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mcqtester.databinding.ItemStudentResultBinding
import com.example.mcqtestlab.model.StudentResult

class StudentResultAdapter(
    private val studentList: List<StudentResult>
) : RecyclerView.Adapter<StudentResultAdapter.ResultViewHolder>() {

    inner class ResultViewHolder(val binding: ItemStudentResultBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemStudentResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val student = studentList[position]
        holder.binding.tvStudentName.text = student.studentId
        holder.binding.tvStudentScore.text = "Score: ${student.score} / ${student.total}"
    }

    override fun getItemCount(): Int = studentList.size
}
