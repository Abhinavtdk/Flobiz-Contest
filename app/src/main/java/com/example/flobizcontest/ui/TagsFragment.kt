package com.example.flobizcontest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import com.example.flobizcontest.R
import com.example.flobizcontest.adapter.QuestionsAdapter
import com.example.flobizcontest.adapter.TagsAdapter
import com.example.flobizcontest.databinding.FragmentTagsBinding
import com.example.flobizcontest.model.Item
import com.example.flobizcontest.service.Resource
import com.example.flobizcontest.utils.openQuestion
import com.example.flobizcontest.viewmodel.StackExchangeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tags.*

@AndroidEntryPoint
class TagsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentTagsBinding? = null
    private val binding get() = _binding
    private val viewModel by activityViewModels<StackExchangeViewModel>()
    private lateinit var tagsAdapter: QuestionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTagsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

            close_tags.setOnClickListener {
                dismiss()
                tagsAdapter.submitList(null)
            }

            use_tags_button.setOnClickListener {
                tags_input_text.editText?.text?.toString()?.trim()?.let { tags ->
                    tags_input_text.error = null
                    viewModel.searchWithFilterTags(tags)
                } ?: kotlin.run { tags_input_text.error = "Cannot be empty" }
            }


        viewModel.taggedQuestions.observe(viewLifecycleOwner) { resource ->
            when (resource) {

                is Resource.Success -> binding?.apply {
                    useTagsButton.text = "Use Tags"
                    searchingTagsProgressbar.isGone = true

                    if (resource.data!!.items.isNotEmpty()) {
                        tagsAdapter.submitList(resource.data.items)
                        questionsAfterTagRv.isGone = false
                        nullSearchTags.isGone = true
                    } else {
                        nullSearchTags.isGone = false
                        questionsAfterTagRv.isGone = true
                    }
                }

                is Resource.Error -> binding?.apply {
                    searchingTagsProgressbar.isGone = true
                    errorNetworkTags.isGone = false
                    useTagsButton.text = "Use Tags"
                    Toast.makeText(requireContext(), "Error!!!", Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> binding?.apply {
                    searchingTagsProgressbar.isGone = false
                    errorNetworkTags.isGone = true
                    useTagsButton.text = "Loading"
                    nullSearchTags.isGone = true
                    questionsAfterTagRv.isGone = true
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding?.apply {
            questionsAfterTagRv.setHasFixedSize(true)
            tagsAdapter = QuestionsAdapter(object : QuestionsAdapter.OnClickListener {
                override fun openQuestion(questionItem: Item) {
                    openQuestion(questionItem, requireContext())
                }
            }, requireContext())
            questionsAfterTagRv.adapter = tagsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}