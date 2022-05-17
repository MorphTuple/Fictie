package io.morphtuple.fictie.activities.reader

import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.morphtuple.fictie.databinding.ActivityReaderBinding

@AndroidEntryPoint
class ReaderActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_FIC_ID = "fic_id"
    }

    private val viewModel by viewModels<ReaderViewModel>()

    private lateinit var binding: ActivityReaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReaderBinding.inflate(layoutInflater)
        binding.readerWebView.setBackgroundColor(Color.TRANSPARENT)

        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        viewModel.getFic(intent.getStringExtra(EXTRA_FIC_ID)!!)

        viewModel.fic.observe(this) {
            if (it == null) return@observe

            val html =
                "<html> <head> <style>.userstuff p { margin: 1.286em auto; padding: 0; line-height: 1.5; font-size: 0.875em; } body { background: black; color: white; margin : 1em 2em; font-family: 'Lucida Grande','Lucida Sans Unicode','GNU Unifont',Verdana,Helvetica,sans-serif; }\n .userstuff {word-wrap: break-word;}</style> </head>  <body> <div class=\"userstuff\">${it.userStuff}</div> </body> </html>"

            val encodedHtml = Base64.encodeToString(html.toByteArray(), Base64.NO_PADDING)

            binding.readerWebView.loadData(encodedHtml, "text/html", "base64")
        }
    }
}