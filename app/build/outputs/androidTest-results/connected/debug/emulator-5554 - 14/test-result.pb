
 
Z
eventIntentTestscom.example.qr_dasherinitializationError2˝ñ—∞Ää√•:˝ñ—∞¿πè∏Â
€
java.lang.RuntimeException: Failed to instantiate test runner class androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner


at androidx.test.ext.junit.runners.AndroidJUnit4.throwInitializationError(AndroidJUnit4.java:129)
at androidx.test.ext.junit.runners.AndroidJUnit4.loadRunner(AndroidJUnit4.java:121)
at androidx.test.ext.junit.runners.AndroidJUnit4.loadRunner(AndroidJUnit4.java:82)
at androidx.test.ext.junit.runners.AndroidJUnit4.<init>(AndroidJUnit4.java:56)
... 14 trimmed
Caused by: java.lang.reflect.InvocationTargetException
at java.lang.reflect.Constructor.newInstance0(Native Method)
at java.lang.reflect.Constructor.newInstance(Constructor.java:343)
at androidx.test.ext.junit.runners.AndroidJUnit4.loadRunner(AndroidJUnit4.java:112)
... 17 more
Caused by: org.junit.runners.model.InvalidTestClassError: Invalid test class 'com.example.qr_dasher.eventIntentTests':
1. Method createUserProfile() should be static
at org.junit.runners.ParentRunner.validate(ParentRunner.java:525)
at org.junit.runners.ParentRunner.<init>(ParentRunner.java:92)
at org.junit.runners.BlockJUnit4ClassRunner.<init>(BlockJUnit4ClassRunner.java:74)
at androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner.<init>(AndroidJUnit4ClassRunner.java:43)
at androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner.<init>(AndroidJUnit4ClassRunner.java:48)
... 20 more¶java.lang.reflect.InvocationTargetException
at java.lang.reflect.Constructor.newInstance0(Native Method)
at java.lang.reflect.Constructor.newInstance(Constructor.java€
java.lang.RuntimeException: Failed to instantiate test runner class androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner


at androidx.test.ext.junit.runners.AndroidJUnit4.throwInitializationError(AndroidJUnit4.java:129)
at androidx.test.ext.junit.runners.AndroidJUnit4.loadRunner(AndroidJUnit4.java:121)
at androidx.test.ext.junit.runners.AndroidJUnit4.loadRunner(AndroidJUnit4.java:82)
at androidx.test.ext.junit.runners.AndroidJUnit4.<init>(AndroidJUnit4.java:56)
... 14 trimmed
Caused by: java.lang.reflect.InvocationTargetException
at java.lang.reflect.Constructor.newInstance0(Native Method)
at java.lang.reflect.Constructor.newInstance(Constructor.java:343)
at androidx.test.ext.junit.runners.AndroidJUnit4.loadRunner(AndroidJUnit4.java:112)
... 17 more
Caused by: org.junit.runners.model.InvalidTestClassError: Invalid test class 'com.example.qr_dasher.eventIntentTests':
1. Method createUserProfile() should be static
at org.junit.runners.ParentRunner.validate(ParentRunner.java:525)
at org.junit.runners.ParentRunner.<init>(ParentRunner.java:92)
at org.junit.runners.BlockJUnit4ClassRunner.<init>(BlockJUnit4ClassRunner.java:74)
at androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner.<init>(AndroidJUnit4ClassRunner.java:43)
at androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner.<init>(AndroidJUnit4ClassRunner.java:48)
... 20 more"É

logcatandroidÌ
ÍC:\Users\jaspr\OneDrive\Documents\University\Third year computing science\CMPUT301\QRdasher\app\build\outputs\androidTest-results\connected\debug\emulator-5554 - 14\logcat-com.example.qr_dasher.eventIntentTests-initializationError.txt"—

device-infoandroid∂
≥C:\Users\jaspr\OneDrive\Documents\University\Third year computing science\CMPUT301\QRdasher\app\build\outputs\androidTest-results\connected\debug\emulator-5554 - 14\device-info.pb"“

device-info.meminfoandroidØ
¨C:\Users\jaspr\OneDrive\Documents\University\Third year computing science\CMPUT301\QRdasher\app\build\outputs\androidTest-results\connected\debug\emulator-5554 - 14\meminfo"“

device-info.cpuinfoandroidØ
¨C:\Users\jaspr\OneDrive\Documents\University\Third year computing science\CMPUT301\QRdasher\app\build\outputs\androidTest-results\connected\debug\emulator-5554 - 14\cpuinfo*∂
c
test-results.logOcom.google.testing.platform.runtime.android.driver.AndroidInstrumentationDriver¿
ΩC:\Users\jaspr\OneDrive\Documents\University\Third year computing science\CMPUT301\QRdasher\app\build\outputs\androidTest-results\connected\debug\emulator-5554 - 14\testlog\test-results.log 2
text/plain