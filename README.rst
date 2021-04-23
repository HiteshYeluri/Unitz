UnitZ
--------

UnitZ is a multi use converter app, made for convertions of Length, Distance, Temperature, and Currency. UnitZ is fast, reliable and uses quality data. UnitZ is your one stop app for all your daliy unit uncertainities, weather it to find out how much a CAD is worth in USD or to know how many kilometers is 3 miles, UnitZ has you covered.

.. figure:: images/UnitZ%20app%20icon.png
   :scale: 25 %
   
   Unitz App Icon
   
   
.. figure:: images/launcher%20screenshot.png 
   :scale: 25 %
   
   Android launcher preview
   
Configuration Instructions:
    - Android SDK 30
    - Android version 4.4.2 and newer

Installation:
    - Click the dropdown menu in the toolbar at the top (Open 'Edit Run/Debug configurations' dialog)
    - Select "Edit Configurations"
    - Click the "+"
    - Select "Gradle"
    - Choose your module as a Gradle project
    - In Tasks: enter assemble
    - Press Run
    - Your unsigned APK is now located in
    - Unitz\app\build\outputs\apk
    - Allow "Install Unknown apps" in android settings
    - Open the app and it is now ready to use.
    
Operating Instructions:
    - Launch the Application in your android smartphone
    - Select the desired conversion from 4 tabs on top of the screen.
    - You can now use the drop down to select the desired conversion.
    - Enter the value of the unit you want to convert.
    - Result will be displayed right below the value you entered.
    
.. figure:: images/step%201.png
   :scale: 25 %
   
   Step 1
   
.. figure:: images/step%202.png
   :scale: 25 %
   
   Step 2
   
.. figure:: images/step%203.PNG
   :scale: 25 %
   
   Step 3
   
.. figure:: images/final%20step.png
   :scale: 25 %
   
   Step 4

Highlights:
    - Multi Comnverstion tool.
    - Free to use with no advertisements.
    - Live currency conversion.
    - Reliable and accurate data.
    - User Friendly.
    

Manifest:
    - activity_main.xml : This file contains the design layout that the user sees in his/her screen. It uses different type of Linear Layouts for button arrangements, has textview fields and also uses Images from mipmap in resources folder for ImageButtons.
    - strings.xml : This file contains names displayed on buttons or within the app in general. It contains app_name, different currency names, different length names, different distance names and different temperature names.
    - basic_conversion_rates.json : This is a json file that sontains data which is then used in MainActivity.kt. It has conversion rates for length(cm, Meter, Inch, Feet) and distance(Km, Mile).
    - mipmap : this folder contains images that can be used in the app.
    - MainActivity.kt : This file has all the funcionalities and puts everyting together using all the recources.
    
Troubleshooting Tips:
 - Force close and restart the app.
 - If the issue didn't resolve, reinstall the application.
 - If the issue still persists, contact us with the info below.

Contact Information:

For further support contact Hitesh Yeluri at hyeluri1@lakeheadu.ca

