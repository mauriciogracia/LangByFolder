# LangByFolder
This tool can help you understand complex folder structures (code repositories, image and video galeries) without having to manually browse each folder manually.

It detects most common programming lanugage extensions, image and video formats

Currently the output is a CSV file but a User interface is on the way

![image](https://user-images.githubusercontent.com/2321661/177031364-db2e0634-4a3f-4ee7-a894-11b72d6ca5e9.png)

For each folder the list of extensions contained in them is show from most common to less common, or alphabetically (you decide)

There are columns that detect files typically used for unit testing, or files that seem to be services

## How to Run

Currently this tool has not been packaged properly, 

If you want to test the current status 

1. You need a JDK
2. will have to clone/download the source code 
3. go to the `src` folder and run `javac org/mgg/LangByFolder.java`
4. run `java org.mgg.LangByFolder` to see the parameters that you can pass 
