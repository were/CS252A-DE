package adder

import chisel3._

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
class OrXor extends Module { 
  val io = IO(new Bundle { 
    val a    = Input(UInt(1.W))
    val b    = Input(UInt(1.W))
    val sum  = Output(UInt(1.W))
    val cout = Output(UInt(1.W))
  })

  io.sum  := io.a ^ io.b
  io.cout := io.a & io.b
}

class CarrySelectAdder16 extends Module {

  val io = IO(new Bundle { 
    val a   = Input(UInt(16.W))
    val b   = Input(UInt(16.W))
    val out = Output(UInt(16.W))
  })

  val a0 = Module(new FullAdder4())
  a0.io.a := io.a(3, 0)
  a0.io.b := io.b(3, 0)

  val a1 = Module(new SelectAdder4())
  a1.io.a := io.a(7, 4)
  a1.io.b := io.b(7, 4)

  val out1  = Mux(a0.io.cout === 0.U, a1.io.sum0, a1.io.sum1)
  val cout1 = Mux(a0.io.cout === 0.U, a1.io.cout0, a1.io.cout1)

  val a2 = Module(new SelectAdder4())
  a2.io.a := io.a(11, 8)
  a2.io.b := io.b(11, 8)

  val out2  = Mux(cout1 === 0.U, a2.io.sum0, a2.io.sum1)
  val cout2 = Mux(cout1 === 0.U, a2.io.cout0, a2.io.cout1)

  val a3 = Module(new SelectAdder4())
  a3.io.a := io.a(15, 12)
  a3.io.b := io.b(15, 12)

  val out3  = Mux(cout2 === 0.U, a3.io.sum0, a3.io.sum1)

  io.out := a0.io.sum | (out1 << 4) | (out2 << 8) | (out3 << 12)
}

class CarrySelectAdder32 extends Module {

  val io = IO(new Bundle { 
    val a   = Input(UInt(32.W))
    val b   = Input(UInt(32.W))
    val out = Output(UInt(32.W))
  })

  val a0 = Module(new FullAdder4())
  a0.io.a := io.a(3, 0)
  a0.io.b := io.b(3, 0)

  val a1 = Module(new SelectAdder4())
  a1.io.a := io.a(7, 4)
  a1.io.b := io.b(7, 4)
  val out1  = Mux(a0.io.cout === 0.U, a1.io.sum0, a1.io.sum1)
  val cout1 = Mux(a0.io.cout === 0.U, a1.io.cout0, a1.io.cout1)

  val a2 = Module(new SelectAdder4())
  a2.io.a := io.a(11, 8)
  a2.io.b := io.b(11, 8)
  val out2  = Mux(cout1 === 0.U, a2.io.sum0, a2.io.sum1)
  val cout2 = Mux(cout1 === 0.U, a2.io.cout0, a2.io.cout1)

  val a3 = Module(new SelectAdder4())
  a3.io.a := io.a(15, 12)
  a3.io.b := io.b(15, 12)
  val out3  = Mux(cout2 === 0.U, a3.io.sum0, a3.io.sum1)
  val cout3 = Mux(cout2 === 0.U, a3.io.cout0, a3.io.cout1)

  val a4 = Module(new SelectAdder4())
  a4.io.a := io.a(19, 16)
  a4.io.b := io.b(19, 16)
  val out4  = Mux(cout3 === 0.U, a4.io.sum0, a4.io.sum1)
  val cout4 = Mux(cout3 === 0.U, a4.io.cout0, a4.io.cout1)

  val a5 = Module(new SelectAdder4())
  a5.io.a := io.a(23, 20)
  a5.io.b := io.b(23, 20)
  val out5  = Mux(cout4 === 0.U, a5.io.sum0, a5.io.sum1)
  val cout5 = Mux(cout4 === 0.U, a5.io.cout0, a5.io.cout1)

  val a6 = Module(new SelectAdder4())
  a6.io.a := io.a(27, 24)
  a6.io.b := io.b(27, 24)
  val out6  = Mux(cout5 === 0.U, a6.io.sum0, a6.io.sum1)
  val cout6 = Mux(cout5 === 0.U, a6.io.cout0, a6.io.cout1)

  val a7 = Module(new SelectAdder4())
  a7.io.a := io.a(31, 28)
  a7.io.b := io.b(31, 28)
  val out7  = Mux(cout6 === 0.U, a7.io.sum0, a7.io.sum1)

  io.out := a0.io.sum | (out1 << 4) | (out2 << 8) | (out3 << 12) | (out4 << 16) | (out5 << 20) | (out6 << 24) | (out7 << 28)
}

class CarrySelectAdder68 extends Module {

  val io = IO(new Bundle { 
    val a   = Input(UInt(32.W))
    val b   = Input(UInt(32.W))
    val out = Output(UInt(32.W))
  })

  val a0 = Module(new FullAdder6())
  a0.io.a := io.a(5, 0)
  a0.io.b := io.b(5, 0)

  val a1 = Module(new SelectAdder6())
  a1.io.a := io.a(11, 6)
  a1.io.b := io.b(11, 6)
  val out1  = Mux(a0.io.cout === 0.U, a1.io.sum0, a1.io.sum1)
  val cout1 = Mux(a0.io.cout === 0.U, a1.io.cout0, a1.io.cout1)

  val a2 = Module(new SelectAdder6())
  a2.io.a := io.a(17, 12)
  a2.io.b := io.b(17, 12)
  val out2  = Mux(cout1 === 0.U, a2.io.sum0, a2.io.sum1)
  val cout2 = Mux(cout1 === 0.U, a2.io.cout0, a2.io.cout1)

  val a3 = Module(new SelectAdder6())
  a3.io.a := io.a(23, 18)
  a3.io.b := io.b(23, 18)
  val out3  = Mux(cout2 === 0.U, a3.io.sum0, a3.io.sum1)
  val cout3 = Mux(cout2 === 0.U, a3.io.cout0, a3.io.cout1)

  val a4 = Module(new SelectAdder8())
  a4.io.a := io.a(31, 24)
  a4.io.b := io.b(31, 24)
  val out4  = Mux(cout3 === 0.U, a4.io.sum0, a4.io.sum1)
  val cout4 = Mux(cout3 === 0.U, a4.io.cout0, a4.io.cout1)

  //printf(p"${Binary(io.a)}\n")
  //printf(p"${Binary(io.b)}\n")
  //printf(p"${Binary(a0.io.sum)}\n")
  //printf(p"${Binary(out1)}\n")
  //printf(p"${Binary(cout1)}\n")
  //printf(p"${Binary(out2)}\n")
  //printf(p"${Binary(cout2)}\n")
  //printf(p"${Binary(out3)}\n")
  //printf(p"${Binary(cout3)}\n")
  //printf(p"${Binary(out4)}\n")
  //printf(p"${Binary(cout4)}\n")
  io.out := a0.io.sum | (out1 << 6) | (out2 << 12) | (out3 << 18) | (out4 << 24)
}