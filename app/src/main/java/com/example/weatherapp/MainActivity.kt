package com.example.weatherapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {

    private val apiKey = "05fde786a4a8d6e6d603a985165120e5"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val locationEditText = findViewById<EditText>(R.id.locationEditText)
        val getWeatherButton = findViewById<Button>(R.id.getWeatherButton)
        val locationTextView = findViewById<TextView>(R.id.locationTextView)
        val temperatureTextView = findViewById<TextView>(R.id.temperatureTextView)
        val descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)

        getWeatherButton.setOnClickListener {
            val location = locationEditText.text.toString()

            if (location.isNotEmpty()) {
                getWeatherData(location, locationTextView, temperatureTextView, descriptionTextView)
            } else {
                Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getWeatherData(location: String, locationTextView: TextView, temperatureTextView: TextView, descriptionTextView: TextView) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getCurrentWeather(location, apiKey)
                }

                // Update the UI
                locationTextView.text = response.name
                temperatureTextView.text = "Temperature: ${response.main.temp}°C"
                descriptionTextView.text = "Description: ${response.weather[0].description}"

            } catch (e: HttpException) {
                Toast.makeText(this@MainActivity, "Error fetching weather data", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
