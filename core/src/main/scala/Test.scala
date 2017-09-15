import me.amanj.LittleThings._

@builder class Test(val a: Int, val b: Int)


object Main {
  def main(args: Array[String]): Unit = {
    val test: Test = null
     val builder = new Test.TestBuilder
     builder.setA(1)
     builder.setB(1)
     println(s"It works: ${builder.build()}")
     println(s"It works: ${(new Test.TestBuilder).setA(1).setB(2).build()}")
  }
}
