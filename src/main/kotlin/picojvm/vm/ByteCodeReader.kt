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
        return when (op) {
            ByteCode.BIPUSH,
            ByteCode.SIPUSH,
            ByteCode.IFNULL,
            ByteCode.IFNONNULL,
            ByteCode.GOTO,
            ByteCode.JSR,
            ByteCode.RET,
            ByteCode.TABLESWITCH,
            ByteCode.LOOKUPSWITCH,
            ByteCode.GETSTATIC,
            ByteCode.PUTSTATIC,
            ByteCode.GETFIELD,
            ByteCode.PUTFIELD,
            ByteCode.INVOKEVIRTUAL,
            ByteCode.INVOKESPECIAL,
            ByteCode.INVOKESTATIC,
            ByteCode.INVOKEINTERFACE,
            ByteCode.INVOKEDYNAMIC,
            ByteCode.NEW,
            ByteCode.NEWARRAY,
            ByteCode.ANEWARRAY,
            ByteCode.CHECKCAST,
            ByteCode.INSTANCEOF -> {
                val o1 = readShort()
                ShortOp(op, o1)
            }

            ByteCode.LDC -> {
                val o1 = readByte()
                ByteOp(op, o1)
            }

            ByteCode.ALOAD_0,
            ByteCode.ICONST_M1,
            ByteCode.ICONST_0,
            ByteCode.ICONST_1,
            ByteCode.ICONST_2,
            ByteCode.ICONST_3,
            ByteCode.ICONST_4,
            ByteCode.ICONST_5,
            ByteCode.LCONST_0,
            ByteCode.LCONST_1,
            ByteCode.FCONST_0,
            ByteCode.FCONST_1,
            ByteCode.FCONST_2,
            ByteCode.DCONST_0,
            ByteCode.DCONST_1,
            ByteCode.IALOAD,
            ByteCode.LALOAD,
            ByteCode.FALOAD,
            ByteCode.DALOAD,
            ByteCode.AALOAD,
            ByteCode.BALOAD,
            ByteCode.CALOAD,
            ByteCode.SALOAD,
            ByteCode.IASTORE,
            ByteCode.LASTORE,
            ByteCode.FASTORE,
            ByteCode.DASTORE,
            ByteCode.AASTORE,
            ByteCode.BASTORE,
            ByteCode.CASTORE,
            ByteCode.SASTORE,
            ByteCode.POP,
            ByteCode.POP2,
            ByteCode.DUP,
            ByteCode.DUP_X1,
            ByteCode.DUP_X2,
            ByteCode.DUP2,
            ByteCode.DUP2_X1,
            ByteCode.DUP2_X2,
            ByteCode.SWAP,
            ByteCode.IADD,
            ByteCode.LADD,
            ByteCode.FADD,
            ByteCode.DADD,
            ByteCode.ISUB,
            ByteCode.LSUB,
            ByteCode.FSUB,
            ByteCode.DSUB,
            ByteCode.IMUL,
            ByteCode.LMUL,
            ByteCode.FMUL,
            ByteCode.DMUL,
            ByteCode.IDIV,
            ByteCode.LDIV,
            ByteCode.FDIV,
            ByteCode.DDIV,
            ByteCode.IREM,
            ByteCode.LREM,
            ByteCode.FREM,
            ByteCode.DREM,
            ByteCode.INEG,
            ByteCode.LNEG,
            ByteCode.FNEG,
            ByteCode.DNEG,
            ByteCode.ISHL,
            ByteCode.LSHL,
            ByteCode.ISHR,
            ByteCode.LSHR,
            ByteCode.IUSHR,
            ByteCode.LUSHR,
            ByteCode.IAND,
            ByteCode.LAND,
            ByteCode.IOR,
            ByteCode.LOR,
            ByteCode.IXOR,
            ByteCode.LXOR,
            ByteCode.I2L,
            ByteCode.I2F,
            ByteCode.I2D,
            ByteCode.L2I,
            ByteCode.L2F,
            ByteCode.L2D,
            ByteCode.F2I,
            ByteCode.F2L,
            ByteCode.F2D,
            ByteCode.D2I,
            ByteCode.D2L,
            ByteCode.D2F,
            ByteCode.I2B,
            ByteCode.I2C,
            ByteCode.I2S,
            ByteCode.LCMP,
            ByteCode.FCMPL,
            ByteCode.FCMPG,
            ByteCode.DCMPL,
            ByteCode.DCMPG,
            ByteCode.IRETURN,
            ByteCode.LRETURN,
            ByteCode.FRETURN,
            ByteCode.DRETURN,
            ByteCode.ARETURN,
            ByteCode.RETURN,
            ByteCode.ARRAYLENGTH,
            ByteCode.ATHROW,
            ByteCode.MONITORENTER,
            ByteCode.MONITOREXIT,
            ByteCode.NOP,
            ByteCode.BREAKPOINT -> {
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
