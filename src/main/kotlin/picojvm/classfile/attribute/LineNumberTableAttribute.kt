package picojvm.classfile.attribute

import java.io.DataInputStream

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
