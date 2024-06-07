package com.example.dictionaryapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dictionaryapp.API.Results
import com.example.dictionaryapp.API.RetrofitInstance
import com.example.dictionaryapp.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
   private lateinit var adaptor: MeaningAdaptor


//   val sharedPref= activity?


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.SearchBtn.setOnClickListener {
            val word= binding.SearchBar.text.toString()

            if(word.isNotEmpty()){
                saveSearchedWord(word)
                getMeaning(word)
            }
        }

        adaptor = MeaningAdaptor(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adaptor
        displayLastSearch()
    }


    private fun displayLastSearch(){
        val lastSearch= getLastFiveSearch()
        binding.searchHistoryRecyclerView.text= lastSearch.joinToString("\n")
    }



private fun getMeaning(word: String) {
    GlobalScope.launch {
        val response=RetrofitInstance.dictionaryApi.getMeaning(word)
        runOnUiThread {
            setInProgressBar(false)
            response.body()?.first()?.let{
                setUI(it)
            }
        }
    }

}

//
    private fun setInProgressBar(inProgress: Boolean) {
        if (inProgress) {
            binding.ProgressBar.visibility = View.VISIBLE
            binding.SearchBtn.visibility = View.INVISIBLE
        } else {
            binding.ProgressBar.visibility = View.INVISIBLE
            binding.SearchBtn.visibility = View.VISIBLE
        }
    }


    private fun setUI(response: Results) {
        binding.SampleText.text = response.word
        binding.PhoneticView.text = response.phonetic
        adaptor.updateNewData(response.meanings)
        getLastFiveSearch()
    }


    //applying the shared preference
    public fun saveSearchedWord(word:String) {
        val sharedPreferences:SharedPreferences= getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor= sharedPreferences.edit()
        val set:MutableSet<String> = sharedPreferences.getStringSet(SEARCHED_WORDS_KEY, mutableSetOf())!!.toMutableSet()
        set.remove(word)
        set.add(word)
        val wordsList =set.toList().sortedByDescending {
            sharedPreferences.getLong(it + "_item", 0L) }.take(5)

        //save the updated list back to sharePerference

        editor.putStringSet(SEARCHED_WORDS_KEY, wordsList.toMutableSet())
        wordsList.forEachIndexed { index, word ->
            editor.putLong(word + "_time",System.currentTimeMillis() -index)
        }
        editor.apply()

    }


    public fun getLastFiveSearch():List<String>{
        val sharedPreferences:SharedPreferences= getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val set: Set<String>? = sharedPreferences.getStringSet(SEARCHED_WORDS_KEY, null)

        return set?.sortedByDescending {
            sharedPreferences.getLong(it + "_item", 0L)}?: emptyList()
    }

    //when ever user search new word by pressing btn




    companion object{
        private const val PREFS_NAME="dictonary_prefs"
        private const val SEARCHED_WORDS_KEY="searched_words"
    }

}