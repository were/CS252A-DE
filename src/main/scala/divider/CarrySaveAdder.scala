// See LICENSE.txt for license details.
package examples

import chisel3._

//A n-bit adder with carry in and carry out
class CarrySaveAdder(val n:Int) extends Module {
  val io = IO(new Bundle {
    val A    = Input(UInt(n.W))
    val B    = Input(UInt(n.W))
    val Cin  = Input(UInt(n.W))
    val Sum  = Output(UInt(n.W))
    val Cout = Output(UInt(n.W))
  })
  //create an Array of FullAdders
  //  NOTE: Since we do all the wiring during elaboration and not at run-time,
  //  i.e., we don't need to dynamically index into the data structure at run-time,
  //  we use an Array instead of a Vec.
  val FAs   = Array.fill(n)(Module(new FullAdder()))
  val carry = Wire(Vec(n, UInt(1.W)))
  val sum   = Wire(Vec(n, UInt(1.W)))

  //wire up the ports of the full adders
  carry(0) := 0.U
  for (i <- 0 until n) {
    FAs(i).io.a := io.A(i)
    FAs(i).io.b := io.B(i)
    FAs(i).io.cin := io.Cin(i)
    carry(i) := FAs(i).io.cout
    sum(i) := FAs(i).io.sum
  }
  io.Sum := sum.asUInt
  io.Cout := (carry.asUInt << 1)(n - 1, 0)
}
