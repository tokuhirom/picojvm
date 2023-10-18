package picojvm.classfile

import picojvm.AccessFlag
import picojvm.AttributeInfo
import picojvm.CodeAttribute
import picojvm.FieldInfo
import picojvm.LineNumberTableAttribute
import picojvm.MethodInfo
import picojvm.SourceFileAttribute

data class ClassFile(
    val minorVersion: Int,
    val majorVersion: Int,
    val constantPool: ConstantPool,
    val accessFlags: Short,
    val thisClass: Short,
    val superClass: Short,
    val interfaces: List<Short>,
    val fields: List<FieldInfo>,
    val methods: List<MethodInfo>,
    val attributes: List<AttributeInfo>
) {
    fun dump() {
        println("this_class: #$thisClass // ${constantPool.getName(thisClass)}")
        println("super_class: #$superClass // ${constantPool.getName(superClass)}")
        println("interfaces: ${interfaces.size}")
        println("Constant pool:")
        constantPool.forEach { (i, info) ->
            printConstantPool(i, info)
        }
        methods.forEach { action ->
            val accessFlag = AccessFlag.fromInt(action.accessFlags.toInt())
            println("$accessFlag ${constantPool.getName(action.nameIndex)}")
            action.attributes.forEach {
                printAttribute(it)
            }
        }
    }

    private fun printAttribute(attributeInfo: AttributeInfo) {
        val name = constantPool.getName(attributeInfo.attributeNameIndex)
        println("  $name:")

        when (attributeInfo) {
            is CodeAttribute -> {
                // attributeNameIndex が "Code" だったら、バイトコードが入っている。
                println("    stack=${attributeInfo.maxStack} local=${attributeInfo.maxLocals}")
                attributeInfo.attributes.forEach {
                    printAttribute(it)
                }
            }
            is SourceFileAttribute -> {
                println("    ${constantPool.getName(attributeInfo.sourceFileIndex)}")
            }
            is LineNumberTableAttribute -> {
                println("    ${attributeInfo.lineNumberTable}")
            }
            else -> {
                println("    (Unsupported attribute info)")
            }
        }
    }

    private fun printConstantPool(i: Short, info: ConstantInfo) {
        System.out.printf("%5s %-15s", "#$i", info.javaClass.simpleName.replace("Constant", "").replace("Info", ""))
        fun p(head: String, tail: String) {
            System.out.printf("%-10s %-20s\n", head, tail)
        }
        fun pp(vararg ids: Short) {
            if (ids.isNotEmpty()) {
                p(
                    ids.joinToString(", ") { "#$it" },
                    "// " + ids.joinToString(", ") { constantPool.getName(it) }
                )
            } else {
                println()
            }
        }

        when (info) {
            is ConstantClassInfo -> {
                pp(info.nameIndex)
            }
            is ConstantMethodrefInfo -> {
                pp(info.classIndex, info.nameAndTypeIndex)
            }
            is ConstantUtf8Info -> {
                p("", "// ${info.str}")
            }
            is ConstantNameAndTypeInfo -> {
                pp(info.nameIndex, info.descriptorIndex)
            }
            is ConstantFieldrefInfo -> {
                pp(info.classIndex, info.nameAndTypeIndex)
            }
            is ConstantStringInfo -> {
                pp(info.stringIndex)
            }
            else -> {
                println()
            }
        }
    }
}
