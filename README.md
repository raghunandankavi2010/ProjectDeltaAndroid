# ProjectDeltaAndroid
Tracking traces with open tel collector 


# opentelsdk

`opentelsdk` is a library that provides an easy way to integrate OpenTelemetry tracing into your Android application. Follow the steps below to add this SDK to your project and set it up.

## Step 1: Add opentelsdk as a Library

1. Download the sdk from git. Open your `build.gradle` file of your app module.
2. Add the dependency for `opentelsdk`by  importing this a library module

    ```gradle
        implementation project(':opentelsdk')
    ```

## Step 2: Add a Config File in the Assets Folder

1. Create a folder named `assets` in the `src/main` directory of your app module if it doesn't already exist.
2. Create a file named `config.json` in the `assets` folder.
3. Add the following content to the `config.json` file: You can add any event name and type

    ```json
    {
      "events": [
        {
          "event_name": "Go Shopping",
          "event_type": "click_event"
        },
        {
          "event_name": "Learn More",
          "event_type": "click_event"
        }
      ]
    }
    ```

## Step 3: Create the Application Class and Initialize OpenTelemetry

1. Create a class that extends `Application` in your app module.
2. Set up the `OpenTelemetry` initialization in the `onCreate` method.

    ```kotlin
    package com.yourapp

    import android.app.Application
    import com.yourcompany.opentelsdk.OpenTelemetry
    import com.yourcompany.opentelsdk.ConfigType

    class MyApplication : Application() {

        override fun onCreate() {
            super.onCreate()
            OpenTelemetry.initialize(
                ConfigType.HTTP, // HTTP or GRPC
                this,
                "https://ingest.in.signoz.cloud:443/v1/traces",
                "https://ingest.in.signoz.cloud:443/v1/traces",
                "c5d49b77-781b-4159-9394-e59b847c349e",
                "test" // Service name
            )
        }
    }
    ```

3. Update your `AndroidManifest.xml` to use this custom `Application` class:

    ```xml
    <application
        android:name=".MyApplication"
        ... >
        ...
    </application>
    ```
   
4. Start collecting events
 
    ```
      lifecycleScope.launch() {
       EventCollector.handleEvent("Go Shopping", "")
      }
    ```
   "Go SHopping" is parent span. The second param is child span. You can leave it empty if you want.

## Conclusion

You’ve now successfully integrated `opentelsdk` into your Android project. Your app should be able to send traces to the specified OpenTelemetry endpoint.

For more information, please refer to the official documentation of `opentelsdk`.
