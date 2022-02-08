package com.cursosant.android.stores.mainModule.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.android.volley.toolbox.JsonObjectRequest
import com.cursosant.android.stores.StoreApplication
import com.cursosant.android.stores.common.entities.StoreEntity
import com.cursosant.android.stores.common.utils.Constants
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainInteractor {

    /*
    fun getStores(callback: (MutableList<StoreEntity>) -> Unit) {
        val isLocal = true
        if (isLocal) {
            getStoresRoom { storeList -> callback(storeList) }
        } else {
            getStoresAPI { storeList -> callback(storeList) }
        }
    }
     */

    /*
    fun getStoresAPI(callback: (MutableList<StoreEntity>) -> Unit) {
        val url = Constants.STORES_URL + Constants.GET_ALL_PATH
        var storeList = mutableListOf<StoreEntity>()

        val jsonObjectRequest = JsonObjectRequest(url, null, { response ->

            val status = response.optInt(Constants.STATUS_PROPERTY, Constants.ERROR)
            if (status == Constants.SUCCESS) {

                val jsonArray = response.optJSONArray(Constants.STORES_PROPERTY)
                val jsonList = if(jsonArray != null) jsonArray.toString() else JsonArray().toString()
                val mutableListType = object : TypeToken<MutableList<StoreEntity>>(){}.type
                storeList = Gson().fromJson(jsonList, mutableListType)

                callback(storeList)
                return@JsonObjectRequest
            }
            callback(storeList)
        }, { error ->
            error.printStackTrace()
            callback(storeList)
        })

        StoreApplication.storeAPI.addToRequestQueue(jsonObjectRequest)
    }
    */

    /*
    fun getStoresRoom(callback: (MutableList<StoreEntity>) -> Unit) {
        doAsync {
            val storesList = StoreApplication.database.storeDao().getAllStores()
            uiThread {
                callback(storesList)
            }
        }
    }
     */

    val stores: LiveData<MutableList<StoreEntity>> = liveData {
        //kotlinx.coroutines.delay(1_000)
        val storesLiveData = StoreApplication.database.storeDao().getAllStores()
        emitSource(storesLiveData.map { stores ->
            stores.sortedBy { it.name }.toMutableList()
        })
    }

    fun deleteStore(storeEntity: StoreEntity, callback: (StoreEntity) -> Unit) {
        doAsync {
            StoreApplication.database.storeDao().deleteStore(storeEntity)
            uiThread {
                callback(storeEntity)
            }
        }
    }

    fun updateStore(storeEntity: StoreEntity, callback: (StoreEntity) -> Unit) {
        doAsync {
            StoreApplication.database.storeDao().updateStore(storeEntity)
            uiThread {
                callback(storeEntity)
            }
        }
    }
}