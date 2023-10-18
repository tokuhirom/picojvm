package picojvm.classfile.attribute

import picojvm.classfile.ConstantPool
import java.io.DataInputStream

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
