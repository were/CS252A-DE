package multiplier

import chisel3._
import chisel3.util.Cat

class Multiplier16x16 extends Module {
    val io = IO(new Bundle{
        val a = Input(UInt(16.W))
        val b = Input(UInt(16.W))
        val c = Output(UInt((16 * 2).W))
    })
    val width = 16
    val n = (width / 2 + 1)

    val booths = for (i <- 0 until n) yield {
        val booth = Module(new Booth(width))
        if (i == 0) {
          booth.io.a := Cat(io.a(1, 0), 0.U(1.W))
        } else if (i != width / 2) {
          booth.io.a := io.a(i*2+1, i*2-1)
        } else {
          booth.io.a := Cat(0.U((3 - (width-i*2+1)).W), io.a(width-1, i*2-1))
        }
        booth.io.b := io.b
        //printf(p"${booth.io.a} ${booth.io.b}\n")
        //printf(p"${booth.io.out} ${booth.io.cout}\n")
        booth
    }

    var acc = 0.U((width * 2).W);

    val correct_low = Cat(for (i <- 16 to 0 by -1) yield if (i % 2 == 0) booths(i/2).io.cout else 0.U(1.W))
    val correct_hgh = Cat(for (i <- (n - 2) to 0 by -1) yield Cat(1.U, if (i == 0) 1.U else 0.U))
    val correct = Cat(correct_hgh, correct_low)

    printf(p"${Binary(correct)}\n")

    for (i <- (n - 1) to 0 by -1) {
        acc = (acc << 2) + booths(i).io.out// + booths(i).io.cout + (1.U << (width + 2))
        //if (i == 0) acc = acc + (1.U << (width + 1))
        //for (j <- 0 until (width - i)) printf(" ")
        //printf(p"${booths(i).io.out} ${booths(i).io.cout}\n")
        //printf(p"${acc(width * 2 - 1, 0)}\n")
    }

    acc = acc + correct

    printf(p"${acc(width * 2 - 1, 0)}\n")
    //printf(p"${acc(width * 2 - 1, 0)}\n")

    io.c := acc(width * 2 - 1, 0)

}