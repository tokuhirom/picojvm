package picojvm.classfile.attribute

import java.io.DataInputStream

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
