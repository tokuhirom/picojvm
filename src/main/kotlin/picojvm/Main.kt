package picojvm

import picojvm.classfile.ClassFile
import picojvm.classfile.ConstantPool
import picojvm.classfile.readConstantPool
import java.io.DataInputStream
import java.io.FileInputStream
import java.nio.charset.StandardCharsets


enum class AccessFlag(val flag: Int) {
    ACC_PUBLIC(0x0001), // Declared public; may be accessed from outside its package.
    ACC_FINAL(0x0010), // Declared final; no subclasses allowed.
    ACC_SUPER(0x0020), // Treat superclass methods specially when invoked by the invokespecial instruction.
    ACC_INTERFACE(0x0200), // Is an interface, not a class.
    ACC_ABSTRACT(0x0400), // Declared abstract; must not be instantiated.
    ACC_SYNTHETIC(0x1000), // Declared synthetic; not present in the source code.
    ACC_ANNOTATION(0x2000), // Declared as an annotation type.
    ACC_ENUM(0x4000); // Declared as an enum type.

    companion object {
        fun fromInt(flag: Int): Set<AccessFlag> {
            return entries.filter { flag and it.flag != 0 }.toSet()
        }
    }
}

//Code_attribute {
//    u2 attribute_name_index;
//    u4 attribute_length;
//    u2 max_stack;
//    u2 max_locals;
//    u4 code_length;
//    u1 code[code_length];
//    u2 exception_table_length;
//    {   u2 start_pc;
//        u2 end_pc;
//        u2 handler_pc;
//        u2 catch_type;
//    } exception_table[exception_table_length];
//    u2 attributes_count;
//    attribute_info attributes[attributes_count];
//}
data class ExceptionTableEntry(
    val startPc: Short,
    val endPc: Short,
    val handlerPc: Short,
    val catchType: Short
)

data class CodeAttribute(
    override val attributeNameIndex: Short,
    override val attributeLength: Int,
    val maxStack: Short,
    val maxLocals: Short,
    val code: ByteArray,
    val exceptionTable: List<ExceptionTableEntry>,
    val attributes: List<AttributeInfo>
) : AttributeInfo {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CodeAttribute

        if (attributeNameIndex != other.attributeNameIndex) return false
        if (attributeLength != other.attributeLength) return false
        if (maxStack != other.maxStack) return false
        if (maxLocals != other.maxLocals) return false
        if (!code.contentEquals(other.code)) return false
        if (exceptionTable != other.exceptionTable) return false
        if (attributes != other.attributes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attributeNameIndex.toInt()
        result = 31 * result + attributeLength
        result = 31 * result + maxStack
        result = 31 * result + maxLocals
        result = 31 * result + code.contentHashCode()
        result = 31 * result + exceptionTable.hashCode()
        result = 31 * result + attributes.hashCode()
        return result
    }
}

fun readCodeAttribute(constantPool: ConstantPool, dataInputStream: DataInputStream, attributeNameIndex: Short, attributeLength: Int): CodeAttribute {
    val maxStack = dataInputStream.readShort()
    val maxLocals = dataInputStream.readShort()
    val codeLength = dataInputStream.readInt()
    val code = dataInputStream.readNBytes(codeLength)

    val exceptionTableLength = dataInputStream.readShort()
    val exceptionTable = (0..<exceptionTableLength).map {
        val startPc = dataInputStream.readShort()
        val endPc = dataInputStream.readShort()
        val handlerPc = dataInputStream.readShort()
        val catchType = dataInputStream.readShort()
        ExceptionTableEntry(startPc, endPc, handlerPc, catchType)
    }

    val attributesCount = dataInputStream.readShort()
    val attributes = (0..<attributesCount).map {
        readAttributeInfo(constantPool, dataInputStream) // Assumes you have a function to read generic attribute_info
    }

    return CodeAttribute(attributeNameIndex, attributeLength, maxStack, maxLocals, code, exceptionTable, attributes)
}


// https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html
fun readClassFile(fileName: String): ClassFile {
    // Multibyte data items are always stored in big-endian order であることに注意
    // DataInputStream は big-endian by default.
    FileInputStream(fileName).use {classFileInputStream ->
        DataInputStream(classFileInputStream).use {dataInputStream ->
            // Read magic number
            val magicNumber = dataInputStream.readInt()
            if (magicNumber != 0xCAFEBABE.toInt()) {
                throw IllegalArgumentException("Invalid class file")
            }
            System.out.printf("Magic Number: %X\n", magicNumber)

            // Read version number
            val minorVersion = dataInputStream.readUnsignedShort()
            val majorVersion = dataInputStream.readUnsignedShort()
            println("Minor Version: $minorVersion, Major Version: $majorVersion")

            // Read constant pool
            val constantPoolCount = dataInputStream.readUnsignedShort()
            println("Constant Pool Count: $constantPoolCount")
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

/**
 * method_info {
 *     u2             access_flags;
 *     u2             name_index;
 *     u2             descriptor_index;
 *     u2             attributes_count;
 *     attribute_info attributes[attributes_count];
 * }
 */
data class MethodInfo(
    val accessFlags: Short,
    val nameIndex: Short,
    val descriptorIndex: Short,
    val attributesCount: Short,
    val attributes: List<AttributeInfo>,
)

fun readMethodInfo(constantPool: ConstantPool, dataInputStream: DataInputStream): MethodInfo {
    val accessFlags = dataInputStream.readShort()
    val nameIndex = dataInputStream.readShort()
    val descriptorIndex = dataInputStream.readShort()
    val attributesCount = dataInputStream.readShort()
    val attributes = (0..<attributesCount).map {
        readAttributeInfo(constantPool, dataInputStream)
    }
    return MethodInfo(accessFlags, nameIndex, descriptorIndex, attributesCount, attributes)
}

//attribute_info {
//    u2 attribute_name_index;
//    u4 attribute_length;
//    u1 info[attribute_length];
//}
interface AttributeInfo {
    val attributeNameIndex: Short
    val attributeLength: Int
}

fun readAttributeInfo(constantPool: ConstantPool, dataInputStream: DataInputStream): AttributeInfo {
    val attributeNameIndex = dataInputStream.readShort()
    val attributeLength = dataInputStream.readInt()
    return when (val attributeName = constantPool.getName(attributeNameIndex)) {
        "Code" -> {
            readCodeAttribute(constantPool, dataInputStream, attributeNameIndex, attributeLength)
        }
        "LineNumberTable" -> {
            readLineNumberTableAttribute(dataInputStream, attributeNameIndex, attributeLength)
        }
        "SourceFile" -> {
            readSourceFileAttribute(dataInputStream, attributeNameIndex, attributeLength)
        }
        else -> {
            TODO("Unknown attribute: $attributeName")
        }
    }
}

//LineNumberTable_attribute {
//    u2 attribute_name_index;
//    u4 attribute_length;
//    u2 line_number_table_length;
//    {   u2 start_pc;
//        u2 line_number;
//    } line_number_table[line_number_table_length];
//}

// Data class to represent an entry in the LineNumberTable
data class LineNumberEntry(
    val startPc: Short,
    val lineNumber: Short
)

// Data class to represent the LineNumberTable attribute
data class LineNumberTableAttribute(
    override val attributeNameIndex: Short,
    override val attributeLength: Int,
    val lineNumberTable: List<LineNumberEntry>
) : AttributeInfo

// Function to read the LineNumberTable attribute from a DataInputStream
fun readLineNumberTableAttribute(dataInputStream: DataInputStream, attributeNameIndex: Short, attributeLength: Int): LineNumberTableAttribute {
    val lineNumberTableLength = dataInputStream.readShort()
    val lineNumberTable = (0 until lineNumberTableLength).map {
        val startPc = dataInputStream.readShort()
        val lineNumber = dataInputStream.readShort()
        LineNumberEntry(startPc, lineNumber)
    }
    return LineNumberTableAttribute(attributeNameIndex, attributeLength, lineNumberTable)
}

//SourceFile_attribute {
//    u2 attribute_name_index;
//    u4 attribute_length;
//    u2 sourcefile_index;
//}

// Data class to represent the SourceFile attribute
data class SourceFileAttribute(
    override val attributeNameIndex: Short,
    override val attributeLength: Int,
    val sourceFileIndex: Short
) : AttributeInfo

// Function to read the SourceFile attribute from a DataInputStream
fun readSourceFileAttribute(dataInputStream: DataInputStream, attributeNameIndex: Short, attributeLength: Int): SourceFileAttribute {
    val sourceFileIndex = dataInputStream.readShort()
    return SourceFileAttribute(attributeNameIndex, attributeLength, sourceFileIndex)
}


//field_info {
//    u2             access_flags;
//    u2             name_index;
//    u2             descriptor_index;
//    u2             attributes_count;
//    attribute_info attributes[attributes_count];
//}
data class FieldInfo(
    val accessFlags: Short,
    val nameIndex: Short,
    val descriptorIndex: Short,
    val attributeCount: Short,
    val attributes: List<AttributeInfo>
)
fun readFieldInfo(constantPool: ConstantPool, dataInputStream: DataInputStream): FieldInfo {
    val accessFlags = dataInputStream.readShort()
    val nameIndex = dataInputStream.readShort()
    val descriptorIndex = dataInputStream.readShort()
    val attributesCount = dataInputStream.readShort()
    val attributes = (0..<attributesCount).map {
        readAttributeInfo(constantPool, dataInputStream)
    }
    return FieldInfo(accessFlags, nameIndex, descriptorIndex, attributesCount, attributes)
}

fun main(args: Array<String>) {
    val classFilePath = "Hello.class"
    val classFile = readClassFile(classFilePath)
    classFile.dump()
}
