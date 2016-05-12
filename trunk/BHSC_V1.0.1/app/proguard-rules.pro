# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/zhanglei/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
#-keep class com.google.gson.examples.android.model.** { *; }
# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep public class * implements java.io.Serializable {*;}
-keep class com.bhsc.mobile.comment.model.**{*;}
-keep class com.bhsc.mobile.disclose.model.**{*;}
-keep class com.bhsc.mobile.news.model.**{*;}
-keep class com.bhsc.mobile.userpages.model.**{*;}
-keep class com.bhsc.mobile.ThirdParty.model.**{*;}
-keep class com.bhsc.mobile.net.**{*;}

##---------------End: proguard configuration for Gson  ----------


##--------------Begin: proguard configuration for Volley---------
-keep class org.apache.commons.logging.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class com.actionbarsherlock.** { *; }
-keep interface com.actionbarsherlock.** { *; }
-keep class com.android.volley.** { *; }
-keep interface com.android.volley.** { *; }

-keepattributes *Annotation*

-dontwarn org.apache.**

##---------------End: proguard configuration for Volley  --------

##--------------Begin: proguard configuration for fresco---------

# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**

##---------------End: proguard configuration for fresco  --------

-optimizationpasses 1
-dontusemixedcaseclassnames
-dontpreverify
-verbose

-keepattributes Signature
-keepattributes Exceptions

-dontwarn com.google.**
-dontwarn com.squareup.okhttp.**
-dontwarn com.tencent.**
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn retrofit.client.ApacheClient$GenericEntityHttpRequest
-dontwarn retrofit.client.ApacheClient$GenericHttpRequest
-dontwarn retrofit.client.ApacheClient$TypedOutputEntity
-dontwarn rx.internal.util.**

-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.* { *; }
-keep public class * extends android.support.v4.*

-keep class com.android.pc.** { *; }
-keep interface com.android.pc.** { *; }
-keep public class * extends com.android.pc.**

-keep class android.annotation.** { *;}
-keep class gson-2.5.** { *;}

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
   long producerNode;
   long consumerNode;
}

-keepclasseswithmembernames class * {
native <methods>;
}
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
public void *(android.view.View);
}
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.FragmentActivity
-keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService

-keep class com.google.**

-keep class com.tencent.** {*;}
-keep class com.sina.**{*;}
-keep class * extends android.app.Dialog
-keep class retrofit.** { *; }
