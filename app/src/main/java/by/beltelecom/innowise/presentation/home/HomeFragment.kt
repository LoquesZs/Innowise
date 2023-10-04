package by.beltelecom.innowise.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.beltelecom.innowise.MainActivity
import by.beltelecom.innowise.R
import by.beltelecom.innowise.databinding.FragmentHomeBinding
import by.beltelecom.innowise.domain.entities.CollectionDomain
import by.beltelecom.innowise.presentation.PhotosAdapter
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private var chipsController: CollectionChipsController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chipsController = CollectionChipsController()

        setupSearchBar()

        setupRecyclerView()

        binding.networkStub.tryAgainButton.setOnClickListener {
            when (val state = viewModel.state.value) {
                is HomeUIState.Error -> {
                    when (val source = state.source) {
                        Source.Featured -> viewModel.getPopular()
                        is Source.Search -> viewModel.search(source.query)
                        else -> Unit
                    }
                }

                else -> Unit
            }
        }

        binding.emptyStub.explore.setOnClickListener {
            binding.searchBarEditText.text = null
            viewModel.getPopular()
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->

            when (state) {
                is HomeUIState.Error -> {
                    Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
                    if (state.source is Source.Collections) {
                        binding.chipGroup.visibility = View.GONE
                    } else {
                        binding.progressIndicator.visibility = View.INVISIBLE
                        binding.recyclerView.visibility = View.INVISIBLE
                        binding.emptyStub.root.visibility = View.GONE
                        binding.networkStub.root.visibility = View.VISIBLE
                    }
                }

                is HomeUIState.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }

                is HomeUIState.Success -> {
                    if (state.source is Source.Collections) {
                        state.collections?.let { chipsController?.submitList(it) }
                        (activity as MainActivity).removeSplashScreen()
                    }
                    binding.progressIndicator.visibility = View.INVISIBLE
                    if (binding.networkStub.root.visibility == View.VISIBLE) binding.networkStub.root.visibility = View.GONE

                    if (state.photos?.isEmpty() == true) {
                        binding.emptyStub.root.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.INVISIBLE
                        return@observe
                    }

                    if (binding.emptyStub.root.visibility == View.VISIBLE) binding.emptyStub.root.visibility = View.GONE
                    if (binding.recyclerView.visibility == View.INVISIBLE) binding.recyclerView.visibility = View.VISIBLE
                    (binding.recyclerView.adapter as PhotosAdapter).submitList(state.photos)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        ).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        }
        binding.recyclerView.layoutManager = staggeredGridLayoutManager
        binding.recyclerView.adapter = PhotosAdapter { photo ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailFragment(photo)
            )
        }
        binding.recyclerView.setItemViewCacheSize(10)
        binding.recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = staggeredGridLayoutManager.childCount
                    val totalItemCount = staggeredGridLayoutManager.itemCount
                    var firstVisibleItemPosition = 0
                    val firstVisibleItemPositions =
                        (recyclerView.layoutManager as StaggeredGridLayoutManager).findFirstVisibleItemPositions(null)
                    firstVisibleItemPosition = firstVisibleItemPositions[0]
                    if (viewModel.state.value !is HomeUIState.Loading) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                            && firstVisibleItemPosition >= 0
                        ) {
                            viewModel.loadMore()
                        }
                    }
                }
            }

        )
    }

    private fun setupSearchBar() {
        binding.searchBarEditText.onFocusChangeListener = View.OnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                binding.searchBar.isHintEnabled = false
                binding.searchBarEditText.hint = ""
            }
        }
        binding.searchBarEditText.doOnTextChanged { text, _, _, _ ->

            chipsController?.clearSelection()

            if (text?.isNotEmpty() == true) {
                binding.searchBar.isHintEnabled = false
                binding.searchBarEditText.hint = ""
                viewModel.search(text.toString())
            } else {
                binding.searchBar.hint = getString(R.string.search_bar_hint)
                viewModel.getPopular()
            }
        }
        binding.searchBarEditText.setOnEditorActionListener { textView, _, _ ->
            viewModel.search(textView.text.toString())
            true
        }
    }

    inner class CollectionChipsController {

        private var collections: List<CollectionDomain> = emptyList()

        private var selectedCollection: CollectionDomain? by Delegates.observable(null) { _, _, _ ->
            invalidate()
        }

        private fun invalidate() {
            with(binding.chipGroup) {
                removeAllViews()
                selectedCollection?.let { addChip(it, true) }
                collections.forEach {
                    if (selectedCollection?.id == it.id) {
                        return@forEach
                    }
                    addChip(it)
                }
                binding.chipScroll.smoothScrollTo(0, 0)
            }
        }

        private fun addChip(collection: CollectionDomain, selected: Boolean = false) {
            (layoutInflater.inflate(R.layout.chip_collection, binding.chipGroup, false) as Chip).apply {
                id = View.generateViewId()
                tag = collection.id
                text = collection.title
                isChecked = selected
                setOnClickListener {
                    selectedCollection = if (selectedCollection?.id == collection.id) {
                        binding.searchBarEditText.text = null
                        viewModel.getPopular()
                        null
                    } else {
                        binding.searchBarEditText.setText(collection.title)
                        collection
                    }
                }
                binding.chipGroup.addView(this)
            }
        }

        fun submitList(collections: List<CollectionDomain>) {
            this.collections = collections
            invalidate()
        }

        fun clearSelection() {
            if (selectedCollection != null) {
                selectedCollection = null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        chipsController = null
        _binding = null
    }
}
