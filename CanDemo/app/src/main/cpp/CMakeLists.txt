# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)
message(STATUS "---------------------------------------------${PROJECT_SOURCE_DIR}")
message(STATUS "---------------------------------------------${CMAKE_SOURCE_DIR}")
# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../../../libs/${ANDROID_ABI})

add_subdirectory(libsocketcan-0.0.10)
add_subdirectory(libcanutils-4.0.6)