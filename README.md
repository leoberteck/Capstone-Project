# Capstone-Project

This project was made in order to complete Udacity Android Nanodegree. In order to run this project you must create a vars.gradle file in the /app folder. This file will contain all necessary information for you to run the project. The file must look something like this:

```
project.ext {
    DB_NAME = [name of your SQLite database]
    UPLOAD_KEY_PASSWORD = [yourkeystore password]
    UPLOAD_SCORE_PASSWORD = [your store password]
    UPLOAD_KEYSTORE_PATH = [your release keystore path]
    ENDPOINTS_SERVER_URL = [your endpoints server url] or "\"https://what-the-word.appspot.com/_ah/api/\"" if you want to use my already deployed server
}
```

## Warning
The play games login will probably not work because of your debugkeystore SHA1 will be different from the one I have registered in the google play games console.
