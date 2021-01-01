package de.reiss.bible2net.theword.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import de.reiss.bible2net.theword.architecture.AppActivity
import de.reiss.bible2net.theword.databinding.PrivacyPolicyActivityBinding

class PrivacyPolicyActivity : AppActivity() {

    companion object {
        fun createIntent(context: Context): Intent =
            Intent(context, PrivacyPolicyActivity::class.java)
    }

    private lateinit var binding: PrivacyPolicyActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PrivacyPolicyActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.webView.loadUrl("file:///android_asset/privacy_policy.html")
    }
}
