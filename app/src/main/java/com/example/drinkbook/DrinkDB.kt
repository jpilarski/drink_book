package com.example.drinkbook

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.launch

@Entity(tableName = "drinks")
data class Drink(
    @PrimaryKey val idDrink: Int,
    val strImg: String,
    val strDrink: String,
    val strAlcoholic: String,
    val strGlass: String,
    val strInstructions: String,
    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strMeasure1: String?,
    val strMeasure2: String?,
    val strMeasure3: String?,
    val strMeasure4: String?,
    val strMeasure5: String?,
    val strMeasure6: String?
)

@Dao
interface DrinkDao {
    @Query("SELECT * FROM drinks ORDER BY strDrink")
    suspend fun getAllDrinks(): List<Drink>

    @Query("SELECT * FROM drinks WHERE idDrink = :id")
    suspend fun getOneDrink(id: Int): Drink
}

@Database(entities = [Drink::class], version = 1)
abstract class DrinksDatabase : RoomDatabase() {
    abstract fun drinkDao(): DrinkDao

    companion object {
        @Volatile
        private var INSTANCE: DrinksDatabase? = null
        fun getDatabase(context: Context): DrinksDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext, DrinksDatabase::class.java, "drinks.db"
                ).createFromAsset("import_drinks.db").build().also { INSTANCE = it }
            }
        }
    }
}

class DrinksViewModel(application: Application) : AndroidViewModel(application) {
    private val drinkDao: DrinkDao = DrinksDatabase.getDatabase(application).drinkDao()
    private val _allDrinks = MutableLiveData<List<Drink>>()
    val allDrinks: LiveData<List<Drink>> = _allDrinks
    private val _selectedDrink = MutableLiveData<Drink?>()
    val selectedDrink: LiveData<Drink?> = _selectedDrink
    fun loadAllDrinks() {
        viewModelScope.launch {
            _allDrinks.value = drinkDao.getAllDrinks()
        }
    }

    fun loadOneDrink(id: Int) {
        viewModelScope.launch {
            _selectedDrink.value = drinkDao.getOneDrink(id)
        }
    }
}