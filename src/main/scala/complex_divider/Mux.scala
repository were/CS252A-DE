

package examples

import chisel3._
import chisel3.util._

class Mux2 extends Module {
    val io = IO(new Bundle {
        val sel = Input(UInt(1.W))
        val in0 = Input(UInt(1.W))
        val in1 = Input(UInt(1.W))
        val out = Output(UInt(1.W))
    })
    io.out := (io.sel & io.in1) | (~io.sel & io.in0)
}

class Multiplexer(val n: Int) extends Module {
    val io = IO(new Bundle {
        val a = Input(UInt(n.W))
        val b = Input(UInt(n.W))
        // sel = 0 -> a, sel = 1 => b
        val sel = Input(UInt(1.W))
        val o = Output(UInt(n.W))
    })

    val Mux2s = Array.fill(n)(Module(new Mux2()))
    val out = Wire(Vec(n, UInt(1.W)))
    for (i <- 0 until n) {
        Mux2s(i).io.in0 := io.a(i)
        Mux2s(i).io.in1 := io.b(i)
        Mux2s(i).io.sel := io.sel
        out(i) := Mux2s(i).io.out
    }

    io.o := out.asUInt
}
