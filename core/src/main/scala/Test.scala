import me.amanj.LittleThings._

@builder class Test(val a: Int, val b: Int)

object Test2
@builder class Test2(val a: Int)

@builder class Test3(val a: Int)
object Test3

object Main {
  def main(args: Array[String]): Unit = {
    val test: Test = null
     val builder = new Test.TestBuilder
     builder.setA(1)
     builder.setB(1)
     println(s"It works: ${builder.build()}")
     println(s"It works: ${(new Test.TestBuilder).setA(1).setB(2).build()}")
     println(s"It works: ${(new Test2.Test2Builder).setA(2).build()}")
     println(s"It works: ${(new Test3.Test3Builder).setA(2).build()}")
  }
}
