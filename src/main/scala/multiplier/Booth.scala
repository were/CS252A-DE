package multiplier

import chisel3._
import chisel3.util.Cat
import adder._

class Booth(width: Int) extends Module {
    val io = IO(new Bundle {
        val a   =  Input(UInt(3.W))
        val b   =  Input(UInt(width.W))
        val out  = Output(UInt((width + 2).W))
        val cout = Output(UInt(1.W))
    })

    when(io.a === 0.U || io.a === 7.U) {
        io.out := 0.U((width + 2).W)
        io.cout := 0.U
    } .elsewhen(io.a === 1.U || io.a === 2.U) {
        io.out := Cat(0.U(2.W), io.b)
        io.cout := 0.U
    } .elsewhen(io.a === 5.U || io.a === 6.U) {
        io.out := Cat(3.U(2.W), ~io.b)
        io.cout := 1.U
    } .elsewhen(io.a === 3.U) {
        io.out := Cat(0.U(1.W), Cat(io.b, 0.U(1.W)))
        io.cout := 0.U
    } .otherwise {
        io.out := Cat(1.U(1.W), Cat(~io.b, 1.U(1.W)))
        io.cout := 1.U
    }

}