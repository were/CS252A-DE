package multiplier

import chisel3._
import chisel3.util.Cat

class FullAdder extends Module { 
  val io = IO(new Bundle { 
    val a    = Input(UInt(1.W))
    val b    = Input(UInt(1.W))
    val cin  = Input(UInt(1.W))
    val sum  = Output(UInt(1.W))
    val cout = Output(UInt(1.W))
  })
  // Generate the sum 
  val a_xor_b = io.a ^ io.b 
  io.sum := a_xor_b ^ io.cin 
  // Generate the carry 
  val a_and_b = io.a & io.b 
  val a_xor_b_and_cin = a_xor_b & io.cin 
  io.cout := a_and_b | a_xor_b_and_cin
}

// The adder with no carry in
class HalfAdder extends Module { 
  val io = IO(new Bundle { 
    val a    = Input(UInt(1.W))
    val b    = Input(UInt(1.W))
    val sum  = Output(UInt(1.W))
    val cout = Output(UInt(1.W))
  })

  io.sum  := io.a ^ io.b
  io.cout := io.a & io.b
}

class ConditionalAdder(width: Int) extends Module {
    val io = IO(new Bundle { 
        val a     = Input(UInt(width.W))
        val b     = Input(UInt(width.W))
        val sum0  = Output(UInt(width.W))
        val sum1  = Output(UInt(width.W))
        val cout0 = Output(UInt(1.W))
        val cout1 = Output(UInt(1.W))
    })

    val bits = for (i <- 0 until width)
        yield (Module(new FullAdder()), Module(new HalfAdder()))
    
    for (i <- 0 until width) {
        val (a, b) = bits(i)
        a.io.a := io.a(i)
        a.io.b := io.b(i)
        b.io.a := a.io.sum
        if (i != 0) {
            val (c, d) = bits(i - 1)
            a.io.cin := c.io.cout
            b.io.b   := d.io.cout
        } else {
            a.io.cin := 0.U(1.W)
            b.io.b   := 1.U(1.W)
        }
    }

    io.sum0 := Cat(for (i <- 0 until width) yield bits(width - 1 - i)._1.io.sum)
    io.sum1 := Cat(for (i <- 0 until width) yield bits(width - 1 - i)._2.io.sum)

    io.cout0 := bits(width - 1)._1.io.cout
    io.cout1 := bits(width - 1)._1.io.cout | bits(width - 1)._2.io.cout

}