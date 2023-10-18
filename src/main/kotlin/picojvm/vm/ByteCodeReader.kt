package picojvm.vm

import picojvm.classfile.attribute.CodeAttribute

class ByteCodeReader(private val attributeInfo: CodeAttribute) {
    var pc = 0

    fun readShort() : Short {
        val high = attributeInfo.code[pc++].toInt() shl 8 and 0xFF00
        val low = attributeInfo.code[pc++].toInt() and 0xFF
        return (high or low).toShort()
    }

    fun readByte() : Byte {
        return attributeInfo.code[pc++]
    }

    fun hasMoreElements() : Boolean {
         return pc < attributeInfo.code.size
     }

    fun readOp(): Op {
        val op = ByteCode.fromOpcode(attributeInfo.code[pc++])
        return when (op?.operandType) {
            OperandType.SHORT_OP -> {
                ShortOp(op, readShort())
            }
            OperandType.BYTE_OP -> {
                ByteOp(op, readByte())
            }
            OperandType.NO_OP -> {
                return NoArgOp(op)
            }
            else -> {
                TODO("Unsupported op: pc=$pc, $op")
            }
        }
    }
}

interface Op {
    val byteCode: ByteCode
}
data class NoArgOp(override val byteCode: ByteCode) : Op
data class ShortOp(override val byteCode: ByteCode, val op1: Short) : Op
data class ByteOp(override val byteCode: ByteCode, val op1: Byte) : Op
