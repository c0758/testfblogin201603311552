Key Hashes換電腦要跟著增加
免得直接clone下來不能用
keytool -exportcert -alias YOUR_RELEASE_KEY_ALIAS -keystore YOUR_RELEASE_KEY_PATH | openssl sha1 -binary | openssl base64
openssl也要裝
OpenSSL這個工具 可以先到OpenSSL for Windows下載Binaries Zip這個版本
openssl-0.9.8h-1-bin\bin\openssl.exe複製到你的keystore位置

FB login: 
https://developers.facebook.com/products/login
一些要註冊的東西: https://developers.facebook.com/quickstarts/

GOOGLE login: 
https://developers.google.com/identity/sign-in/android/start-integrating
一些要註冊的東西: https://console.developers.google.com/projectselector/apis/credentials