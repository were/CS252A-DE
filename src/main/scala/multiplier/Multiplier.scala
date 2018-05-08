package multiplier

import chisel3._
import chisel3.util.Cat
import adder.FullAdder

class Multiplier(width: Int) extends Module {
    val io = IO(new Bundle{
        val a = Input(UInt(width.W))
        val b = Input(UInt(width.W))
        val c = Output(UInt((width * 2).W))
    })

    val booths = for (i <- 0 until (width / 2 + 1)) yield {
        val booth = Module(new Booth(width))
        if (i == 0) {
          booth.io.a := Cat(io.a(1, 0), 0.U(1.W))
        } else if (i != width / 2) {
          booth.io.a := io.a(i*2+1, i*2-1)
        } else {
          booth.io.a := Cat(0.U(1.W), io.a(width-1, width-2))
        }
        booth.io.b := io.b
        booth
    }

    for (i <- 0 until (width / 2 + 1)) {
        for (j <- 0 until (width - i)) printf(" ")
        printf(p"${Binary(booths(i).io.out)} ${booths(i).io.cout}\n")
    }

    val right_cols_even = for (i <- 0 until width + 1 if i % 2 == 0) yield {
        val n = (i / 2 + 1)
        val _col = Cat(for (j <- 0 until n) yield booths(n - j - 1).io.out(j * 2))
        val  col = Cat(_col, booths(i / 2).io.cout)
        col
    }

    val right_cols_odd = for (i <- 0 until width + 1 if i % 2 == 1) yield {
        val n = (i / 2 + 1)
        val col = Cat(for (j <- 0 until n) yield booths(n - j - 1).io.out(j * 2 + 1))
        col
    }

    //for (i <- 0 until (width + 1)) {
    //    if (i % 2 == 0)
    //        printf(p"${right_cols_even(i / 2)}\n")
    //    else 
    //        printf(p"${right_cols_odd(i / 2)}\n")
    //}

    //booths foreach {
    //    case booth =>
    //        printf(p"${booth.io.a} ${booth.io.b} ${booth.io.cout} ${booth.io.out}\n")
    //}

    io.c := 0.U

}