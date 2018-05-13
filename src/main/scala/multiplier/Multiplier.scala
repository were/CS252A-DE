package multiplier

import chisel3._
import chisel3.util.Cat

/// a reduction like this:
/// short:       ********
/// long:      ************  
/// slices indicates how the overlapped portion is splited to do carry select
/// NOTE: long - short should be an even
class AlignedReduce(short: Int, long: Int, slices: List[Int]) extends Module {
    val io = IO(new Bundle{
        val a = Input(UInt(short.W))
        val b = Input(UInt(long.W))
        val out = Output(UInt(long.W))
        val cout = Output(UInt(1.W))
    })

    //printf(p"a: ${io.a} b: ${io.b}\n")

    val pad = (long - short) / 2

    val high_cond = Module(new AddOne(pad))
    high_cond.io.a := io.b(long - 1, long - pad)
    //printf(p"${long-1},${long-pad} ${Binary(io.b)} ${Binary(io.b(long - 1, long - pad))}\n")

    val adder = Module(new CarrySelectAdder(slices))
    adder.io.a := io.a
    adder.io.b := io.b(long-pad-1, pad)
    //printf(p"${slices.sum} ${long-pad-pad}\n")
    //printf(p"${Binary(io.a)} + ${Binary(io.b(long-pad-1, pad))} = ${adder.io.out}\n")

    val high_bits = Mux(adder.io.cout === 0.U, io.b(long - 1, long - pad), high_cond.io.out)
    val mid_bits  = adder.io.out
    val low_bits  = io.b(pad-1, 0)

    io.out := Cat(high_bits, mid_bits, low_bits)
    //printf(p"${high_bits} ${mid_bits} ${low_bits}\n")
    io.cout:= Mux(adder.io.cout === 0.U, 0.U(1.W), high_cond.io.cout)

    val expected = (io.a << pad) + io.b
    //printf(p"${io.a<<pad} + ${io.b} = ${expected}\n${io.cout} ${io.out}\n")
}

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

    val correct_low = Cat(for (i <- 16 to 0 by -1) yield if (i % 2 == 0) booths(i/2).io.cout else 0.U(1.W))
    val correct_hgh = Cat(for (i <- (n - 2) to 0 by -1) yield Cat(1.U, if (i == 0) 1.U else 0.U))
    val correct = Cat(0.U, correct_hgh, correct_low)

    val pyramid = for (i <- 0 until n) yield
        if (i == n - 1) booths(i).io.out((n - i) * 2 - 1, 0)
        else Cat(Cat(for (j <- (n-1) to (i + 1) by -1) yield booths(j).io.out((n - i) * 2 - 1, (n - i) * 2 - 2)), booths(i).io.out((n - i) * 2 - 1, 0))

    //for (i <- 1 until n) {
    //    printf(p"${pyramid(i)}\n")
    //    acc = acc + (pyramid(i) << (i * 2))
    //}

    val red_0c = Module(new CarrySelectAdder(List(7, 7, 6, 6, 4, 4)))

    red_0c.io.a := correct
    red_0c.io.b := pyramid(0)

    val carry_slices = List(
        List(5, 5, 4, 4, 4, 4),
        List(4, 4, 4, 3, 3),
        List(4, 3, 3),
        List(2)
    )

    val reduction = for (i <- 1 to (n - 1) by 2) yield {
        val long  = width * 2 + 2 - i * 4
        val short = long - 4
        val reduced = Module(new AlignedReduce(short, long, carry_slices(i / 2)))
        reduced.io.a := pyramid(i + 1)
        reduced.io.b := pyramid(i)
        val to_yield = Cat(reduced.io.cout, reduced.io.out)
        //val expected = (pyramid(i + 1) << 2) + pyramid(i)
        //printf(p"${pyramid(i + 1)} ${pyramid(i)} ${to_yield} ${expected}\n")
        to_yield
    }

    //val acc = red_0c.io.out + (reduction(0) << 2) + (reduction(1) << 6) + (reduction(2) << 10) + (reduction(3) << 14)

    val red01 = Module(new AlignedReduce(23, 31, List(5, 5, 4, 3, 3, 3)))
    red01.io.a := reduction(1)
    red01.io.b := reduction(0)
    val reduced32 = Cat(red01.io.cout, red01.io.out)

    val red23 = Module(new AlignedReduce(7, 15, List(3, 2, 2)))
    red23.io.a := reduction(3)
    red23.io.b := reduction(2)
    val reduced16 = Cat(red23.io.cout, red23.io.out)

    val fin  = Module(new AlignedReduce(16, 32, List(4, 4, 4, 4)))
    fin.io.a := reduced16
    fin.io.b := reduced32
    val reduced_fin = Cat(fin.io.cout, fin.io.out)

    //val acc = red_0c.io.out + (reduced16 << 10) + (reduced32 << 2)

    val prod = Module(new CarrySelectAdder(List(6, 6, 5, 4, 4, 3, 2)))
    prod.io.a := reduced_fin(29, 0)
    prod.io.b := red_0c.io.out(31, 2)

    io.c := Cat(prod.io.out(29, 0), red_0c.io.out(1, 0))

    //val acc = red_0c.io.out + (reduced_fin << 2)
    //io.c := acc(width * 2 - 1, 0)

}