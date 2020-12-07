package com.example.tooter.daos

import com.example.tooter.models.Post
import com.example.tooter.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {

    private val db = FirebaseFirestore.getInstance()
    private val postCollections = db.collection("posts")
    private val auth = Firebase.auth

    fun addPost(text: String) {
        val currentUser = auth.currentUser!!.uid
        GlobalScope.launch {
            val userDao = UserDao()
            val user = userDao.getUserById(currentUser).await().toObject(User::class.java)!!
            val currentTime = System.currentTimeMillis()
            val post = Post(text, user, currentTime)
            postCollections.document().set(post)
        }
    }
}