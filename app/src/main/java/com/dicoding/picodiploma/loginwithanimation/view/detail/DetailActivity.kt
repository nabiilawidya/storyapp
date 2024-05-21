package com.dicoding.picodiploma.loginwithanimation.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDetail()
    }

    private fun setupDetail() {
        val storyItem: ListStoryItem? = intent.getParcelableExtra(EXTRA_DETAIL)
        storyItem?.let {
            binding.apply {
                tvName.text = it.name
                tvDescription.text = it.description
                Glide.with(this@DetailActivity).load(it.photoUrl).fitCenter().into(ivPhoto)
            }
        }
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
}
