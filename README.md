# PAXSTORE 3rd App Android SDK [ ![Download](https://api.bintray.com/packages/paxstore-support/paxstore/paxstore-3rd-app-android-sdk/images/download.svg?version=6.3.0) ](https://bintray.com/paxstore-support/paxstore/paxstore-3rd-app-android-sdk/6.3.0/link)
PAXSTORE 3rd App Android SDK provides simple and easy-to-use service interfaces for third party developers to develop android apps on PAXSTORE. The services currently include the following points:

1. Download parameter
2. Inquire update for 3rd party app

By using this SDK, developers can easily integrate with PAXSTORE. Please take care of your AppKey and AppSecret that generated by PAXSTORE system when you create an app.
<br>Refer to the following steps for integration.

## Requirements
**Android SDK version**
>SDK 19 or higher, depending on the terminal's paydroid version.

**Gradle's and Gradle plugin's version**
>Gradle version 4.1 or higher  
>Gradle plugin version 3.0.0+ or higher

## Download
Gradle:

    implementation 'com.pax.market:paxstore-3rd-app-android-sdk:6.3.0'

## Permissions
PAXSTORE Android SDK need the following permissions, please add them in AndroidManifest.xml.

`<uses-permission android:name="android.permission.INTERNET" />`<br>
`<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />`<br>
`<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />`<br>

## ProGuard
If you are using [ProGuard](https://www.guardsquare.com/en/products/proguard/manual) in your project add the following lines to your configuration:

    #Gson
    -dontwarn com.google.gson.**
    -keep class sun.misc.Unsafe { *; }
    -keep class com.google.gson.** { *; }
    -keep class com.google.gson.examples.android.model.** { *; }
    
    #JJWT
    -keepnames class com.fasterxml.jackson.databind.** { *; }
    -dontwarn com.fasterxml.jackson.databind.*
    -keepattributes InnerClasses
    -keep class org.bouncycastle.** { *; }
    -keepnames class org.bouncycastle.** { *; }
    -dontwarn org.bouncycastle.**
    -keep class io.jsonwebtoken.** { *; }
    -keepnames class io.jsonwebtoken.* { *; }
    -keepnames interface io.jsonwebtoken.* { *; }
    -dontwarn javax.xml.bind.DatatypeConverter
    -dontwarn io.jsonwebtoken.impl.Base64Codec
    -keepnames class com.fasterxml.jackson.** { *; }
    -keepnames interface com.fasterxml.jackson.** { *; }
    
    #dom4j
    -dontwarn org.dom4j.**
    -keep class org.dom4j.**{*;}
    -dontwarn org.xml.sax.**
    -keep class org.xml.sax.**{*;}
    -dontwarn com.fasterxml.jackson.**
    -keep class com.fasterxml.jackson.**{*;}
    -dontwarn com.pax.market.api.sdk.java.base.util.**
    -keep class com.pax.market.api.sdk.java.base.util.**{*;}
    -dontwarn org.w3c.dom.**
    -keep class org.w3c.dom.**{*;}
    -dontwarn javax.xml.**
    -keep class javax.xml.**{*;}
    
    #dto
    -dontwarn com.pax.market.api.sdk.java.base.dto.**
    -keep class com.pax.market.api.sdk.java.base.dto.**{*;}

## API Usage

### Step 1: Get Application Key and Secret
Create a new app in PAXSTORE, and get **AppKey** and **AppSecret** from app detail page in developer center.

### Step 2: Initialization
Configuring the application element, edit AndroidManifest.xml, it will have an application element. You need to configure the android:name attribute to point to your Application class (put the full name with package if the application class package is not the same as manifest root element declared package)

    <application
        android:name=".BaseApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme">

Initializing AppKey,AppSecret and SN
>Please note, make sure you have put your own app's AppKey and AppSecret correctly

public class BaseApplication extends Application {

    private static final String TAG = BaseApplication.class.getSimpleName();
    
    //todo make sure to replace with your own app's appKey and appSecret
    private String APP_KEY = "Your APPKEY";
    private String APP_SECRET = "Your APPSECRET";
    private String SN = Build.SERIAL;
    
    @Override
    public void onCreate() {
        super.onCreate();
        initPaxStoreSdk();
    }
    
    private void initPaxStoreSdk() {
       //todo Init AppKey，AppSecret and SN, make sure the appKey and appSecret is corret.
       StoreSdk.getInstance().init(getApplicationContext(), appkey, appSecret, SN, new BaseApiService.Callback() {
                  @Override
                  public void initSuccess() {
                      //TODO Do your business here
                  }
    
                  @Override
                  public void initFailed(RemoteException e) {
                    //TODO Do failed logic here
                      Toast.makeText(getApplicationContext(), "Cannot get API URL from PAXSTORE, Please install PAXSTORE first.", Toast.LENGTH_LONG).show();
                  }
              });
    }
}
### Step 3：Download Parameters API
Download parameter (Optional, ignore this part if you don't have download parameter requirement)

Register your receiver (**Replace the category name with your own app package name**)
       
     <receiver android:name=".YourReceiver">
              <intent-filter>
                  <action android:name="com.paxmarket.ACTION_TO_DOWNLOAD_PARAMS" />
                  <category android:name="Your PackageName" />
              </intent-filter>
     </receiver>

Create your receiver. Since download will cost some time, we recommend you do it in your own service

      public class YourReceiver extends BroadcastReceiver {
          @Override
          public void onReceive(Context context, Intent intent) {
              //todo add log to see if the broadcast is received, if not, please check whether the bradcast config is correct
              Log.i("DownloadParamReceiver", "broadcast received");
              //since download may cost a long time, we recommend you do it in your own service
              context.startService(new Intent(context, YourService.class));
          }
      }
After you get broadcast, download params in your service

    public int onStartCommand(Intent intent, int flags, int startId) {
            //Specifies the download path for the parameter file, you can replace the path to your app's internal storage for security.
            final String saveFilePath = getFilesDir() + "YourPath";
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //todo Call this method to download into your specific directory, you can add some log here to monitor
                    DownloadResultObject downloadResult = null;
                    try {
                        downloadResult = StoreSdk.getInstance().paramApi().downloadParamToPath(getApplication().getPackageName(), BuildConfig.VERSION_CODE, saveFilePath);
                    } catch (NotInitException e) {
                        Log.e(TAG, "e:" + e);
                    }
    
                    //businesscode==0, means download successful, if not equal to 0, please check the return message when need.
                    if(downloadResult != null && downloadResult.getBusinessCode()==0){
                        Log.i("downloadParamToPath: ", "download successful");
                        //file download to saveFilePath above.
                        //todo can start to add your logic.
                    }else{
                        //todo check the Error Code and Error Message for fail reason
                        Log.e("downloadParamToPath: ", "ErrorCode: "+downloadResult.getBusinessCode()+"ErrorMessage: "+downloadResult.getMessage());
                    }
                }
            });
            thread.start();
            return super.onStartCommand(intent, flags, startId);
        }
### Step 4: Update Inquirer (Optional)
Update inquirer: Your app will be asked whether it can be updated when there is a new version afther you
integrated this function.

If you do not have this requirement, just skip this step.

Integrate with this function only need to call initInquirer() after you init StoreSdk success.

    public class BaseApplication extends Application {
    
        private static final String TAG = BaseApplication.class.getSimpleName();
        
        //todo make sure to replace with your own app's appKey and appSecret
        private String APP_KEY = "Your APPKEY";
        private String APP_SECRET = "Your APPSECRET";
        private String SN = Build.SERIAL;
        @Override
        public void onCreate() {
            super.onCreate();
            initPaxStoreSdk();  //Initializing AppKey，AppSecret and SN
        }
    
         private void initPaxStoreSdk() {
                //todo Init AppKey，AppSecret and SN, make sure the appKey and appSecret is corret.
                StoreSdk.getInstance().init(getApplicationContext(), appkey, appSecret, SN, new BaseApiService.Callback() {
                           @Override
                           public void initSuccess() {
                               initInquirer();
                           }
    
                           @Override
                           public void initFailed(RemoteException e) {
                               Toast.makeText(getApplicationContext(), "Cannot get API URL from PAXSTORE, Please install PAXSTORE first.", Toast.LENGTH_LONG).show();
                           }
                       });
            }


         private void initInquirer() {
                //todo Init checking of whether app can be updated
                StoreSdk.initInquirer(new StoreSdk.Inquirer() {
                    @Override
                    public boolean isReadyUpdate() {
                        Log.i(TAG, "call business function....isReadyUpdate = " + !isTrading());
                        //todo call your business function here while is ready to update or not
                        return !isTrading();
                    }
                });
         }
    
        //This is a sample of your business logic method
        public boolean isTrading(){
            return true;
        }
    }

## More API Description

### Initialize StoreSdk

```
// Initialize StoreSdk api
public void init(final Context context, final String appKey, final String appSecret,
                     final String terminalSerialNo, final BaseApiService.Callback callback) throws NullPointerException
```

| Parameter        | Type                    | Description    |
| ---------------- | ----------------------- | -------------- |
| context          | Context                 | Context        |
| appKey           | String                  | The app key    |
| appSecret        | String                  | The app secret |
| terminalSerialNo | String                  | The teminal SN |
| callback         | BaseApiService.Callback | Callback       |

**com.pax.market.android.app.sdk.BaseApiService**

BaseApiService, implements ProxyDelegate, the structure shows below

| Property   | Type              | Description                    |
| ---------- | ----------------- | ------------------------------ |
| instance   | BaseApiService    | The instance of BaseApiService |
| context    | Context           | Context                        |
| sp         | SharedPreferences | ShardPreferences               |
| storeProxy | StoreProxyInfo    | Store proxy info               |

**BaseApiService.Callback**

```
public interface Callback {
    void initSuccess();
    void initFailed(RemoteException e);
}
```

**com.pax.market.api.sdk.java.base.client.ProxyDelegate**

```
public interface ProxyDelegate {
    Proxy retrieveProxy();
    String retrieveProxyAuthorization();
}
```

**com.pax.market.android.app.sdk.dto.StoreProxyInfo**

The store proxy info, the structure shows below

| Property      | Type   | Description                          |
| ------------- | ------ | ------------------------------------ |
| type          | int    | Store proxy type, 0: DIRECT, 1: HTTP |
| host          | String | Store proxy host                     |
| port          | int    | Store proxy port                     |
| authroization | String | Store authroization                  |

### Get ParamApi instance

```
// Get ParamApi instance api
public ParamApi paramApi() throws NotInitException {...}
// usage
ParamApi paramApi = StoreSdk.getInstance().paramApi();
```

### Get SyncApi instance

```
// Get SyncApi instance api
public SyncApi syncApi() throws NotInitException {...}
// usage
SyncApi syncApi = StoreSdk.getInstance().syncApi();
```

### Get UpdateApi instance

```
// Get UpdateApi instance api
public UpdateApi updateApi() throws NotInitException {...}
// usage
UpdateApi updateApi = StoreSdk.getInstance().updateApi();
```
### Check if initialized

```
// Check if initialized api
public boolean checkInitialization() {...}
// usage
boolean init = StoreSdk.getInstance().checkInitialization();
```

### Update inquirer

Store app will ask you before installing the new version of your app. Ignore this if you don't have Update inquirer requirement. You can implement com.pax.market.android.app.sdk.StoreSdk.Inquirer#isReadyUpdate() to tell Store App whether your app can be updated now.

```
// Update inquirer api
public void initInquirer(final Inquirer inquirer) {...}
```

### Initialize api directly

If you known the exact apiUrl, you can call this method to initialize ParamApi and SyncApi directly instead of calling method init().

```
// Initialize api directly api
public void initApi(String apiUrl, String appKey, String appSecret, String terminalSerialNo, ProxyDelegate proxyDelegate)
```

| Parameter        | Type          | Description      |
| ---------------- | ------------- | ---------------- |
| apiUrl           | String        | The exact apiUrl |
| appKey           | String        | The app key      |
| appSecret        | String        | The app secret   |
| terminalSerialNo | String        | The teminal SN   |
| proxyDelegate    | ProxyDelegate | ProxyDelegate    |

### Get Terminal Base Information

API to get base terminal information from PAXSTORE client. (Support from PAXSTORE client V6.1.)

    // Get terminal base information api
    public void getBaseTerminalInfo(Context context, BaseApiService.ICallBack callback) {...}
    
    // usage
    StoreSdk.getInstance().getBaseTerminalInfo(getApplicationContext(),new BaseApiService.ICallBack() {
        @Override
        public void onSuccess(Object obj) {
            TerminalInfo terminalInfo = (TerminalInfo) obj;
            Log.i("onSuccess: ",terminalInfo.toString());
        }
    
        @Override	
        public void onError(Exception e) {
            Log.i("onError: ",e.toString());
        }
    });

### Sync and update PAXSTORE proxy information

```
// api
public void updateStoreProxyInfo(Context context, StoreProxyInfo storeProxyInfo) {...}
```
### Open app  detail page

Open your app detail page in PAXSTORE. If the market don't have this app, it will show app not found, else will go to detail page in PAXSTORE market

    // api
    public void openAppDetailPage(String packageName, Context context) {...}
    // usage
    StoreSdk.getInstance().openAppDetailPage(getPackageName(), getApplicationContext());

### Open PAXSTORE's download page

Open download page in PAXSTORE. You can see app's downloading progress in this page.

    // api
    public void openDownloadListPage(String packageName, Context context) {...}
    // usage
    StoreSdk.getInstance().openDownloadListPage(getPackageName(), getApplicationContext());

### Get PAXSTORE PUSH online status

```
public OnlineStatusInfo getOnlineStatusFromPAXSTORE(Context context) {...}
```

**com.pax.market.android.app.sdk.dto.OnlineStatusInfo**

The terminal online status info, the structure shows below

| Property | Type    | Description                            |
| -------- | ------- | -------------------------------------- |
| online   | boolean | The PAXSTORE online push online status |

### Get location from PAXSTORE.

    // Get location api 
    public void startLocate(Context context, LocationService.LocationCallback locationCallback) {...}
    // usage
    StoreSdk.getInstance().startLocate(getApplicationContext(), new LocationService.LocationCallback() {
                        @Override
                        public void locationResponse(LocationInfo locationInfo) {
                            Log.d("MainActivity", "Get Location Result：" + locationInfo.toString());
                        }
                    });

### QueryResult

| code | message                     | Description                        |
| ---- | --------------------------- | ---------------------------------- |
| 0    | success                     | success                            |
| -1   | Get location failed         | Get location failed                |
| -2   | Init LocationManager failed | Init LocationManager failed        |
| -3   | Not allowed                 | Get info not allowed               |
| -4   | Get location too fast       | Get location too fast              |
| -5   | Push not enabled            | Push not enabled                   |
| -6   | Query failed                | Query from content provider failed |
| -10  | unknown                     | Unknown                            |

### Other object

**com.pax.market.android.app.sdk.dto.LocationInfo**

The terminal location info, the structure shows below. 

| Property       | Type   | Description                    |
| -------------- | ------ | ------------------------------ |
| longitude      | String | The longitude of location info |
| latitude       | String | The latitude of location info  |
| lastLocateTime | Long   | The last locate time           |

**com.pax.market.android.app.sdk.dto.TerminalInfo**

The terminal info, the structure shows below

| Property     | Type   | Description                                         |
| ------------ | ------ | --------------------------------------------------- |
| tid          | String | The tid of terminal                                 |
| terminalName | String | The name of terminal                                |
| serialNo     | String | The serial no of terminal                           |
| modelName    | String | The modle name of terminal                          |
| factory      | String | The manufactory of terminal                         |
| merchantName | String | The merchant name of terminal                       |
| status       | String | The online status of terminal, 0:online, -1:offline |

### [ParamApi](docs/ParamApi.md)

### [SyncApi](docs/SyncApi.md)

### [UpdateApi](docs/UpdateApi.md)

### [ResultCode](docs/ResultCode.md)

### [CloudMsg APIs](docs/CloudMsg+APIs.md)

## Template
The **parameter template file** used in **demo** is under folder assets/param_template.xml.

## FAQ

#### 1. How to resolve dependencies conflict?

When dependencies conflict occur, the error message may like below:

    Program type already present: xxx.xxx.xxx

**Solution:**

You can use **exclude()** method to exclude the conflict dependencies by **group** or **module** or **both**.

e.g. To exclude 'com.google.code.gson:gson:2.8.5' in SDK, you can use below:

    implementation ('com.pax.market:paxstoresdk:x.xx.xx'){
        exclude group: 'com.google.code.gson', module: 'gson'
    }

#### 2. How to resolve attribute conflict?

When attribute conflict occur, the error message may like below:

    Manifest merger failed : Attribute application@allowBackup value=(false) from 
    AndroidManifest.xml...
    is also present at [com.pax.market:paxstore-3rd-app-android-sdk:x.xx.xx] 
    AndroidManifest.xml...
    Suggestion: add 'tools:replace="android:allowBackup"' to <application> element
    at AndroidManifest.xml:..

**Solution:**

Add **xmlns:tools="http\://<span></span>schemas.android.com/tools"** in your manifest header

       <manifest xmlns:android="http://schemas.android.com/apk/res/android"
            package="com.yourpackage"
            xmlns:tools="http://schemas.android.com/tools">

Add **tools:replace = "the confilct attribute"** to your application tag:

        <application
            ...
            tools:replace="allowBackup"/>


More questions, please refer to [FAQ](https://github.com/PAXSTORE/paxstore-3rd-app-android-sdk/wiki/FAQ)

## License

See the [Apache 2.0 license](https://github.com/PAXSTORE/paxstore-3rd-app-android-sdk/blob/master/LICENSE) file for details.

    Copyright 2018 PAX Computer Technology(Shenzhen) CO., LTD ("PAX")
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at following link.
    
         http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
