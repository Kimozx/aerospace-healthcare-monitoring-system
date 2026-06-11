cmake_minimum_required(VERSION 3.20)

project(sensor_simulator LANGUAGES CXX ASM_MASM)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_executable(sensor-simulator
    src/main.cpp
    ../assembly-checksum/checksum.asm
)

target_include_directories(sensor-simulator PRIVATE include)

if(MSVC)
    target_compile_definitions(sensor-simulator PRIVATE USE_ASM_CHECKSUM=1)
endif()

target_link_libraries(sensor-simulator PRIVATE winhttp)