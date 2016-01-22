# Collect sources into the variable MATH_SOURCES without
# having to explicitly list each header and source file.
#
# CMake documentation states "We do not recommend using GLOB to collect a
# list of source files from your source tree. If no CMakeLists.txt file
# changes when a source is added or removed then the generated build system
# cannot know when to ask CMake to regenerate".
file (GLOB API_SOURCES "*.h")
 
# Collect sources into the variable SIMPLE_FUNCTION_SOURCES
file (GLOB SIMPLE_API_SOURCES
      "api/*.h",
      "api/*.cxx")
 
# The recommended way to collect sources in variable 
# ADVANCED_FUNCTION_SOURCES by explicitly specifying the source files
#set  (ADVANCED_API_SOURCES
#      "advanced/AdvancedFunctions.h"
#      "advanced/AdvancedFunctions.cxx")
 
# Create named folders for the sources within the .vcproj
# Empty name lists them directly under the .vcproj
source_group("" FILES ${API_SOURCES})
source_group("simple" FILES ${SIMPLE_API_SOURCES})
#source_group("advanced" FILES ${ADVANCED_FUNCTION_SOURCES})
 
# Properties->C/C++->General->Additional Include Directories
include_directories (.)
 
# Set Properties->General->Configuration Type to Dynamic Library(.dll)
# Creates math.dll with the listed sources collected in the variables
# Also adds sources to the Solution Explorer
add_library(api SHARED ${API_SOURCES} 
                        ${SIMPLE_API_SOURCES})
#                        ${ADVANCED_FUNCTION_SOURCES})
 
# Creates folder "libraries" and adds target project (math.vcproj)
set_property(TARGET api PROPERTY FOLDER "libraries")
 
# Adds logic to INSTALL.vcproj to copy math.dll to destination directory
install (TARGETS api
         RUNTIME DESTINATION ${PROJECT_BINARY_DIR}/bin)