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

class SimpleAdder(width: Int) extends Module {
    val io = IO(new Bundle{
        val a = Input(UInt(width.W))
        val b = Input(UInt(width.W))
        val out  = Output(UInt(width.W))
        val cout = Output(UInt(1.W))
    })
    
    val first = Module(new HalfAdder())
    val other = for (i <- 0 until (width - 1)) yield Module(new FullAdder())

    first.io.a := io.a(0)
    first.io.b := io.b(0)

    for (i <- 0 until (width - 1)) {
        other(i).io.a := io.a(i+1)
        other(i).io.b := io.b(i+1)
        if (i == 0)
            other(i).io.cin := first.io.cout
        else
            other(i).io.cin := other(i-1).io.cout
    }
    io.cout := other(width-2).io.cout
    io.out  := Cat(Cat(for (i <- 0 until (width - 1)) yield other(width-2-i).io.sum), first.io.sum)
}

class CarrySelectAdder(slices_ : Int*) extends Module {
    val io = IO(new Bundle{
        val a = Input(UInt(slices_.sum.W))
        val b = Input(UInt(slices_.sum.W))
        val out  = Output(UInt(slices_.sum.W))
        val cout  = Output(UInt(1.W))
    })
    val slices = slices_.reverse

    val first = Module(new SimpleAdder(slices(0)))
    val other = for (i <- 1 until slices.length) yield Module(new ConditionalAdder(slices(i)))

    first.io.a := io.a(slices(0) - 1, 0)
    first.io.b := io.b(slices(0) - 1, 0)

    other(0).io.a := io.a(slices(0) + slices(1) - 1, slices(0))
    other(0).io.b := io.b(slices(0) + slices(1) - 1, slices(0))

    var previous = (Mux(first.io.cout === 0.U, other(0).io.cout0, other(0).io.cout1), Mux(first.io.cout === 0.U, other(0).io.sum0, other(0).io.sum1))
    val second = previous
    var cur = slices(0) + slices(1)
    
    //printf(p"${first.io.a}+${first.io.b}=${first.io.out} (${first.io.cout})\n")
    //printf(p"${other(0).io.sum0} ${other(0).io.sum1} -> ${second._2}\n")

    var sums = for (i <- 2 until slices.length) yield {
        other(i-1).io.a := io.a(cur + slices(i) - 1, cur)
        other(i-1).io.b := io.b(cur + slices(i) - 1, cur)
        cur += slices(i)
        val (cout, sum) = previous
        val to_yeild = (Mux(cout === 0.U, other(i-1).io.cout0, other(i-1).io.cout1), Mux(cout === 0.U, other(i-1).io.sum0, other(i-1).io.sum1))
        previous = to_yeild
        //printf(p"${to_yeild._2}\n")
        to_yeild
    }

    io.out := Cat(Cat(sums.map(_._2).reverse), Cat(second._2, first.io.out))
    //printf(p"${Binary(io.out)}")
    //io.out := Cat(sums.map(_._2).reverse)
    io.cout := sums.last._1
}
