package io.morphtuple.fictie.activities.reader

import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
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

        binding = ActivityReaderBinding.inflate(layoutInflater)
        binding.readerWebView.setBackgroundColor(Color.TRANSPARENT)

        setContentView(binding.root)

        viewModel.getFic(intent.getStringExtra(EXTRA_FIC_ID)!!)

        viewModel.fic.observe(this) {
            if (it == null) return@observe

            val html = readerHtml.replace("$1", it.userStuff)
            val encodedHtml = Base64.encodeToString(html.toByteArray(), Base64.NO_PADDING)

            binding.readerWebView.loadData(encodedHtml, "text/html", "base64")
        }

        // TODO better error handling
        viewModel.networkError.observe(this) {
            if (it) {
                Toast.makeText(applicationContext, "Error fetching data :(", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}