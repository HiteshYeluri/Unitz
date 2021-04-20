package com.example.unitz

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
//    Holds the conversion rate of the selection
    var conversionRate = 0f
//   from conversion unit
    var primaryUnit = ""
//    To conversion unit
    var secondaryUnit = ""

//    Default conversion method
    var selectedUnit = "currency"
//    This method is invoked when the current activity is created. This is the first method in the activity life cycle.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        calls the layout for this activity
        setContentView(R.layout.activity_main)
//        Initial setup of the custom methods
        actionButtonsSetup()
        spinnerSetup()
        textChanged()
    }

//    Action listeners of the buttons are defined
    private fun actionButtonsSetup(){
        val lengthBtn: ImageButton = findViewById(R.id.lengthBtn)
        val distanceBtn: ImageButton = findViewById(R.id.distanceBtn)
        val temperatureBtn: ImageButton = findViewById(R.id.tempBtn)
        val currencyBtn: ImageButton = findViewById(R.id.currencyBtn)

        val reverseBtn: ImageView = findViewById(R.id.reverse_btn)
        lengthBtn.setOnClickListener {
            selectedUnit = "length"
            spinnerSetup()
        }
        distanceBtn.setOnClickListener {
            selectedUnit = "distance"
            spinnerSetup()
        }
        temperatureBtn.setOnClickListener {
            selectedUnit = "temperature"
            spinnerSetup()
        }
        currencyBtn.setOnClickListener {
            selectedUnit = "currency"
            spinnerSetup()
        }
//        Alters the conversion units
        reverseBtn.setOnClickListener{
            val spinner1: Spinner = findViewById(R.id.spinner_firstConversion)
            val spinner2: Spinner = findViewById(R.id.spinner_secondConversion)

            val s1_pos = spinner1.getSelectedItemPosition()
            val s2_pos = spinner2.getSelectedItemPosition()

            spinner1.setSelection(s2_pos)
            spinner2.setSelection(s1_pos)

        }

    }

//    Create and assign the options for the spinners when a conversion is selected
    private fun spinnerSetup(){
//        Holds all the available units for the selected conversion
        var primaryUnits = 0
        var secondaryUnits = 0
        when(selectedUnit){
            "length" -> {
                primaryUnits = R.array.length
                secondaryUnits = R.array.length
            }
            "distance" ->{
                primaryUnits = R.array.distance
                secondaryUnits = R.array.distance
            }
            "temperature" -> {
                primaryUnits = R.array.temperature
                secondaryUnits = R.array.temperature
            }
            "currency" -> {
                primaryUnits = R.array.currency
                secondaryUnits = R.array.currency
            }
        }
//        Initialise the spinners
        val spinner1: Spinner = findViewById(R.id.spinner_firstConversion)
        val spinner2: Spinner = findViewById(R.id.spinner_secondConversion)

//      Declares an adapter for the spinners with available units
        ArrayAdapter.createFromResource(
                this,
                primaryUnits,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner1.adapter = adapter
        }
        ArrayAdapter.createFromResource(
                this,
                secondaryUnits,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner2.adapter = adapter
        }

//      handles the items selections in the spinner
        spinner1.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
//                If the selected primary unit is same as required unit then the required unit is updated
                primaryUnit = parent?.getItemAtPosition(position).toString()
                val s1_pos = spinner1.getSelectedItemPosition()
                val s2_pos = spinner2.getSelectedItemPosition()
                if(s1_pos == s2_pos){
                    if(s2_pos != 0){
                        spinner2.setSelection(0)
                    }else{
                        spinner2.setSelection(1)
                    }
                }

                perform_conversion()
            }

        })

        spinner2.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
//                If the required unit is same as primary unit then the primary unit is updated
                secondaryUnit = parent?.getItemAtPosition(position).toString()
                val s1_pos = spinner1.getSelectedItemPosition()
                val s2_pos = spinner2.getSelectedItemPosition()
                if(s1_pos == s2_pos){
                    if(s1_pos != 0){
                        spinner1.setSelection(0)
                    }else{
                        spinner1.setSelection(1)
                    }
                }

                perform_conversion()
            }

        })
        spinner1.setSelection(0)
        spinner2.setSelection(1)
        et_firstConversion?.setText("1")
        perform_conversion()
    }

//    defines the rules for the edit text fields
    private fun textChanged(){
        et_firstConversion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    perform_conversion()
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "Type a value", Toast.LENGTH_SHORT).show()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("Main", "Before Text Changed")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("Main", "OnTextChanged")
            }

        })
    }
//    Retrieves the conversion rate for length, distance and currency conversions
    private fun get_conversion_rate() : Float {
        var conversionRate = 0f
        when(selectedUnit) {
            "length", "distance" -> {

                try{
//                    As the conversion rates are fixed for length and distance are fixed, they are saved as json object
//                    in a file "basic_conversion_rates.json"
                    val file_name = "basic_conversion_rates.json"
//                    reads the file content and stored in a variable
                    val jsonString = application.assets.open(file_name).bufferedReader().use{
                        it.readText()
                    }
//                    A json object is created from the file content
                    val jsonObject = JSONObject(jsonString)
//                    conversion rates of the selected conversion and unit are retrived from the json
                    conversionRate = jsonObject.getJSONObject("$selectedUnit").getJSONObject("$primaryUnit").getString("$secondaryUnit").toFloat()

                    Log.d("Main", "Distance conversion rate : $conversionRate")
                } catch (e: Exception){
                    Log.e("Main", "Distance conversion : $e")
                }
            }
//            As the currency rates updated frequently, I'am using rates api to retrieve the current conversion rates
            "currency" -> {
//                This is the api url formatted with primary and required currencies
                val API = "https://api.ratesapi.io/api/latest?base=$primaryUnit&symbols=$secondaryUnit"
//                The api can't take a single currency for both so this is handled with the following if else condition
                if (primaryUnit == secondaryUnit) {
                    Toast.makeText(applicationContext, "Cannot convert same currency", Toast.LENGTH_SHORT).show()
                } else {
                    try {
//                        Using the URL method from the java, this opens a connection with the api and retrieves the rates
                        val apiResult = URL(API).readText()
//                      Json object is created with the api response
                        val jsonObject = JSONObject(apiResult)
//                        Conversion rate is retrieved from the api response
                        conversionRate = jsonObject.getJSONObject("rates").getString(secondaryUnit).toFloat()
                        Log.d("Main", "Currency conversion :" + apiResult)
                    } catch (e: Exception) {
                        Log.e("Main", "Currency conversion : $e")
                    }
                }
            }
        }
        return conversionRate
    }
//  This handles and perform the required conversions
    private fun perform_conversion(){

        var secondaryValue = 0f
//      Make sure that the input value is not empty
        if(et_firstConversion != null && et_firstConversion.text.isNotEmpty() && et_firstConversion.text.isNotBlank()){


            GlobalScope.launch(Dispatchers.IO) {
//                the value entered by the user
                var primaryValue = et_firstConversion.text.toString().toFloat()
                try {
                    when(selectedUnit){
                        "length", "distance" ->{
                            conversionRate = get_conversion_rate()
                            secondaryValue = (et_firstConversion.text.toString().toFloat()) * conversionRate
                        }
                        "temperature" -> {

                            if(primaryUnit == "Celsius"){
                                if(secondaryUnit == "Celsius"){
                                    secondaryValue = primaryValue
                                }else {
                                    secondaryValue = ((primaryValue * (9.0/5.0)) + 32).toFloat()
                                    Log.d("Main", "Celsius to Fahrenheit : $secondaryValue")
                                }
                            }else{
                                if(secondaryUnit == "Fahrenheit"){
                                    secondaryValue = primaryValue
                                }else{
                                    secondaryValue = ((primaryValue - 32) * (5.0/9.0)).toFloat()
                                    Log.d("Main", "Fahrenheit to Celsius : $secondaryValue")
                                }
                            }
                        }
                        "currency" -> {
                            conversionRate = get_conversion_rate()
                            secondaryValue = (et_firstConversion.text.toString().toFloat()) * conversionRate
                        }
                    }
//                    Update the value with the appropriate selected required unit
                    withContext(Dispatchers.Main) {
                        et_secondConversion?.setText(secondaryValue.toString())
                    }

                } catch (e: Exception) {
                    Log.e("Main", "$e")
                }
            }
        }
    }

}