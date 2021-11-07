package com.example.flobizcontest.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.flobizcontest.R
import com.example.flobizcontest.adapter.QuestionsAdapter
import com.example.flobizcontest.databinding.FragmentQuestionsListBinding
import com.example.flobizcontest.databinding.FragmentTagsBinding
import com.example.flobizcontest.model.Item
import com.example.flobizcontest.service.Resource
import com.example.flobizcontest.utils.openQuestion
import com.example.flobizcontest.viewmodel.StackExchangeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_questions_list.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.math.log

@AndroidEntryPoint
class QuestionsListFragment : Fragment() {

    private val viewModel: StackExchangeViewModel by viewModels()
    private var job: Job? = null
    private lateinit var trendingQuestionsAdapter: QuestionsAdapter
    private lateinit var searchedQuestionsAdapter: QuestionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_questions_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        clearQuery.setOnClickListener {
            searchET.text.clear()
            hideKeyboard()
        }
        filterIcon.setOnClickListener {
            findNavController().navigate(R.id.action_questionsListFragment_to_tagsFragment)
        }

        viewModel.questions.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    shimmerQuestions.isGone = true
                    shimmerQuestions.stopShimmer()
                    questions_trending_rv.isGone = false
                    Log.d(
                        "QuestionsFragment",
                        "onViewCreated: + InSuccess + ${resource.data!!.items} "
                    )
                    trendingQuestionsAdapter.submitList(resource.data!!.items)
                    Log.d(
                        "QuestionsFragment",
                        "onViewCreated: + inAdapter + ${trendingQuestionsAdapter.currentList} "
                    )
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                    Log.d("QuestionsFragment", "onViewCreated: + In Error")
                }

                is Resource.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    shimmerQuestions.isGone = false
                    shimmerQuestions.stopShimmer()
                }
            }
        }

        viewModel.searchedQuestions.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    progress_bar.isGone = true
                    if (resource.data!!.items.isNotEmpty()) {
                        searchedQuestionsAdapter.submitList(resource.data.items)
                        questions_searched_rv.isGone = false
                        null_search.isGone = true
                    } else {
                        null_search.isGone = false
                        questions_searched_rv.isGone = true
                    }
                }
            }
        }

        searchET?.doOnTextChanged { text, _, _, _ ->
            text?.let {
                if (it.trim().isNotBlank() && it.trim().isNotEmpty()) {
                    clearQuery.isGone = false
                    searchIcon.isVisible = false
                    questions_trending_rv.isGone = true
                    //Show progress bar
                    search_key_text.text = "Searched questions"
                    progress_bar.isGone = false
                    null_search.isGone = true
                    error_network.isGone = true
                    questions_searched_rv.isGone = true
                    searchQuestion(it.trim().toString())
                } else {
                    clearQuery.isGone = true
                    searchIcon.isVisible = true
                    search_key_text.text = "Trending Questions"
                    questions_trending_rv.isGone = false
                    questions_searched_rv.isGone = true
                }
            }
        }

    }

    private fun searchQuestion(query: String) {
        job?.cancel()
        job = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            delay(2000)
            viewModel.searchQuestions(query)
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun setUpRecyclerView() {

        questions_trending_rv.setHasFixedSize(true)
        trendingQuestionsAdapter = QuestionsAdapter(object : QuestionsAdapter.OnClickListener {
            override fun openQuestion(questionItem: Item) {
                openQuestion(questionItem, requireContext())
            }
        }, requireContext())
        questions_trending_rv.adapter = trendingQuestionsAdapter

        questions_searched_rv.setHasFixedSize(true)
        searchedQuestionsAdapter = QuestionsAdapter(object : QuestionsAdapter.OnClickListener {
            override fun openQuestion(questionItem: Item) {
                openQuestion(questionItem, requireContext())
            }
        }, requireContext())
        questions_searched_rv.adapter = searchedQuestionsAdapter


    }


}