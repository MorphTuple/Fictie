package io.morphtuple.fictie.activities.reader

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import io.morphtuple.fictie.common.changeBackground
import io.morphtuple.fictie.common.changeTextColor
import io.morphtuple.fictie.databinding.ActivityReaderBinding
import io.morphtuple.fictie.readAssetString

@AndroidEntryPoint
class ReaderActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_FIC_ID = "fic_id"
        const val READER_FILENAME = "reader.html"
    }

    private val viewModel by viewModels<ReaderViewModel>()

    private lateinit var binding: ActivityReaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val readerHtml = assets.open(READER_FILENAME).readAssetString()

        val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val rt = sp.getString("reader_theme", null)

        binding = ActivityReaderBinding.inflate(layoutInflater)
        binding.readerWebView.setBackgroundColor(Color.TRANSPARENT)
        binding.readerWebView.settings.builtInZoomControls = true
        binding.readerWebView.settings.displayZoomControls = false
        binding.readerWebView.settings.javaScriptEnabled = true

        setContentView(binding.root)

        fun injectTheming() {
            fun setDarkReader() {
                binding.readerWebView.changeBackground("black")
                binding.readerWebView.changeTextColor("white")
            }

            fun setLightReader() {
                binding.readerWebView.setBackgroundColor(Color.WHITE)
                binding.readerWebView.changeBackground("white")
                binding.readerWebView.changeTextColor("black")
            }

            when (rt) {
                null, "follow_application" -> {
                    when (applicationContext.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_YES -> setDarkReader()
                        Configuration.UI_MODE_NIGHT_NO -> setLightReader()
                        else -> setDarkReader()
                    }
                }
                "dark" -> {
                    setDarkReader()
                }
                else -> {
                    setLightReader()
                }
            }
        }

        viewModel.getFic(intent.getStringExtra(EXTRA_FIC_ID)!!)

        viewModel.fic.observe(this) {
            if (it == null) return@observe

            val html = readerHtml.replace("$1", it.userStuff)
            val encodedHtml = Base64.encodeToString(html.toByteArray(), Base64.NO_PADDING)

            binding.loadingIndicator.visibility = View.GONE
            binding.readerWebView.loadData(encodedHtml, "text/html", "base64")
            injectTheming()
        }

        // TODO better error handling
        viewModel.networkError.observe(this) {
            if (it) {
                Toast.makeText(applicationContext, "Error fetching data :(", Toast.LENGTH_LONG)
                    .show()
                finish()
            }
        }
    }
}