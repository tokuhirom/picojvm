package picojvm.classfile.attribute

import picojvm.classfile.ConstantPool
import java.io.DataInputStream

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
