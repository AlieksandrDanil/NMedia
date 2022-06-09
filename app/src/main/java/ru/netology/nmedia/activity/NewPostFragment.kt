package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.repository.getConstDraftPostId
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.DraftContentPostViewModel
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val viewModelForDraft: DraftContentPostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private lateinit var draft: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )

        var draftContent: String? = null

        arguments?.textArg?.let(binding.edit::setText)
            ?: binding.edit.setText(
                viewModelForDraft.getPostById(getConstDraftPostId())?.content
            ).let {
                draftContent = it.toString()
            }

        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            //if (draftContent != null)
            draft = binding.edit.text.toString()
            if (::draft.isInitialized)
                viewModelForDraft.save(draft)
            findNavController().navigateUp()
        }
        // The callback can be enabled or disabled here or in the lambda
        callback.isEnabled = true

        binding.ok.setOnClickListener {
            if (!binding.edit.text.isNullOrBlank()) {
                if (draftContent != null && viewModel.edited.value?.id != 0L) {
                    viewModel.toEmpty()
                }
                viewModel.changeContent(binding.edit.text.toString())
                viewModel.save()
            }
            viewModelForDraft.save("")
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        return binding.root
    }
}