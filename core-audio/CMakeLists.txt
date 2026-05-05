cmake_minimum_required(VERSION 3.10.2)
project("core-audio")

# Defines the native library
add_library(
        core-audio
        SHARED
        AudioEngine.cpp
)

# Target options, standard includes etc can be added here
target_include_directories(core-audio PUBLIC ${CMAKE_CURRENT_SOURCE_DIR})