package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.*
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

        arguments?.textArg?.let(binding.edit::setText)
        ?: binding.edit.setText(
            viewModelForDraft.getPostById(getConstDraftPostId())?.content
        )

        binding.edit.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
                if (p2?.action == KeyEvent.ACTION_DOWN ||
                    p1 == KeyEvent.KEYCODE_ENTER
                ) {
                    draft = binding.edit.text.toString()
                    binding.edit.clearFocus()
                    binding.edit.isCursorVisible = false
                    AndroidUtils.hideKeyboard(requireView())
                    findNavController().navigateUp()
                    return true
                }
                return false
            }
        })

        binding.ok.setOnClickListener {
            if (binding.edit.text.isNullOrBlank()) {
                AndroidUtils.hideKeyboard(requireView())
            } else {
                viewModel.changeContent(binding.edit.text.toString())
                viewModel.save()
                AndroidUtils.hideKeyboard(requireView())
            }
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        if(::draft.isInitialized) viewModelForDraft.save(draft)
    }
}