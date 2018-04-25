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

class SelectAdder4 extends Module {
  val io = IO(new Bundle { 
    val a    = Input(UInt(4.W))
    val b    = Input(UInt(4.W))
    val sum0  = Output(UInt(4.W))
    val sum1  = Output(UInt(4.W))
    val cout0 = Output(UInt(1.W))
    val cout1 = Output(UInt(1.W))
  })
  
  val m0 = Module(new OrXor())
  m0.io.a := io.a(0)
  m0.io.b := io.b(0)

  val _m0 = Module(new OrXor())
  _m0.io.a := m0.io.sum
  _m0.io.b := 1.U

  val m1 = Module(new FullAdder())
  m1.io.a := io.a(1)
  m1.io.b := io.b(1)
  m1.io.cin := m0.io.cout

  val _m1 = Module(new OrXor())
  _m1.io.a := m1.io.sum
  _m1.io.b := _m0.io.cout

  val m2 = Module(new FullAdder())
  m2.io.a := io.a(2)
  m2.io.b := io.b(2)
  m2.io.cin := m1.io.cout

  val _m2 = Module(new OrXor())
  _m2.io.a := m2.io.sum
  _m2.io.b := _m1.io.cout

  val m3 = Module(new FullAdder())
  m3.io.a := io.a(3)
  m3.io.b := io.b(3)
  m3.io.cin := m2.io.cout

  val _m3 = Module(new OrXor())
  _m3.io.a := m3.io.sum
  _m3.io.b := _m2.io.cout

  io.sum0 :=  m0.io.sum |  (m1.io.sum << 1) |  (m2.io.sum << 2) |  (m3.io.sum << 3)
  io.sum1 := _m0.io.sum | (_m1.io.sum << 1) | (_m2.io.sum << 2) | (_m3.io.sum << 3)

  io.cout0 := m3.io.cout
  io.cout1 := _m3.io.cout | m3.io.cout

}

class FullAdder4 extends Module {

  val io = IO(new Bundle { 
    val a    = Input(UInt(4.W))
    val b    = Input(UInt(4.W))
    val sum  = Output(UInt(4.W))
    val cout = Output(UInt(1.W))
  })
  
  val m0 = Module(new OrXor())
  m0.io.a := io.a(0)
  m0.io.b := io.b(0)

  val m1 = Module(new FullAdder())
  m1.io.a := io.a(1)
  m1.io.b := io.b(1)
  m1.io.cin := m0.io.cout

  val m2 = Module(new FullAdder())
  m2.io.a := io.a(2)
  m2.io.b := io.b(2)
  m2.io.cin := m1.io.cout

  val m3 = Module(new FullAdder())
  m3.io.a := io.a(3)
  m3.io.b := io.b(3)
  m3.io.cin := m2.io.cout

  io.sum :=  m0.io.sum |  (m1.io.sum << 1) |  (m2.io.sum << 2) |  (m3.io.sum << 3)
  io.cout := m3.io.cout

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

