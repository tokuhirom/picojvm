package picojvm.vm

import picojvm.classfile.ConstantFieldrefInfo
import picojvm.classfile.ConstantMethodrefInfo
import picojvm.classfile.ConstantNameAndTypeInfo
import picojvm.classfile.ConstantStringInfo
import picojvm.classfile.ConstantUtf8Info
import picojvm.classfile.readClassFile

fun interface VMMethod {
    fun call(args: List<Any>): Any
}

class VMObject {
    val statics = mutableMapOf<String, VMObject>()
    val methods = mutableMapOf<String, VMMethod>()
}

class VirtualMachine {
    val global = mutableMapOf<String, VMObject>();

    init {
        // global 変数とかを入れていく

        val system = VMObject()
        system.statics["out"] = VMObject()

        global["java/lang/System"] = system

        val printStream = VMObject()
        printStream.methods["println:(Ljava/lang/String;)V"] = VMMethod {
            println(it[0].toString())
        }

        global["java/io/PrintStream"] = printStream
    }

    fun start(classFileName: String) {
        val classFile = readClassFile(classFileName)

        // main method を取り出して実行する
        val mainMethod = classFile.getMainMethod()
        val stack = arrayOfNulls<Any>(mainMethod.maxStack.toInt())
        var sp = 0 // stack pointer
        val reader = ByteCodeReader(mainMethod)
        while (reader.hasMoreElements()) {
            val op = reader.readOp()
            when (op.byteCode) {
                ByteCode.GETSTATIC -> {
                    val op1 = (op as ShortOp).op1
                    val target = classFile.constantPool.get(op1) as ConstantFieldrefInfo
                    val className = classFile.constantPool.getName(target.classIndex)
                    val nameAndTypeInfo = classFile.constantPool.get(target.nameAndTypeIndex) as ConstantNameAndTypeInfo
                    val name = classFile.constantPool.getName(nameAndTypeInfo.nameIndex)
                    val descriptor = classFile.constantPool.getName(nameAndTypeInfo.descriptorIndex)
                    val staticMethod = this.global[className]?.statics?.get(name)
                    stack[sp++] = staticMethod
                }
                ByteCode.LDC -> {
                    val constantValue = classFile.constantPool.get(((op as ByteOp).op1.toInt() and 0xff).toShort())
                    if (constantValue is ConstantStringInfo) {
                        val utf8Info = classFile.constantPool.get(constantValue.stringIndex) as ConstantUtf8Info
                        stack[sp++] = utf8Info.str
                    } else {
                        TODO()
                    }
                }
                ByteCode.INVOKEVIRTUAL -> {
                    val op1 = (op as ShortOp).op1
                    val method = classFile.constantPool.get(op1) as ConstantMethodrefInfo
                    val arg1 = stack[--sp] as String
                    val self = stack[--sp] as VMObject
                    val className = classFile.constantPool.getName(method.classIndex)
                    val nameAndType = classFile.constantPool.getName(method.nameAndTypeIndex)
                    val targetClass = this.global[className]
                    val methodInstance = targetClass?.methods?.get(nameAndType)
                    methodInstance?.call(listOf(arg1))
                }
                    ByteCode.RETURN -> {
                    return
                }
                else -> {
                    TODO("Unsupported byte code: $op")
                }
            }
        }
    }
}
