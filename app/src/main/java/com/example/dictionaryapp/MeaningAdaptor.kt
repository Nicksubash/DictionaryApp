package com.example.dictionaryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionaryapp.API.Meaning
import com.example.dictionaryapp.databinding.MeaningDetailsBinding

class MeaningAdaptor(
    private var meaning: List<Meaning>
):RecyclerView.Adapter<MeaningAdaptor.MeaningViewHolder>() {

    class MeaningViewHolder( private val binding: MeaningDetailsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(meaning: Meaning){
            binding.noun.text = meaning.partOfSpeech
            binding.definition.text= meaning.definitions.joinToString("\n") {
                var currentIndex= meaning.definitions.indexOf(it)
                (currentIndex+1).toString()+ " : "+ it.definition
            }
            if(meaning.synonyms.isEmpty()){
                binding.Synonyms.visibility=View.GONE
                binding.synonymsTextView.visibility=View.GONE
            }else{
                binding.Synonyms.visibility=View.VISIBLE
                binding.synonymsTextView.visibility= View.VISIBLE
            }


            if(meaning.antonyms.isEmpty()){
                binding.Antonyms .visibility=View.GONE
                binding.AntonymsTextView.visibility=View.GONE
            }else{
                binding.Antonyms.visibility=View.VISIBLE
                binding.AntonymsTextView.visibility= View.VISIBLE
            }


        }
    }


    fun updateNewData(newMeaning:List<Meaning>){
        meaning= newMeaning
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeaningViewHolder {
        val binding= MeaningDetailsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MeaningViewHolder(binding)

    }

    override fun getItemCount(): Int {
       return meaning.size
    }

    override fun onBindViewHolder(holder: MeaningViewHolder, position: Int) {
      holder.bind(meaning[position])
    }


}