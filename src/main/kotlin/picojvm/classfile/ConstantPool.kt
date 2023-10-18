package picojvm.classfile

import java.io.DataInputStream
import java.nio.charset.StandardCharsets

enum class ConstantTag(val tag: Int) {
    CONSTANT_Class(7),
    CONSTANT_Fieldref(9),
    CONSTANT_Methodref(10),
    CONSTANT_InterfaceMethodref(11),
    CONSTANT_String(8),
    CONSTANT_Integer(3),
    CONSTANT_Float(4),
    CONSTANT_Long(5),
    CONSTANT_Double(6),
    CONSTANT_NameAndType(12),
    CONSTANT_Utf8(1),
    CONSTANT_MethodHandle(15),
    CONSTANT_MethodType(16),
    CONSTANT_InvokeDynamic(18);

    companion object {
        fun fromTag(tag: Int): ConstantTag? {
            return values().find { it.tag == tag }
        }
    }
}

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
    return when (ConstantTag.fromTag(tag.toInt())) {
        ConstantTag.CONSTANT_Class -> {
            ConstantClassInfo(dataInputStream.readShort())
        }
        ConstantTag.CONSTANT_Methodref -> {
            ConstantMethodrefInfo(dataInputStream.readShort(), dataInputStream.readShort())
        }
        ConstantTag.CONSTANT_NameAndType -> {
            ConstantNameAndTypeInfo(dataInputStream.readShort(), dataInputStream.readShort())
        }
        ConstantTag.CONSTANT_Utf8 -> {
            val length = dataInputStream.readShort()
            val bytes = dataInputStream.readNBytes(length.toInt())
            ConstantUtf8Info(String(bytes, StandardCharsets.UTF_8))
        }
        ConstantTag.CONSTANT_Fieldref -> {
            ConstantFieldrefInfo(dataInputStream.readShort(), dataInputStream.readShort())
        }
        ConstantTag.CONSTANT_String -> {
            ConstantStringInfo(dataInputStream.readShort())
        }
        else -> {
            TODO("Unknown constant pool tag: $tag")
        }
    }
}

class ConstantPool {
    private val constantPool = mutableMapOf<Short, ConstantInfo>()

    operator fun set(toShort: Short, value: ConstantInfo) {
        constantPool[toShort] = value
    }

    fun getName(i: Short): String {
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
            is ConstantMethodrefInfo -> {
                getName(p.classIndex) + ":" + getName(p.nameAndTypeIndex)
            }
            else -> {
                p.toString()
            }
        }
    }

    fun forEach(action: (Map.Entry<Short, ConstantInfo>) -> Unit) {
        constantPool.forEach(action)
    }
}
