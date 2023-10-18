package picojvm

import picojvm.vm.VirtualMachine

fun main(args: Array<String>) {
    val classFilePath = "Hello.class"
//    val classFile = readClassFile(classFilePath)
//    classFile.dump()
    VirtualMachine().start(classFilePath)
}
