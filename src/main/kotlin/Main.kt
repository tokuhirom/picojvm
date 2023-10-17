import java.io.DataInputStream
import java.io.FileInputStream
import java.nio.charset.StandardCharsets

const val CONSTANT_Class=	7
const val CONSTANT_Fieldref = 	9
const val CONSTANT_Methodref = 	10
const val CONSTANT_InterfaceMethodref = 	11
const val CONSTANT_String = 	8
const val CONSTANT_Integer = 	3
const val CONSTANT_Float = 	4
const val CONSTANT_Long = 	5
const val CONSTANT_Double = 	6
const val CONSTANT_NameAndType = 	12
const val CONSTANT_Utf8 = 	1
const val CONSTANT_MethodHandle = 	15
const val CONSTANT_MethodType = 	16
const val CONSTANT_InvokeDynamic = 	18

interface ConstantInfo

//            CONSTANT_Class_info {
//                u1 tag;
//                u2 name_index;
//            }
data class ConstantClassInfo(val nameIndex: Short) : ConstantInfo
// https://qiita.com/mima_ita/items/a42f3f016a411627bd7a#constant_methodref
//            CONSTANT_Methodref_info {
//                u1 tag;
//                u2 class_index;
//                u2 name_and_type_index;
//            }
data class ConstantMethodrefInfo(val classIndex: Short, val nameAndTypeIndex: Short) : ConstantInfo
//CONSTANT_NameAndType_info {
//    u1 tag;
//    u2 name_index;
//    u2 descriptor_index;
//}
data class ConstantNameAndTypeInfo(
    val nameIndex: Short,
    val descriptorIndex: Short,
) : ConstantInfo
//CONSTANT_Utf8_info {
//    u1 tag;
//    u2 length;
//    u1 bytes[length];
//}
data class ConstantUtf8Info(
    val str: String,
) : ConstantInfo
//CONSTANT_Fieldref_info {
//    u1 tag;
//    u2 class_index;
//    u2 name_and_type_index;
//}
data class ConstantFieldrefInfo(
    val classIndex: Short,
    val nameAndTypeIndex: Short,
) : ConstantInfo

//CONSTANT_String_info {
//    u1 tag;
//    u2 string_index;
//}
data class ConstantStringInfo(
    val stringIndex: Short,
) : ConstantInfo

fun readConstantPool(dataInputStream: DataInputStream) : ConstantInfo {
    val tag = dataInputStream.readByte()
    return when (tag.toInt()) {
        CONSTANT_Class -> {
            ConstantClassInfo(dataInputStream.readShort())
        }
        CONSTANT_Methodref -> {
            ConstantMethodrefInfo(dataInputStream.readShort(), dataInputStream.readShort())
        }
        CONSTANT_NameAndType -> {
            ConstantNameAndTypeInfo(dataInputStream.readShort(), dataInputStream.readShort())
        }
        CONSTANT_Utf8 -> {
            val length = dataInputStream.readShort()
            val bytes = dataInputStream.readNBytes(length.toInt())
            ConstantUtf8Info(String(bytes, StandardCharsets.UTF_8))
        }
        CONSTANT_Fieldref -> {
            ConstantFieldrefInfo(dataInputStream.readShort(), dataInputStream.readShort())
        }
        CONSTANT_String -> {
            ConstantStringInfo(dataInputStream.readShort())
        }
        else -> {
            TODO("Unknown constant pool tag: $tag")
        }
    }
}

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

data class ClassFile(
    val minorVersion: Int,
    val majorVersion: Int,
    val constantPool: MutableMap<Short, ConstantInfo>,
    val accessFlags: Short,
    val thisClass: Short,
    val superClass: Short,
    val interfaces: List<Short>,
    val fields: List<FieldInfo>,
    val methods: List<MethodInfo>,
    val attributes: List<AttributeInfo>
) {
    fun dump() {
        println("this_class: #$thisClass // ${getName(thisClass)}")
        println("super_class: #$superClass // ${getName(superClass)}")
        println("interfaces: ${interfaces.size}")
        println("Constant pool:")
        constantPool.forEach { (i, info) ->
            printConstantPool(i, info)
        }
        methods.forEach { action ->
            val accessFlag = AccessFlag.fromInt(action.accessFlags.toInt())
            println("$accessFlag ${getName(action.nameIndex)}")
            action.attributes.forEach {
                println(getName(it.attributeNameIndex) + ":")
                // attributeNameIndex が "Code" だったら、バイトコードが入っている。
                // TODO このとき、info は Code_attribute
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
                    "// " + ids.map { getName(it) }.joinToString(", ")
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

    private fun getName(i: Short): String {
        val p = constantPool[i] ?: return "null"
        return when (p) {
            is ConstantClassInfo -> {
                getName(p.nameIndex)
            }
            is ConstantUtf8Info -> {
                p.str
            }
            is ConstantNameAndTypeInfo -> {
                getName(p.nameIndex) + ":" + getName(p.descriptorIndex)
            }
            else -> {
                p.toString()
            }
        }
    }
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
            val constantPool = mutableMapOf<Short, ConstantInfo>()
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
                readFieldInfo(dataInputStream)
            }

            val methodsCount = dataInputStream.readShort()
            val methods = (0..<methodsCount).map {
                readMethodInfo(dataInputStream)
            }


            val attributesCount = dataInputStream.readShort()
            val attributes = (0..<attributesCount).map {
                readAttributeInfo(dataInputStream)
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

fun readMethodInfo(dataInputStream: DataInputStream): MethodInfo {
    val accessFlags = dataInputStream.readShort()
    val nameIndex = dataInputStream.readShort()
    val descriptorIndex = dataInputStream.readShort()
    val attributesCount = dataInputStream.readShort()
    val attributes = (0..<attributesCount).map {
        readAttributeInfo(dataInputStream)
    }
    return MethodInfo(accessFlags, nameIndex, descriptorIndex, attributesCount, attributes)
}

//attribute_info {
//    u2 attribute_name_index;
//    u4 attribute_length;
//    u1 info[attribute_length];
//}
data class AttributeInfo(
    val attributeNameIndex: Short,
    val attributeLength: Int,
    val info: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AttributeInfo

        if (attributeNameIndex != other.attributeNameIndex) return false
        if (attributeLength != other.attributeLength) return false
        if (!info.contentEquals(other.info)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attributeNameIndex.toInt()
        result = 31 * result + attributeLength
        result = 31 * result + info.contentHashCode()
        return result
    }
}

fun readAttributeInfo(dataInputStream: DataInputStream): AttributeInfo {
    val attributeNameIndex = dataInputStream.readShort()
    val attributeLength = dataInputStream.readInt()
    val info = dataInputStream.readNBytes(attributeLength)
    return AttributeInfo(attributeNameIndex, attributeLength, info)
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
fun readFieldInfo(dataInputStream: DataInputStream): FieldInfo {
    val accessFlags = dataInputStream.readShort()
    val nameIndex = dataInputStream.readShort()
    val descriptorIndex = dataInputStream.readShort()
    val attributesCount = dataInputStream.readShort()
    val attributes = (0..<attributesCount).map {
        readAttributeInfo(dataInputStream)
    }
    return FieldInfo(accessFlags, nameIndex, descriptorIndex, attributesCount, attributes)
}

fun main(args: Array<String>) {
    val classFilePath = "Hello.class"
    val classFile = readClassFile(classFilePath)
    classFile.dump()
}
