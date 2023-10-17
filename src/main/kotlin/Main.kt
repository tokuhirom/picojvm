import java.io.DataInputStream
import java.io.FileInputStream

fun main(args: Array<String>) {
    val classFilePath = "Hello.class"
    val classFileInputStream = FileInputStream(classFilePath)
    val dataInputStream = DataInputStream(classFileInputStream)

    // マジックナンバーの読み取り
    val magicNumber = dataInputStream.readInt()
    if (magicNumber != 0xCAFEBABE) {
        throw IllegalArgumentException("Invalid class file")
    }
    println("Magic Number: ${magicNumber.toString(16)}")  // Output: Magic Number: cafebabe

    // バージョン情報の読み取り
    val minorVersion = dataInputStream.readUnsignedShort()
    val majorVersion = dataInputStream.readUnsignedShort()
    println("Minor Version: $minorVersion, Major Version: $majorVersion")

    // 定数プールカウントの読み取り
    val constantPoolCount = dataInputStream.readUnsignedShort()
    println("Constant Pool Count: $constantPoolCount")

    // 他のセクションの読み取りは省略...

    // ストリームのクローズ
    dataInputStream.close()
    classFileInputStream.close()
}
