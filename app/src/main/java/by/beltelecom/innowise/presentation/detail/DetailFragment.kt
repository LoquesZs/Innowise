package by.beltelecom.innowise.presentation.detail

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.beltelecom.innowise.R
import by.beltelecom.innowise.databinding.FragmentDetailBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.properties.Delegates

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    private val args by navArgs<DetailFragmentArgs>()

    private val SCALE_FACTOR_DEFAULT = 1F

    private var scaleFactor: Float by Delegates.observable(initialValue = SCALE_FACTOR_DEFAULT) { property, oldValue, newValue ->
        binding.image.scaleX = newValue
        binding.image.scaleY = newValue
    }

    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            scaleFactor = SCALE_FACTOR_DEFAULT
        }
    }

    private lateinit var scaleDetector: ScaleGestureDetector

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        scaleDetector = ScaleGestureDetector(requireContext(), scaleListener)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navUpButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.errorStub.explore.setOnClickListener {
            findNavController().navigate(
                DetailFragmentDirections.actionDetailFragmentToHomeFragment()
            )
        }

        val id = args.id
        viewModel.setID(id)

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressIndicator.visibility = View.VISIBLE
                binding.bookmark.isEnabled = false
                binding.download.isEnabled = false
            } else {
                binding.progressIndicator.visibility = View.INVISIBLE
                binding.bookmark.isEnabled = true
                binding.download.isEnabled = true
            }
        }

        viewModel.photo.observe(viewLifecycleOwner) {
            binding.toolbar.title = it.photographer
            Glide.with(this)
                .load(it.sources.original)
                .thumbnail(
                    Glide.with(this)
                        .load(it.sources.large2x),
                    Glide.with(this)
                        .load(it.sources.large),
                    Glide.with(this)
                        .load(it.sources.medium),
                    Glide.with(this)
                        .load(it.sources.small)
                )
                .placeholder(R.drawable.placeholder)
                .into(binding.image)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            if (it) {
                binding.imageScroll.visibility = View.INVISIBLE
                binding.errorStub.root.visibility = View.VISIBLE
                Toast.makeText(
                    requireContext(),
                    getString(R.string.photo_not_found),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.imageScroll.visibility = View.VISIBLE
                binding.errorStub.root.visibility = View.INVISIBLE
            }
        }

        binding.imageScroll.setOnTouchListener { view, motionEvent ->
            scaleDetector.onTouchEvent(motionEvent)
            view.performClick()
            return@setOnTouchListener true
        }

        val getFilePath = registerForActivityResult(
            ActivityResultContracts.CreateDocument("image/jpeg")
        ) { filePath ->
            filePath?.let { filePath ->
                Single
                    .fromCallable {
                        Glide.with(this)
                            .asBitmap()
                            .load(viewModel.photo.value?.sources?.original)
                            .submit()
                            .get()
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { bitmap ->
                        val fos = requireContext().contentResolver.openOutputStream(
                            filePath,
                            "w"
                        )
                        fos?.use {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                        }
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.image_saved_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .subscribe(
                        { },
                        {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.download_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
            }
        }

        binding.download.setOnClickListener {
            getFilePath.launch("image-${viewModel.photo.value?.id}")
        }

        binding.bookmark.setOnTouchListener { view, event ->
            view.performClick()
            return@setOnTouchListener when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    when (viewModel.bookmark.value) {
                        true -> viewModel.removeFromBookmarks()
                        false -> viewModel.addToBookmarks()
                        else -> { /* Do nothing */
                        }
                    }
                    true
                }

                else -> {
                    false
                }
            }
        }

        viewModel.bookmark.observe(viewLifecycleOwner) {
            binding.bookmarkIcon.isChecked = it
        }
    }
}