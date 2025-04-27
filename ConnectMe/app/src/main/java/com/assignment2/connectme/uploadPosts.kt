package com.assignment2.connectme

import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment2.connectme.adapters.GalleryAdapter
import com.bumptech.glide.Glide

class UploadPosts : AppCompatActivity() {

    private lateinit var imagePreview: ImageView
    private lateinit var recyclerGallery: RecyclerView
    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var btnOpenGallery: TextView
    private lateinit var btnOpenCamera: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_posts)

        imagePreview = findViewById(R.id.imagePreview)
        recyclerGallery = findViewById(R.id.recyclerGallery)
        btnOpenGallery = findViewById(R.id.btnOpenGallery)
        btnOpenCamera = findViewById(R.id.btnOpenCamera)

        setSupportActionBar(findViewById(R.id.toolbarUpload))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarUpload).apply {
            findViewById<ImageView>(R.id.buttonClose).setOnClickListener {
                finish() // close when cross button clicked
            }
        }

        // Setup RecyclerView
        galleryAdapter = GalleryAdapter { imageUri ->
            Glide.with(this)
                .load(imageUri)
                .centerCrop()
                .into(imagePreview)
        }
        recyclerGallery.layoutManager = GridLayoutManager(this, 4)
        recyclerGallery.adapter = galleryAdapter

        loadImagesFromGallery()

        btnOpenGallery.setOnClickListener {
            // TODO: Implement open full gallery view
        }

        btnOpenCamera.setOnClickListener {
            // TODO: Open Camera Intent
        }
    }

    private fun loadImagesFromGallery() {
        val images = mutableListOf<Uri>()
        val uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)

        val cursor = contentResolver.query(
            uriExternal,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )

        cursor?.use {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(uriExternal, id)
                images.add(contentUri)
            }
            galleryAdapter.submitList(images)
            if (images.isNotEmpty()) {
                Glide.with(this)
                    .load(images[0])
                    .centerCrop()
                    .into(imagePreview)
            }
        }
    }
}