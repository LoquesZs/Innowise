package by.beltelecom.innowise.presentation.bookmark

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.beltelecom.innowise.databinding.FragmentBookmarksBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class BookmarksFragment : Fragment() {

    private var _binding: FragmentBookmarksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: BookmarkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressIndicator.visibility = if (it) View.VISIBLE else View.INVISIBLE
            binding.shimmer.root.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }

        binding.explore.setOnClickListener {
            findNavController().navigate(
                BookmarksFragmentDirections.actionBookmarkFramentToHomeFragment()
            )
        }

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        ).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        }

        val adapter = BookmarksAdapter(
            onPhotoClick = {
                findNavController().navigate(
                    BookmarksFragmentDirections.actionBookmarkFramentToDetailFragment(it)
                )
            }
        )
        binding.bookmarks.layoutManager = staggeredGridLayoutManager
        binding.bookmarks.adapter = adapter

        adapter.addLoadStateListener {
            Log.d("Bookmarks", "photos: ${adapter.itemCount}")
            if (adapter.itemCount > 0) {
                binding.emptyStub.visibility = View.GONE
                binding.bookmarks.visibility = View.VISIBLE
            } else {
                binding.emptyStub.visibility = View.VISIBLE
                binding.bookmarks.visibility = View.GONE
            }
        }

        viewModel.bookmarks.observe(viewLifecycleOwner) { photos ->
            adapter.submitData(lifecycle, photos)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
