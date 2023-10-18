package picojvm

import picojvm.classfile.readClassFile

fun main(args: Array<String>) {
    val classFilePath = "Hello.class"
    val classFile = readClassFile(classFilePath)
    classFile.dump()
}
