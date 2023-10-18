package picojvm.vm

enum class ByteCode(val opcodeName: String, val opcode: Int, val description: String) {
    NOP("nop", 0, "Do nothing"),
    ACONST_NULL("aconst_null", 1, "Push null"),
    ICONST_M1("iconst_m1", 2, "Push int constant -1"),
    ICONST_0("iconst_0", 3, "Push int constant 0"),
    ICONST_1("iconst_1", 4, "Push int constant 1"),
    ICONST_2("iconst_2", 5, "Push int constant 2"),
    ICONST_3("iconst_3", 6, "Push int constant 3"),
    ICONST_4("iconst_4", 7, "Push int constant 4"),
    ICONST_5("iconst_5", 8, "Push int constant 5"),
    LCONST_0("lconst_0", 9, "Push long constant"),
    LCONST_1("lconst_1", 10, "Push long constant"),
    FCONST_0("fconst_0", 11, "Push float value 0.0"),
    FCONST_1("fconst_1", 12, "Push float value 1.0"),
    FCONST_2("fconst_2", 13, "Push float value 2.0"),
    DCONST_0("dconst_0", 14, "Push double value 0.0"),
    DCONST_1("dconst_1", 15, "Push double value 1.0"),
    BIPUSH("bipush", 16, "Push byte"),
    SIPUSH("sipush", 17, "Push short"),
    LDC("ldc", 18, "Push item from constant pool"),
    LDC_W("ldc_w", 19, "Push item from constant pool (wide index)"),
    LDC2_W("ldc2_w", 20, "Push long or double from constant pool (wide index)"),
    ILOAD("iload", 21, "Load int from local variable"),
    LLOAD("lload", 22, "Load long from local variable"),
    FLOAD("fload", 23, "Load float from local variable"),
    DLOAD("dload", 24, "Load double from local variable"),
    ALOAD("aload", 25, "Load reference from local variable"),
    ILOAD_0("iload_0", 26, "Load int from local variable"),
    ILOAD_1("iload_1", 27, "Load int from local variable"),
    ILOAD_2("iload_2", 28, "Load int from local variable"),
    ILOAD_3("iload_3", 29, "Load int from local variable"),
    LLOAD_0("lload_0", 30, "Load long from local variable 0"),
    LLOAD_1("lload_1", 31, "Load long from local variable 1"),
    LLOAD_2("lload_2", 32, "Load long from local variable 2"),
    LLOAD_3("lload_3", 33, "Load long from local variable 3"),
    FLOAD_0("fload_0", 34, "Load float from local variable 0"),
    FLOAD_1("fload_1", 35, "Load float from local variable 1"),
    FLOAD_2("fload_2", 36, "Load float from local variable 2"),
    FLOAD_3("fload_3", 37, "Load float from local variable 3"),
    DLOAD_0("dload_0", 38, "Load double from local variable 0"),
    DLOAD_1("dload_1", 39, "Load double from local variable 1"),
    DLOAD_2("dload_2", 40, "Load double from local variable 2"),
    DLOAD_3("dload_3", 41, "Load double from local variable 3"),
    ALOAD_0("aload_0", 42, "Load reference from local variable 0"),
    ALOAD_1("aload_1", 43, "Load reference from local variable 1"),
    ALOAD_2("aload_2", 44, "Load reference from local variable 2"),
    ALOAD_3("aload_3", 45, "Load reference from local variable 3"),
    IALOAD("iaload", 46, "Load int from array"),
    LALOAD("laload", 47, "Load long from array"),
    FALOAD("faload", 48, "Load float from array"),
    DALOAD("daload", 49, "Load double from array"),
    AALOAD("aaload", 50, "Load reference from array"),
    BALOAD("baload", 51, "Load byte or boolean from array"),
    CALOAD("caload", 52, "Load char from array"),
    SALOAD("saload", 53, "Load short from array"),
    ISTORE("istore", 54, "Store int into local variable"),
    LSTORE("lstore", 55, "Store long into local variable"),
    FSTORE("fstore", 56, "Store float into local variable"),
    DSTORE("dstore", 57, "Store double into local variable"),
    ASTORE("astore", 58, "Store reference into local variable"),
    ISTORE_0("istore_0", 59, "Store int into local variable 0"),
    ISTORE_1("istore_1", 60, "Store int into local variable 1"),
    ISTORE_2("istore_2", 61, "Store int into local variable 2"),
    ISTORE_3("istore_3", 62, "Store int into local variable 3"),
    LSTORE_0("lstore_0", 63, "Store long into local variable 0"),
    LSTORE_1("lstore_1", 64, "Store long into local variable 1"),
    LSTORE_2("lstore_2", 65, "Store long into local variable 2"),
    LSTORE_3("lstore_3", 66, "Store long into local variable 3"),
    FSTORE_0("fstore_0", 67, "Store float into local variable 0"),
    FSTORE_1("fstore_1", 68, "Store float into local variable 1"),
    FSTORE_2("fstore_2", 69, "Store float into local variable 2"),
    FSTORE_3("fstore_3", 70, "Store float into local variable 3"),
    DSTORE_0("dstore_0", 71, "Store double into local variable 0"),
    DSTORE_1("dstore_1", 72, "Store double into local variable 1"),
    DSTORE_2("dstore_2", 73, "Store double into local variable 2"),
    DSTORE_3("dstore_3", 74, "Store double into local variable 3"),
    ASTORE_0("astore_0", 75, "Store reference into local variable 0"),
    ASTORE_1("astore_1", 76, "Store reference into local variable 1"),
    ASTORE_2("astore_2", 77, "Store reference into local variable 2"),
    ASTORE_3("astore_3", 78, "Store reference into local variable 3"),
    IASTORE("iastore", 79, "Store into int array"),
    LASTORE("lastore", 80, "Store into long array"),
    FASTORE("fastore", 81, "Store into float array"),
    DASTORE("dastore", 82, "Store into double array"),
    AASTORE("aastore", 83, "Store into reference array"),
    BASTORE("bastore", 84, "Store into byte or boolean array"),
    CASTORE("castore", 85, "Store into char array"),
    SASTORE("sastore", 86, "Store into short array"),
    POP("pop", 87, "Pop the top operand stack value"),
    POP2("pop2", 88, "Pop the top one or two operand stack values"),
    DUP("dup", 89, "Duplicate the top operand stack value"),
    DUP_X1("dup_x1", 90, "Duplicate the top operand stack value and insert two values down"),
    DUP_X2("dup_x2", 91, "Duplicate the top operand stack value and insert two or three values down"),
    DUP2("dup2", 92, "Duplicate the top one or two operand stack values"),
    DUP2_X1("dup2_x1", 93, "Duplicate the top one or two operand stack values and insert two or three values down"),
    DUP2_X2("dup2_x2", 94, "Duplicate the top one or two operand stack values and insert two, three, or four values down"),
    SWAP("swap", 95, "Swap the top two operand stack values"),
    IADD("iadd", 96, "Add int"),
    LADD("ladd", 97, "Add long"),
    FADD("fadd", 98, "Add float"),
    DADD("dadd", 99, "Add double"),
    ISUB("isub", 100, "Subtract int"),
    LSUB("lsub", 101, "Subtract long"),
    FSUB("fsub", 102, "Subtract float"),
    DSUB("dsub", 103, "Subtract double"),
    IMUL("imul", 104, "Multiply int"),
    LMUL("lmul", 105, "Multiply long"),
    FMUL("fmul", 106, "Multiply float"),
    DMUL("dmul", 107, "Multiply double"),
    IDIV("idiv", 108, "Divide int"),
    LDIV("ldiv", 109, "Divide long"),
    FDIV("fdiv", 110, "Divide float"),
    DDIV("ddiv", 111, "Divide double"),
    IREM("irem", 112, "Remainder int"),
    LREM("lrem", 113, "Remainder long"),
    FREM("frem", 114, "Remainder float"),
    DREM("drem", 115, "Remainder double"),
    INEG("ineg", 116, "Negate int"),
    LNEG("lneg", 117, "Negate long"),
    FNEG("fneg", 118, "Negate float"),
    DNEG("dneg", 119, "Negate double"),
    ISHL("ishl", 120, "Shift left int"),
    LSHL("lshl", 121, "Shift left long"),
    ISHR("ishr", 122, "Arithmetic shift right int"),
    LSHR("lshr", 123, "Arithmetic shift right long"),
    IUSHR("iushr", 124, "Logical shift right int"),
    LUSHR("lushr", 125, "Logical shift right long"),
    IAND("iand", 126, "Boolean AND int"),
    LAND("land", 127, "Boolean AND long"),
    IOR("ior", 128, "Boolean OR int"),
    LOR("lor", 129, "Boolean OR long"),
    IXOR("ixor", 130, "Boolean XOR int"),
    LXOR("lxor", 131, "Boolean XOR long"),
    IINC("iinc", 132, "Increment local variable by constant"),
    I2L("i2l", 133, "Convert int to long"),
    I2F("i2f", 134, "Convert int to float"),
    I2D("i2d", 135, "Convert int to double"),
    L2I("l2i", 136, "Convert long to int"),
    L2F("l2f", 137, "Convert long to float"),
    L2D("l2d", 138, "Convert long to double"),
    F2I("f2i", 139, "Convert float to int"),
    F2L("f2l", 140, "Convert float to long"),
    F2D("f2d", 141, "Convert float to double"),
    D2I("d2i", 142, "Convert double to int"),
    D2L("d2l", 143, "Convert double to long"),
    D2F("d2f", 144, "Convert double to float"),
    I2B("i2b", 145, "Convert int to byte"),
    I2C("i2c", 146, "Convert int to char"),
    I2S("i2s", 147, "Convert int to short"),
    LCMP("lcmp", 148, "Compare long"),
    FCMPL("fcmpl", 149, "Compare float"),
    FCMPG("fcmpg", 150, "Compare float"),
    DCMPL("dcmpl", 151, "Compare double"),
    DCMPG("dcmpg", 152, "Compare double"),
    IFEQ("ifeq", 153, "Jump if int comparison with zero succeeds"),
    IFNE("ifne", 154, "Jump if int comparison with zero succeeds"),
    IFLT("iflt", 155, "Jump if int comparison with zero succeeds"),
    IFGE("ifge", 156, "Jump if int comparison with zero succeeds"),
    IFGT("ifgt", 157, "Jump if int comparison with zero succeeds"),
    IFLE("ifle", 158, "Jump if int comparison with zero succeeds"),
    IF_ICMPEQ("if_icmpeq", 159, "Jump if int comparison succeeds"),
    IF_ICMPNE("if_icmpne", 160, "Jump if int comparison succeeds"),
    IF_ICMPLT("if_icmplt", 161, "Jump if int comparison succeeds"),
    IF_ICMPGE("if_icmpge", 162, "Jump if int comparison succeeds"),
    IF_ICMPGT("if_icmpgt", 163, "Jump if int comparison succeeds"),
    IF_ICMPLE("if_icmple", 164, "Jump if int comparison succeeds"),
    IF_ACMPEQ("if_acmpeq", 165, "Jump if reference comparison succeeds"),
    IF_ACMPNE("if_acmpne", 166, "Jump if reference comparison succeeds"),
    GOTO("goto", 167, "Unconditional jump"),
    JSR("jsr", 168, "Jump subroutine"),
    RET("ret", 169, "Return from subroutine"),
    TABLESWITCH("tableswitch", 170, "Access jump table by index and jump"),
    LOOKUPSWITCH("lookupswitch", 171, "Access jump table by key match and jump"),
    IRETURN("ireturn", 172, "Return int from method"),
    LRETURN("lreturn", 173, "Return long from method"),
    FRETURN("freturn", 174, "Return float from method"),
    DRETURN("dreturn", 175, "Return double from method"),
    ARETURN("areturn", 176, "Return reference from method"),
    RETURN("return", 177, "Return void from method"),
    GETSTATIC("getstatic", 178, "Get static field from class"),
    PUTSTATIC("putstatic", 179, "Set static field in class"),
    GETFIELD("getfield", 180, "Fetch field from object"),
    PUTFIELD("putfield", 181, "Set field in object"),
    INVOKEVIRTUAL("invokevirtual", 182, "Invoke instance method, dispatch based on class"),
    INVOKESPECIAL("invokespecial", 183, "Directly invoke instance (initialization) method of the current class or its supertypes"),
    INVOKESTATIC("invokestatic", 184, "Invoke static method"),
    INVOKEINTERFACE("invokeinterface", 185, "Invoke interface method"),
    INVOKEDYNAMIC("invokedynamic", 186, "Invoke a dynamically-computed call site"),
    NEW("new", 187, "Create new object"),
    NEWARRAY("newarray", 188, "Create new array"),
    ANEWARRAY("anewarray", 189, "Create new array of reference"),
    ARRAYLENGTH("arraylength", 190, "Get length of array"),
    ATHROW("athrow", 191, "Throw Throwable reference"),
    CHECKCAST("checkcast", 192, "Check whether reference is of given type"),
    INSTANCEOF("instanceof", 193, "Determine if reference is of given type"),
    MONITORENTER("monitorenter", 194, "Enter monitor for object"),
    MONITOREXIT("monitorexit", 195, "Exit monitor for object"),
    WIDE("wide", 196, "Extend local variable index by additional bytes"),
    MULTIANEWARRAY("multianewarray", 197, "Create new multidimensional array"),
    IFNULL("ifnull", 198, "Jump if reference is null"),
    IFNONNULL("ifnonnull", 199, "Jump if reference not null"),
    GOTO_W("goto_w", 200, "Unconditional jump (wide index)"),
    JSR_W("jsr_w", 201, "Jump subroutine (wide index)"),
    BREAKPOINT("breakpoint", 202, "Reserved for internal usage in debuggers"),
    IMPDEP1("impdep1", 254, "Reserved for internal usage in JVM"),
    IMPDEP2("impdep2", 255, "Reserved for internal usage in JVM");

    companion object {
        private val opcodeLookup = entries.associateBy(ByteCode::opcode)

        fun fromOpcode(opcode: Byte): ByteCode? {
            // kotlin の Byte は signed 8bit int なので、これを unsigned 8bit int として解釈しなおす
            return opcodeLookup[opcode.toInt() and 0xFF]
        }
    }
}