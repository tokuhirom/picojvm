package picojvm.classfile

import picojvm.classfile.attribute.AttributeInfo
import picojvm.classfile.attribute.CodeAttribute
import picojvm.classfile.attribute.LineNumberTableAttribute
import picojvm.classfile.attribute.SourceFileAttribute
import picojvm.classfile.attribute.readAttributeInfo
import picojvm.vm.ByteCodeReader
import picojvm.vm.ByteOp
import picojvm.vm.NoArgOp
import picojvm.vm.ShortOp
import java.io.DataInputStream
import java.io.FileInputStream

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

                val reader = ByteCodeReader(attributeInfo)

                while (reader.hasMoreElements()) {
                    val op = reader.readOp()
                    when (op) {
                        is ShortOp -> {
                            println("      ${op.byteCode.opcodeName} #${op.op1} // ${constantPool.getName(op.op1)}")
                        }
                        is ByteOp -> {
                            println("      ${op.byteCode.opcodeName} #${op.op1} // ${constantPool.getName((op.op1.toInt() and 0xff).toShort())}")
                        }
                        is NoArgOp -> {
                            println("      ${op.byteCode.opcodeName}")
                        }
                        else -> {
                            TODO("Unsupported op: pc=${reader.pc}, $op")
                        }
                    }
                }

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

    fun getMainMethod(): CodeAttribute {
        return methods.first {
            constantPool.getName(it.nameIndex) == "main"
        }.attributes
            .filterIsInstance<CodeAttribute>()
            .first()
    }
}

// https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html
fun readClassFile(fileName: String): ClassFile {
    // Multibyte data items are always stored in big-endian order であることに注意
    // DataInputStream は big-endian by default.
    FileInputStream(fileName).use { classFileInputStream ->
        DataInputStream(classFileInputStream).use { dataInputStream ->
            // Read magic number
            val magicNumber = dataInputStream.readInt()
            if (magicNumber != 0xCAFEBABE.toInt()) {
                throw IllegalArgumentException("Invalid class file")
            }

            // Read version number
            val minorVersion = dataInputStream.readUnsignedShort()
            val majorVersion = dataInputStream.readUnsignedShort()

            // Read constant pool
            val constantPoolCount = dataInputStream.readUnsignedShort()
            val constantPool = ConstantPool()
            for (i in 1..<constantPoolCount) {
                constantPool[i.toShort()] = readConstantPool(dataInputStream)
            }

            val accessFlags = dataInputStream.readShort()
            val thisClass = dataInputStream.readShort()
            val superClass = dataInputStream.readShort()

            val interfacesCount = dataInputStream.readShort()
            val interfaces = (0..<interfacesCount).map { i ->
                dataInputStream.readShort()
            }

            val fieldsCount = dataInputStream.readShort()
            val fields = (0..<fieldsCount).map {
                readFieldInfo(constantPool, dataInputStream)
            }

            val methodsCount = dataInputStream.readShort()
            val methods = (0..<methodsCount).map {
                readMethodInfo(constantPool, dataInputStream)
            }


            val attributesCount = dataInputStream.readShort()
            val attributes = (0..<attributesCount).map {
                readAttributeInfo(constantPool, dataInputStream)
            }

            return ClassFile(
                minorVersion, majorVersion,
                constantPool,
                accessFlags, thisClass, superClass,
                interfaces,
                fields,
                methods,
                attributes,
            )
        }
    }
}
