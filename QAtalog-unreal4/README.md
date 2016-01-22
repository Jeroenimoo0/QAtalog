# QAtalog-unreal4 (Simple)
This sub-project is source code for a simple QAtalog client implementation for Unreal Engine 4.

# Instalation
1. Copy the two source files into your project
2. Open your project Build.cs and add the following code in the constructor:
```
PublicDependencyModuleNames.AddRange(new string[] { "Json", "JsonUtilities", "HTTP"});
```
# Usage
### Initialization
1. Open the Qatalog.cpp and replace localhost with the host of your Qatalog server
2. Compile your project and make sure unreal has found the code.
3. Create a blueprint from which you will initialize. Create the following blueprint node: 

![alt tag](https://i.gyazo.com/9050fe4fec6a04d2ddb5f2267d237a6a.png)

QAtalog is now properly initialized and can be used.
### Posting data
As unreal blueprints do not have any support for json objects data is supplies as two String arrays, One contains the keys, the other contains the values. You will also have to supply an String key which will define to which field this data will be added in the database. You can then post the data as follows: <br>
![alt tag](https://i.gyazo.com/6a7ca3bfd4bb87cbbe00b504677f5c32.png)

Data is posted asynchronous instantly. There is no batching options in this client application.
