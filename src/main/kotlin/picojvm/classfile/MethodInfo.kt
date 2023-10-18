package picojvm.classfile

import picojvm.classfile.attribute.AttributeInfo
import picojvm.classfile.attribute.readAttributeInfo
import java.io.DataInputStream

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
