package com.msoe.dndassistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment

class CheatSheetPageFragment : Fragment() {

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_CONTENT = "arg_content"

        fun newInstance(title: String, content: String): CheatSheetPageFragment {
            val fragment = CheatSheetPageFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_CONTENT, content)
            }
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rawHtml = requireArguments().getString(ARG_CONTENT)

        val textView = TextView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            text = HtmlCompat.fromHtml(rawHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)
            textSize = 16f
            setPadding(24, 24, 24, 24)

            setLineSpacing(8f, 1.2f)
        }
        return ScrollView(requireContext()).apply {
            addView(textView)
        }
    }
}
