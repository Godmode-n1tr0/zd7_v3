package com.bignerdranch.android.pract7.data.db

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bignerdranch.android.pract7.data.entities.FurnitureTypeEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseInitializer(private val db: AppDatabase) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            populateData(this@DatabaseInitializer.db)
        }
    }

    private suspend fun populateData(db: AppDatabase) {
        val furnitureDao = db.furnitureDao()
        furnitureDao.insertAll(listOf(
            FurnitureTypeEntity(name = "Диван", imageUrl = "https://avatars.mds.yandex.net/i?id=da1c1082fba898410eef6c5aea06db808457561b-16187200-images-thumbs&n=13"),
            FurnitureTypeEntity(name = "Стул", imageUrl = "https://yandex-images.clstorage.net/Ua5Eg1215/42db5czrII/h42rq38DJs-l3f_agrIr0zlz6xcj4zsWrGDO_jpyTDlbTCb0-Jk4JdfPXlgJFfk9z2iM3D6GJfoo0Ws3NIeqWcDV6ec7RDGW953ljoqxLtEkLqgGP7k_S7YikqB_fUY1CE1MyI7uSBiupHQrdj2xOMNpi50hxWOQQMkYZjG07x-pgIqiaAhx14v4nuGXmh7lES3OMzGgMESjZ6CBZhx9Y3EPY_GE-nAXjaV_KkwcgTHDd7iXeNB7jmWkM20eg8QxqYWbs3shQdaCx4jgm486xzcLwhEenwp6j0GrlEwhSkdsLULdleZGMJW-TwZvMI8a3lmEs2LyfIZuoClkN6b4Bp6Gt4hkPW3bu_qzypaoA9s5NekaPJoKY690p4lgIltlTxddwYXLeSKuu2kVdz6YPudMpIBF2WeYW4cqbBuvzg-_jYugWClY2o3ssMGCszbvIDzWFx2ZOka0VZKmZAx8UHE3Yf2e91s9gI5SDUYWmxDJYrSKWdxerXqhCGkxq_4imqOuunodQNeS6IXroqsl4SQ51hc2rwdilWq6jkU-T2N4O33RotdEIq-ObCJ8G5w010m8jkL_XI5mgxhwGZX-C5irqJNjCEXkMuZ1rK3JdoNLvwkAbo6Q49ni6BbJUJrVyp027rpfDOOj18IXTigD9talJFE23SOXq4cdh-R8Rq2oJiCQAVNxKzsl-2CjxvsMCjxNACOOG-IQrCAZAtwQkgxZdie424trqZZJUY9pRbJWb6WSdB4vHaeEFIGsvMAkomos2c8T_e566jFoY0nwSQa9BQbtB5YqFWwo0Mka0NcEmXRqO1zEo6RayBOJJAg70-PsGjxZIdQgzl1BpjRKoC6vaJ9Fkntot-96o2TBNsDL88BI78Gb6ttva5jMWxwSjNpx4n3fQqikWQebDGGIcl7moF15letUqQRZBaj6yCnmr2OZhlCxIztuOyTsyX7JAzGJD64JVmrZaa_RyM"),
            FurnitureTypeEntity(name = "Стол", imageUrl = "https://cs1.livemaster.ru/storage/54/c9/5848b4b605b63825e7b4b25052ve--dlya-doma-i-interera-stol-iz-massiva.jpg")
        ))
    }
}
