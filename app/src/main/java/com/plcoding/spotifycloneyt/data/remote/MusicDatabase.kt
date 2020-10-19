package com.plcoding.spotifycloneyt.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.plcoding.spotifycloneyt.data.entity.Song
import com.plcoding.spotifycloneyt.other.Constants.SONG_COLLECTION
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class MusicDatabase {

    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection(SONG_COLLECTION)

    suspend fun getAllSongs(): List<Song>{
        return try{
            //                     ↓ await은 any타입이므로 toObject이용해서 Song으로 받음
            songCollection.get().await().toObjects(Song::class.java)
        }catch (e:Exception){
            return emptyList()
        }
    }

}