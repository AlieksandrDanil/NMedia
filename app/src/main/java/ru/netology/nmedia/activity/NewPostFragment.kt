package ru.netology.nmedia.activity

//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import androidx.activity.result.contract.ActivityResultContract
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

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

        binding.ok.setOnClickListener {
            if (binding.edit.text.isNullOrBlank()) {
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            } else {
                viewModel.changeContent(binding.edit.text.toString())
                viewModel.save()
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            }
        }
        return binding.root
    }
}

//        setContentView(binding.root)

//        binding.edit.setText(intent.getStringExtra(RESULT_KEY))
//        binding.edit.requestFocus()
//        binding.ok.setOnClickListener {
//            val intent = Intent()
//            if (binding.edit.text.isNullOrBlank()) {
//                setResult(RESULT_CANCELED, intent)
//            } else {
//                val content = binding.edit.text.toString()
//                intent.putExtra(RESULT_KEY, content)
//                setResult(RESULT_OK, intent)
//            }
//            finish()
//        }
//    }
//}
//    object NewPostResultContract : ActivityResultContract<Unit, String?>() {
//        override fun createIntent(context: Context, input: Unit): Intent =
//            Intent(context, NewPostFragment::class.java)
//
//        override fun parseResult(resultCode: Int, intent: Intent?): String? =
//            if (resultCode == Activity.RESULT_OK) {
//                intent?.getStringExtra(RESULT_KEY)
//            } else {
//                null
//            }
//    }
//
//    object EditPostResultContract : ActivityResultContract<String, String?>() {
//        override fun createIntent(context: Context, input: String): Intent {
//            val intent = Intent(context, NewPostFragment::class.java)
//            intent.putExtra(RESULT_KEY, input)
//            return intent
//        }
//
//        override fun parseResult(resultCode: Int, intent: Intent?): String? =
//            if (resultCode == Activity.RESULT_OK) {
//                intent?.getStringExtra(RESULT_KEY)
//            } else {
//                null
//            }
//    }
//
//    private companion object {
//        private const val RESULT_KEY = "newPostContent"
//    }
